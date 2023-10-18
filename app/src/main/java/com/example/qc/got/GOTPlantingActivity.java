package com.example.qc.got;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
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

public class GOTPlantingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageButton back_nav;
    private LinearLayout ll_nurserysowing;
    private LinearLayout ll_directsowing;
    private CheckBox checkBox_retest;
    private LinearLayout ll_retest_reason;
    private TextView tv_sampleno;
    private TextView tv_date;
    private Button btn_submit;
    private EditText et_noofseeds_direct;
    private EditText et_plotno;
    private EditText et_rangeno;
    private EditText et_bedno;
    private EditText et_noofrows;
    private Spinner spinner_direction;
    private Spinner spinner_location_nursery;
    private RadioGroup radioGrpMos;
    private RadioGroup radioGrpNM;
    private EditText et_retest_reason;
    private EditText et_noofcells;
    private EditText et_nooftrey;
    private EditText et_noofplants;
    private  String retest = "No";
    private Spinner spinner_state;

    private ArrayList<String> stateArray;
    private ArrayAdapter<String> stateadapter;

    private ArrayList<String> locArray;
    private ArrayAdapter<String> locadapter;

    private int mYear, mMonth, mDay;
    private LinearLayout ll_withsample;
    private RadioGroup radiosample;
    private SampleGOTInfo sampleGOTInfo;
    private RadioButton radio_soilsowing;
    private RadioButton radio_cocopeatsowing;
    private RadioButton radio_direct;
    private RadioButton radio_nursery;
    private LinearLayout ll_nurserymedium;
    private LinearLayout ll_direction;
    private LinearLayout ll_treynumber;
    private EditText et_treynumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_o_t_planting);
        getSupportActionBar().hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        back_nav = findViewById(R.id.back_nav);
        ll_nurserysowing = findViewById(R.id.ll_nurserysowing);
        ll_directsowing = findViewById(R.id.ll_directsowing);
        checkBox_retest = findViewById(R.id.checkBox_retest);
        ll_retest_reason = findViewById(R.id.ll_retest_reason);
        ll_withsample = findViewById(R.id.ll_withsample);
        radiosample = findViewById(R.id.radiosample);
        TextView tv_crop = findViewById(R.id.tv_crop);
        TextView tv_variety = findViewById(R.id.tv_variety);
        TextView tv_lono = findViewById(R.id.tv_lono);
        TextView tv_stage = findViewById(R.id.tv_stage);
        TextView tv_dosa = findViewById(R.id.tv_dosa);
        tv_sampleno = findViewById(R.id.tv_sampleno);
        tv_date = findViewById(R.id.tv_date);
        btn_submit = findViewById(R.id.btn_submit);

        et_noofseeds_direct = findViewById(R.id.et_noofseeds_direct);
        et_plotno = findViewById(R.id.et_plotno);
        et_rangeno = findViewById(R.id.et_rangeno);
        et_bedno = findViewById(R.id.et_bedno);
        et_noofrows = findViewById(R.id.et_noofrows);
        et_retest_reason = findViewById(R.id.et_retest_reason);
        et_noofcells = findViewById(R.id.et_noofcells);
        et_nooftrey = findViewById(R.id.et_nooftrey);
        et_noofplants = findViewById(R.id.et_noofplants);
        et_treynumber = findViewById(R.id.et_treynumber);
        spinner_direction = findViewById(R.id.spinner_direction);
        spinner_location_nursery = findViewById(R.id.spinner_location_nursery);
        spinner_state = findViewById(R.id.spinner_state);

        radioGrpMos = findViewById(R.id.radioGrpMos);
        radioGrpNM = findViewById(R.id.radioGrpNM);
        radio_soilsowing = findViewById(R.id.radio_soilsowing);
        radio_cocopeatsowing = findViewById(R.id.radio_cocopeatsowing);
        radio_direct = findViewById(R.id.radio_direct);
        radio_nursery = findViewById(R.id.radio_nursery);
        ll_nurserymedium = findViewById(R.id.ll_nurserymedium);
        ll_direction = findViewById(R.id.ll_direction);
        ll_treynumber = findViewById(R.id.ll_treynumber);
        LinearLayout ll_noofplants = findViewById(R.id.ll_noofplants);

        sampleGOTInfo = (SampleGOTInfo) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_SAMPLEINFO_OBJ,SampleGOTInfo.class);
        tv_crop.setText(sampleGOTInfo.getCrop());
        tv_variety.setText(sampleGOTInfo.getVariety());
        tv_lono.setText(sampleGOTInfo.getLotno());
        tv_stage.setText(sampleGOTInfo.getTrstage());
        tv_dosa.setText(sampleGOTInfo.getAckdate());
        tv_sampleno.setText(sampleGOTInfo.getSampleno());
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tv_date.setText(date);

        ll_noofplants.setVisibility(View.GONE);

        //String[] direction_list = {"E-W","E-N","E-S","W-E","W-N","W-S","N-E","N-W","N-S","S-E","S-W","S-N","E-W-E","E-N-E","E-S-E","W-E-W","W-N-W","W-S-W","N-E-N","N-W-N","N-S-N","S-E-S","S-W-S","S-N-S"};
        String[] direction_list = {"E-W","W-E","E-W-E","W-E-W","N-S","S-N","N-S-N","S-N-S"};

        ArrayAdapter<String> adapter_direction = new ArrayAdapter<>(GOTPlantingActivity.this, android.R.layout.simple_spinner_item, direction_list);
        adapter_direction.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_direction.setAdapter(adapter_direction);
    }

    private void init() {
        stateArray = new ArrayList<>();
        stateArray.add(0, "Select");
        stateadapter = Utils.getInstance().spinnerAdapter(GOTPlantingActivity.this, stateArray);
        spinner_state.setAdapter(stateadapter);

        locArray = new ArrayList<>();
        locArray.add(0, "Select");
        locadapter = Utils.getInstance().spinnerAdapter(GOTPlantingActivity.this, locArray);
        spinner_location_nursery.setAdapter(locadapter);

        getStateList();

        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GOTPlantingActivity.this, GOTResultStepsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(GOTPlantingActivity.this,
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
                String location_nursery = spinner_location_nursery.getSelectedItem().toString().trim();
                String state = spinner_state.getSelectedItem().toString().trim();
                String sowingdate = tv_date.getText().toString().trim();
                String noofseeds_direct = et_noofseeds_direct.getText().toString().trim();
                String plotno = et_plotno.getText().toString().trim();
                String rangeno = et_rangeno.getText().toString().trim();
                String bedno = et_bedno.getText().toString().trim();
                String noofrows = et_noofrows.getText().toString().trim();
                String noofcells = et_noofcells.getText().toString().trim();
                String nooftrey = et_nooftrey.getText().toString().trim();
                String noofplants = et_noofplants.getText().toString().trim();
                String retest_reason = et_retest_reason.getText().toString().trim();
                String treynumber = et_treynumber.getText().toString().trim();
                int method = radioGrpMos.getCheckedRadioButtonId();
                RadioButton mos = findViewById(method);
                String methodofsowing = mos.getText().toString();

                int medium = radioGrpNM.getCheckedRadioButtonId();
                RadioButton sm = findViewById(medium);
                String nurserymedium = sm.getText().toString();

                int rdsample = radiosample.getCheckedRadioButtonId();
                RadioButton rdsamp = findViewById(rdsample);
                String retesttype = rdsamp.getText().toString();

                if(checkBox_retest.isChecked()) {
                    if (retest_reason.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter Re-Test Reason", Toast.LENGTH_LONG).show();
                    }else {
                        retest = "Yes";
                        confirmAlertSGT(noofseeds_direct,plotno,rangeno,bedno,noofrows,sowingdate,direction,location_nursery,methodofsowing,retest_reason,noofcells,nooftrey,state,noofplants,retest,retesttype,nurserymedium,treynumber);
                        //updateSwoingData(noofseeds_direct,plotno,bedno,noofrows,sowingdate,direction,location_nursery,methodofsowing,retest_reason,noofcells,nooftrey,state,noofplants,retest);
                    }
                }
                else {
                    retest = "No";
                    if (direction.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Select Direction", Toast.LENGTH_LONG).show();
                    }else if (location_nursery.equalsIgnoreCase("Select")){
                        Toast.makeText(getApplicationContext(), "Select Location", Toast.LENGTH_LONG).show();
                    }else if (state.equalsIgnoreCase("Select")){
                        Toast.makeText(getApplicationContext(), "Select State", Toast.LENGTH_LONG).show();
                    }else if (sowingdate.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Select Sowing Date", Toast.LENGTH_LONG).show();
                    }else if (noofseeds_direct.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Seeds", Toast.LENGTH_LONG).show();
                    }else if (plotno.equalsIgnoreCase("") && methodofsowing.equalsIgnoreCase("Direct Sowing")){
                        Toast.makeText(getApplicationContext(), "Enter Plot No.", Toast.LENGTH_LONG).show();
                    }else if (rangeno.equalsIgnoreCase("") && methodofsowing.equalsIgnoreCase("Direct Sowing")){
                        Toast.makeText(getApplicationContext(), "Enter Range No.", Toast.LENGTH_LONG).show();
                    }else if (bedno.equalsIgnoreCase("") && methodofsowing.equalsIgnoreCase("Direct Sowing")){
                        Toast.makeText(getApplicationContext(), "Enter Bed No.", Toast.LENGTH_LONG).show();
                    }else if (noofrows.equalsIgnoreCase("") && methodofsowing.equalsIgnoreCase("Direct Sowing")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Rows", Toast.LENGTH_LONG).show();
                    }else if (noofcells.equalsIgnoreCase("") && methodofsowing.equalsIgnoreCase("Cocopeat Sowing") && !sampleGOTInfo.getCrop().equalsIgnoreCase("Paddy Seed")  && !sampleGOTInfo.getCrop().equalsIgnoreCase("Bajra Seed")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Cells", Toast.LENGTH_LONG).show();
                    }else if (nooftrey.equalsIgnoreCase("") && methodofsowing.equalsIgnoreCase("Cocopeat Sowing")  && !sampleGOTInfo.getCrop().equalsIgnoreCase("Paddy Seed")  && !sampleGOTInfo.getCrop().equalsIgnoreCase("Bajra Seed")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Treys", Toast.LENGTH_LONG).show();
                    }else if (noofplants.equalsIgnoreCase("") && methodofsowing.equalsIgnoreCase("Cocopeat Sowing") && !sampleGOTInfo.getCrop().equalsIgnoreCase("Paddy Seed")  && !sampleGOTInfo.getCrop().equalsIgnoreCase("Bajra Seed")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Plants", Toast.LENGTH_LONG).show();
                    }else {
                        confirmAlertSGT(noofseeds_direct,plotno,rangeno,bedno,noofrows,sowingdate,direction,location_nursery,methodofsowing,retest_reason,noofcells,nooftrey,state,noofplants,retest,retesttype,nurserymedium,treynumber);
                        //updateSwoingData(noofseeds_direct,plotno,bedno,noofrows,sowingdate,direction,location_nursery,methodofsowing,retest_reason,noofcells,nooftrey,state,noofplants,retest);
                    }
                }
            }
        });
        spinner_state.setOnItemSelectedListener(GOTPlantingActivity.this);
    }

    private void confirmAlertSGT(final String noofseeds_direct, final String plotno, final String rangeno, final String bedno, final String noofrows, final String sowingdate, final String direction, final String location_nursery, final String methodofsowing, final String retest_reason, final String noofcells, final String nooftrey, final String state, final String noofplants, final String retest, final String retesttype, final String nurserymedium, final String treynumber) {
        final Dialog dialog = new Dialog(GOTPlantingActivity.this);
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
                updateSwoingData(noofseeds_direct,plotno,rangeno,bedno,noofrows,sowingdate,direction,location_nursery,methodofsowing,retest_reason,noofcells,nooftrey,state,noofplants,retest,retesttype,nurserymedium,treynumber);
            }
        });
    }

    private void getStateList() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);

        new SendVolleyCall().executeProgram(GOTPlantingActivity.this, AppConfig.GETSATELIST_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                stateArray = JsonParser.getInstance().parseStateList(GOTPlantingActivity.this, response);
                String message = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MESSAGE);
                if (message.equalsIgnoreCase("Success")){
                    stateadapter = Utils.getInstance().spinnerAdapter(GOTPlantingActivity.this, stateArray);
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

        new SendVolleyCall().executeProgram(GOTPlantingActivity.this, AppConfig.GETLOCATIONLIST_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                locArray = JsonParser.getInstance().parseLocList(GOTPlantingActivity.this, response);
                String message = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MESSAGE);
                if (message.equalsIgnoreCase("Success")){
                    locadapter = Utils.getInstance().spinnerAdapter(GOTPlantingActivity.this, locArray);
                    spinner_location_nursery.setAdapter(locadapter);
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

    private void updateSwoingData(String noofseeds_direct, String plotno, String rangeno, String bedno, String noofrows, String sowingdate, String direction, String location_nursery, String methodofsowing, String retest_reason, String noofcells, String nooftrey, String state, String noofplants, String retest, String retesttype, String nurserymedium, String treynumber) {
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
        params.put("dosow", sowingdate);
        params.put("sow_nurplotno", plotno);
        params.put("sow_nurrangeno", rangeno);
        params.put("sow_noofseeds", noofseeds_direct);
        params.put("sow_sptype", methodofsowing);
        params.put("sow_loc", location_nursery);
        params.put("sow_noofcellstray", noofcells);
        params.put("sow_nooftraylot", nooftrey);
        params.put("sow_bedno", bedno);
        params.put("sow_direction", direction);
        params.put("sow_noofrows", noofrows);
        params.put("sow_noofplants", noofplants);
        params.put("retest_type", retest);
        params.put("retest_reason", retest_reason);
        params.put("sow_state", state);
        params.put("retest", retest);
        params.put("retesttype", retesttype);
        params.put("nurserymedium", nurserymedium);
        params.put("treynumber", treynumber);

        new SendVolleyCall().executeProgram(GOTPlantingActivity.this, AppConfig.SAMPLE_SOWINGUPDATE_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitData(response);
                if (message.equalsIgnoreCase("Success")){
                    Intent intent = new Intent(GOTPlantingActivity.this, SampleGOTResultHomeActivity.class);
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

    public void direct_sowing(View v)
    {
        /*if (!sampleGOTInfo.getCrop().equalsIgnoreCase("Paddy Seed") && !sampleGOTInfo.getCrop().equalsIgnoreCase("Maize Seed") && !sampleGOTInfo.getCrop().equalsIgnoreCase("Bajra Seed")) {
            ll_directsowing.setVisibility(View.VISIBLE);
            ll_nurserysowing.setVisibility(View.GONE);
        }*/
        ll_directsowing.setVisibility(View.VISIBLE);
        ll_nurserysowing.setVisibility(View.GONE);
        ll_nurserymedium.setVisibility(View.GONE);
    }

    public void nursery_sowing(View v)
    {
        /*if (!sampleGOTInfo.getCrop().equalsIgnoreCase("Paddy Seed") && !sampleGOTInfo.getCrop().equalsIgnoreCase("Maize Seed") && !sampleGOTInfo.getCrop().equalsIgnoreCase("Bajra Seed")) {
            ll_directsowing.setVisibility(View.GONE);
            ll_nurserysowing.setVisibility(View.VISIBLE);
        }*/
        ll_nurserymedium.setVisibility(View.VISIBLE);
        if (radio_soilsowing.isChecked()) {
            ll_directsowing.setVisibility(View.VISIBLE);
            ll_nurserysowing.setVisibility(View.GONE);
            ll_treynumber.setVisibility(View.GONE);
            ll_direction.setVisibility(View.VISIBLE);
        }else {
            ll_directsowing.setVisibility(View.GONE);
            ll_nurserysowing.setVisibility(View.VISIBLE);
            ll_treynumber.setVisibility(View.VISIBLE);
            ll_direction.setVisibility(View.GONE);
        }
    }

    public void soil_sowing(View v)
    {
        /*if (radio_direct.isChecked()) {
            ll_directsowing.setVisibility(View.VISIBLE);
            ll_nurserysowing.setVisibility(View.GONE);
        }else {
            ll_directsowing.setVisibility(View.GONE);
            ll_nurserysowing.setVisibility(View.VISIBLE);
        }*/
        ll_directsowing.setVisibility(View.VISIBLE);
        ll_nurserysowing.setVisibility(View.GONE);
        ll_treynumber.setVisibility(View.GONE);
        ll_direction.setVisibility(View.VISIBLE);
    }

    public void cocopeat_sowing(View v)
    {
        ll_directsowing.setVisibility(View.GONE);
        ll_nurserysowing.setVisibility(View.VISIBLE);
        ll_treynumber.setVisibility(View.VISIBLE);
        ll_direction.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(GOTPlantingActivity.this, GOTResultStepsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}