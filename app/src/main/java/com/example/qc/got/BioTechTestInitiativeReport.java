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
import com.example.qc.adapter.BioTechTestInitiativeReportAdapter;
import com.example.qc.adapter.SowingPendingReportAdapter;
import com.example.qc.pojo.BioTechTestInitiativeReportPojo;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.CommonVolleyCall;
import com.example.qc.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BioTechTestInitiativeReport extends AppCompatActivity {

    private List<BioTechTestInitiativeReportPojo> bioTechTestInitiativeReportPojo = new ArrayList<>();
    private BioTechTestInitiativeReportAdapter bio_adapter;
    private String trstring;
    private RecyclerView recyclerView;
    private ImageButton back_nav;
    private TextView tv_reportname;
    private String crop;
    private String variety;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_tech_test_initiative_report);

        Objects.requireNonNull(getSupportActionBar()).hide();
        setTheme();
        init();

    }

    @SuppressLint("SetTextI18n")
    public void setTheme(){
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        Toolbar toolbar = findViewById(R.id.toolbar_bio);
        recyclerView = findViewById(R.id.recyclerView_bio);
        TextView toolbar_title = findViewById(R.id.toolbar_title);

        back_nav = findViewById(R.id.back_nav);
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

        bio_adapter = new BioTechTestInitiativeReportAdapter(BioTechTestInitiativeReport.this, bioTechTestInitiativeReportPojo);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bio_adapter);
    }

    public void init(){
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BioTechTestInitiativeReport.this, ReportSummeryActivity.class);
                intent.putExtra("repstring", trstring);
                startActivity(intent);
                finish();
            }
        });
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        getBioTechTestInitiativeReportData(userid);
    }

    public void getBioTechTestInitiativeReportData(String userid){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.COMMON_URL + AppConfig.SOWPENDING_REPORT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                bioTechTestInitiativeReportPojo.clear();
                bio_adapter.notifyDataSetChanged();

                try {

                    trstring = getIntent().getStringExtra("repstring");
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if (!error){
                        JSONObject jObj = jsonObject.getJSONObject("user");
                        JSONArray jsonArray = jObj.getJSONArray("gotrepdetailsarray");
                        int res = 1;
                        String crop = jObj.getString("crop");
                        String spcode = jObj.getString("spcodes");
                        String variety = jObj.getString("variety");
                        String lotno = jObj.getString("lotno");
                        String sampleno = jObj.getString("sampleno");
                        String arrivaldate = jObj.getString("arrivaldate");
                        String dateofsowing = jObj.getString("dateofsowing");
                        String trd = jObj.getString("ttivresultdate");
                        bioTechTestInitiativeReportPojo.add(new BioTechTestInitiativeReportPojo(res, crop, spcode, variety, lotno, sampleno, arrivaldate, dateofsowing, trd));
                        bio_adapter.notifyDataSetChanged();
                        res++;
                    }
                    else {
                        Toast.makeText(BioTechTestInitiativeReport.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e){
                    Toast.makeText(BioTechTestInitiativeReport.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BioTechTestInitiativeReport.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        CommonVolleyCall.getInstance(BioTechTestInitiativeReport.this).addRequestQueue(stringRequest);

    }

}
