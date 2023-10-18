package com.example.qc.dencity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qc.MainActivity;
import com.example.qc.R;
import com.example.qc.got.SampleReceiptHomeActivity;
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
import java.util.Objects;

public class DencityReadingFormActivity extends AppCompatActivity {

    private TextView tv_sampleno;
    private EditText et_dencity;
    private Button btn_submit;
    private ImageButton back_nav;
    private TextView tv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dencity_reading_form);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        SampleInfo sampleInfores = (SampleInfo) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_SAMPLEINFO_OBJ,SampleInfo.class);

        TextView tv_crop = findViewById(R.id.tv_crop);
        TextView tv_variety = findViewById(R.id.tv_variety);
        TextView tv_lono = findViewById(R.id.tv_lono);
        TextView tv_qtykg = findViewById(R.id.tv_qtykg);
        TextView tv_stage = findViewById(R.id.tv_stage);
        tv_sampleno = findViewById(R.id.tv_sampleno);

        tv_crop.setText(sampleInfores.getCrop());
        tv_variety.setText(sampleInfores.getVariety());
        tv_lono.setText(sampleInfores.getLotno());
        tv_qtykg.setText(sampleInfores.getQty());
        tv_stage.setText(sampleInfores.getTrstage());
        tv_sampleno.setText(sampleInfores.getSampleno());

        et_dencity = findViewById(R.id.et_dencity);
        btn_submit = findViewById(R.id.btn_submit);
        back_nav = findViewById(R.id.back_nav);
        tv_date = findViewById(R.id.tv_date);

        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tv_date.setText(date);
    }

    private void init() {
        back_nav.setOnClickListener(v -> {
            Intent intent = new Intent(DencityReadingFormActivity.this, DencityHomeActivity.class);
            startActivity(intent);
            finish();
        });

        btn_submit.setOnClickListener(view -> {
            String sampleno = tv_sampleno.getText().toString().trim();
            String dencity = et_dencity.getText().toString().trim();
            String spdate = tv_date.getText().toString().trim();
            String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
            String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
            if (!dencity.equalsIgnoreCase("")){
                updatedencity(dencity,sampleno,userid,scode);
            }else {
                Toast.makeText(getApplicationContext(), "Enter Dencity Data", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void updatedencity(String dencity, String sampleno, String userid, String scode) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("scode", scode);
        params.put("sampleno", sampleno);
        params.put("samplewt", dencity);

        new SendVolleyCall().executeProgram(DencityReadingFormActivity.this, AppConfig.SAMPLEINFO_DENCITY_SUBMITPROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitData(response);
                if (message.equalsIgnoreCase("Success")){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DencityReadingFormActivity.this, DencityHomeActivity.class);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(DencityReadingFormActivity.this, DencityHomeActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}