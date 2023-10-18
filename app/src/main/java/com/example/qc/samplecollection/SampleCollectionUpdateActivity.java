package com.example.qc.samplecollection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qc.MainActivity;
import com.example.qc.R;
import com.example.qc.parser.JsonParser;
import com.example.qc.pojo.LoginResponse;
import com.example.qc.pojo.SampleInfo;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.Utils;
import com.example.qc.volley.SendVolleyCall;
import com.example.qc.volley.volleyCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SampleCollectionUpdateActivity extends AppCompatActivity {

    private EditText et_dosr;
    private EditText et_crop;
    private EditText et_variety;
    private EditText et_lotno;
    private EditText et_stage;
    private EditText et_nob;
    private EditText et_qty;
    private EditText et_sloc;
    private EditText et_qctest;
    private EditText et_sampleno;
    private TextView tv_date;
    private EditText et_spdate;
    private LinearLayout ll_spdate;
    private LinearLayout ll_spdate2;
    private int mYear, mMonth, mDay;
    private ImageButton back_nav;
    private Button btn_cancel;
    private Button btn_abort;
    private Button btn_submit;
    private TextView tv_textmsg;
    private EditText et_srf_no;
    private Button btn_generateNewSRF;
    private String srf_series;
    private SampleInfo sampleInfores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_collection_update);
        setTheme();
        init();
    }

    private void setTheme() {
        getSupportActionBar().hide();
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        back_nav = findViewById(R.id.back_nav);
        et_dosr = findViewById(R.id.et_dosr);
        et_crop = findViewById(R.id.et_crop);
        et_variety = findViewById(R.id.et_variety);
        et_lotno = findViewById(R.id.et_lotno);
        et_stage = findViewById(R.id.et_stage);
        et_nob = findViewById(R.id.et_nob);
        et_qty = findViewById(R.id.et_qty);
        et_sloc = findViewById(R.id.et_sloc);
        et_qctest = findViewById(R.id.et_qctest);
        et_sampleno = findViewById(R.id.et_sampleno);
        et_spdate = findViewById(R.id.et_spdate);
        tv_date = findViewById(R.id.tv_date);
        tv_textmsg = findViewById(R.id.tv_textmsg);
        ll_spdate = findViewById(R.id.ll_spdate);
        ll_spdate2 = findViewById(R.id.ll_spdate2);
        et_srf_no = findViewById(R.id.et_srf_no);
        btn_generateNewSRF = findViewById(R.id.btn_generateNewSRF);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_abort = findViewById(R.id.btn_abort);
        btn_submit = findViewById(R.id.btn_submit);

        sampleInfores = (SampleInfo) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_SAMPLEINFO_OBJ,SampleInfo.class);
        et_dosr.setText(sampleInfores.getSrdate());
        et_crop.setText(sampleInfores.getCrop());
        et_variety.setText(sampleInfores.getVariety());
        et_lotno.setText(sampleInfores.getLotno());
        et_stage.setText(sampleInfores.getTrstage());
        et_nob.setText(sampleInfores.getNob());
        et_qty.setText(sampleInfores.getQty());
        et_sloc.setText(sampleInfores.getSloc());
        et_qctest.setText(sampleInfores.getQctest());
        et_sampleno.setText(sampleInfores.getSampleno());
        et_spdate.setText(sampleInfores.getSpdate());
        et_srf_no.setText(sampleInfores.getSrfno());

        srf_series = sampleInfores.getSrfseries();

        if (sampleInfores.getSampflg().equals("0")){
            ll_spdate.setVisibility(View.GONE);
            ll_spdate2.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.VISIBLE);
            tv_textmsg.setVisibility(View.GONE);
            btn_generateNewSRF.setVisibility(View.VISIBLE);
        }else {
            ll_spdate.setVisibility(View.VISIBLE);
            ll_spdate2.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);
            tv_textmsg.setVisibility(View.VISIBLE);
            btn_generateNewSRF.setVisibility(View.GONE);
        }
    }

    private void init() {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tv_date.setText(date);

        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SampleCollectionUpdateActivity.this, SampleCollectionHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_generateNewSRF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNewSRF();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sampleno = et_sampleno.getText().toString().trim();
                String spdate = tv_date.getText().toString().trim();
                String srf_no = et_srf_no.getText().toString().trim();
                String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
                String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
                //LoginResponse loginRes = (LoginResponse) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_LOGIN_OBJ,LoginResponse.class);
                //String scode = loginRes.getScode();
                updateSampleCollection(sampleno,spdate,userid,scode,srf_no);
            }
        });
    }

    private void getNewSRF() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("sampleno", sampleInfores.getSampleno());

        new SendVolleyCall().executeProgram(SampleCollectionUpdateActivity.this, AppConfig.SAMPLECLLECTNEWSRF_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseNewSRF(response);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                srf_series="new";
                if (message.length()==13) {
                    et_srf_no.setText(message);
                }
            }

            @Override
            public void onError(String response) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSampleCollection(String sampleno, String spdate, String userid, String scode, String sef_no) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("scode", scode);
        params.put("sampleno", sampleno);
        params.put("smpdate", spdate);
        params.put("srfseries", srf_series);
        params.put("srfno", sef_no);

        new SendVolleyCall().executeProgram(SampleCollectionUpdateActivity.this, AppConfig.SAMPLECLLECTUPDATE_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitData(response);
                if (message.equalsIgnoreCase("Success")){
                    srf_series="old";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SampleCollectionUpdateActivity.this, SampleCollectionHomeActivity.class);
                    startActivity(intent);
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
}