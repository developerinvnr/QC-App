package com.example.qc.got;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.qc.MainActivity;
import com.example.qc.R;
import com.example.qc.common.NavigationActivity;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.ui.home.HomeFragment;
import com.example.qc.utils.Utils;

public class ReportHomeActivity extends AppCompatActivity {

    private ImageButton back_nav;
    private Button btn_sowing_report;
    private Button btn_tp_report;
    private Button btn_gotobservation_report;
    private Button btn_biotech_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_home);
        getSupportActionBar().hide();

        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        Toolbar toolbar = findViewById(R.id.toolbar);
        back_nav = findViewById(R.id.back_nav);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Reports");
        btn_sowing_report = findViewById(R.id.btn_sowing_report);
        btn_tp_report = findViewById(R.id.btn_tp_report);
        btn_gotobservation_report = findViewById(R.id.btn_gotobservation_report);
        btn_biotech_report = findViewById(R.id.btn_biotech_report);
    }

    private void init() {
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportHomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        btn_sowing_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportHomeActivity.this, ReportSummeryActivity.class);
                intent.putExtra("repstring", "sowpending");
                startActivity(intent);
                finish();
            }
        });

        btn_tp_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportHomeActivity.this, ReportSummeryActivity.class);
                intent.putExtra("repstring", "tppending");
                startActivity(intent);
                finish();
            }
        });

        btn_gotobservation_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportHomeActivity.this, ReportSummeryActivity.class);
                intent.putExtra("repstring", "fmvpending");
                startActivity(intent);
                finish();
            }
        });

        btn_biotech_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportHomeActivity.this, ReportSummeryActivity.class);
                intent.putExtra("repstring", "btspending");
                startActivity(intent);
                finish();
            }
        });
    }
}