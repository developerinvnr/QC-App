package com.example.qc.got;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.qc.R;
import com.example.qc.adapter.FMV_Data;
import com.example.qc.adapter.ReportSummeryAdapter;
import com.example.qc.pojo.ReportSummeryPojo;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.CommonVolleyCall;
import com.example.qc.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReportSummeryActivity extends AppCompatActivity implements ReportSummeryAdapter.SummeryAdapterListener {

    private ImageButton back_nav;
    private RecyclerView recycler_view;
    private ReportSummeryAdapter mAdapter;
    private final List<ReportSummeryPojo> summeryArray = new ArrayList<>();
    private String trstring;
    private TextView tv_reportname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_summery);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbar_title = findViewById(R.id.toolbar_title);

        back_nav = findViewById(R.id.back_nav);
        recycler_view = findViewById(R.id.recycler_view);
        tv_reportname = findViewById(R.id.tv_reportname);
        trstring = getIntent().getStringExtra("repstring");

        if (trstring.equalsIgnoreCase("sowpending")){
            tv_reportname.setText("Sowing Pending Report");
        }else if(trstring.equalsIgnoreCase("tppending")){
            tv_reportname.setText("Transplanting Pending Report");
        }else if(trstring.equalsIgnoreCase("fmvpending")){
            tv_reportname.setText("GOT Observation Pending Report");
        }else if(trstring.equalsIgnoreCase("btspending")){
            tv_reportname.setText("Biotech Test Initiative Report");
        }
        toolbar_title.setText("Report");
        mAdapter = new ReportSummeryAdapter(this, summeryArray,  this);
        recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(mAdapter);
    }

    private void init() {
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportSummeryActivity.this, ReportHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        getSummeryData(userid);
    }

    private void getSummeryData(String userid) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.COMMON_URL + AppConfig.SUMMERY_PENDING_REPORT, new Response.Listener<String>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                summeryArray.clear();
                mAdapter.notifyDataSetChanged();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json

                    if (!error) {
                        //success
                        JSONObject user = jObj.getJSONObject("user");
                        JSONArray samplearray = user.getJSONArray("gotrepsummeryarray");
                        int cnt=1;
                        for (int i = 0; i < samplearray.length(); i++) {
                            JSONObject details=samplearray.getJSONObject(i);
                            String crop=details.getString("crop");
                            String variety=details.getString("variety");
                            String nooflots=details.getString("nooflots");
                            summeryArray.add(new ReportSummeryPojo(cnt, crop,variety,nooflots));
                            mAdapter.notifyDataSetChanged();
                            cnt++;
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
                params.put("reptype", trstring);
                return params;
            }

        };
        CommonVolleyCall.getInstance(ReportSummeryActivity.this).addRequestQueue(strReq);
    }

    @Override
    public void onContactSelected(ReportSummeryPojo reportSummeryPojo) {

        if (trstring.equalsIgnoreCase("sowpending")){
            Intent intent = new Intent(ReportSummeryActivity.this, SowingPendingReportActivity.class);
            intent.putExtra("repstring", "sowpending");
            intent.putExtra("crop", reportSummeryPojo.getCrop());
            intent.putExtra("variety", reportSummeryPojo.getVariety());
            startActivity(intent);
            finish();
        }
        if(trstring.equalsIgnoreCase("tppending")){
            Intent intent = new Intent(ReportSummeryActivity.this, TransplantingPendingReportActivity.class);
            intent.putExtra("repstring", "tppending");
            intent.putExtra("crop", reportSummeryPojo.getCrop());
            intent.putExtra("variety", reportSummeryPojo.getVariety());
            startActivity(intent);
            finish();
        }
        if(trstring.equalsIgnoreCase("fmvpending")){
            Intent intent = new Intent(ReportSummeryActivity.this, GOTObservationPendingReport.class);
            intent.putExtra("repstring" ,"fmvpending");
            intent.putExtra("crop", reportSummeryPojo.getCrop());
            intent.putExtra("variety", reportSummeryPojo.getVariety());
            startActivity(intent);
            finish();
        }
        if(trstring.equalsIgnoreCase("btspending")){
           Intent intent = new Intent(ReportSummeryActivity.this, BioTechTestInitiativeReport.class);
           intent.putExtra("repstring", "btspending");
            intent.putExtra("crop", reportSummeryPojo.getCrop());
            intent.putExtra("variety", reportSummeryPojo.getVariety());
           startActivity(intent);
           finish();
        }

        
        Toast.makeText(getApplicationContext(), reportSummeryPojo.getNoofLots(), Toast.LENGTH_LONG).show();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}