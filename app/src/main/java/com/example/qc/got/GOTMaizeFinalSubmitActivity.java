package com.example.qc.got;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class GOTMaizeFinalSubmitActivity extends AppCompatActivity {
    private ImageButton back_nav;
    private CheckBox checkBox_retest;
    private LinearLayout ll_retest_reason;
    private EditText et_noofplants_final;
    private EditText et_selfplants;
    private EditText et_per_Selfplants;
    private EditText et_maleplants;
    private EditText et_per_maleplants;
    private EditText et_pencilplants;
    private EditText et_per_pencilplants;
    private EditText et_ooftypes;
    private EditText et_per_ooftypes;
    private EditText et_total;
    private EditText et_per_total;
    private EditText et_obrdatefinal;
    private EditText et_remark;
    private EditText et_retest_reason;
    private TextView tv_sampleno;
    private Button btn_submit;
    private String retest="No";
    private LinearLayout ll_withsample;
    private RadioGroup radiosample;
    private LinearLayout ll_directblock;
    private LinearLayout ll_nurseryblock;
    private TextView tv_crop,tv_variety,tv_lono,tv_stage,tv_dosa;
    private String sampleno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gotmaize_final_submit);
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

        sampleno = getIntent().getStringExtra("sampleno");

        et_noofplants_final = findViewById(R.id.et_noofplants_final);
        et_selfplants = findViewById(R.id.et_selfplants);
        et_per_Selfplants = findViewById(R.id.et_per_Selfplants);
        et_maleplants = findViewById(R.id.et_maleplants);
        et_per_maleplants = findViewById(R.id.et_per_maleplants);
        et_ooftypes = findViewById(R.id.et_ooftypes);
        et_per_ooftypes = findViewById(R.id.et_per_ooftypes);
        et_pencilplants = findViewById(R.id.et_pencilplants);
        et_per_pencilplants = findViewById(R.id.et_per_pencilplants);
        et_total = findViewById(R.id.et_total);
        et_per_total = findViewById(R.id.et_per_total);
        et_obrdatefinal = findViewById(R.id.et_obrdatefinal);
        et_remark = findViewById(R.id.et_remark);
        et_retest_reason = findViewById(R.id.et_retest_reason);

        tv_crop = findViewById(R.id.tv_crop);
        tv_variety = findViewById(R.id.tv_variety);
        tv_lono = findViewById(R.id.tv_lono);
        tv_stage = findViewById(R.id.tv_stage);
        tv_dosa = findViewById(R.id.tv_dosa);
        tv_sampleno = findViewById(R.id.tv_sampleno);
        btn_submit = findViewById(R.id.btn_submit);

        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        et_obrdatefinal.setText(date);

    }

    private void init() {
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GOTMaizeFinalSubmitActivity.this, GOTResultStepsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        getSampleDetails();

        final DecimalFormat form = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
        et_selfplants.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String selfplants = String.valueOf(charSequence);
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && selfplants.equals("")) {
                    float selfplants1 = Float.parseFloat(selfplants);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float selfplantsper = (selfplants1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_Selfplants.setText(form.format(selfplantsper));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_maleplants.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String maleplants = String.valueOf(charSequence);
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && maleplants.equals("")) {
                    float maleplants1 = Float.parseFloat(maleplants);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float maleplantsper = (maleplants1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_maleplants.setText(form.format(maleplantsper));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_pencilplants.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pencilplants = String.valueOf(charSequence);
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && pencilplants.equals("")) {
                    float pencilplants1 = Float.parseFloat(pencilplants);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float pencilplantsper = (pencilplants1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_pencilplants.setText(form.format(pencilplantsper));
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
                String ooftypes = String.valueOf(charSequence);
                String selfplants = et_selfplants.getText().toString().trim();
                String maleplants = et_maleplants.getText().toString().trim();
                String pencilplants = et_pencilplants.getText().toString().trim();
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && ooftypes.equals("")) {
                    float ooftypes1 = Float.parseFloat(ooftypes);
                    float selfplants1 = Float.parseFloat(selfplants);
                    float maleplants1 = Float.parseFloat(maleplants);
                    float pencilplants1 = Float.parseFloat(pencilplants);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float ooftypesper = (ooftypes1 * 100) / noofplants_final1;
                    float total = selfplants1+maleplants1+pencilplants1+ooftypes1;
                    float totalper = (total * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_ooftypes.setText(form.format(ooftypesper));
                    et_total.setText(String.valueOf(total));
                    et_per_total.setText(form.format(totalper));
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
                String selfplants = et_selfplants.getText().toString().trim();
                String maleplants = et_maleplants.getText().toString().trim();
                String offtypesplants = et_ooftypes.getText().toString().trim();
                String pencilplants = et_pencilplants.getText().toString().trim();
                String total = et_total.getText().toString().trim();
                String per_Selfplants = et_per_Selfplants.getText().toString().trim();
                String per_femaleplants = et_per_maleplants.getText().toString().trim();
                String per_ooftypes = et_per_ooftypes.getText().toString().trim();
                String per_pencilplants = et_per_pencilplants.getText().toString().trim();
                String per_total = et_per_total.getText().toString().trim();
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
                        confirmAlertSubmit(obrdatefinal,noofplants_final,selfplants,maleplants,offtypesplants,pencilplants,total,per_Selfplants,per_femaleplants,per_ooftypes,per_pencilplants,per_total,remark,retest_reason,retest,retesttype);
                        //updateTransplantingData(plotno,bedno,noofrows,sowingdate,direction,location_nursery,retest_reason,state,noofplants,retest,range);
                    }
                } else {
                    retest = "No";
                    if (obrdatefinal.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Select Observatin Date", Toast.LENGTH_LONG).show();
                    }else if (noofplants_final.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Plants", Toast.LENGTH_LONG).show();
                    }else if (selfplants.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Self Plant", Toast.LENGTH_LONG).show();
                    }else if (maleplants.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Male Plants", Toast.LENGTH_LONG).show();
                    }else if (offtypesplants.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Off Type Plants", Toast.LENGTH_LONG).show();
                    }else if (pencilplants.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Pencil Plants", Toast.LENGTH_LONG).show();
                    }else {
                        confirmAlertSubmit(obrdatefinal,noofplants_final,selfplants,maleplants,offtypesplants,pencilplants,total,per_Selfplants,per_femaleplants,per_ooftypes,per_pencilplants,per_total,remark,retest_reason,retest,retesttype);
                        //updateTransplantingData(noofseeds_direct,plotno,bedno,noofrows,sowingdate,direction,location_nursery,methodofsowing,retest_reason,noofcells,nooftrey,state,noofplants,retest);
                    }
                }
            }
        });
    }

    private void confirmAlertSubmit(final String obrdatefinal, final String noofplants_final, final String selfplants, final String maleplants, final String offtypesplants, final String pencilplants, final String total, final String per_selfplants, final String per_femaleplants, final String per_ooftypes, final String per_pencilplants, final String per_total, final String remark, final String retest_reason, final String retest, final String retesttype) {
        final Dialog dialog = new Dialog(GOTMaizeFinalSubmitActivity.this);
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
                updateFinalObservationData(obrdatefinal,noofplants_final,selfplants,maleplants,offtypesplants,pencilplants,total,per_selfplants,per_femaleplants,per_ooftypes,per_pencilplants,per_total,remark,retest_reason,retest,retesttype);
            }
        });
    }

    private void updateFinalObservationData(String obrdatefinal, String noofplants_final, String selfplants, String maleplants, String offtypesplants, String pencilplants, String total, String per_selfplants, String per_maleplants, String per_ooftypes, String per_pencilplants, String per_total, String remark, String retest_reason, String retest, String retesttype) {
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
        params.put("fnobser_femaleplants", "");
        params.put("fnobser_femaleper", "");
        params.put("fnobser_total", "");
        params.put("fnobser_totalper", "");
        params.put("fnobser_otherofftype", offtypesplants);
        params.put("fnobser_otheroffper", per_ooftypes);
        params.put("fnobser_blineps", "");
        params.put("fnobser_blinepsper", "");
        params.put("fnobser_totalofftype", total);
        params.put("fnobser_totalofftypeper", per_total);
        params.put("fnobser_self", selfplants);
        params.put("fnobser_selfper", per_selfplants);
        params.put("fnobser_pencilplants", pencilplants);
        params.put("fnobser_pencilplantsper", per_pencilplants);
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

        new SendVolleyCall().executeProgram(GOTMaizeFinalSubmitActivity.this, AppConfig.SAMPLE_FINALOBRUPDATE_PROGRAM1, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitData(response);
                if (message.equalsIgnoreCase("Success")){
                    Intent intent = new Intent(GOTMaizeFinalSubmitActivity.this, ReviewAndFinalSubmitActivity.class);
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

    private void getSampleDetails() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.COMMON_URL + AppConfig.SAMPLE_PENDINGSAMPLEDETAILS_PROGRAM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json

                    if (!error) {
                        //success
                        JSONObject user = jObj.getJSONObject("user");
                        JSONArray samplearray = user.getJSONArray("infodataarray");
                        for (int i = 0; i < samplearray.length(); i++) {
                            JSONObject details = samplearray.getJSONObject(i);
                            tv_crop.setText(details.getString("crop"));
                            tv_variety.setText(details.getString("variety"));
                            tv_lono.setText(details.getString("lotno"));
                            tv_stage.setText(details.getString("trstage"));
                            tv_dosa.setText(details.getString("scdate"));
                            tv_sampleno.setText(details.getString("sampleno"));

                            et_obrdatefinal.setText(details.getString("fnobser_obserdate"));
                            et_noofplants_final.setText(details.getString("fnobser_noofplants"));
                            et_maleplants.setText(details.getString("fnobser_maleplants"));
                            et_per_maleplants.setText(details.getString("fnobser_maleper"));
                            et_selfplants.setText(details.getString("fnobser_self"));
                            et_per_Selfplants.setText(details.getString("fnobser_selfper"));
                            et_ooftypes.setText(details.getString("fnobser_otherofftype"));
                            et_per_ooftypes.setText(details.getString("fnobser_otheroffper"));
                            et_pencilplants.setText(details.getString("fnobser_pencilplants"));
                            et_per_pencilplants.setText(details.getString("fnobser_pencilplantsper"));
                            et_remark.setText(details.getString("fnobser_remarks"));
                            String retestflg = details.getString("retestflg");

                            if(retestflg.equalsIgnoreCase("1")) {
                                // true,do the task
                                ll_retest_reason.setVisibility(View.VISIBLE);
                                ll_withsample.setVisibility(View.VISIBLE);
                                retest = "Yes";
                            } else {
                                ll_retest_reason.setVisibility(View.GONE);
                                ll_withsample.setVisibility(View.GONE);
                                retest = "No";
                            }
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
                params.put("sampleno", sampleno);
                return params;
            }

        };

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        CommonVolleyCall.getInstance(GOTMaizeFinalSubmitActivity.this).addRequestQueue(strReq);
    }

    public void retest_onClick(View v1)
    {
        if(checkBox_retest.isChecked()) {
            // true,do the task
            ll_retest_reason.setVisibility(View.VISIBLE);
            ll_withsample.setVisibility(View.VISIBLE);
            retest = "Yes";
        } else {
            ll_retest_reason.setVisibility(View.GONE);
            ll_withsample.setVisibility(View.GONE);
            retest = "No";
        }
    }
}