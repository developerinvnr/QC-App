package com.example.qc.germination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qc.MainActivity;
import com.example.qc.R;
import com.example.qc.adapter.OfflineFGTDataListAdapter;
import com.example.qc.adapter.SyncFGTDataToServerAdapter;
import com.example.qc.database.DatabaseHelper;
import com.example.qc.moisture.MoistureHomeActivity;
import com.example.qc.moisture.MoistureReadingFormActivity;
import com.example.qc.parser.JsonParser;
import com.example.qc.parser.SubmitSuccessResponse;
import com.example.qc.parser.gotofflinedatafgt.FGTOfflineDataResponse;
import com.example.qc.parser.gotofflinedatafgt.User;
import com.example.qc.pojo.OfflineDataListPojo;
import com.example.qc.pojo.OfflineDataSyncToServer;
import com.example.qc.retrofit.ApiInterface;
import com.example.qc.retrofit.RetrofitClient;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.ConnectionDetector;
import com.example.qc.utils.Utils;
import com.example.qc.volley.SendVolleyCall;
import com.example.qc.volley.volleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kotlin.io.TextStreamsKt;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncOfflineDataToServerActivity extends AppCompatActivity {
    private static final String TAG = "OfflineDataListActivity";
    private ImageButton back_nav;
    private ImageButton btn_syncfromserver;
    private ImageButton btn_synctoserver;
    private RecyclerView recycler_view;
    private SyncFGTDataToServerAdapter adapter;

    private DatabaseHelper dbobj;
    private List<OfflineDataSyncToServer> pendingList=new ArrayList<>();
    private Button btn_submit;
    private List<OfflineDataSyncToServer> data = new ArrayList<>();
    private String fgtJsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_offline_data_to_server);
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
        recycler_view = findViewById(R.id.recycler_view);
        btn_submit = findViewById(R.id.btn_submit);

        adapter = new SyncFGTDataToServerAdapter(SyncOfflineDataToServerActivity.this, pendingList);
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
                Intent intent = new Intent(SyncOfflineDataToServerActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(SyncOfflineDataToServerActivity.this);
                dialog.setContentView(R.layout.custom_confirmalert);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setAttributes(lp);
                dialog.show();

                Button cancel = dialog.findViewById(R.id.no_cancel);
                Button submit = dialog.findViewById(R.id.yes_submit);
                final TextView message = dialog.findViewById(R.id.tv_message);
                final TextView alerttitle = dialog.findViewById(R.id.tv_alerttitle);
                alerttitle.setText("Review Data Before Submission");
                message.setText(R.string.confirm_alert);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        sendDataToServer();
                    }
                });
            }
        });
    }

    private void sendDataToServer(){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("scode", scode);
        params.put("germpdata", fgtJsonData);

        new SendVolleyCall().executeProgram(SyncOfflineDataToServerActivity.this, AppConfig.OFFLINEFGTSUBMIT_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitGermpData(response);
                if (message.equalsIgnoreCase("Success")){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    dbobj.updateArrSyncFlag("1","1");
                    Intent intent = new Intent(SyncOfflineDataToServerActivity.this, OfflineDataListActivity.class);
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

    @SuppressLint("NotifyDataSetChanged")
    private void getOfflinePendingList() {
        //transactionObj = dbobj.getTransactioList();
        pendingList.clear();
        data = dbobj.getFGTDataForServer();
        pendingList.addAll(dbobj.getFGTDataForServer());
        fgtJsonData = SharedPreferences.getInstance().getJsonFromObject(data);
        Log.e("pendingList", fgtJsonData);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SyncOfflineDataToServerActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}