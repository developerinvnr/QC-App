package com.example.qc.germination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toast;

import com.example.qc.MainActivity;
import com.example.qc.R;
import com.example.qc.adapter.OfflineFGTDataListAdapter;
import com.example.qc.database.DatabaseHelper;
import com.example.qc.parser.gotofflinedatafgt.FGTOfflineDataResponse;
import com.example.qc.parser.gotofflinedatafgt.User;
import com.example.qc.pojo.OfflineDataListPojo;
import com.example.qc.retrofit.ApiInterface;
import com.example.qc.retrofit.RetrofitClient;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.ConnectionDetector;
import com.example.qc.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.io.TextStreamsKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfflineDataListActivity extends AppCompatActivity {
    private static final String TAG = "OfflineDataListActivity";
    private ImageButton back_nav;
    private ImageButton btn_syncfromserver;
    private ImageButton btn_synctoserver;
    private RecyclerView recycler_view;
    private OfflineFGTDataListAdapter adapter;

    private DatabaseHelper dbobj;
    private List<OfflineDataListPojo> pendingList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_data_list);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        dbobj=new DatabaseHelper(getApplicationContext());
        back_nav = findViewById(R.id.back_nav);
        btn_syncfromserver = findViewById(R.id.btn_syncfromserver);
        btn_synctoserver = findViewById(R.id.btn_synctoserver);
        recycler_view = findViewById(R.id.recycler_view);

        adapter = new OfflineFGTDataListAdapter(OfflineDataListActivity.this, pendingList);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(adapter);

        getOfflinePendingList();

    }

    private void init(){
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OfflineDataListActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        btn_syncfromserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new ConnectionDetector(OfflineDataListActivity.this).isConnectingToInternet()) {
                    String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
                    String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
                    getDataFromServer(userid,scode);
                }
            }
        });

        btn_synctoserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OfflineDataListActivity.this, SyncOfflineDataToServerActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getDataFromServer(String userid, String scode) {
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Log.e("Params:", userid+"="+scode);

        Call<FGTOfflineDataResponse> call =apiInterface.getFGTOfflineData(userid,scode);

        call.enqueue(new Callback<FGTOfflineDataResponse>() {
            @Override
            public void onResponse(@NonNull Call<FGTOfflineDataResponse> call, @NonNull Response<FGTOfflineDataResponse> response) {
                if (response.isSuccessful()) {
                    FGTOfflineDataResponse fgtOfflineDataResponse = response.body();
                    if (fgtOfflineDataResponse != null) {
                        if (fgtOfflineDataResponse.getStatus()){
                            List<User> sampleData = fgtOfflineDataResponse.getUser();
                            for(User obj:sampleData){
                                int rowCount = dbobj.getRowCount(obj.getSampleno());
                                if (rowCount==0){
                                    dbobj.saveSampleDetails(obj.getCrop(), obj.getVariety(), obj.getLotno(), obj.getSampleno(), obj.getTrstage(), obj.getQcgTesttype(), obj.getSeedsize(), obj.getQcgNoofseedinonerepfgt(), obj.getQcgVigtesttype(), obj.getQcgVignoofrep());
                                }
                            }
                            getOfflinePendingList();
                        }else {
                            /*assert response.body() != null;
                            JSONObject jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            String msg = jsonObj.getString("error_msg");*/
                            Toast.makeText(OfflineDataListActivity.this, fgtOfflineDataResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.cancel();
                    }
                } else {
                    progressDialog.cancel();
                    if (response.errorBody() != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            String msg = jsonObj.getString("error_msg");
                            Toast.makeText(OfflineDataListActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FGTOfflineDataResponse> call, @NonNull Throwable t) {
                progressDialog.cancel();
                Log.e(TAG, "RetrofitError : " + t.getMessage());
                Toast.makeText(OfflineDataListActivity.this, "RetrofitError : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getOfflinePendingList() {
        //transactionObj = dbobj.getTransactioList();
        pendingList.clear();
        List<OfflineDataListPojo> data = dbobj.getTransactioList();
        pendingList.addAll(dbobj.getTransactioList());
        Log.e("pendingList", data.toString());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OfflineDataListActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}