package com.example.qc.samplecollection;

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
import com.example.qc.common.LoginActivity;
import com.example.qc.parser.JsonParser;
import com.example.qc.pojo.LoginResponse;
import com.example.qc.pojo.SampleInfo;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.Utils;
import com.example.qc.volley.SendVolleyCall;
import com.example.qc.volley.volleyCallback;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

public class SampleCollectionHomeActivity extends AppCompatActivity {

    private ImageButton btn_samplecollectionscan;
    private ImageButton btn_samplecollectionmanual;
    private ImageButton back_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_collection_home);
        getSupportActionBar().hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        back_nav = findViewById(R.id.back_nav);
        btn_samplecollectionscan = findViewById(R.id.btn_samplecollectionscan);
        btn_samplecollectionmanual = findViewById(R.id.btn_samplecollectionmanual);
    }

    private void init() {
        btn_samplecollectionscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(SampleCollectionHomeActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(false);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        btn_samplecollectionmanual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(SampleCollectionHomeActivity.this);
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
                        getSampleInfo(sampleno);
                    }
                });
            }
        });

        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SampleCollectionHomeActivity.this, MainActivity.class);
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

        new SendVolleyCall().executeProgram(SampleCollectionHomeActivity.this, AppConfig.SAMPLEINFO_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                JsonParser.getInstance().sampleInfoParser(response);
                SampleInfo sampleInfo = (SampleInfo) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_SAMPLEINFO_OBJ,SampleInfo.class);

                if (sampleInfo.getMassage().equalsIgnoreCase("Success")){
                    Intent intent = new Intent(SampleCollectionHomeActivity.this, SampleCollectionUpdateActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), sampleInfo.getMassage(), Toast.LENGTH_SHORT).show();
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