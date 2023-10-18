package com.example.qc.germination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.qc.database.DatabaseHelper;
import com.example.qc.got.TransplantingPendingReportActivity;
import com.example.qc.parser.Gotrepdetailsarray;
import com.example.qc.parser.JsonParser;
import com.example.qc.parser.PendingReportResponse;
import com.example.qc.parser.germpsampleinfo.GermpSampleInfoResponse;
import com.example.qc.parser.germpsampleinfo.Samplegemparray;
import com.example.qc.pojo.GotReportDetailedPojo;
import com.example.qc.pojo.SampleGerminationInfo;
import com.example.qc.pojo.SampleInfo;
import com.example.qc.pp.PPHomeActivity;
import com.example.qc.pp.PPReadingFormActivity;
import com.example.qc.retrofit.ApiInterface;
import com.example.qc.retrofit.RetrofitClient;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.ConnectionDetector;
import com.example.qc.utils.Utils;
import com.example.qc.volley.SendVolleyCall;
import com.example.qc.volley.volleyCallback;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.io.TextStreamsKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GerminationHomeActivity extends AppCompatActivity {

    private static final String TAG = "GerminationHomeActivity";
    private ImageButton back_nav;
    private ImageButton btn_samplescan;
    private ImageButton btn_samplemanual;
    private DatabaseHelper dbobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_germination_home);
        getSupportActionBar().hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        dbobj=new DatabaseHelper(getApplicationContext());
        back_nav = findViewById(R.id.back_nav);
        btn_samplescan = findViewById(R.id.btn_samplescan);
        btn_samplemanual = findViewById(R.id.btn_samplemanual);
    }

    private void init() {
        btn_samplescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(GerminationHomeActivity.this);
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
                final Dialog dialog = new Dialog(GerminationHomeActivity.this);
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
                        if (sampleno.length()!=8){
                            Toast.makeText(getApplicationContext(), "Enter Valid Sample Number", Toast.LENGTH_LONG).show();
                        }else {
                            dialog.dismiss();
                            if (new ConnectionDetector(GerminationHomeActivity.this).isConnectingToInternet()) {
                                getSampleInfo(sampleno);
                            }else {
                                getOfflineSampleInfo(sampleno);
                                Intent intent = new Intent(GerminationHomeActivity.this, OfflineFGTDataUpdateActivity.class);
                                startActivity(intent);
                            }

                        }
                    }
                });
            }
        });

        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GerminationHomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getOfflineSampleInfo(String sampleno) {
        int rowCount = dbobj.getRowCount(sampleno);
        if (rowCount>0){
            int row = dbobj.getSampleStatus(sampleno);
            if (row==0){
                SharedPreferences.getInstance().createString(sampleno, SharedPreferences.KEY_SAMPLENO);
                Intent intent = new Intent(GerminationHomeActivity.this, OfflineFGTDataUpdateActivity.class);
                intent.putExtra("sampleno", sampleno);
                startActivity(intent);
            }else {
                Toast.makeText(this, "Sample data already updated", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, "Sample number not in offline system", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {

            } else {
                if (new ConnectionDetector(GerminationHomeActivity.this).isConnectingToInternet()) {
                    getSampleInfo(result.getContents());
                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                }else {
                    getOfflineSampleInfo(result.getContents());
                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getSampleInfo(String contents){
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Log.e("Params:", contents);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        Call<GermpSampleInfoResponse> call =apiInterface.getGermpSampleInfo(userid,contents);

        call.enqueue(new Callback<GermpSampleInfoResponse>() {
            @Override
            public void onResponse(@NonNull Call<GermpSampleInfoResponse> call, @NonNull Response<GermpSampleInfoResponse> response) {
                if (response.isSuccessful()) {
                    progressDialog.cancel();
                    GermpSampleInfoResponse germpSampleInfoResponse = response.body();
                    if (germpSampleInfoResponse != null) {
                        if (!germpSampleInfoResponse.getError()){
                            Samplegemparray sampleData = germpSampleInfoResponse.getUser().getSamplegemparray();
                            SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_SAMPLEGERMINATIONINFO_OBJ, sampleData);
                            if (sampleData.getQcgSetupflg()==0){
                                Intent intent = new Intent(GerminationHomeActivity.this, GerminationSetupActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(GerminationHomeActivity.this, GerminationReadingFormActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("gmm", sampleData.getQcgTesttype());
                                intent.putExtra("sgtnoofrep", sampleData.getQcgSgtnoofrep());
                                intent.putExtra("fgtnoofrep", sampleData.getQcgVignoofrep());
                                intent.putExtra("mediatype", sampleData.getQcgVigtesttype());
                                intent.putExtra("dbtnoofrep", sampleData.getQcgSgtdnoofrep());
                                startActivity(intent);
                            }
                        }else {
                            /*assert response.body() != null;
                            JSONObject jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            String msg = jsonObj.getString("error_msg");*/
                            Toast.makeText(GerminationHomeActivity.this, germpSampleInfoResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    progressDialog.cancel();
                    if (response.errorBody() != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            String msg = jsonObj.getString("error_msg");
                            Toast.makeText(GerminationHomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GermpSampleInfoResponse> call, @NonNull Throwable t) {
                progressDialog.cancel();
                Log.e(TAG, "RetrofitError : " + t.getMessage());
                Toast.makeText(GerminationHomeActivity.this, "RetrofitError : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSampleInfo_old(String contents) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("sampleno", contents);

        new SendVolleyCall().executeProgram(GerminationHomeActivity.this, AppConfig.SAMPLEGERMINATIONINFO_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                JsonParser.getInstance().sampleGerminationInfoParser(response);
                SampleGerminationInfo sampleGerminationInfo = (SampleGerminationInfo) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_SAMPLEGERMINATIONINFO_OBJ,SampleGerminationInfo.class);
                if (sampleGerminationInfo.getMassage().equalsIgnoreCase("Success")){
                    if (sampleGerminationInfo.getQcg_setupflg().equals("0")){
                        Intent intent = new Intent(GerminationHomeActivity.this, GerminationSetupActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(GerminationHomeActivity.this, GerminationReadingFormActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("gmm", sampleGerminationInfo.getQcg_testtype());
                        intent.putExtra("sgtnoofrep", sampleGerminationInfo.getQcg_sgtnoofrep());
                        intent.putExtra("fgtnoofrep", sampleGerminationInfo.getQcg_vignoofrep());
                        intent.putExtra("mediatype", sampleGerminationInfo.getQcg_vigtesttype());
                        startActivity(intent);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), sampleGerminationInfo.getMassage(), Toast.LENGTH_SHORT).show();
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