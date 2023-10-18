package com.example.qc.got;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.qc.R;
import com.example.qc.adapter.FMV_Data;
import com.example.qc.adapter.FMV_DataAdapter;
import com.example.qc.adapter.FMV_Reasons;
import com.example.qc.adapter.FMV_ReasonsAdapter;
import com.example.qc.parser.JsonParser;
import com.example.qc.pojo.SampleGOTInfo;
import com.example.qc.pp.PPReadingFormActivity;
import com.example.qc.pp.Utility;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.CommonVolleyCall;
import com.example.qc.utils.UserPermissions;
import com.example.qc.utils.Utils;
import com.example.qc.volley.SendVolleyCall;
import com.example.qc.volley.volleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GOTFieldMonitoringVisitActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, FMV_ReasonsAdapter.ContactsAdapterListener{

    private ImageButton back_nav;
    private CheckBox checkBox_retest;
    private LinearLayout ll_retest_reason;

    private RecyclerView rv_reasonlist;
    private FMV_ReasonsAdapter mAdapter;
    private List<FMV_Reasons> ReasonsListArray = new ArrayList<>();
    private HashMap<String, Boolean> mapOfSetupVariety;
    private String crop_condition = "Good";
    private Button btn_submit;
    private RadioButton rb_good;
    private RadioButton rb_average;
    private RadioButton rb_poor;
    private RadioGroup radiocropgrp;
    private EditText et_retest_reason;
    private String retest = "No";
    private EditText et_noofplants;
    private TextView tv_sampleno;

    private LinearLayout ll_sowingdata;
    private LinearLayout sowingdata_expand;
    boolean sowing = false;
    private TextView tv_date;
    private LinearLayout ll_transplanting;
    private LinearLayout transplanting_expand;
    boolean transplanting = false;
    private LinearLayout ll_fmv;
    private LinearLayout fmv_expand;
    boolean fmv = false;
    private ArrayList<FMV_Data> fmvDataArray;
    private FMV_DataAdapter fmvadapter;
    private ListView lv_fmvdata;
    private LinearLayout ll_withsample;
    private RadioGroup radiosample;
    private LinearLayout ll_directblock;
    private LinearLayout ll_nurseryblock;
    private int mYear, mMonth, mDay;
    private ImageView iv_photo;
    private Button button_photo;
    private String userChoosenTask;
    private String ImageString = "";
    private String retImageString="";
    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;

    private ImageView iv_photo2;
    private Button button_photo2;
    private String retImageString2="";

    private ImageView iv_photo3;
    private Button button_photo3;
    private String retImageString3="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_o_t_field_monitoring_visit);
        getSupportActionBar().hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        mapOfSetupVariety = new HashMap<>();
        lv_fmvdata = findViewById(R.id.lv_fmvdata);
        fmvDataArray = new ArrayList<>();
        fmvadapter = new FMV_DataAdapter(GOTFieldMonitoringVisitActivity.this, R.layout.custom_fmvdata, fmvDataArray);
        lv_fmvdata.setAdapter(fmvadapter);
        back_nav = findViewById(R.id.back_nav);
        checkBox_retest = findViewById(R.id.checkBox_retest);
        ll_retest_reason = findViewById(R.id.ll_retest_reason);
        ll_withsample = findViewById(R.id.ll_withsample);
        radiosample = findViewById(R.id.radiosample);
        et_retest_reason = findViewById(R.id.et_retest_reason);
        et_noofplants = findViewById(R.id.et_noofplants);

        ll_sowingdata = findViewById(R.id.ll_sowingdata);
        sowingdata_expand = findViewById(R.id.sowingdata_expand);
        ll_transplanting = findViewById(R.id.ll_transplanting);
        transplanting_expand = findViewById(R.id.transplanting_expand);
        ll_fmv = findViewById(R.id.ll_fmv);
        fmv_expand = findViewById(R.id.fmv_expand);
        ll_directblock = findViewById(R.id.ll_directblock);
        ll_nurseryblock = findViewById(R.id.ll_nurseryblock);

        rv_reasonlist = findViewById(R.id.rv_reasonlist);
        radiocropgrp = findViewById(R.id.radiocropgrp);

        iv_photo = findViewById(R.id.iv_retphoto);
        button_photo = findViewById(R.id.button_photo);

        iv_photo2 = findViewById(R.id.iv_retphoto2);
        button_photo2 = findViewById(R.id.button_photo2);

        iv_photo3 = findViewById(R.id.iv_retphoto3);
        button_photo3 = findViewById(R.id.button_photo3);

        TextView tv_crop = findViewById(R.id.tv_crop);
        TextView tv_variety = findViewById(R.id.tv_variety);
        TextView tv_lono = findViewById(R.id.tv_lono);
        TextView tv_stage = findViewById(R.id.tv_stage);
        TextView tv_dosa = findViewById(R.id.tv_dosa);
        tv_sampleno = findViewById(R.id.tv_sampleno);
        tv_date = findViewById(R.id.tv_date);
        btn_submit = findViewById(R.id.btn_submit);

        TextView tv_dos = findViewById(R.id.tv_dos);
        TextView tv_noofseeds = findViewById(R.id.tv_noofseeds);
        TextView tv_sowing_location = findViewById(R.id.tv_sowing_location);
        TextView tv_sowingplotno = findViewById(R.id.tv_sowingplotno);
        TextView tv_bedno = findViewById(R.id.tv_bedno);
        TextView tv_noofrows = findViewById(R.id.tv_noofrows);
        TextView tv_noofcells = findViewById(R.id.tv_noofcells);
        TextView tv_nooftrey = findViewById(R.id.tv_nooftrey);

        TextView tv_dot = findViewById(R.id.tv_dot);
        TextView tv_transplot = findViewById(R.id.tv_transplot);
        TextView tv_transrange = findViewById(R.id.tv_transrange);
        TextView tv_transnoofrows = findViewById(R.id.tv_transnoofrows);
        TextView tv_transbedno = findViewById(R.id.tv_transbedno);
        TextView tv_transdirection = findViewById(R.id.tv_transdirection);
        TextView tv_transstate = findViewById(R.id.tv_transstate);
        TextView tv_translocation = findViewById(R.id.tv_translocation);
        TextView tv_transnoofplants = findViewById(R.id.tv_transnoofplants);

        SampleGOTInfo sampleGOTInfo = (SampleGOTInfo) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_SAMPLEINFO_OBJ,SampleGOTInfo.class);
        tv_crop.setText(sampleGOTInfo.getCrop());
        tv_variety.setText(sampleGOTInfo.getVariety());
        tv_lono.setText(sampleGOTInfo.getLotno());
        tv_stage.setText(sampleGOTInfo.getTrstage());
        tv_dosa.setText(sampleGOTInfo.getAckdate());
        tv_sampleno.setText(sampleGOTInfo.getSampleno());
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tv_date.setText(date);

        tv_dos.setText(sampleGOTInfo.getDosow());
        tv_noofseeds.setText(sampleGOTInfo.getSow_noofseeds());
        tv_sowing_location.setText(sampleGOTInfo.getSow_loc());
        tv_sowingplotno.setText(sampleGOTInfo.getSow_nurplotno());
        tv_bedno.setText(sampleGOTInfo.getSow_bedno());
        tv_noofrows.setText(sampleGOTInfo.getSow_noofrows());
        tv_noofcells.setText(sampleGOTInfo.getSow_noofcellstray());
        tv_nooftrey.setText(sampleGOTInfo.getSow_nooftraylot());

        tv_dot.setText(sampleGOTInfo.getTransp_date());
        tv_transplot.setText(sampleGOTInfo.getTransp_plotno());
        tv_transrange.setText(sampleGOTInfo.getTransp_range());
        tv_transnoofrows.setText(sampleGOTInfo.getTransp_noofrows());
        tv_transbedno.setText(sampleGOTInfo.getTransp_bedno());
        tv_transdirection.setText(sampleGOTInfo.getTransp_direction());
        tv_transstate.setText(sampleGOTInfo.getTransp_state());
        tv_translocation.setText(sampleGOTInfo.getTransp_loc());
        tv_transnoofplants.setText(sampleGOTInfo.getTransp_plotno());

        if (sampleGOTInfo.getSow_sptype().equalsIgnoreCase("Direct Sowing")){
            ll_transplanting.setVisibility(View.GONE);
            transplanting_expand.setVisibility(View.GONE);
            ll_directblock.setVisibility(View.VISIBLE);
            ll_nurseryblock.setVisibility(View.GONE);
        }else {
            ll_transplanting.setVisibility(View.VISIBLE);
            //transplanting_expand.setVisibility(View.VISIBLE);
            ll_directblock.setVisibility(View.GONE);
            ll_nurseryblock.setVisibility(View.VISIBLE);
        }

        mAdapter = new FMV_ReasonsAdapter(this, ReasonsListArray, this);
        rv_reasonlist.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_reasonlist.setLayoutManager(mLayoutManager);
        rv_reasonlist.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rv_reasonlist.setItemAnimator(new DefaultItemAnimator());
        rv_reasonlist.setAdapter(mAdapter);
    }

    private void init() {
        String sampleno = tv_sampleno.getText().toString().trim();
        getFMV_Data(sampleno);
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GOTFieldMonitoringVisitActivity.this, GOTResultStepsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageString = "photo1";
                selectImage();
            }
        });

        button_photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageString = "photo2";
                selectImage();
            }
        });

        button_photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageString = "photo3";
                selectImage();
            }
        });

        ll_sowingdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sowing){
                    sowingdata_expand.setVisibility(View.GONE);
                    sowing = false;
                }else {
                    sowingdata_expand.setVisibility(View.VISIBLE);
                    sowing = true;
                }
            }
        });

        ll_transplanting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transplanting){
                    transplanting_expand.setVisibility(View.GONE);
                    transplanting = false;
                }else {
                    transplanting_expand.setVisibility(View.VISIBLE);
                    transplanting = true;
                }
            }
        });
        ll_fmv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fmv){
                    fmv_expand.setVisibility(View.GONE);
                    fmv = false;
                }else {
                    fmv_expand.setVisibility(View.VISIBLE);
                    fmv = true;
                }
            }
        });

        /*tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(GOTFieldMonitoringVisitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String seldate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                               String pastdate = getCalculatedDate("dd-MM-yyyy", -2);
                               if (seldate < pastdate){

                               }
                                tv_date.setText(seldate);
                                //tv_date.setText(dayOfMonth + " " + MONTHS[(monthOfYear + 1)-1]);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                if (Build.VERSION.SDK_INT >= 21) {
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                }
            }
        });*/

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Map.Entry<String, Boolean> variety : mapOfSetupVariety.entrySet()) {
                    String varietyName = variety.getKey();
                    Boolean isCheck = variety.getValue();
                    if (isCheck) {
                        if (stringBuilder.toString().isEmpty()) {
                            stringBuilder.append(varietyName);
                        } else {
                            stringBuilder.append(",").append(varietyName);
                        }
                    }
                }

                int cropcon = radiocropgrp.getCheckedRadioButtonId();
                RadioButton type = findViewById(cropcon);
                String crophealth = type.getText().toString();
                String retest_reason = et_retest_reason.getText().toString().trim();
                String noofplants = et_noofplants.getText().toString().trim();
                String crophealthreason = stringBuilder.toString();
                int rdsample = radiosample.getCheckedRadioButtonId();
                RadioButton rdsamp = findViewById(rdsample);
                String retesttype = rdsamp.getText().toString();
                if(checkBox_retest.isChecked()) {
                    if (retest_reason.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter Re-Test Reason", Toast.LENGTH_LONG).show();
                    }else {
                        retest = "Yes";
                        confirmAlertSubmit(crophealth,noofplants,crophealthreason,retest,retest_reason,retesttype);
                        //updateTransplantingData(plotno,bedno,noofrows,sowingdate,direction,location_nursery,retest_reason,state,noofplants,retest,range);
                    }
                } else {
                    retest = "No";
                    confirmAlertSubmit(crophealth,noofplants,crophealthreason,retest,retest_reason,retesttype);
                    /*if (direction.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Select Direction", Toast.LENGTH_LONG).show();
                    }else if (location_nursery.equalsIgnoreCase("Select")){
                        Toast.makeText(getApplicationContext(), "Select Location", Toast.LENGTH_LONG).show();
                    }else if (state.equalsIgnoreCase("Select")){
                        Toast.makeText(getApplicationContext(), "Select State", Toast.LENGTH_LONG).show();
                    }else if (sowingdate.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Select Sowing Date", Toast.LENGTH_LONG).show();
                    }else if (plotno.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter Plot No.", Toast.LENGTH_LONG).show();
                    }else if (bedno.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter Bed No.", Toast.LENGTH_LONG).show();
                    }else if (noofrows.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Rows", Toast.LENGTH_LONG).show();
                    }else if (noofplants.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Plants", Toast.LENGTH_LONG).show();
                    }else {
                        //confirmAlertSubmit(plotno,bedno,noofrows,sowingdate,direction,location_nursery,retest_reason,state,noofplants,retest,range);
                        //updateTransplantingData(noofseeds_direct,plotno,bedno,noofrows,sowingdate,direction,location_nursery,methodofsowing,retest_reason,noofcells,nooftrey,state,noofplants,retest);
                    }*/
                }
            }
        });
    }

    private String getCalculatedDate(String dateFormat, int i) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, i);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    private void getFMV_Data(final String sampleno) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.COMMON_URL + AppConfig.SAMPLEINFO_GOT_PROGRAM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                fmvDataArray.clear();
                fmvadapter.notifyDataSetChanged();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json

                    if (!error) {
                        //success
                        JSONObject user = jObj.getJSONObject("user");
                        JSONArray samplearray = user.getJSONArray("infodataarray");
                        for (int i = 0; i < samplearray.length(); i++) {
                            JSONObject details=samplearray.getJSONObject(i);
                            JSONArray fmvarray = details.getJSONArray("fmvarray");
                            for (int j = 0; j < fmvarray.length(); j++) {
                                JSONObject details2=fmvarray.getJSONObject(j);
                                String fmv_crophealth=details2.getString("fmv_crophealth");
                                String fmv_reasons=details2.getString("fmv_reasons");
                                String fmv_noofplants=details2.getString("fmv_noofplants");
                                fmvDataArray.add(new FMV_Data(j + 1, fmv_crophealth,fmv_reasons,fmv_noofplants));
                            }
                        }
                        fmvadapter.notifyDataSetChanged();
                    } else {
                        String returnString = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), returnString, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
                Map<String, String> params = new HashMap<>();
                params.put("mobile1", userid);
                params.put("sampleno", sampleno);
                return params;
            }

        };

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        CommonVolleyCall.getInstance(GOTFieldMonitoringVisitActivity.this).addRequestQueue(strReq);
    }

    private void confirmAlertSubmit(final String crophealth, final String noofplants, final String crophealthreason, final String retest, final String retest_reason, final String retesttype) {
        final Dialog dialog = new Dialog(GOTFieldMonitoringVisitActivity.this);
        dialog.setContentView(R.layout.custom_confirmalert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        Button cancel = dialog.findViewById(R.id.no_cancel);
        Button submit = dialog.findViewById(R.id.yes_submit);
        final TextView message = dialog.findViewById(R.id.tv_message);
        final TextView alerttitle = dialog.findViewById(R.id.tv_alerttitle);
        alerttitle.setText("Review Data Before Submission");
        message.setText(R.string.confirm_alert);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                updateFMVData(crophealth,noofplants,crophealthreason,retest,retest_reason,retesttype);
            }
        });
    }

    private void updateFMVData(String crophealth, String noofplants, String crophealthreason, String retest, String retest_reason, String retesttype) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Processing...");
        pDialog.show();
        pDialog.setCancelable(false);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
        String sampleno = tv_sampleno.getText().toString().trim();
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("scode", scode);
        params.put("sampleno", sampleno);
        params.put("fmv_crophealth", crophealth);
        params.put("fmv_reasons", crophealthreason);
        params.put("fmv_noofplants", noofplants);
        params.put("retest_reason", retest_reason);
        params.put("retest", retest);
        params.put("retesttype", retesttype);
        params.put("fmvphoto1", retImageString);
        params.put("fmvphoto2", retImageString2);
        params.put("fmvphoto3", retImageString3);

        new SendVolleyCall().executeProgram(GOTFieldMonitoringVisitActivity.this, AppConfig.SAMPLE_FMVUPDATE_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitData(response);
                if (message.equalsIgnoreCase("Success")){
                    Intent intent = new Intent(GOTFieldMonitoringVisitActivity.this, SampleGOTResultHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(String response) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void retest_onClick(View v1)
    {
        if(checkBox_retest.isChecked()) {
            // true,do the task
            ll_retest_reason.setVisibility(View.VISIBLE);
            ll_withsample.setVisibility(View.VISIBLE);
            retest = "Yes";
        }
        else {
            ll_retest_reason.setVisibility(View.GONE);
            ll_withsample.setVisibility(View.GONE);
            retest = "No";
        }
    }

    public void click_good(View view){
        crop_condition = "Good";
        ReasonsListArray.clear();
        mAdapter.notifyDataSetChanged();
        //getReasonList(crop_condition);
    }

    public void click_average(View view){
        crop_condition = "Average";
        getReasonList(crop_condition);
    }

    public void click_poor(View view){
        crop_condition = "Poor";
        getReasonList(crop_condition);
    }

    private void getReasonList(final String crop_condition) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.COMMON_URL + AppConfig.GET_FMVREASONS_PROGRAM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                ReasonsListArray.clear();
                mAdapter.notifyDataSetChanged();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json

                    if (!error) {
                        //success
                        JSONObject user = jObj.getJSONObject("user");
                        JSONArray retarray = user.getJSONArray("fmreasonarray");

                        for (int i = 0; i < retarray.length(); i++) {
                            String details = retarray.getString(i);
                            ReasonsListArray.add(new FMV_Reasons(i+1, details));
                            mAdapter.notifyDataSetChanged();
                        }

                    } else {
                        String returnString = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), returnString, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
                Map<String, String> params = new HashMap<>();
                params.put("mobile1", userid);
                params.put("crophealth", crop_condition);
                return params;
            }

        };

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        CommonVolleyCall.getInstance(GOTFieldMonitoringVisitActivity.this).addRequestQueue(strReq);
    }

    @Override
    public void onBackPressed() {
        
        super.onBackPressed();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onContactSelected(FMV_Reasons contact) {

    }

    @Override
    public void onCheckBoxCheck(FMV_Reasons contact, boolean ischeck) {
        mapOfSetupVariety.put(contact.getReason_name(), ischeck);
    }

    private void selectImage() {
        final CharSequence[] items = {"Click here to open camera", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(GOTFieldMonitoringVisitActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(GOTFieldMonitoringVisitActivity.this);

                if (items[item].equals("Click here to open camera")) {
                    userChoosenTask = "Click here to open camera";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (new UserPermissions().checkPermission(GOTFieldMonitoringVisitActivity.this, Manifest.permission.CAMERA)) {
                Toast.makeText(GOTFieldMonitoringVisitActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(GOTFieldMonitoringVisitActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private Bitmap onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        Bitmap imageBitmap;
        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ImageString.equalsIgnoreCase("photo1")){
            retImageString = getStringFromBitmap(thumbnail);
            iv_photo.setImageBitmap(thumbnail);
        }
        if (ImageString.equalsIgnoreCase("photo2")){
            retImageString2 = getStringFromBitmap(thumbnail);
            iv_photo2.setImageBitmap(thumbnail);
        }
        if (ImageString.equalsIgnoreCase("photo3")){
            retImageString3 = getStringFromBitmap(thumbnail);
            iv_photo3.setImageBitmap(thumbnail);
        }



        //Toast.makeText(AddRetailerActivity.this, retImageString, Toast.LENGTH_SHORT).show();
        return thumbnail;
    }

    public String getStringFromBitmap(Bitmap bitmap) {
        String imageString = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] bytes = baos.toByteArray();
        imageString = Base64.encodeToString(bytes, Base64.DEFAULT);
        return imageString;
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        iv_photo.setImageBitmap(bm);
    }
}