package com.example.qc.got;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.CommonVolleyCall;
import com.example.qc.utils.Utils;
import com.example.qc.volley.SendVolleyCall;
import com.example.qc.volley.volleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.BreakIterator;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GOTFinalObservationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, FMV_ReasonsAdapter.ContactsAdapterListener{

    private ImageButton back_nav;
    private CheckBox checkBox_retest;
    private LinearLayout ll_retest_reason;
    private EditText et_obrdatefinal;
    private TextView tv_sampleno;
    private Button btn_submit;
    private LinearLayout ll_sowingdata;
    private LinearLayout sowingdata_expand;
    boolean sowing = false;
    private LinearLayout ll_transplanting;
    private LinearLayout transplanting_expand;
    boolean transplanting = false;
    private LinearLayout ll_fmv;
    private LinearLayout fmv_expand;
    boolean fmv = false;
    private EditText et_noofplants_final;
    private EditText et_maleplants;
    private EditText et_per_maleplants;
    private EditText et_femaleplants;
    private EditText et_per_femaleplants;
    private EditText et_ooftypes;
    private EditText et_per_ooftypes;
    private EditText et_totalplants;
    private EditText et_per_totalplants;
    private EditText et_remark;
    private EditText et_retest_reason;
    private String retest="No";

    private ArrayList<FMV_Data> fmvDataArray;
    private FMV_DataAdapter fmvadapter;
    private LinearLayout ll_withsample;
    private RadioGroup radiosample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_o_t_final_observation);
        getSupportActionBar().hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        back_nav = findViewById(R.id.back_nav);
        checkBox_retest = findViewById(R.id.checkBox_retest);
        ll_retest_reason = findViewById(R.id.ll_retest_reason);
        ll_withsample = findViewById(R.id.ll_withsample);
        radiosample = findViewById(R.id.radiosample);
        ll_sowingdata = findViewById(R.id.ll_sowingdata);
        sowingdata_expand = findViewById(R.id.sowingdata_expand);
        ll_transplanting = findViewById(R.id.ll_transplanting);
        transplanting_expand = findViewById(R.id.transplanting_expand);
        ll_fmv = findViewById(R.id.ll_fmv);
        fmv_expand = findViewById(R.id.fmv_expand);
        LinearLayout ll_directblock = findViewById(R.id.ll_directblock);
        LinearLayout ll_nurseryblock = findViewById(R.id.ll_nurseryblock);
        LinearLayout ll_totalplants = findViewById(R.id.ll_totalplants);
        LinearLayout ll_totalplantsper = findViewById(R.id.ll_totalplantsper);
        LinearLayout ll_noofplants = findViewById(R.id.ll_noofplants);

        TextView tv_crop = findViewById(R.id.tv_crop);
        TextView tv_variety = findViewById(R.id.tv_variety);
        TextView tv_lono = findViewById(R.id.tv_lono);
        TextView tv_stage = findViewById(R.id.tv_stage);
        TextView tv_dosa = findViewById(R.id.tv_dosa);
        tv_sampleno = findViewById(R.id.tv_sampleno);
        btn_submit = findViewById(R.id.btn_submit);

        et_obrdatefinal = findViewById(R.id.et_obrdatefinal);
        et_noofplants_final = findViewById(R.id.et_noofplants_final);
        et_maleplants = findViewById(R.id.et_maleplants);
        et_per_maleplants = findViewById(R.id.et_per_maleplants);
        et_femaleplants = findViewById(R.id.et_femaleplants);
        et_per_femaleplants = findViewById(R.id.et_per_femaleplants);
        et_ooftypes = findViewById(R.id.et_ooftypes);
        et_per_ooftypes = findViewById(R.id.et_per_ooftypes);
        et_totalplants = findViewById(R.id.et_totalplants);
        et_per_totalplants = findViewById(R.id.et_per_totalplants);
        et_remark = findViewById(R.id.et_remark);
        et_retest_reason = findViewById(R.id.et_retest_reason);

        TextView tv_dos = findViewById(R.id.tv_dos);
        TextView tv_noofseeds = findViewById(R.id.tv_noofseeds);
        TextView tv_sowingplot = findViewById(R.id.tv_sowingplot);
        TextView tv_bedno = findViewById(R.id.tv_bedno);
        TextView tv_sowing_location = findViewById(R.id.tv_sowing_location);
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
        et_obrdatefinal.setText(date);

        tv_dos.setText(sampleGOTInfo.getDosow());
        tv_noofseeds.setText(sampleGOTInfo.getSow_noofseeds());
        tv_sowing_location.setText(sampleGOTInfo.getSow_loc());
        tv_sowingplot.setText(sampleGOTInfo.getSow_nurplotno());
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
            ll_totalplants.setVisibility(View.VISIBLE);
            ll_totalplantsper.setVisibility(View.VISIBLE);
            //ll_noofplants.setVisibility(View.VISIBLE);
        }else {
            ll_transplanting.setVisibility(View.VISIBLE);
            //transplanting_expand.setVisibility(View.VISIBLE);
            ll_directblock.setVisibility(View.GONE);
            ll_nurseryblock.setVisibility(View.VISIBLE);
            ll_totalplants.setVisibility(View.GONE);
            ll_totalplantsper.setVisibility(View.GONE);
            //ll_noofplants.setVisibility(View.GONE);
        }
        ListView lv_fmvdata = findViewById(R.id.lv_fmvdata);
        fmvDataArray = new ArrayList<>();
        fmvadapter = new FMV_DataAdapter(GOTFinalObservationActivity.this, R.layout.custom_fmvdata, fmvDataArray);
        lv_fmvdata.setAdapter(fmvadapter);
    }

    private void init() {
        String sampleno = tv_sampleno.getText().toString().trim();
        getFMV_Data(sampleno);
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GOTFinalObservationActivity.this, GOTResultStepsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
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
        final DecimalFormat form = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
        et_maleplants.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String maleplants = String.valueOf(charSequence);
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !maleplants.equals("")) {
                    float maleplants1 = Float.parseFloat(maleplants);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float normalmeanper = (maleplants1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_maleplants.setText(form.format(normalmeanper));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_femaleplants.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String femaleplants = String.valueOf(charSequence);
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !femaleplants.equals("")) {
                    float femaleplants1 = Float.parseFloat(femaleplants);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float femaleplantper = (femaleplants1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_femaleplants.setText(form.format(femaleplantper));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_ooftypes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String offtypesplants = String.valueOf(charSequence);
                String femaleplants = et_femaleplants.getText().toString().trim();
                String maleplants = et_maleplants.getText().toString().trim();
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !offtypesplants.equals("") && !femaleplants.equals("") && !maleplants.equals("")) {
                    float offtypesplants1 = Float.parseFloat(offtypesplants);
                    float femaleplants1 = Float.parseFloat(femaleplants);
                    float maleplants1 = Float.parseFloat(maleplants);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float offtypesplantper = (offtypesplants1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    float total = offtypesplants1+femaleplants1+maleplants1;
                    float totalper = (total * 100) / noofplants_final1;
                    et_per_ooftypes.setText(form.format(offtypesplantper));
                    et_totalplants.setText(String.valueOf(total));
                    et_per_totalplants.setText(String.valueOf(totalper));

                    float femaleplantper = (femaleplants1 * 100) / noofplants_final1;
                    et_per_femaleplants.setText(form.format(femaleplantper));

                    float normalmeanper = (maleplants1 * 100) / noofplants_final1;
                    et_per_maleplants.setText(form.format(normalmeanper));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_noofplants_final.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String offtypesplants = et_ooftypes.getText().toString().trim();
                String femaleplants = et_femaleplants.getText().toString().trim();
                String maleplants = et_maleplants.getText().toString().trim();
                String noofplants_final = String.valueOf(charSequence);
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !offtypesplants.equals("") && !femaleplants.equals("") && !maleplants.equals("")) {
                    float offtypesplants1 = Float.parseFloat(offtypesplants);
                    float femaleplants1 = Float.parseFloat(femaleplants);
                    float maleplants1 = Float.parseFloat(maleplants);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float offtypesplantper = (offtypesplants1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    float total = offtypesplants1+femaleplants1+maleplants1;
                    float totalper = (total * 100) / noofplants_final1;
                    et_per_ooftypes.setText(form.format(offtypesplantper));
                    et_totalplants.setText(String.valueOf(total));
                    et_per_totalplants.setText(String.valueOf(totalper));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String obrdatefinal = et_obrdatefinal.getText().toString().trim();
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                String maleplants = et_maleplants.getText().toString().trim();
                String femaleplants = et_femaleplants.getText().toString().trim();
                String offtypesplants = et_ooftypes.getText().toString().trim();
                String totalplants = et_totalplants.getText().toString().trim();
                String per_maleplants = et_per_maleplants.getText().toString().trim();
                String per_femaleplants = et_per_femaleplants.getText().toString().trim();
                String per_ooftypes = et_per_ooftypes.getText().toString().trim();
                String per_totalplants = et_per_totalplants.getText().toString().trim();
                String remark = et_remark.getText().toString().trim();
                String retest_reason = et_retest_reason.getText().toString().trim();
                int rdsample = radiosample.getCheckedRadioButtonId();
                RadioButton rdsamp = findViewById(rdsample);
                String retesttype = rdsamp.getText().toString();
                if(checkBox_retest.isChecked()) {
                    if (retest_reason.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter Re-Test Reason", Toast.LENGTH_LONG).show();
                    }else {
                        retest = "Yes";
                        confirmAlertSubmit(obrdatefinal,noofplants_final,maleplants,femaleplants,offtypesplants,totalplants,per_maleplants,per_femaleplants,per_ooftypes,per_totalplants,remark,retest_reason,retest,retesttype);
                        //updateTransplantingData(plotno,bedno,noofrows,sowingdate,direction,location_nursery,retest_reason,state,noofplants,retest,range);
                    }
                } else {
                    retest = "No";
                    if (obrdatefinal.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Select Observatin Date", Toast.LENGTH_LONG).show();
                    }else if (noofplants_final.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Plants", Toast.LENGTH_LONG).show();
                    }else if (maleplants.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Male Plants", Toast.LENGTH_LONG).show();
                    }else if (femaleplants.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Female Plants", Toast.LENGTH_LONG).show();
                    }else if (offtypesplants.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Off Type Plants", Toast.LENGTH_LONG).show();
                    }else {
                        confirmAlertSubmit(obrdatefinal,noofplants_final,maleplants,femaleplants,offtypesplants,totalplants,per_maleplants,per_femaleplants,per_ooftypes,per_totalplants,remark,retest_reason,retest,retesttype);
                        //updateTransplantingData(noofseeds_direct,plotno,bedno,noofrows,sowingdate,direction,location_nursery,methodofsowing,retest_reason,noofcells,nooftrey,state,noofplants,retest);
                    }
                }
            }
        });
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
        CommonVolleyCall.getInstance(GOTFinalObservationActivity.this).addRequestQueue(strReq);
    }

    private void confirmAlertSubmit(final String obrdatefinal, final String noofplants_final, final String maleplants, final String femaleplants, final String offtypesplants, final String totalplants, final String per_maleplants, final String per_femaleplants, final String per_ooftypes, final String per_totalplants, final String remark, final String retest_reason, final String retest, final String retesttype) {
        final Dialog dialog = new Dialog(GOTFinalObservationActivity.this);
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
                updateFinalObservationData(obrdatefinal,noofplants_final,maleplants,femaleplants,offtypesplants,totalplants,per_maleplants,per_femaleplants,per_ooftypes,per_totalplants,remark,retest_reason,retest,retesttype);
            }
        });
    }

    private void updateFinalObservationData(String obrdatefinal, String noofplants_final, String maleplants, String femaleplants, String offtypesplants, String totalplants, String per_maleplants, String per_femaleplants, String per_ooftypes, String per_totalplants, String remark, String retest_reason, String retest, String retesttype) {
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
        params.put("fnobser_obserdate", obrdatefinal);
        params.put("fnobser_noofplants", noofplants_final);
        params.put("fnobser_maleplants", maleplants);
        params.put("fnobser_maleper", per_maleplants);
        params.put("fnobser_femaleplants", femaleplants);
        params.put("fnobser_femaleper", per_femaleplants);
        params.put("fnobser_total", totalplants);
        params.put("fnobser_totalper", per_totalplants);
        params.put("fnobser_otherofftype", offtypesplants);
        params.put("fnobser_otheroffper", per_ooftypes);
        params.put("fnobser_blineps", "");
        params.put("fnobser_blinepsper", "");
        params.put("fnobser_totalofftype", "");
        params.put("fnobser_totalofftypeper", "");
        params.put("fnobser_self", "");
        params.put("fnobser_selfper", "");
        params.put("fnobser_pencilplants", "");
        params.put("fnobser_pencilplantsper", "");
        params.put("fnobser_aline", "");
        params.put("fnobser_alineper", "");
        params.put("fnobser_blinesh", "");
        params.put("fnobser_blineshper", "");
        params.put("fnobser_lg", "");
        params.put("fnobser_lgper", "");
        params.put("fnobser_fg", "");
        params.put("fnobser_fgper", "");
        params.put("fnobser_bg", "");
        params.put("fnobser_bgper", "");
        params.put("fnobser_rt", "");
        params.put("fnobser_rtper", "");
        params.put("fnobser_tall", "");
        params.put("fnobser_tallper", "");
        params.put("fnobser_late", "");
        params.put("fnobser_lateper", "");
        params.put("fnobser_fertile", "");
        params.put("fnobser_fertileper", "");
        params.put("fnobser_sterile", "");
        params.put("fnobser_sterileper", "");
        params.put("fnobser_outcrosspart", "");
        params.put("fnobser_outcrosspartper", "");
        params.put("fnobser_remarks", remark);
        params.put("retest_reason", retest_reason);
        params.put("retest", retest);
        params.put("retesttype", retesttype);

        new SendVolleyCall().executeProgram(GOTFinalObservationActivity.this, AppConfig.SAMPLE_FINALOBRUPDATE_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitData(response);
                if (message.equalsIgnoreCase("Success")){
                    Intent intent = new Intent(GOTFinalObservationActivity.this, SampleGOTResultHomeActivity.class);
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

    }
}