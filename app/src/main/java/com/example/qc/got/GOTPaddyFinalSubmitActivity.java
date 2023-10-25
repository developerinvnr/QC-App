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

public class GOTPaddyFinalSubmitActivity extends AppCompatActivity {
    private ImageButton back_nav;
    private CheckBox checkBox_retest;
    private LinearLayout ll_retest_reason;
    private EditText et_noofplants_final;
    private EditText et_obrdatefinal;
    private EditText et_remark;
    private EditText et_retest_reason;
    private TextView tv_sampleno;
    private Button btn_submit;
    private String retest="No";
    private EditText et_aline;
    private EditText et_per_aline;
    private EditText et_blineshadder;
    private EditText et_per_blineshadder;
    private EditText et_long_grain;
    private EditText et_per_long_grain;
    private EditText et_finegrain;
    private EditText et_per_finegrain;
    private EditText et_boldgrain;
    private EditText et_per_boldgrain;
    private EditText et_redtip;
    private EditText et_per_redtip;
    private EditText et_tall;
    private EditText et_per_tall;
    private EditText et_late;
    private EditText et_per_late;
    private EditText et_fertile;
    private EditText et_per_fertile;
    private EditText et_sterile;
    private EditText et_per_sterile;
    private EditText et_outcross;
    private EditText et_per_outcross;
    private EditText et_Total;
    private EditText et_per_total;
    private LinearLayout ll_withsample;
    private RadioGroup radiosample;
    private LinearLayout ll_directblock;
    private LinearLayout ll_nurseryblock;
    private TextView tv_crop,tv_variety,tv_lono,tv_stage,tv_dosa;
    private String sampleno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gotpaddy_final_submit);
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
        et_aline = findViewById(R.id.et_aline);
        et_per_aline = findViewById(R.id.et_per_aline);
        et_blineshadder = findViewById(R.id.et_blineshadder);
        et_per_blineshadder = findViewById(R.id.et_per_blineshadder);
        et_long_grain = findViewById(R.id.et_long_grain);
        et_per_long_grain = findViewById(R.id.et_per_long_grain);
        et_finegrain = findViewById(R.id.et_finegrain);
        et_per_finegrain = findViewById(R.id.et_per_finegrain);
        et_boldgrain = findViewById(R.id.et_boldgrain);
        et_per_boldgrain = findViewById(R.id.et_per_boldgrain);
        et_redtip = findViewById(R.id.et_redtip);
        et_per_redtip = findViewById(R.id.et_per_redtip);
        et_tall = findViewById(R.id.et_tall);
        et_per_tall = findViewById(R.id.et_per_tall);
        et_late = findViewById(R.id.et_late);
        et_per_late = findViewById(R.id.et_per_late);
        et_fertile = findViewById(R.id.et_fertile);
        et_per_fertile = findViewById(R.id.et_per_fertile);
        et_sterile = findViewById(R.id.et_sterile);
        et_per_sterile = findViewById(R.id.et_per_sterile);
        et_outcross = findViewById(R.id.et_outcross);
        et_per_outcross = findViewById(R.id.et_per_outcross);
        et_Total = findViewById(R.id.et_Total);
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
                Intent intent = new Intent(GOTPaddyFinalSubmitActivity.this, ReviewAndFinalSubmitActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getSampleDetails();

        final DecimalFormat form = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
        et_aline.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String aline = String.valueOf(charSequence);
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !aline.equals("")) {
                    float aline1 = Float.parseFloat(aline);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float alineper = (aline1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_aline.setText(form.format(alineper));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_blineshadder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String blineshadder = String.valueOf(charSequence);
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !blineshadder.equals("")) {
                    float blineshadder1 = Float.parseFloat(blineshadder);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float blineshadderper = (blineshadder1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_blineshadder.setText(form.format(blineshadderper));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_long_grain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String long_grain = String.valueOf(charSequence);
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !long_grain.equals("")) {
                    float long_grain1 = Float.parseFloat(long_grain);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float long_grainper = (long_grain1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_long_grain.setText(form.format(long_grainper));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_finegrain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String finegrain = String.valueOf(charSequence);
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !finegrain.equals("")) {
                    float finegrain1 = Float.parseFloat(finegrain);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float finegrainper = (finegrain1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_finegrain.setText(form.format(finegrainper));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_boldgrain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String boldgrain = String.valueOf(charSequence);
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !boldgrain.equals("")) {
                    float boldgrain1 = Float.parseFloat(boldgrain);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float boldgrainper = (boldgrain1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_boldgrain.setText(form.format(boldgrainper));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_redtip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String redtip = String.valueOf(charSequence);
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !redtip.equals("")) {
                    float redtip1 = Float.parseFloat(redtip);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float redtipper = (redtip1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_boldgrain.setText(form.format(redtipper));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_tall.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String tall = String.valueOf(charSequence);
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !tall.equals("")) {
                    float tall1 = Float.parseFloat(tall);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float tallper = (tall1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_tall.setText(form.format(tallper));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_late.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String late = String.valueOf(charSequence);
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !late.equals("")) {
                    float late1 = Float.parseFloat(late);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float lateper = (late1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_late.setText(form.format(lateper));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_fertile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String fertile = String.valueOf(charSequence);
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !fertile.equals("")) {
                    float fertile1 = Float.parseFloat(fertile);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float fertileper = (fertile1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_fertile.setText(form.format(fertileper));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_sterile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String sterile = String.valueOf(charSequence);
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !sterile.equals("")) {
                    float sterile1 = Float.parseFloat(sterile);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float sterileper = (sterile1 * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_sterile.setText(form.format(sterileper));
                }else {
                    Toast.makeText(getApplicationContext(), "Enter All Plants Numbers", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_outcross.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String outcross = String.valueOf(charSequence);
                String aline = et_aline.getText().toString().trim();
                String blineshadder = et_blineshadder.getText().toString().trim();
                String long_grain = et_long_grain.getText().toString().trim();
                String finegrain = et_finegrain.getText().toString().trim();
                String boldgrain = et_boldgrain.getText().toString().trim();
                String redtip = et_redtip.getText().toString().trim();
                String tall = et_tall.getText().toString().trim();
                String late = et_late.getText().toString().trim();
                String fertile = et_fertile.getText().toString().trim();
                String sterile = et_sterile.getText().toString().trim();
                String noofplants_final = et_noofplants_final.getText().toString().trim();
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !outcross.equals("") && !aline.equals("") && !blineshadder.equals("") && !long_grain.equals("") && !finegrain.equals("") && !boldgrain.equals("") && !redtip.equals("") && !tall.equals("") && !late.equals("") && !fertile.equals("") && !sterile.equals("")) {
                    float outcross1 = Float.parseFloat(outcross);
                    float aline1 = Float.parseFloat(aline);
                    float blineshadder1 = Float.parseFloat(blineshadder);
                    float long_grain1 = Float.parseFloat(long_grain);
                    float finegrain1 = Float.parseFloat(finegrain);
                    float boldgrain1 = Float.parseFloat(boldgrain);
                    float redtip1 = Float.parseFloat(redtip);
                    float tall1 = Float.parseFloat(tall);
                    float late1 = Float.parseFloat(late);
                    float fertile1 = Float.parseFloat(fertile);
                    float sterile1 = Float.parseFloat(sterile);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float outcrossper = (outcross1 * 100) / noofplants_final1;
                    float total = aline1+blineshadder1+long_grain1+finegrain1+boldgrain1+redtip1+tall1+late1+fertile1+sterile1+outcross1;
                    float pertotal = (total * 100) / noofplants_final1;
                    //int perround = Math.round(normalmeanper);
                    et_per_outcross.setText(form.format(outcrossper));
                    et_Total.setText(String.valueOf(total));
                    et_per_total.setText(form.format(pertotal));
                }else {
                    Toast.makeText(getApplicationContext(), "Enter All Plants Numbers", Toast.LENGTH_LONG).show();
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
                String outcross = String.valueOf(charSequence);
                String aline = et_aline.getText().toString().trim();
                String blineshadder = et_blineshadder.getText().toString().trim();
                String long_grain = et_long_grain.getText().toString().trim();
                String finegrain = et_finegrain.getText().toString().trim();
                String boldgrain = et_boldgrain.getText().toString().trim();
                String redtip = et_redtip.getText().toString().trim();
                String tall = et_tall.getText().toString().trim();
                String late = et_late.getText().toString().trim();
                String fertile = et_fertile.getText().toString().trim();
                String sterile = et_sterile.getText().toString().trim();
                String noofplants_final = String.valueOf(charSequence);
                if (!noofplants_final.equals("") && !noofplants_final.equals("0") && !outcross.equals("") && !aline.equals("") && !blineshadder.equals("") && !long_grain.equals("") && !finegrain.equals("") && !boldgrain.equals("") && !redtip.equals("") && !tall.equals("") && !late.equals("") && !fertile.equals("") && !sterile.equals("")) {
                    float outcross1 = Float.parseFloat(outcross);
                    float aline1 = Float.parseFloat(aline);
                    float blineshadder1 = Float.parseFloat(blineshadder);
                    float long_grain1 = Float.parseFloat(long_grain);
                    float finegrain1 = Float.parseFloat(finegrain);
                    float boldgrain1 = Float.parseFloat(boldgrain);
                    float redtip1 = Float.parseFloat(redtip);
                    float tall1 = Float.parseFloat(tall);
                    float late1 = Float.parseFloat(late);
                    float fertile1 = Float.parseFloat(fertile);
                    float sterile1 = Float.parseFloat(sterile);
                    float noofplants_final1 = Float.parseFloat(noofplants_final);
                    float outcrossper = (outcross1 * 100) / noofplants_final1;
                    float alineper = (aline1 * 100) / noofplants_final1;
                    float blineshadderper = (blineshadder1 * 100) / noofplants_final1;
                    float long_grainper = (long_grain1 * 100) / noofplants_final1;
                    float finegrainper = (finegrain1 * 100) / noofplants_final1;
                    float boldgrainper = (boldgrain1 * 100) / noofplants_final1;
                    float redtipper = (redtip1 * 100) / noofplants_final1;
                    float tallper = (tall1 * 100) / noofplants_final1;
                    float lateper = (late1 * 100) / noofplants_final1;
                    float fertileper = (fertile1 * 100) / noofplants_final1;
                    float sterileper = (sterile1 * 100) / noofplants_final1;
                    et_per_aline.setText(form.format(alineper));
                    et_per_blineshadder.setText(String.valueOf(blineshadderper));
                    et_per_long_grain.setText(String.valueOf(long_grainper));
                    et_per_finegrain.setText(String.valueOf(finegrainper));
                    et_per_boldgrain.setText(String.valueOf(boldgrainper));
                    et_per_redtip.setText(String.valueOf(redtipper));
                    et_per_tall.setText(String.valueOf(tallper));
                    et_per_late.setText(String.valueOf(lateper));
                    et_per_fertile.setText(String.valueOf(fertileper));
                    et_per_sterile.setText(String.valueOf(sterileper));
                    et_per_outcross.setText(String.valueOf(outcrossper));
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
                String aline = et_aline.getText().toString().trim();
                String per_aline = et_per_aline.getText().toString().trim();
                String blineshadder = et_blineshadder.getText().toString().trim();
                String per_blineshadder = et_per_blineshadder.getText().toString().trim();
                String long_grain = et_long_grain.getText().toString().trim();
                String per_long_grain = et_per_long_grain.getText().toString().trim();
                String finegrain = et_finegrain.getText().toString().trim();
                String per_finegrain = et_per_finegrain.getText().toString().trim();
                String boldgrain = et_boldgrain.getText().toString().trim();
                String per_boldgrain = et_per_boldgrain.getText().toString().trim();
                String redtip = et_redtip.getText().toString().trim();
                String per_redtip = et_per_redtip.getText().toString().trim();
                String tall = et_tall.getText().toString().trim();
                String per_tall = et_per_tall.getText().toString().trim();
                String late = et_late.getText().toString().trim();
                String per_late = et_per_late.getText().toString().trim();
                String fertile = et_fertile.getText().toString().trim();
                String per_fertile = et_per_fertile.getText().toString().trim();
                String sterile = et_sterile.getText().toString().trim();
                String per_sterile = et_per_sterile.getText().toString().trim();
                String outcross = et_outcross.getText().toString().trim();
                String per_outcross = et_per_outcross.getText().toString().trim();
                String total = et_Total.getText().toString().trim();
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
                        confirmAlertSubmit(obrdatefinal,noofplants_final,aline,per_aline,blineshadder,per_blineshadder,long_grain,per_long_grain,finegrain,per_finegrain,boldgrain,per_boldgrain,redtip,per_redtip,tall,per_tall,late,per_late,fertile,per_fertile,sterile,per_sterile,outcross,per_outcross,total,per_total,remark,retest_reason,retest,retesttype);
                        //updateTransplantingData(plotno,bedno,noofrows,sowingdate,direction,location_nursery,retest_reason,state,noofplants,retest,range);
                    }
                } else {
                    retest = "No";
                    if (obrdatefinal.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Select Observatin Date", Toast.LENGTH_LONG).show();
                    }else if (noofplants_final.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Plants", Toast.LENGTH_LONG).show();
                    }else if (aline.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of A Line Plants", Toast.LENGTH_LONG).show();
                    }else if (blineshadder.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of B Line Plants", Toast.LENGTH_LONG).show();
                    }else if (long_grain.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Off Long Grain", Toast.LENGTH_LONG).show();
                    }else if (finegrain.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Off Fine Grain", Toast.LENGTH_LONG).show();
                    }else if (boldgrain.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Off Bold Grain", Toast.LENGTH_LONG).show();
                    }else if (redtip.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Off Red Tip", Toast.LENGTH_LONG).show();
                    }else if (tall.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Off Tall", Toast.LENGTH_LONG).show();
                    }else if (late.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Off Late", Toast.LENGTH_LONG).show();
                    }else if (fertile.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Off Fertile", Toast.LENGTH_LONG).show();
                    }else if (sterile.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Off Sterile", Toast.LENGTH_LONG).show();
                    }else if (outcross.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter No. of Off Outcross", Toast.LENGTH_LONG).show();
                    }else {
                        confirmAlertSubmit(obrdatefinal,noofplants_final,aline,per_aline,blineshadder,per_blineshadder,long_grain,per_long_grain,finegrain,per_finegrain,boldgrain,per_boldgrain,redtip,per_redtip,tall,per_tall,late,per_late,fertile,per_fertile,sterile,per_sterile,outcross,per_outcross,total,per_total,remark,retest_reason,retest,retesttype);
                        //updateTransplantingData(noofseeds_direct,plotno,bedno,noofrows,sowingdate,direction,location_nursery,methodofsowing,retest_reason,noofcells,nooftrey,state,noofplants,retest);
                    }
                }
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void confirmAlertSubmit(final String obrdatefinal, final String noofplants_final, final String aline, final String per_aline, final String blineshadder, final String per_blineshadder, final String long_grain, final String per_long_grain, final String finegrain, final String per_finegrain, final String boldgrain, final String per_boldgrain, final String redtip, final String per_redtip, final String tall, final String per_tall, final String late, final String per_late, final String fertile, final String per_fertile, final String sterile, final String per_sterile, final String outcross, final String per_outcross, final String total, final String per_total, final String remark, final String retest_reason, final String retest, final String retesttype) {
        final Dialog dialog = new Dialog(GOTPaddyFinalSubmitActivity.this);
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
                updateFinalObservationData(obrdatefinal,noofplants_final,aline,per_aline,blineshadder,per_blineshadder,long_grain,per_long_grain,finegrain,per_finegrain,boldgrain,per_boldgrain,redtip,per_redtip,tall,per_tall,late,per_late,fertile,per_fertile,sterile,per_sterile,outcross,per_outcross,total,per_total,remark,retest_reason,retest,retesttype);
            }
        });
    }

    private void updateFinalObservationData(String obrdatefinal, String noofplants_final, String aline, String per_aline, String blineshadder, String per_blineshadder, String long_grain, String per_long_grain, String finegrain, String per_finegrain, String boldgrain, String per_boldgrain, String redtip, String per_redtip, String tall, String per_tall, String late, String per_late, String fertile, String per_fertile, String sterile, String per_sterile, String outcross, String per_outcross, String total, String per_total, String remark, String retest_reason, String retest, String retesttype) {
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
        params.put("fnobser_maleplants", "");
        params.put("fnobser_maleper", "");
        params.put("fnobser_femaleplants", "");
        params.put("fnobser_femaleper", "");
        params.put("fnobser_total", "");
        params.put("fnobser_totalper", "");
        params.put("fnobser_otherofftype", "");
        params.put("fnobser_otheroffper", "");
        params.put("fnobser_blineps", "");
        params.put("fnobser_blinepsper", "");
        params.put("fnobser_totalofftype", "");
        params.put("fnobser_totalofftypeper", "");
        params.put("fnobser_self", "");
        params.put("fnobser_selfper", "");
        params.put("fnobser_pencilplants", "");
        params.put("fnobser_pencilplantsper", "");
        params.put("fnobser_aline", aline);
        params.put("fnobser_alineper", per_aline);
        params.put("fnobser_blinesh", blineshadder);
        params.put("fnobser_blineshper", per_blineshadder);
        params.put("fnobser_lg", long_grain);
        params.put("fnobser_lgper", per_long_grain);
        params.put("fnobser_fg", finegrain);
        params.put("fnobser_fgper", per_finegrain);
        params.put("fnobser_bg", boldgrain);
        params.put("fnobser_bgper", per_boldgrain);
        params.put("fnobser_rt", redtip);
        params.put("fnobser_rtper", per_redtip);
        params.put("fnobser_tall", tall);
        params.put("fnobser_tallper", per_tall);
        params.put("fnobser_late", late);
        params.put("fnobser_lateper", per_late);
        params.put("fnobser_fertile", fertile);
        params.put("fnobser_fertileper", per_fertile);
        params.put("fnobser_sterile", sterile);
        params.put("fnobser_sterileper", per_sterile);
        params.put("fnobser_outcrosspart", outcross);
        params.put("fnobser_outcrosspartper", per_outcross);
        params.put("fnobser_remarks", remark);
        params.put("retest_reason", retest_reason);
        params.put("retest", retest);
        params.put("retesttype", retesttype);

        new SendVolleyCall().executeProgram(GOTPaddyFinalSubmitActivity.this, AppConfig.SAMPLE_FINALOBRUPDATE_PROGRAM1, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitData(response);
                if (message.equalsIgnoreCase("Success")){
                    Intent intent = new Intent(GOTPaddyFinalSubmitActivity.this, SampleGOTResultHomeActivity.class);
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
                            et_aline.setText(details.getString("fnobser_aline"));
                            et_per_aline.setText(details.getString("fnobser_alineper"));
                            et_blineshadder.setText(details.getString("fnobser_blinesh"));
                            et_per_blineshadder.setText(details.getString("fnobser_blineshper"));
                            et_long_grain.setText(details.getString("fnobser_lg"));
                            et_per_long_grain.setText(details.getString("fnobser_lgper"));
                            et_finegrain.setText(details.getString("fnobser_fg"));
                            et_per_finegrain.setText(details.getString("fnobser_fgper"));
                            et_boldgrain.setText(details.getString("fnobser_bg"));
                            et_per_boldgrain.setText(details.getString("fnobser_bgper"));
                            et_redtip.setText(details.getString("fnobser_rt"));
                            et_per_redtip.setText(details.getString("fnobser_rtper"));
                            et_tall.setText(details.getString("fnobser_tall"));
                            et_per_tall.setText(details.getString("fnobser_tallper"));
                            et_late.setText(details.getString("fnobser_late"));
                            et_per_late.setText(details.getString("fnobser_lateper"));
                            et_fertile.setText(details.getString("fnobser_fertile"));
                            et_per_fertile.setText(details.getString("fnobser_fertileper"));
                            et_sterile.setText(details.getString("fnobser_sterile"));
                            et_per_sterile.setText(details.getString("fnobser_sterileper"));
                            et_outcross.setText(details.getString("fnobser_outcrosspart"));
                            et_per_outcross.setText(details.getString("fnobser_outcrosspartper"));
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
        CommonVolleyCall.getInstance(GOTPaddyFinalSubmitActivity.this).addRequestQueue(strReq);
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