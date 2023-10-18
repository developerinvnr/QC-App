package com.example.qc.got;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qc.R;
import com.example.qc.parser.JsonParser;
import com.example.qc.pojo.SampleGOTInfo;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.Utils;
import com.example.qc.volley.SendVolleyCall;
import com.example.qc.volley.volleyCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class GOTTransplantingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    private ImageButton back_nav;
    private CheckBox checkBox_retest;
    private LinearLayout ll_retest_reason;
    private TextView tv_sampleno;
    private TextView tv_date;

    private Spinner spinner_direction;
    private Spinner spinner_location;
    private Spinner spinner_state;
    private String retest = "No";

    private int mYear, mMonth, mDay;
    private Button btn_submit;
    private EditText et_retest_reason;
    private EditText et_noofplants;
    private EditText et_plotno;
    private EditText et_range;
    private EditText et_noofrows;
    private EditText et_bedno;

    private ArrayList<String> stateArray;
    private ArrayAdapter<String> stateadapter;

    private ArrayList<String> locArray;
    private ArrayAdapter<String> locadapter;

    private LinearLayout ll_sowingdata;
    private LinearLayout sowingdata_expand;
    boolean sowing = false;
    private LinearLayout ll_withsample;
    private RadioGroup radiosample;
    private LinearLayout ll_directblock;
    private LinearLayout ll_nurseryblock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_o_t_transplanting);
        Objects.requireNonNull(getSupportActionBar()).hide();
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

        spinner_direction = findViewById(R.id.spinner_direction);
        spinner_location = findViewById(R.id.spinner_location);
        spinner_state = findViewById(R.id.spinner_state);
        ll_directblock = findViewById(R.id.ll_directblock);
        ll_nurseryblock = findViewById(R.id.ll_nurseryblock);

        TextView tv_crop = findViewById(R.id.tv_crop);
        TextView tv_variety = findViewById(R.id.tv_variety);
        TextView tv_lono = findViewById(R.id.tv_lono);
        TextView tv_stage = findViewById(R.id.tv_stage);
        TextView tv_dosa = findViewById(R.id.tv_dosa);
        tv_sampleno = findViewById(R.id.tv_sampleno);
        tv_date = findViewById(R.id.tv_date);
        btn_submit = findViewById(R.id.btn_submit);

        et_retest_reason = findViewById(R.id.et_retest_reason);
        et_noofplants = findViewById(R.id.et_noofplants);
        et_plotno = findViewById(R.id.et_plotno);
        et_range = findViewById(R.id.et_range);
        et_noofrows = findViewById(R.id.et_noofrows);
        et_bedno = findViewById(R.id.et_bedno);

        TextView tv_dos = findViewById(R.id.tv_dos);
        TextView tv_noofseeds = findViewById(R.id.tv_noofseeds);
        TextView tv_sowing_location = findViewById(R.id.tv_sowing_location);
        TextView tv_sowingplotno = findViewById(R.id.tv_sowingplotno);
        TextView tv_bedno = findViewById(R.id.tv_bedno);
        TextView tv_noofrows = findViewById(R.id.tv_noofrows);
        TextView tv_noofcells = findViewById(R.id.tv_noofcells);
        TextView tv_nooftrey = findViewById(R.id.tv_nooftrey);

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

        if (sampleGOTInfo.getSow_sptype().equalsIgnoreCase("Direct Sowing")){
            ll_directblock.setVisibility(View.VISIBLE);
            ll_nurseryblock.setVisibility(View.GONE);
        }else {
            ll_directblock.setVisibility(View.GONE);
            ll_nurseryblock.setVisibility(View.VISIBLE);
        }

        String[] direction_list = {"E-W","W-E","E-W-E","W-E-W","N-S","S-N","N-S-N","S-N-S"};
        ArrayAdapter<String> adapter_direction = new ArrayAdapter<>(GOTTransplantingActivity.this, android.R.layout.simple_spinner_item, direction_list);
        adapter_direction.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_direction.setAdapter(adapter_direction);
    }

    private void init() {
        stateArray = new ArrayList<>();
        stateArray.add(0, "Select");
        stateadapter = Utils.getInstance().spinnerAdapter(GOTTransplantingActivity.this, stateArray);
        spinner_state.setAdapter(stateadapter);

        locArray = new ArrayList<>();
        locArray.add(0, "Select");
        locadapter = Utils.getInstance().spinnerAdapter(GOTTransplantingActivity.this, locArray);
        spinner_location.setAdapter(locadapter);

        getStateList();

        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GOTTransplantingActivity.this, GOTResultStepsActivity.class);
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

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(GOTTransplantingActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                tv_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                //tv_date.setText(dayOfMonth + " " + MONTHS[(monthOfYear + 1)-1]);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                if (Build.VERSION.SDK_INT >= 21) {
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String direction = spinner_direction.getSelectedItem().toString().trim();
                String location_nursery = spinner_location.getSelectedItem().toString().trim();
                String state = spinner_state.getSelectedItem().toString().trim();
                String sowingdate = tv_date.getText().toString().trim();
                String noofplants = et_noofplants.getText().toString().trim();
                String plotno = et_plotno.getText().toString().trim();
                String range = et_range.getText().toString().trim();
                String noofrows = et_noofrows.getText().toString().trim();
                String bedno = et_bedno.getText().toString().trim();
                String retest_reason = et_retest_reason.getText().toString().trim();
                int rdsample = radiosample.getCheckedRadioButtonId();
                RadioButton rdsamp = findViewById(rdsample);
                String retesttype = rdsamp.getText().toString();
                if(checkBox_retest.isChecked()) {
                    if (retest_reason.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter Re-Test Reason", Toast.LENGTH_LONG).show();
                    }else {
                        retest = "Yes";
                        confirmAlertSubmit(plotno,bedno,noofrows,sowingdate,direction,location_nursery,retest_reason,state,noofplants,retest,range,retesttype);
                        //updateTransplantingData(plotno,bedno,noofrows,sowingdate,direction,location_nursery,retest_reason,state,noofplants,retest,range);
                    }
                } else {
                    retest = "No";
                    if (direction.equalsIgnoreCase("")){
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
                        confirmAlertSubmit(plotno,bedno,noofrows,sowingdate,direction,location_nursery,retest_reason,state,noofplants,retest,range,retesttype);
                        //updateTransplantingData(noofseeds_direct,plotno,bedno,noofrows,sowingdate,direction,location_nursery,methodofsowing,retest_reason,noofcells,nooftrey,state,noofplants,retest);
                    }
                }
            }
        });

        spinner_state.setOnItemSelectedListener(GOTTransplantingActivity.this);
    }

    private void getStateList() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);

        new SendVolleyCall().executeProgram(GOTTransplantingActivity.this, AppConfig.GETSATELIST_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                stateArray = JsonParser.getInstance().parseStateList(GOTTransplantingActivity.this, response);
                String message = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MESSAGE);
                if (message.equalsIgnoreCase("Success")){
                    stateadapter = Utils.getInstance().spinnerAdapter(GOTTransplantingActivity.this, stateArray);
                    spinner_state.setAdapter(stateadapter);
                    stateadapter.notifyDataSetChanged();
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

    private void getLocationList(String state) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("statename", state);

        new SendVolleyCall().executeProgram(GOTTransplantingActivity.this, AppConfig.GETLOCATIONLIST_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                locArray = JsonParser.getInstance().parseLocList(GOTTransplantingActivity.this, response);
                String message = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MESSAGE);
                if (message.equalsIgnoreCase("Success")){
                    locadapter = Utils.getInstance().spinnerAdapter(GOTTransplantingActivity.this, locArray);
                    spinner_location.setAdapter(locadapter);
                    locadapter.notifyDataSetChanged();
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

    private void confirmAlertSubmit(final String plotno, final String bedno, final String noofrows, final String sowingdate, final String direction, final String location_nursery, final String retest_reason, final String state, final String noofplants, final String retest, final String range, final String retesttype) {
        final Dialog dialog = new Dialog(GOTTransplantingActivity.this);
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
                updateTransplantingData(plotno,bedno,noofrows,sowingdate,direction,location_nursery,retest_reason,state,noofplants,retest,range,retesttype);
            }
        });
    }

    private void updateTransplantingData(String plotno, String bedno, String noofrows, String sowingdate, String direction, String location_nursery, String retest_reason, String state, String noofplants, String retest, String range, String retesttype) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Processing ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
        String sampleno = tv_sampleno.getText().toString().trim();
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("scode", scode);
        params.put("sampleno", sampleno);
        params.put("transp_date", sowingdate);
        params.put("transp_plotno", plotno);
        params.put("transp_loc", location_nursery);
        params.put("transp_bedno", bedno);
        params.put("transp_direction", direction);
        params.put("transp_noofrows", noofrows);
        params.put("transp_noofplants", noofplants);
        params.put("transp_range", range);
        params.put("retest_reason", retest_reason);
        params.put("transp_state", state);
        params.put("retest", retest);
        params.put("retesttype", retesttype);

        new SendVolleyCall().executeProgram(GOTTransplantingActivity.this, AppConfig.SAMPLE_TRANSPLANTINGUPDATE_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitData(response);
                if (message.equalsIgnoreCase("Success")){
                    Intent intent = new Intent(GOTTransplantingActivity.this, SampleGOTResultHomeActivity.class);
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
        if (adapterView.getId() == R.id.spinner_state) {
            String state = spinner_state.getSelectedItem().toString().trim();
            if (!state.equalsIgnoreCase("Select")) {
                getLocationList(state);
            } else {
                locArray.clear();
                locArray.add("Select");
                locadapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}