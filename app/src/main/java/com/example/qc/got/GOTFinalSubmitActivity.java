package com.example.qc.got;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.example.qc.parser.JsonParser;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class GOTFinalSubmitActivity extends AppCompatActivity {

    private ImageButton back_nav;
    private TextView tv_sampleno,tv_crop,tv_variety,tv_lono,tv_stage,tv_dosa;
    private String sampleno;

    private EditText et_obrdatefinal,et_noofplants_final,et_maleplants,et_per_maleplants,et_femaleplants,et_per_femaleplants,
            et_ooftypes,et_per_ooftypes,et_totalplants,et_per_totalplants,et_remark,et_retest_reason;
    private String retest="No";
    private CheckBox checkBox_retest;
    private LinearLayout ll_retest_reason;
    private LinearLayout ll_withsample;
    private RadioGroup radiosample;
    private Button btn_submit;
    private EditText et_blinepollen;
    private EditText et_per_blinepollen;
    private LinearLayout ll_male,ll_male_per,ll_bline,ll_bline_per;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gotfinal_submit);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        back_nav = findViewById(R.id.back_nav);

        sampleno = getIntent().getStringExtra("sampleno");

        tv_crop = findViewById(R.id.tv_crop);
        tv_variety = findViewById(R.id.tv_variety);
        tv_lono = findViewById(R.id.tv_lono);
        tv_stage = findViewById(R.id.tv_stage);
        tv_dosa = findViewById(R.id.tv_dosa);
        tv_sampleno = findViewById(R.id.tv_sampleno);

        btn_submit = findViewById(R.id.btn_submit);

        checkBox_retest = findViewById(R.id.checkBox_retest);
        ll_retest_reason = findViewById(R.id.ll_retest_reason);
        ll_withsample = findViewById(R.id.ll_withsample);
        radiosample = findViewById(R.id.radiosample);

        et_obrdatefinal = findViewById(R.id.et_obrdatefinal);
        et_noofplants_final = findViewById(R.id.et_noofplants_final);
        et_maleplants = findViewById(R.id.et_maleplants);
        et_per_maleplants = findViewById(R.id.et_per_maleplants);
        et_femaleplants = findViewById(R.id.et_femaleplants);
        et_per_femaleplants = findViewById(R.id.et_per_femaleplants);
        et_blinepollen = findViewById(R.id.et_blinepollen);
        et_per_blinepollen = findViewById(R.id.et_per_blinepollen);
        et_ooftypes = findViewById(R.id.et_ooftypes);
        et_per_ooftypes = findViewById(R.id.et_per_ooftypes);
        et_totalplants = findViewById(R.id.et_totalplants);
        et_per_totalplants = findViewById(R.id.et_per_totalplants);
        et_remark = findViewById(R.id.et_remark);
        et_retest_reason = findViewById(R.id.et_retest_reason);

        ll_male = findViewById(R.id.ll_male);
        ll_male_per = findViewById(R.id.ll_male_per);
        ll_bline = findViewById(R.id.ll_bline);
        ll_bline_per = findViewById(R.id.ll_bline_per);
    }

    private void init(){
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GOTFinalSubmitActivity.this, ReviewAndFinalSubmitActivity.class);
                startActivity(intent);
                finish();
            }
        });
        getSampleDetails();

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

        et_blinepollen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String blinepollen = String.valueOf(charSequence);
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !blinepollen.equals("")) {
                    float blinepollen1 = Float.parseFloat(blinepollen);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float blinepollenper = (blinepollen1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_blinepollen.setText(form.format(blinepollenper));
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
                String blinepollen = et_blinepollen.getText().toString().trim();
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !offtypesplants.equals("") && !femaleplants.equals("") && !maleplants.equals("")) {
                    float offtypesplants1 = Float.parseFloat(offtypesplants);
                    float femaleplants1 = Float.parseFloat(femaleplants);
                    float maleplants1 = Float.parseFloat(maleplants);
                    float blinepollen1 = Float.parseFloat(blinepollen);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float offtypesplantper = (offtypesplants1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    float total = 0;
                    if (tv_crop.getText().toString().equalsIgnoreCase("Bajra Seed")){
                        total = offtypesplants1+femaleplants1+blinepollen1;
                    }else {
                        total = offtypesplants1+femaleplants1+maleplants1;
                    }

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
                String blinepollen = et_blinepollen.getText().toString().trim();
                String per_blinepollen = et_per_blinepollen.getText().toString().trim();
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
                        confirmAlertSubmit(obrdatefinal,noofplants_final,maleplants,femaleplants,offtypesplants,totalplants,per_maleplants,per_femaleplants,per_ooftypes,per_totalplants,remark,retest_reason,retest,retesttype,blinepollen,per_blinepollen);
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
                        confirmAlertSubmit(obrdatefinal,noofplants_final,maleplants,femaleplants,offtypesplants,totalplants,per_maleplants,per_femaleplants,per_ooftypes,per_totalplants,remark,retest_reason,retest,retesttype,blinepollen,per_blinepollen);
                        //updateTransplantingData(noofseeds_direct,plotno,bedno,noofrows,sowingdate,direction,location_nursery,methodofsowing,retest_reason,noofcells,nooftrey,state,noofplants,retest);
                    }
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void confirmAlertSubmit(String obrdatefinal, String noofplants_final, String maleplants, String femaleplants, String offtypesplants, String totalplants, String per_maleplants, String per_femaleplants, String per_ooftypes, String per_totalplants, String remark, String retest_reason, String retest, String retesttype, String blinepollen, String per_blinepollen) {
        final Dialog dialog = new Dialog(GOTFinalSubmitActivity.this);
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
                updateFinalObservationData(obrdatefinal,noofplants_final,maleplants,femaleplants,offtypesplants,totalplants,per_maleplants,per_femaleplants,per_ooftypes,per_totalplants,remark,retest_reason,retest,retesttype,blinepollen,per_blinepollen);
            }
        });
    }

    private void updateFinalObservationData(String obrdatefinal, String noofplants_final, String maleplants, String femaleplants, String offtypesplants, String totalplants, String per_maleplants, String per_femaleplants, String per_ooftypes, String per_totalplants, String remark, String retest_reason, String retest, String retesttype, String blinepollen, String per_blinepollen) {
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
        params.put("fnobser_blineps", blinepollen);
        params.put("fnobser_blinepsper", per_blinepollen);
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

        new SendVolleyCall().executeProgram(GOTFinalSubmitActivity.this, AppConfig.SAMPLE_FINALOBRUPDATE_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitData(response);
                if (message.equalsIgnoreCase("Success")){
                    Intent intent = new Intent(GOTFinalSubmitActivity.this, ReviewAndFinalSubmitActivity.class);
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

                            if (details.getString("crop").equalsIgnoreCase("Bajra Seed")){
                                et_blinepollen.setText(details.getString("fnobser_blineps"));
                                et_per_blinepollen.setText(details.getString("fnobser_blinepsper"));
                                ll_male.setVisibility(View.GONE);
                                ll_male_per.setVisibility(View.GONE);
                                ll_bline.setVisibility(View.VISIBLE);
                                ll_bline_per.setVisibility(View.VISIBLE);
                            }

                            et_obrdatefinal.setText(details.getString("fnobser_obserdate"));
                            et_noofplants_final.setText(details.getString("fnobser_noofplants"));
                            et_maleplants.setText(details.getString("fnobser_maleplants"));
                            et_per_maleplants.setText(details.getString("fnobser_maleper"));
                            et_femaleplants.setText(details.getString("fnobser_femaleplants"));
                            et_per_femaleplants.setText(details.getString("fnobser_femaleper"));
                            et_ooftypes.setText(details.getString("fnobser_otherofftype"));
                            et_per_ooftypes.setText(details.getString("fnobser_otheroffper"));
                            et_totalplants.setText(details.getString("fnobser_total"));
                            et_per_totalplants.setText(details.getString("fnobser_totalper"));
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
        CommonVolleyCall.getInstance(GOTFinalSubmitActivity.this).addRequestQueue(strReq);
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