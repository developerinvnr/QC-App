package com.example.qc.got;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qc.MainActivity;
import com.example.qc.R;
import com.example.qc.germination.GerminationHomeActivity;
import com.example.qc.germination.GerminationSetupActivity;
import com.example.qc.parser.JsonParser;
import com.example.qc.pojo.SampleInfo;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.Utils;
import com.example.qc.volley.SendVolleyCall;
import com.example.qc.volley.volleyCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SampleReceiptUpdateActivity extends AppCompatActivity {

    private ImageButton back_nav;
    private Spinner spinner_remark;
    private EditText et_dosd;
    private EditText et_crop;
    private EditText et_variety;
    private EditText et_lotno;
    private EditText et_sampleno;
    private TextView tv_date;
    private Button btn_submit;
    private Button btn_resample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_receipt_update);
        getSupportActionBar().hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        back_nav = findViewById(R.id.back_nav);
        spinner_remark = findViewById(R.id.spinner_remark);
        et_dosd = findViewById(R.id.et_dosd);
        et_crop = findViewById(R.id.et_crop);
        et_variety = findViewById(R.id.et_variety);
        et_lotno = findViewById(R.id.et_lotno);
        et_sampleno = findViewById(R.id.et_sampleno);
        tv_date = findViewById(R.id.tv_date);
        btn_submit = findViewById(R.id.btn_submit);
        btn_resample = findViewById(R.id.btn_resample);

        SampleInfo sampleInfo = (SampleInfo) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_SAMPLEINFO_OBJ,SampleInfo.class);
        et_dosd.setText(sampleInfo.getSrdate());
        et_crop.setText(sampleInfo.getCrop());
        et_variety.setText(sampleInfo.getVariety());
        et_lotno.setText(sampleInfo.getLotno());
        et_sampleno.setText(sampleInfo.getSampleno());
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tv_date.setText(date);

        String[] DayOfWeek = {"Received OK", "Damage", "Not Received"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SampleReceiptUpdateActivity.this, android.R.layout.simple_spinner_item, DayOfWeek);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_remark.setAdapter(adapter);
    }

    private void init() {
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SampleReceiptUpdateActivity.this, SampleReceiptHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remark = spinner_remark.getSelectedItem().toString().trim();
                String ackdate = tv_date.getText().toString().trim();
                String trstring = "Acknowledge";
                updateAck(remark,ackdate,trstring);
            }
        });

        btn_resample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remark = spinner_remark.getSelectedItem().toString().trim();
                String ackdate = tv_date.getText().toString().trim();
                String trstring = "Resampling";
                updateAck(remark,ackdate,trstring);
            }
        });
    }

    private void updateAck(String remark, String ackdate, String trstring) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
        String sampleno = et_sampleno.getText().toString().trim();
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("scode", scode);
        params.put("sampleno", sampleno);
        params.put("ackremark", remark);
        params.put("smpackdate", ackdate);
        params.put("trtype", trstring);

        new SendVolleyCall().executeProgram(SampleReceiptUpdateActivity.this, AppConfig.SAMPLE_ACKNOWLEDGEMENT_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String msg = JsonParser.getInstance().parseSubmitAckData(response);
                String message = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MESSAGE);
                if (message.equalsIgnoreCase("Success")){
                    Intent intent = new Intent(SampleReceiptUpdateActivity.this, SampleReceiptHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String response) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(SampleReceiptUpdateActivity.this, SampleReceiptHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}