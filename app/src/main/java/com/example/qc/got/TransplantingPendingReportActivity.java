package com.example.qc.got;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
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
import com.example.qc.adapter.TransplantingPendingReportAdapter;
import com.example.qc.parser.Gotrepdetailsarray;
import com.example.qc.parser.PendingReportResponse;
import com.example.qc.pojo.GotReportDetailedPojo;
import com.example.qc.pojo.TransplantingPendingReportPojo;
import com.example.qc.retrofit.ApiInterface;
import com.example.qc.retrofit.RetrofitClient;
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

import kotlin.io.TextStreamsKt;
import retrofit2.Call;
import retrofit2.Callback;

public class TransplantingPendingReportActivity extends AppCompatActivity {

    private static final String TAG = "TransplantingPendingReportActivity";
    private ImageButton nav_back;
    private TransplantingPendingReportAdapter tp_adapter;
    private String trstring;
    private List<GotReportDetailedPojo> gotReportDetailedPojos = new ArrayList<>();
    private ImageButton back_imgBtn;
    private TextView tv_reportname;
    private String crop;
    private String variety;
    private List<Gotrepdetailsarray> gotrepdetailsarray=new ArrayList<>();
    private TextView tv_days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transplanting_pending_report);
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
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        trstring = getIntent().getStringExtra("repstring");
        crop = getIntent().getStringExtra("crop");
        variety = getIntent().getStringExtra("variety");
        back_imgBtn = findViewById(R.id.nav_backImg);
        tv_reportname = findViewById(R.id.tv_reportname);
        tv_days = findViewById(R.id.tv_days);
        Log.e("trstring" , trstring);
        Log.e("crop:" , crop);
        Log.e("variety" , variety);
        if (trstring.equalsIgnoreCase("sowpending")){
            tv_reportname.setText("Sowing Pending Report");
        }else if(trstring.equalsIgnoreCase("tppending")){
            tv_reportname.setText("Transplanting Pending Report");
        }else if(trstring.equalsIgnoreCase("fmvpending")){
            tv_reportname.setText("GOT Observation Pending Report");
        }else if(trstring.equalsIgnoreCase("btspending")){
            tv_reportname.setText("Biotech Test Initiative Report");
        }

        tp_adapter = new TransplantingPendingReportAdapter(TransplantingPendingReportActivity.this, gotReportDetailedPojos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tp_adapter);
    }

    private void init(){

     back_imgBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent i = new Intent(TransplantingPendingReportActivity.this, ReportSummeryActivity.class);
             i.putExtra("repstring", trstring);
             startActivity(i);
             finish();
         }
     });
     String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
     //getTransplantingPendingReportData(userid);
        getReportDetails(userid);
    }

    private void getReportDetails(String userid) {
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Log.e("Params:", userid+"="+trstring+"="+crop+"="+variety);
        Call<PendingReportResponse> call =apiInterface.getReportDetails(userid, trstring, crop, variety);
        call.enqueue(new Callback<PendingReportResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<PendingReportResponse> call, @NonNull retrofit2.Response<PendingReportResponse> response) {
                if (response.isSuccessful()) {
                    progressDialog.cancel();
                    PendingReportResponse pendingReportResponse = response.body();
                    if (pendingReportResponse != null) {
                        gotrepdetailsarray=pendingReportResponse.getUser().getGotrepdetailsarray();
                        Log.e("ReportDetails:", String.valueOf(gotrepdetailsarray));
                        int res=1;
                        for (Gotrepdetailsarray obj:gotrepdetailsarray) {
                            gotReportDetailedPojos.add(new GotReportDetailedPojo(res, obj.getArrivaldate(), obj.getCrop(), obj.getSpcodes(), obj.getVariety(), obj.getLotno(), obj.getSampleno(), obj.getDateofsowing(), obj.getSownurseryloc(), obj.getTpbedno(), obj.getSowtrayno(), obj.getSownooftray()));
                            tp_adapter.notifyDataSetChanged();
                            res++;
                        }
                    }
                } else {
                    progressDialog.cancel();
                    if (response.errorBody() != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            String msg = jsonObj.getString("message");
                            Toast.makeText(TransplantingPendingReportActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Call<PendingReportResponse> call, @NonNull Throwable t) {
                progressDialog.cancel();
                Log.e(TAG, "RetrofitError : " + t.getMessage());
            }
        });
    }

    public void getTransplantingPendingReportData(String userid){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.COMMON_URL + AppConfig.SOWPENDING_REPORT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                gotReportDetailedPojos.clear();
                tp_adapter.notifyDataSetChanged();
                try {
                    progressDialog.dismiss();
                    //trstring = getIntent().getStringExtra("repstring");
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error){
                        JSONObject jObj = jsonObject.getJSONObject("user");
                        JSONArray jsonArray = jObj.getJSONArray("gotrepdetailsarray");
                        int res = 1;
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            String DOA = object.getString("arrivaldate");
                            String crop = object.getString("crop");
                            String spCode = object.getString("spcodes");
                            String variety = object.getString("variety");
                            String lotno = object.getString("lotno");
                            String sampleno = object.getString("sampleno");
                            String dos = object.getString("dateofsowing");
                            String nl = object.getString("sownurseryloc");
                            String bedno = object.getString("tpbedno");
                            String treyno = object.getString("sowtrayno");
                            String nooftrey = object.getString("sownooftray");
                            gotReportDetailedPojos.add(new GotReportDetailedPojo(res, DOA, crop, spCode, variety, lotno, sampleno, dos, nl , bedno, treyno, nooftrey));
                            tp_adapter.notifyDataSetChanged();
                            res++;
                        }
                        Log.e("List: ", String.valueOf(gotReportDetailedPojos));
                    }
                    else {
                        Toast.makeText(TransplantingPendingReportActivity.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(TransplantingPendingReportActivity.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error!=null){
                    Toast.makeText(TransplantingPendingReportActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
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
        CommonVolleyCall.getInstance(TransplantingPendingReportActivity.this).addRequestQueue(stringRequest);
    }

}