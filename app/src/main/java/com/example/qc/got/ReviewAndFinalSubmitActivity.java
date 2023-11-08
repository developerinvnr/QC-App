package com.example.qc.got;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.qc.MainActivity;
import com.example.qc.R;
import com.example.qc.adapter.DataReviewAdapter;
import com.example.qc.adapter.ReportSummeryAdapter;
import com.example.qc.germination.GerminationHomeActivity;
import com.example.qc.germination.GerminationReadingFormActivity;
import com.example.qc.germination.GerminationSetupActivity;
import com.example.qc.parser.JsonParser;
import com.example.qc.parser.germpsampleinfo.GermpSampleInfoResponse;
import com.example.qc.parser.germpsampleinfo.Samplegemparray;
import com.example.qc.parser.gotpendinglist.GOTPendingList;
import com.example.qc.parser.gotpendinglist.Samparray;
import com.example.qc.retrofit.ApiInterface;
import com.example.qc.retrofit.RetrofitClient;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewAndFinalSubmitActivity extends AppCompatActivity implements DataReviewAdapter.DataReviewAdapterListener, AdapterView.OnItemSelectedListener{
    private static final String TAG = "ReviewAndFinalSubmitActivity";
    private ImageButton back_nav;
    private DataReviewAdapter adapter;
    private RecyclerView recycler_view;
    private Spinner spinner_location;
    private Spinner spinner_state;
    private ArrayList<String> stateArray;
    private ArrayAdapter<String> stateadapter;
    private ArrayList<String> locArray;
    private ArrayAdapter<String> locadapter;
    private String userid;
    private List<Samparray> pendingList = new ArrayList<>();

    private DataReviewAdapter.DataReviewAdapterListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_and_final_submit);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        back_nav = findViewById(R.id.back_nav);

        recycler_view = findViewById(R.id.recycler_view);
        spinner_location = findViewById(R.id.spinner_location);
        spinner_state = findViewById(R.id.spinner_state);

        stateArray = new ArrayList<>();
        stateArray.add(0, "Select");
        stateadapter = Utils.getInstance().spinnerAdapter(ReviewAndFinalSubmitActivity.this, stateArray);
        spinner_state.setAdapter(stateadapter);

        locArray = new ArrayList<>();
        locArray.add(0, "Select");
        locadapter = Utils.getInstance().spinnerAdapter(ReviewAndFinalSubmitActivity.this, locArray);
        spinner_location.setAdapter(locadapter);

        adapter = new DataReviewAdapter(ReviewAndFinalSubmitActivity.this, pendingList, this);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(adapter);
    }

    private void init(){
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewAndFinalSubmitActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        Log.e("UserId", userid);

        getStateList(userid);


        spinner_state.setOnItemSelectedListener(ReviewAndFinalSubmitActivity.this);
        spinner_location.setOnItemSelectedListener(ReviewAndFinalSubmitActivity.this);
    }

    private void getStateList(String userid) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);

        new SendVolleyCall().executeProgram(ReviewAndFinalSubmitActivity.this, AppConfig.GETSATELIST_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                stateArray = JsonParser.getInstance().parseStateList(ReviewAndFinalSubmitActivity.this, response);
                String message = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MESSAGE);
                if (message.equalsIgnoreCase("Success")){
                    stateadapter = Utils.getInstance().spinnerAdapter(ReviewAndFinalSubmitActivity.this, stateArray);
                    spinner_state.setAdapter(stateadapter);
                    stateadapter.notifyDataSetChanged();
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

    private void getLocationList(String state) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("statename", state);

        new SendVolleyCall().executeProgram(ReviewAndFinalSubmitActivity.this, AppConfig.SAMPLE_GOTLOCATIONLIST_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                locArray = JsonParser.getInstance().parseLocList(ReviewAndFinalSubmitActivity.this, response);
                String message = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MESSAGE);
                if (message.equalsIgnoreCase("Success")){
                    locadapter = Utils.getInstance().spinnerAdapter(ReviewAndFinalSubmitActivity.this, locArray);
                    spinner_location.setAdapter(locadapter);
                    locadapter.notifyDataSetChanged();
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
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spinner_state) {
            String state = spinner_state.getSelectedItem().toString().trim();
            if (!state.equalsIgnoreCase("Select")) {
                locArray.clear();
                locArray.add(0, "Select");
                locadapter = Utils.getInstance().spinnerAdapter(ReviewAndFinalSubmitActivity.this, locArray);
                pendingList.clear();
                spinner_location.setAdapter(locadapter);
                locadapter.notifyDataSetChanged();
                getLocationList(state);
            } else {
                locArray.clear();
                locArray.add(0, "Select");
                locadapter = Utils.getInstance().spinnerAdapter(ReviewAndFinalSubmitActivity.this, locArray);
                pendingList.clear();
                spinner_location.setAdapter(locadapter);
                locadapter.notifyDataSetChanged();
            }
        } else if (adapterView.getId() == R.id.spinner_location) {
            String state = spinner_state.getSelectedItem().toString().trim();
            String location = spinner_location.getSelectedItem().toString().trim();
            if (!location.equalsIgnoreCase("Select")) {
                pendingList.clear();
                adapter.notifyDataSetChanged();
                recycler_view.setAdapter(adapter);
                getPendingList(userid,state,location);
            } else {
                pendingList.clear();
                adapter.notifyDataSetChanged();
                recycler_view.setAdapter(adapter);
            }
        }
    }

    private void getPendingList(String userid, String state, String location) {
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Log.e("Params:", userid+"="+location);
        Call<GOTPendingList> call =apiInterface.getGOTPendingList(userid,state,location);

        call.enqueue(new Callback<GOTPendingList>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<GOTPendingList> call, @NonNull Response<GOTPendingList> response) {
                if (response.isSuccessful()) {
                    progressDialog.cancel();
                    GOTPendingList gotPendingList = response.body();
                    if (gotPendingList != null) {
                        if (gotPendingList.getStatus()){
                            pendingList = gotPendingList.getUser().getSamparray();
                            adapter = new DataReviewAdapter(ReviewAndFinalSubmitActivity.this, pendingList, listener);
                            recycler_view.setHasFixedSize(true);
                            recycler_view.setLayoutManager(new LinearLayoutManager(ReviewAndFinalSubmitActivity.this));
                            recycler_view.addItemDecoration(new DividerItemDecoration(ReviewAndFinalSubmitActivity.this, LinearLayoutManager.VERTICAL));
                            recycler_view.setItemAnimator(new DefaultItemAnimator());
                            recycler_view.setAdapter(adapter);
                        }else {
                            Toast.makeText(ReviewAndFinalSubmitActivity.this, gotPendingList.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    progressDialog.cancel();
                    if (response.errorBody() != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            String msg = jsonObj.getString("error_msg");
                            Toast.makeText(ReviewAndFinalSubmitActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Call<GOTPendingList> call, @NonNull Throwable t) {
                progressDialog.cancel();
                Log.e(TAG, "RetrofitError : " + t.getMessage());
                Toast.makeText(ReviewAndFinalSubmitActivity.this, "RetrofitError : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onContactSelected(Samparray samparray) {
        Intent intent = new Intent(ReviewAndFinalSubmitActivity.this, GOTFinalSubmitActivity.class);
        intent.putExtra("sampleno", samparray.getSampleno());
        startActivity(intent);
        finish();
    }

    public void viewSampleDetails(Samparray samparray) {
        if (samparray.getCropname().equalsIgnoreCase("Paddy Seed")){
            Intent intent = new Intent(ReviewAndFinalSubmitActivity.this, GOTPaddyFinalSubmitActivity.class);
            intent.putExtra("sampleno", samparray.getSampleno());
            startActivity(intent);
            finish();
        } else if (samparray.getCropname().equalsIgnoreCase("Maize Seed")) {
            Intent intent = new Intent(ReviewAndFinalSubmitActivity.this, GOTMaizeFinalSubmitActivity.class);
            intent.putExtra("sampleno", samparray.getSampleno());
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(ReviewAndFinalSubmitActivity.this, GOTFinalSubmitActivity.class);
            intent.putExtra("sampleno", samparray.getSampleno());
            startActivity(intent);
            finish();
        }
    }
}