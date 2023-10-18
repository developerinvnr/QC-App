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
import com.example.qc.adapter.GOTObservationPendingReportAdapter;
import com.example.qc.adapter.SowingPendingReportAdapter;
import com.example.qc.pojo.GOTObservationPendingReportPojo;
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

public class GOTObservationPendingReport extends AppCompatActivity {

    private final List<GOTObservationPendingReportPojo> gotObservationPendingReportPojos = new ArrayList<>();
    private GOTObservationPendingReportAdapter ob_adapter;
    private RecyclerView recyclerView;
    String trstring;
    private ImageButton back_nav;
    private RecyclerView recycler_view;
    private TextView tv_reportname;
    private String crop;
    private String variety;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gotobservation_pending_report);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setTheme();
        init();

    }

    @SuppressLint("SetTextI18n")
    private void setTheme(){
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        Toolbar toolbar = findViewById(R.id.toolbar_ob);
        recyclerView = findViewById(R.id.recycler_view);
        TextView toolbar_title = findViewById(R.id.toolbar_title);

        back_nav = findViewById(R.id.back_nav);
        recycler_view = findViewById(R.id.recycler_view);
        tv_reportname = findViewById(R.id.tv_reportname);
        trstring = getIntent().getStringExtra("repstring");
        crop = getIntent().getStringExtra("crop");
        variety = getIntent().getStringExtra("variety");
        Log.e("retype", trstring);
        Log.e("crop", crop);
        Log.e("variety", variety);
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

        ob_adapter = new GOTObservationPendingReportAdapter(GOTObservationPendingReport.this, gotObservationPendingReportPojos);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(ob_adapter);
    }

    private void init() {
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GOTObservationPendingReport.this, ReportSummeryActivity.class);
                intent.putExtra("repstring", trstring);
                startActivity(intent);
                finish();
            }
        });
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        getObservationPendingReportData(userid);
    }

    public void getObservationPendingReportData(String userid){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.COMMON_URL + AppConfig.SOWPENDING_REPORT, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                gotObservationPendingReportPojos.clear();
                ob_adapter.notifyDataSetChanged();
                try {
                    trstring = getIntent().getStringExtra("repstring");
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if (!error){
                        JSONObject jObj = jsonObject.getJSONObject("user");
                        JSONArray jsonArray = jObj.getJSONArray("gotrepdetailsarray");
                        int res = 1;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jObject = jsonArray.getJSONObject(i);
                            String crop = jObject.getString("crop");
                            String spCode = jObject.getString("spcodes");
                            String variety = jObject.getString("variety");
                            String lotno = jObject.getString("lotno");
                            String sampleno = jObject.getString("sampleno");
                            String sowloc = jObject.getString("sownurseryloc");
                            String dot = jObject.getString("dateoftp");
                            String bedno = jObject.getString("sowbedno");
                            String direction = jObject.getString("tpdirection");
                            gotObservationPendingReportPojos.add(new GOTObservationPendingReportPojo(res, crop, spCode, variety, lotno, sampleno, sowloc, dot, bedno, direction));
                            ob_adapter.notifyDataSetChanged();
                            res++;
                        }
                    }
                    else {
                        Toast.makeText(GOTObservationPendingReport.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e){
                    Toast.makeText(GOTObservationPendingReport.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GOTObservationPendingReport.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        })
        {
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

        CommonVolleyCall.getInstance(GOTObservationPendingReport.this).addRequestQueue(stringRequest);

    }
}