package com.example.qc.got;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.qc.R;
import com.example.qc.pojo.LoginResponse;
import com.example.qc.pojo.SampleGOTInfo;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.Utils;

public class GOTResultStepsActivity extends AppCompatActivity {

    private ImageButton back_nav;
    private ImageButton btn_sampleplanting;
    private ImageButton btn_sampletransplanting;
    private ImageButton btn_monitoring;
    private ImageButton btn_finalobservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_o_t_result_steps);
        getSupportActionBar().hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        back_nav = findViewById(R.id.back_nav);
        btn_sampleplanting = findViewById(R.id.btn_sampleplanting);
        btn_sampletransplanting = findViewById(R.id.btn_sampletransplanting);
        btn_monitoring = findViewById(R.id.btn_monitoring);
        btn_finalobservation = findViewById(R.id.btn_finalobservation);
        CardView cv_transplanting = findViewById(R.id.cv_transplanting);
        CardView cv_monitoring = findViewById(R.id.cv_monitoring);
        CardView cv_finalobservation = findViewById(R.id.cv_finalobservation);
        CardView cv_sampleplanting = findViewById(R.id.cv_sampleplanting);
        LoginResponse loginRes = (LoginResponse) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_LOGIN_OBJ, LoginResponse.class);
        if (loginRes.getOprtype().equalsIgnoreCase("Primary")){
            cv_transplanting.setCardBackgroundColor(Color.parseColor("#b8b8b6"));
            cv_monitoring.setCardBackgroundColor(Color.parseColor("#b8b8b6"));
            cv_finalobservation.setCardBackgroundColor(Color.parseColor("#b8b8b6"));
            btn_sampletransplanting.setEnabled(false);
            cv_monitoring.setEnabled(false);
            cv_finalobservation.setEnabled(false);
        }else if (loginRes.getOprtype().equalsIgnoreCase("Intermediate")){
            cv_finalobservation.setCardBackgroundColor(Color.parseColor("#b8b8b6"));
            cv_finalobservation.setEnabled(false);
        }else {
            //return;
        }
        SampleGOTInfo sampleGOTInfo = (SampleGOTInfo) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_SAMPLEINFO_OBJ,SampleGOTInfo.class);
        if (sampleGOTInfo.getSowingflg().equalsIgnoreCase("1")){
            cv_sampleplanting.setCardBackgroundColor(Color.parseColor("#b8b8b6"));
            btn_sampleplanting.setEnabled(false);
        }
        if (sampleGOTInfo.getTransplantflg().equalsIgnoreCase("1")){
            cv_transplanting.setCardBackgroundColor(Color.parseColor("#b8b8b6"));
            btn_sampletransplanting.setEnabled(false);
        }else if (sampleGOTInfo.getTransplantflg().equalsIgnoreCase("2")){
            cv_transplanting.setCardBackgroundColor(Color.parseColor("#b8b7b6"));
            btn_sampletransplanting.setEnabled(false);
        }else {
            //return;
        }
        if (sampleGOTInfo.getFieldmflg().equalsIgnoreCase("1")){
            cv_monitoring.setCardBackgroundColor(Color.parseColor("#b8b8b6"));
            btn_monitoring.setEnabled(false);
        }
        if (sampleGOTInfo.getFinobrflg().equalsIgnoreCase("1")){
            cv_finalobservation.setCardBackgroundColor(Color.parseColor("#b8b8b6"));
            btn_finalobservation.setEnabled(false);
        }
    }

    private void init() {
        final SampleGOTInfo sampleGOTInfo = (SampleGOTInfo) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_SAMPLEINFO_OBJ,SampleGOTInfo.class);
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GOTResultStepsActivity.this, SampleGOTResultHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_sampleplanting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GOTResultStepsActivity.this, GOTPlantingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_sampletransplanting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sampleGOTInfo.getSowingflg().equalsIgnoreCase("1")){
                    Intent intent = new Intent(GOTResultStepsActivity.this, GOTTransplantingActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Sowing Data Not Updated", Toast.LENGTH_LONG).show();
                }

            }
        });

        btn_monitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sampleGOTInfo.getSowingflg().equalsIgnoreCase("1")) {
                    Intent intent = new Intent(GOTResultStepsActivity.this, GOTFieldMonitoringVisitActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Transplanting Data Not Updated", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_finalobservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sampleGOTInfo.getSowingflg().equalsIgnoreCase("1")) {
                    if (sampleGOTInfo.getCrop().equalsIgnoreCase("Maize Seed")) {
                        Intent intent = new Intent(GOTResultStepsActivity.this, GOTMaizeFinalObservationActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (sampleGOTInfo.getCrop().equalsIgnoreCase("Paddy Seed")) {
                        Intent intent = new Intent(GOTResultStepsActivity.this, GOTPaddyFinalObservationActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (sampleGOTInfo.getCrop().equalsIgnoreCase("Bajra Seed")) {
                        Intent intent = new Intent(GOTResultStepsActivity.this, GOTBajraFinalObservationActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(GOTResultStepsActivity.this, GOTFinalObservationActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Field Monitoring Data Not Updated", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}