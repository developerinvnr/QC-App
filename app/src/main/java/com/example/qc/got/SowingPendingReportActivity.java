package com.example.qc.got;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.qc.R;
import com.example.qc.adapter.ReportSummeryAdapter;
import com.example.qc.adapter.SowingPendingReportAdapter;
import com.example.qc.pojo.GotReportDetailedPojo;
import com.example.qc.pojo.ReportSummeryPojo;
import com.example.qc.pojo.SowingPendingReportPojo;
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

public class SowingPendingReportActivity extends AppCompatActivity {
    private ImageButton back_nav;
    private RecyclerView recycler_view;
    private String trstring;
    private TextView tv_reportname;
    private SowingPendingReportAdapter adapter;
    private List<GotReportDetailedPojo> gotReportDetailedPojo = new ArrayList<>();
    private String crop;
    private String variety;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_pending_report);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        Toolbar toolbar = findViewById(R.id.toolbar);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        TextView toolbar_title = findViewById(R.id.toolbar_title);

        back_nav = findViewById(R.id.back_nav);
        recycler_view = findViewById(R.id.recycler_view);
        tv_reportname = findViewById(R.id.tv_reportname);
        trstring = getIntent().getStringExtra("repstring");
        crop = getIntent().getStringExtra("crop");
        variety = getIntent().getStringExtra("variety");
        Log.e("retype", trstring);
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

        adapter = new SowingPendingReportAdapter(SowingPendingReportActivity.this, gotReportDetailedPojo);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(adapter);

        /* mAdapter = new ReportSummeryAdapter(this, summeryArray,  this);
        recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(mAdapter); */

    }

    private void init() {
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SowingPendingReportActivity.this, ReportSummeryActivity.class);
                intent.putExtra("repstring", trstring);
                startActivity(intent);
                finish();
            }
        });
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        Log.e("UserId", userid);
        Log.e("reptype", trstring);
        getSowingPendingReport(userid);
    }

    private void getSowingPendingReport(String userid){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.COMMON_URL + AppConfig.SOWPENDING_REPORT,  new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                gotReportDetailedPojo.clear();
                adapter.notifyDataSetChanged();

                try {
//                    trstring = getIntent().getStringExtra("repstring");
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error){
                        JSONObject jObj = jsonObject.getJSONObject("user");
                        JSONArray jsonArray = jObj.getJSONArray("gotrepdetailsarray");
                        int res = 1;
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jObject = jsonArray.getJSONObject(i);
                            String DOA = jObject.getString("arrivaldate");
                            String crop = jObject.getString("crop");
                            String spCode = jObject.getString("spcodes");
                            String variety = jObject.getString("variety");
                            String lotNo = jObject.getString("lotno");
                            String sampleNo = jObject.getString("sampleno");
                            String NoB = jObject.getString("nob");
                            String qty = jObject.getString("qty");
                            String gotStatus = jObject.getString("gotstatus");
                            String pdLoc = jObject.getString("prodloc");
                            String organiser = jObject.getString("organizer");
                            String farmer = jObject.getString("farmer");
                            String pp = jObject.getString("prodper");
                            String sloc = jObject.getString("sloc");
                            gotReportDetailedPojo.add(new GotReportDetailedPojo(res, DOA, crop, spCode, variety, lotNo, sampleNo, NoB, qty, gotStatus, pdLoc, organiser, farmer, pp, sloc));
                            adapter.notifyDataSetChanged();
                            res++;
                        }
                    }
                    else {
                        Toast.makeText(SowingPendingReportActivity.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(SowingPendingReportActivity.this, "JSON error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(SowingPendingReportActivity.this,"Error", Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
                Map<String, String> params = new HashMap<>();
                params.put("mobile1", userid);
                params.put("reptype", trstring);
                params.put("crop", crop);
                params.put("variety", variety);
                return params;
            }
        };

        CommonVolleyCall.getInstance(SowingPendingReportActivity.this).addRequestQueue(stringRequest);
    }

}

