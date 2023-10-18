package com.example.qc.bts_seedling;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qc.R;
import com.example.qc.parser.JsonParser;
import com.example.qc.pojo.SampleInfo;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.Utils;
import com.example.qc.volley.SendVolleyCall;
import com.example.qc.volley.volleyCallback;

import java.util.HashMap;
import java.util.Map;

public class BTSSeedlingDispatchFormActivity extends AppCompatActivity {

    private ImageButton back_nav;
    private Button btn_submit;
    private Button btn_back;
    private TextView tv_sampleno;
    private EditText et_remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_t_s_seedling_dispatch_form);
        getSupportActionBar().hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        SampleInfo sampleInfo = (SampleInfo) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_SAMPLEINFO_OBJ,SampleInfo.class);
        back_nav = findViewById(R.id.back_nav);
        TextView tv_crop = findViewById(R.id.tv_crop);
        TextView tv_variety = findViewById(R.id.tv_variety);
        TextView tv_lono = findViewById(R.id.tv_lono);
        TextView tv_qtykg = findViewById(R.id.tv_qtykg);
        TextView tv_stage = findViewById(R.id.tv_stage);
        tv_sampleno = findViewById(R.id.tv_sampleno);
        btn_submit=findViewById(R.id.btn_submit);
        btn_back=findViewById(R.id.btn_back);
        et_remarks=findViewById(R.id.et_remarks);

        //Sample Info Display
        tv_crop.setText(sampleInfo.getCrop());
        tv_variety.setText(sampleInfo.getVariety());
        tv_lono.setText(sampleInfo.getLotno());
        tv_qtykg.setText(sampleInfo.getQty());
        tv_stage.setText(sampleInfo.getTrstage());
        tv_sampleno.setText(sampleInfo.getSampleno());
    }

    private void init() {
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BTSSeedlingDispatchFormActivity.this, BTSSeedlingDispatchHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BTSSeedlingDispatchFormActivity.this, BTSSeedlingDispatchHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
    }

    private void submitData() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        String sampleno = tv_sampleno.getText().toString().trim();
        String remarks = et_remarks.getText().toString().trim();
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("sampleno", sampleno);
        params.put("qc_remarks", remarks);
        new SendVolleyCall().executeProgram(BTSSeedlingDispatchFormActivity.this, AppConfig.SAMPLEINFO_BTS_DISPSUBMIT, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitData(response);
                if (message.equalsIgnoreCase("Success")){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BTSSeedlingDispatchFormActivity.this, BTSSeedlingDispatchHomeActivity.class);
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