package com.example.qc.pp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.qc.MainActivity;
import com.example.qc.R;
import com.example.qc.moisture.MoistureHomeActivity;
import com.example.qc.parser.JsonParser;
import com.example.qc.pojo.SampleInfo;
import com.example.qc.pojo.SamplePPInfo;
import com.example.qc.samplecollection.SampleCollectionUpdateActivity;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.Utils;
import com.example.qc.volley.SendVolleyCall;
import com.example.qc.volley.volleyCallback;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

public class PPHomeActivity extends AppCompatActivity {

    private ImageButton back_nav;
    private ImageButton btn_samplescan;
    private ImageButton btn_samplemanual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_p_home);
        getSupportActionBar().hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        back_nav = findViewById(R.id.back_nav);
        btn_samplescan = findViewById(R.id.btn_samplescan);
        btn_samplemanual = findViewById(R.id.btn_samplemanual);
    }

    private void init() {
        btn_samplescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(PPHomeActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        btn_samplemanual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(PPHomeActivity.this);
                // Include dialog.xml file
                dialog.setContentView(R.layout.custompop_samplesearch);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setAttributes(lp);
                dialog.show();

                Button search_Btn = dialog.findViewById(R.id.search_Btn);
                final EditText editText_sampleno1 = dialog.findViewById(R.id.editText_sampleno1);
                final EditText editText_sampleno2 = dialog.findViewById(R.id.editText_sampleno2);

                search_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String sample1 = editText_sampleno1.getText().toString().trim();
                        String sample2 = editText_sampleno2.getText().toString().trim();
                        String sampleno = sample1+""+sample2;
                        dialog.dismiss();
                        //getSampleInfo(sampleno);
                        if (sample2.length()==6) {
                            getSampleInfo(sampleno);
                        }else {
                            Toast.makeText(PPHomeActivity.this, "Invalid sample number", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PPHomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {

            } else {
                getSampleInfo(result.getContents());
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getSampleInfo(String contents) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("sampleno", contents);

        new SendVolleyCall().executeProgram(PPHomeActivity.this, AppConfig.SAMPLEPPINFO_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                JsonParser.getInstance().samplePPInfoParser(response);
                SamplePPInfo samplePPInfo = (SamplePPInfo) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_SAMPLEPPINFO_OBJ,SamplePPInfo.class);
                if (samplePPInfo.getMessage().equalsIgnoreCase("Success")){
                    Intent intent = new Intent(PPHomeActivity.this, PPReadingFormActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), samplePPInfo.getMessage(), Toast.LENGTH_SHORT).show();
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