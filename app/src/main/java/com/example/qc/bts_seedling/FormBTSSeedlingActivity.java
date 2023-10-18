package com.example.qc.bts_seedling;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qc.R;
import com.example.qc.moisture.MoistureHomeActivity;
import com.example.qc.moisture.MoistureReadingFormActivity;
import com.example.qc.parser.JsonParser;
import com.example.qc.pojo.SampleInfo;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.Utils;
import com.example.qc.volley.SendVolleyCall;
import com.example.qc.volley.volleyCallback;

import java.util.HashMap;
import java.util.Map;

public class FormBTSSeedlingActivity extends AppCompatActivity {

    private ImageButton back_nav;

    private RadioGroup radioGrpdecision;
    private Button btn_submit;
    private Button btn_back;
    private TextView tv_sampleno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_b_t_s_seedling);
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
        radioGrpdecision = findViewById(R.id.radioGrpdecision);
        btn_submit=findViewById(R.id.btn_submit);
        btn_back=findViewById(R.id.btn_back);

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
                Intent intent = new Intent(FormBTSSeedlingActivity.this, HomeBTSSeedlingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormBTSSeedlingActivity.this, HomeBTSSeedlingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dectype = radioGrpdecision.getCheckedRadioButtonId();
                RadioButton type = findViewById(dectype);
                String decisiontype = type.getText().toString();
                String decision = "BTS";
                if (decisiontype.equalsIgnoreCase("BTS and Field GOT (Both)")) {
                    decision = "Both";
                }
                submitData(decision);
            }
        });
    }

    private void submitData(String decision) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        String sampleno = tv_sampleno.getText().toString().trim();
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("decision", decision);
        params.put("scode", scode);
        params.put("sampleno", sampleno);
        new SendVolleyCall().executeProgram(FormBTSSeedlingActivity.this, AppConfig.SAMPLEINFO_BTS_GERMPSUBMIT, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitData(response);
                if (message.equalsIgnoreCase("Success")){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FormBTSSeedlingActivity.this, HomeBTSSeedlingActivity.class);
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