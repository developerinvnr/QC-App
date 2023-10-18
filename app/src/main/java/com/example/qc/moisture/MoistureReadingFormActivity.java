package com.example.qc.moisture;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qc.MainActivity;
import com.example.qc.R;
import com.example.qc.parser.JsonParser;
import com.example.qc.parser.SubmitSuccessResponse;
import com.example.qc.pojo.SampleInfo;
import com.example.qc.pojo.SampleMoistureInfo;
import com.example.qc.pp.PPHomeActivity;
import com.example.qc.pp.PPReadingFormActivity;
import com.example.qc.pp.Utility;
import com.example.qc.retrofit.ApiInterface;
import com.example.qc.retrofit.RetrofitClient;
import com.example.qc.samplecollection.SampleCollectionHomeActivity;
import com.example.qc.samplecollection.SampleCollectionUpdateActivity;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.UserPermissions;
import com.example.qc.utils.Utils;
import com.example.qc.volley.SendVolleyCall;
import com.example.qc.volley.volleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import kotlin.io.TextStreamsKt;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MoistureReadingFormActivity extends AppCompatActivity {

    private static final String TAG = "MoistureReadingFormActivity";
    private ImageButton back_nav;
    private LinearLayout ll_hotairmethod;
    private LinearLayout ll_moisturemeter;
    private TextView tv_sampleno;

    private EditText et_mmreading1;
    private EditText et_mmreading2;
    private EditText et_mmreading3;
    private EditText et_mm_moistureper;

    private EditText et_m1rep1;
    private EditText et_m1rep2;
    private EditText et_m1rep3;
    private EditText et_m1rep4;
    private EditText et_m2rep1;
    private EditText et_m2rep2;
    private EditText et_m2rep3;
    private EditText et_m2rep4;
    private EditText et_m3rep1;
    private EditText et_m3rep2;
    private EditText et_m3rep3;
    private EditText et_m3rep4;
    private Button btn_submitm1;
    private Button btn_submitm2;
    private Button btn_submitm3;
    private EditText et_moistureper1;
    private EditText et_moistureper2;
    private EditText et_moistureper3;
    private EditText et_moistureper4;
    private EditText et_moistureper_mean;
    private Button btn_submit_mm;
    private Button btn_submit_haom;
    private Button btn_finalsubmit_mm;

    //Image Upload
    private ImageView iv_retphoto;
    private ImageView iv_retphoto_haom;
    private Button button_photo;
    private Button button_photo_haom;
    private static final int PERMISSION_CODE = 1234;
    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    private String path_billcopy;
    private final List<MultipartBody.Part> list = new ArrayList<>();
    private File BILL_COPY;
    private Uri image_uri;
    private String userChoosenTask;
    private String imageString;
    private EditText et_mmRemarks;
    private EditText et_haomRemarks;
    private LinearLayout ll_mmPhoto;
    private LinearLayout ll_mmRemarks;
    private LinearLayout ll_haomPhoto;
    private LinearLayout ll_haomRemarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moisture_reading_form);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        SampleMoistureInfo sampleMoistureInfo = (SampleMoistureInfo) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_SAMPLEMOISTUREINFO_OBJ,SampleMoistureInfo.class);
        back_nav = findViewById(R.id.back_nav);
        ll_moisturemeter = findViewById(R.id.ll_moisturemeter);
        ll_hotairmethod = findViewById(R.id.ll_hotairmethod);

        TextView tv_crop = findViewById(R.id.tv_crop);
        TextView tv_variety = findViewById(R.id.tv_variety);
        TextView tv_lono = findViewById(R.id.tv_lono);
        TextView tv_qtykg = findViewById(R.id.tv_qtykg);
        TextView tv_stage = findViewById(R.id.tv_stage);
        TextView tv_srfNo = findViewById(R.id.tv_srfNo);
        tv_sampleno = findViewById(R.id.tv_sampleno);

        //Photo Upload
        iv_retphoto = findViewById(R.id.iv_retphoto);
        iv_retphoto_haom = findViewById(R.id.iv_retphoto_haom);
        button_photo = findViewById(R.id.button_photo);
        button_photo_haom = findViewById(R.id.button_photo_haom);

        //Moisture Meter ID's Mapping
        et_mmreading1 = findViewById(R.id.et_mmreading1);
        et_mmreading2 = findViewById(R.id.et_mmreading2);
        et_mmreading3 = findViewById(R.id.et_mmreading3);
        et_mm_moistureper = findViewById(R.id.et_mm_moistureper);
        et_mmRemarks = findViewById(R.id.et_mmRemarks);
        ll_mmPhoto = findViewById(R.id.ll_mmPhoto);
        ll_mmRemarks = findViewById(R.id.ll_mmRemarks);

        //HAOM ID's Mapping
        et_m1rep1 = findViewById(R.id.et_m1rep1);
        et_m1rep2 = findViewById(R.id.et_m1rep2);
        et_m1rep3 = findViewById(R.id.et_m1rep3);
        et_m1rep4 = findViewById(R.id.et_m1rep4);
        btn_submitm1 = findViewById(R.id.btn_submitm1);
        et_m2rep1 = findViewById(R.id.et_m2rep1);
        et_m2rep2 = findViewById(R.id.et_m2rep2);
        et_m2rep3 = findViewById(R.id.et_m2rep3);
        et_m2rep4 = findViewById(R.id.et_m2rep4);
        btn_submitm2 = findViewById(R.id.btn_submitm2);
        et_m3rep1 = findViewById(R.id.et_m3rep1);
        et_m3rep2 = findViewById(R.id.et_m3rep2);
        et_m3rep3 = findViewById(R.id.et_m3rep3);
        et_m3rep4 = findViewById(R.id.et_m3rep4);
        btn_submitm3 = findViewById(R.id.btn_submitm3);
        et_moistureper1 = findViewById(R.id.et_moistureper1);
        et_moistureper2 = findViewById(R.id.et_moistureper2);
        et_moistureper3 = findViewById(R.id.et_moistureper3);
        et_moistureper4 = findViewById(R.id.et_moistureper4);
        et_moistureper_mean = findViewById(R.id.et_moistureper_mean);
        btn_submit_mm = findViewById(R.id.btn_submit_mm);
        btn_finalsubmit_mm = findViewById(R.id.btn_finalsubmit_mm);
        btn_submit_haom = findViewById(R.id.btn_submit_haom);
        et_haomRemarks = findViewById(R.id.et_haomRemarks);
        ll_haomPhoto = findViewById(R.id.ll_haomPhoto);
        ll_haomRemarks = findViewById(R.id.ll_haomRemarks);

        //Sample Info Display
        tv_crop.setText(sampleMoistureInfo.getCrop());
        tv_variety.setText(sampleMoistureInfo.getVariety());
        tv_lono.setText(sampleMoistureInfo.getLotno());
        tv_qtykg.setText(sampleMoistureInfo.getQty());
        tv_stage.setText(sampleMoistureInfo.getTrstage());
        tv_srfNo.setText(sampleMoistureInfo.getSrfno());
        tv_sampleno.setText(sampleMoistureInfo.getSampleno());

        //MM Form
        if (!sampleMoistureInfo.getQcm_mmrep1().equals("0")){
            et_mmreading1.setEnabled(false);
            et_mmreading2.setEnabled(false);
            et_mmreading3.setEnabled(false);
            et_mm_moistureper.setEnabled(false);
            btn_submit_mm.setVisibility(View.GONE);
            btn_finalsubmit_mm.setVisibility(View.VISIBLE);
            //ll_mmPhoto.setVisibility(View.VISIBLE);
            ll_mmRemarks.setVisibility(View.VISIBLE);
            et_mmreading1.setText(sampleMoistureInfo.getQcm_mmrep1());
            et_mmreading2.setText(sampleMoistureInfo.getQcm_mmrep2());
            et_mmreading3.setText(sampleMoistureInfo.getQcm_mmrep3());
            et_mm_moistureper.setText(sampleMoistureInfo.getQcm_mmrmoistper());
        }else {
            et_mmreading1.setEnabled(true);
            et_mmreading2.setEnabled(true);
            et_mmreading3.setEnabled(true);
            //ll_mmPhoto.setVisibility(View.GONE);
            ll_mmRemarks.setVisibility(View.GONE);
        }

        if (sampleMoistureInfo.getQcm_mmrflg().equalsIgnoreCase("0")){
            btn_finalsubmit_mm.setVisibility(View.GONE);
            //ll_mmPhoto.setVisibility(View.GONE);
            ll_mmRemarks.setVisibility(View.GONE);
        }else {
            btn_finalsubmit_mm.setVisibility(View.VISIBLE);
            //ll_mmPhoto.setVisibility(View.VISIBLE);
            ll_mmRemarks.setVisibility(View.VISIBLE);
        }

        //HAOM Form
        if (!sampleMoistureInfo.getQcm_m1rep1().equals("0")) {
            et_m1rep1.setText(sampleMoistureInfo.getQcm_m1rep1());
            et_m1rep2.setText(sampleMoistureInfo.getQcm_m1rep2());
            et_m1rep3.setText(sampleMoistureInfo.getQcm_m1rep3());
            et_m1rep4.setText(sampleMoistureInfo.getQcm_m1rep4());
            et_m1rep1.setEnabled(false);
            et_m1rep2.setEnabled(false);
            et_m1rep3.setEnabled(false);
            et_m1rep4.setEnabled(false);
        }
        if (!sampleMoistureInfo.getQcm_m2rep1().equals("0")) {
            et_m2rep1.setText(sampleMoistureInfo.getQcm_m2rep1());
            et_m2rep2.setText(sampleMoistureInfo.getQcm_m2rep2());
            et_m2rep3.setText(sampleMoistureInfo.getQcm_m2rep3());
            et_m2rep4.setText(sampleMoistureInfo.getQcm_m2rep4());
            et_m2rep1.setEnabled(false);
            et_m2rep2.setEnabled(false);
            et_m2rep3.setEnabled(false);
            et_m2rep4.setEnabled(false);
            btn_submitm2.setVisibility(View.GONE);
        }
        if (!sampleMoistureInfo.getQcm_m3rep1().equals("0")) {
            et_m3rep1.setText(sampleMoistureInfo.getQcm_m3rep1());
            et_m3rep2.setText(sampleMoistureInfo.getQcm_m3rep2());
            et_m3rep3.setText(sampleMoistureInfo.getQcm_m3rep3());
            et_m3rep4.setText(sampleMoistureInfo.getQcm_m3rep4());
            et_m3rep1.setEnabled(false);
            et_m3rep2.setEnabled(false);
            et_m3rep3.setEnabled(false);
            et_m3rep4.setEnabled(false);
            btn_submit_haom.setEnabled(true);
            btn_submitm3.setVisibility(View.GONE);
        }
        if (sampleMoistureInfo.getQcm_moistflg().equalsIgnoreCase("0") && sampleMoistureInfo.getQcm_haomflg().equalsIgnoreCase("1")){
            btn_submit_haom.setEnabled(true);
            btn_submit_haom.setVisibility(View.VISIBLE);
            //ll_haomPhoto.setVisibility(View.VISIBLE);
            ll_haomRemarks.setVisibility(View.VISIBLE);
        }else {
            btn_submit_haom.setEnabled(false);
            btn_submit_haom.setVisibility(View.GONE);
            //ll_haomPhoto.setVisibility(View.GONE);
            ll_haomRemarks.setVisibility(View.GONE);
        }
        if (!sampleMoistureInfo.getQcm_rep1moistper().equals("0") && !sampleMoistureInfo.getQcm_rep2moistper().equals("0") && !sampleMoistureInfo.getQcm_rep3moistper().equals("0") && !sampleMoistureInfo.getQcm_rep4moistper().equals("0")){
            et_moistureper1.setText(sampleMoistureInfo.getQcm_rep1moistper());
            et_moistureper2.setText(sampleMoistureInfo.getQcm_rep2moistper());
            et_moistureper3.setText(sampleMoistureInfo.getQcm_rep3moistper());
            et_moistureper4.setText(sampleMoistureInfo.getQcm_rep4moistper());
            float mean = (Float.parseFloat(sampleMoistureInfo.getQcm_rep1moistper())+Float.parseFloat(sampleMoistureInfo.getQcm_rep2moistper())+Float.parseFloat(sampleMoistureInfo.getQcm_rep3moistper())+Float.parseFloat(sampleMoistureInfo.getQcm_rep4moistper()))/4;
            //et_moistureper_mean.setText(String.valueOf(mean));
            et_moistureper_mean.setText(String.format(Locale.US, "%.2f", mean));
        }else if (!sampleMoistureInfo.getQcm_rep1moistper().equals("0") && !sampleMoistureInfo.getQcm_rep2moistper().equals("0") && !sampleMoistureInfo.getQcm_rep3moistper().equals("0") && sampleMoistureInfo.getQcm_rep4moistper().equals("0")){
            et_moistureper1.setText(sampleMoistureInfo.getQcm_rep1moistper());
            et_moistureper2.setText(sampleMoistureInfo.getQcm_rep2moistper());
            et_moistureper3.setText(sampleMoistureInfo.getQcm_rep3moistper());
            float mean = (Float.parseFloat(sampleMoistureInfo.getQcm_rep1moistper())+Float.parseFloat(sampleMoistureInfo.getQcm_rep2moistper())+Float.parseFloat(sampleMoistureInfo.getQcm_rep3moistper()))/3;
            //et_moistureper_mean.setText(String.valueOf(mean));
            et_moistureper_mean.setText(String.format(Locale.US, "%.2f", mean));
        }else if (!sampleMoistureInfo.getQcm_rep1moistper().equals("0") && !sampleMoistureInfo.getQcm_rep2moistper().equals("0") && sampleMoistureInfo.getQcm_rep3moistper().equals("0") && sampleMoistureInfo.getQcm_rep4moistper().equals("0")){
            et_moistureper1.setText(sampleMoistureInfo.getQcm_rep1moistper());
            et_moistureper2.setText(sampleMoistureInfo.getQcm_rep2moistper());
            float mean = (Float.parseFloat(sampleMoistureInfo.getQcm_rep1moistper())+Float.parseFloat(sampleMoistureInfo.getQcm_rep2moistper()))/2;
            //et_moistureper_mean.setText(String.valueOf(mean));
            et_moistureper_mean.setText(String.format(Locale.US, "%.2f", mean));
        }else {
            et_moistureper1.setText(sampleMoistureInfo.getQcm_rep1moistper());
            float mean = Float.parseFloat(sampleMoistureInfo.getQcm_rep1moistper());
            //et_moistureper_mean.setText(String.valueOf(mean));
            et_moistureper_mean.setText(String.format(Locale.US, "%.2f", mean));
        }
    }

    private void init() {
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoistureReadingFormActivity.this, MoistureHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageString = "MM";
                //selectImage();
            }
        });

        button_photo_haom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageString = "HAOM";
                //selectImage();
            }
        });

        btn_submitm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String formname="M1";
                String m1rep1 = et_m1rep1.getText().toString().trim();
                String m1rep2 = et_m1rep2.getText().toString().trim();
                String m1rep3 = et_m1rep3.getText().toString().trim();
                String m1rep4 = et_m1rep4.getText().toString().trim();
                confirm_alert(m1rep1,m1rep2,m1rep3,m1rep4,formname);
            }
        });

        btn_submitm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String formname="M2";
                String m1rep1 = et_m1rep1.getText().toString().trim();
                String m1rep2 = et_m1rep2.getText().toString().trim();
                String m1rep3 = et_m1rep3.getText().toString().trim();
                String m1rep4 = et_m1rep4.getText().toString().trim();

                String m2rep1 = et_m2rep1.getText().toString().trim();
                String m2rep2 = et_m2rep2.getText().toString().trim();
                String m2rep3 = et_m2rep3.getText().toString().trim();
                String m2rep4 = et_m2rep4.getText().toString().trim();
                if (!m1rep1.equals("0") && !m1rep1.equals("")){
                    confirm_alert(m2rep1,m2rep2,m2rep3,m2rep4,formname);
                }else {
                    Toast.makeText(getApplicationContext(), "Enter M1 Data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_submitm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String formname="M3";
                String m1rep1 = et_m1rep1.getText().toString().trim();
                String m1rep2 = et_m1rep2.getText().toString().trim();
                String m1rep3 = et_m1rep3.getText().toString().trim();
                String m1rep4 = et_m1rep4.getText().toString().trim();

                String m2rep1 = et_m2rep1.getText().toString().trim();
                String m2rep2 = et_m2rep2.getText().toString().trim();
                String m2rep3 = et_m2rep3.getText().toString().trim();
                String m2rep4 = et_m2rep4.getText().toString().trim();

                String m3rep1 = et_m2rep1.getText().toString().trim();
                String m3rep2 = et_m2rep2.getText().toString().trim();
                String m3rep3 = et_m2rep3.getText().toString().trim();
                String m3rep4 = et_m2rep4.getText().toString().trim();


               /* if (!m1rep1.equals("0") && !m1rep1.equals("") && !m1rep2.equals("0") && !m1rep2.equals("") && !m1rep3.equals("0") && !m1rep3.equals("") && !m1rep4.equals("0") && !m1rep4.equals("")){
                    if (!m2rep1.equals("0") && !m2rep1.equals("") && !m2rep2.equals("0") && !m2rep2.equals("") && !m2rep3.equals("0") && !m2rep3.equals("") && !m2rep4.equals("0") && !m2rep4.equals("")){*/
                        confirm_alert(m3rep1,m3rep2,m3rep3,m3rep4,formname);
                   /* }else {
                        Toast.makeText(getApplicationContext(), "Enter M2's All Replications Data", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "Enter M1's All Replications Data", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        btn_submit_mm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = "MM";
                String rep1 = et_mmreading1.getText().toString().trim();
                String rep2 = et_mmreading2.getText().toString().trim();
                String rep3 = et_mmreading3.getText().toString().trim();
                String moistureper = et_mm_moistureper.getText().toString().trim();
                if (rep1.equals("0") || rep1.equals("") || rep2.equals("0") || rep2.equals("")){
                    Toast.makeText(getApplicationContext(), "Enter all readings", Toast.LENGTH_LONG).show();
                }else {
                    confirm_alert(rep1,rep2,rep3,moistureper,type);
                    //finalSubmitAlert(type);
                }
            }
        });

        btn_finalsubmit_mm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = "MM";
                String remarks = et_mmRemarks.getText().toString().trim();
                finalSubmitAlert(type,remarks);
            }
        });

        btn_submit_haom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = "HAOM";
                String m1rep1 = et_m1rep1.getText().toString().trim();
                String m1rep2 = et_m1rep2.getText().toString().trim();
                String m1rep3 = et_m1rep3.getText().toString().trim();
                String m1rep4 = et_m1rep4.getText().toString().trim();

                String m2rep1 = et_m2rep1.getText().toString().trim();
                String m2rep2 = et_m2rep2.getText().toString().trim();
                String m2rep3 = et_m2rep3.getText().toString().trim();
                String m2rep4 = et_m2rep4.getText().toString().trim();

                String m3rep1 = et_m3rep1.getText().toString().trim();
                String m3rep2 = et_m3rep2.getText().toString().trim();
                String m3rep3 = et_m3rep3.getText().toString().trim();
                String m3rep4 = et_m3rep4.getText().toString().trim();

                String remarks = et_haomRemarks.getText().toString().trim();

                /*if (!m1rep1.equals("0") && !m1rep1.equals("") && !m1rep2.equals("0") && !m1rep2.equals("") && !m1rep3.equals("0") && !m1rep3.equals("") && !m1rep4.equals("0") && !m1rep4.equals("")){
                    if (!m2rep1.equals("0") && !m2rep1.equals("") && !m2rep2.equals("0") && !m2rep2.equals("") && !m2rep3.equals("0") && !m2rep3.equals("") && !m2rep4.equals("0") && !m2rep4.equals("")){
                        if (!m3rep1.equals("0") && !m3rep1.equals("") && !m3rep2.equals("0") && !m3rep2.equals("") && !m3rep3.equals("0") && !m3rep3.equals("") && !m3rep4.equals("0") && !m3rep4.equals("")){*/
                            finalSubmitAlert(type,remarks);
                        /*}else {
                            Toast.makeText(getApplicationContext(), "Enter M3's All Replication's Data", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(getApplicationContext(), "Enter M2's All Replication's Data", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "Enter M1's All Replication's Data", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        et_m3rep1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Moisture (%)    =(M2-M3)*100/(M2-M1)
                String m1rep1 = et_m1rep1.getText().toString().trim();
                String m2rep1 = et_m2rep1.getText().toString().trim();
                String m3rep1 = String.valueOf(charSequence);
                if (!m3rep1.equalsIgnoreCase("")) {
                    double moist1 = 0;
                    if (!m1rep1.equals("") && !m1rep1.equals("0") && !m2rep1.equals("") && !m2rep1.equals("0")) {
                        double M1REP1 = Double.parseDouble(m1rep1);
                        double M2REP1 = Double.parseDouble(m2rep1);
                        double M3REP1 = Double.parseDouble(m3rep1);
                        moist1 = (M2REP1 - M3REP1) * 100 / (M2REP1 - M1REP1);
                        //et_moistureper1.setText(String.valueOf(moist1));
                        et_moistureper1.setText(String.format(Locale.US, "%.1f", moist1));
                        String moisture1 = et_moistureper1.getText().toString().trim();

                        double moist11 = Double.parseDouble(moisture1);
                        double final_moistureper = moist1;
                        et_moistureper_mean.setText(String.format(Locale.US, "%.1f", final_moistureper));
                    }else {
                        Toast.makeText(getApplicationContext(), "Enter M1's & M2's Replication's Data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_m3rep2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Moisture (%)    =(M2-M3)*100/(M2-M1)
                String m1rep2 = et_m1rep2.getText().toString().trim();
                String m2rep2 = et_m2rep2.getText().toString().trim();
                String m3rep2 = String.valueOf(charSequence);
                if (!m3rep2.equalsIgnoreCase("")) {
                    double moist2 = 0;
                    if (!m1rep2.equals("") && !m1rep2.equals("0") && !m2rep2.equals("") && !m2rep2.equals("0")) {
                        double M1REP2 = Double.parseDouble(m1rep2);
                        double M2REP2 = Double.parseDouble(m2rep2);
                        double M3REP2 = Double.parseDouble(m3rep2);
                        moist2 = (M2REP2 - M3REP2) * 100 / (M2REP2 - M1REP2);
                        //et_moistureper2.setText(String.valueOf(moist2));
                        et_moistureper2.setText(String.format(Locale.US, "%.1f", moist2));

                        String moisture1 = et_moistureper1.getText().toString().trim();
                        String moisture2 = et_moistureper2.getText().toString().trim();

                        double moist1 = Double.parseDouble(moisture1);
                        double moist22 = Double.parseDouble(moisture2);
                        double final_moistureper = (moist1 + moist22) / 2;
                        et_moistureper_mean.setText(String.format(Locale.US, "%.1f", final_moistureper));
                    }else {
                        Toast.makeText(getApplicationContext(), "Enter M1's & M2's Replication's Data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_m3rep3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Moisture (%)    =(M2-M3)*100/(M2-M1)
                String m1rep3 = et_m1rep3.getText().toString().trim();
                String m2rep3 = et_m2rep3.getText().toString().trim();
                String m3rep3 = String.valueOf(charSequence);
                if (!m3rep3.equalsIgnoreCase("")) {
                    double moist3 = 0;
                    if (!m1rep3.equals("") && !m1rep3.equals("0") && !m2rep3.equals("") && !m2rep3.equals("0")) {
                        double M1REP3 = Double.parseDouble(m1rep3);
                        double M2REP3 = Double.parseDouble(m2rep3);
                        double M3REP3 = Double.parseDouble(m3rep3);
                        moist3 = (M2REP3 - M3REP3) * 100 / (M2REP3 - M1REP3);
                        //et_moistureper3.setText(String.valueOf(moist3));
                        et_moistureper3.setText(String.format(Locale.US, "%.1f", moist3));
                        String moisture1 = et_moistureper1.getText().toString().trim();
                        String moisture2 = et_moistureper2.getText().toString().trim();
                        String moisture3 = et_moistureper3.getText().toString().trim();

                        double moist1 = Double.parseDouble(moisture1);
                        double moist2 = Double.parseDouble(moisture2);
                        double moist33 = Double.parseDouble(moisture3);
                        double final_moistureper = (moist1 + moist2 + moist33) / 3;
                        et_moistureper_mean.setText(String.format(Locale.US, "%.1f", final_moistureper));
                    }else {
                        Toast.makeText(getApplicationContext(), "Enter M1's & M2's Replication's Data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_m3rep4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Moisture(%) = (M2-M3)*100/(M2-M1);
                String m1rep4 = et_m1rep4.getText().toString().trim();
                String m2rep4 = et_m2rep4.getText().toString().trim();
                String m3rep4 = String.valueOf(charSequence);
                if (!m3rep4.equalsIgnoreCase("")) {
                    double moist4 = 0;
                    if (!m1rep4.equals("") && !m1rep4.equals("0") && !m2rep4.equals("") && !m2rep4.equals("0")) {
                        double M1REP4 = Double.parseDouble(m1rep4);
                        double M2REP4 = Double.parseDouble(m2rep4);
                        double M3REP4 = Double.parseDouble(m3rep4);
                        moist4 = (M2REP4 - M3REP4) * 100 / (M2REP4 - M1REP4);
                        //et_moistureper4.setText(String.valueOf(moist4));
                        et_moistureper4.setText(String.format(Locale.US, "%.1f", moist4));
                        String moisture1 = et_moistureper1.getText().toString().trim();
                        String moisture2 = et_moistureper2.getText().toString().trim();
                        String moisture3 = et_moistureper3.getText().toString().trim();
                        String moisture4 = et_moistureper4.getText().toString().trim();
                        double moist1 = Double.parseDouble(moisture1);
                        double moist2 = Double.parseDouble(moisture2);
                        double moist3 = Double.parseDouble(moisture3);
                        moist4 = Double.parseDouble(moisture4);

                        double final_moistureper = (moist1 + moist2 + moist3 + moist4) / 4;
                        et_moistureper_mean.setText(String.format(Locale.US, "%.1f", final_moistureper));
                    }else {
                        Toast.makeText(getApplicationContext(), "Enter M1's & M2's Replication's Data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_mmreading1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et_mm_moistureper.setText(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_mmreading2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String rep1 = et_mmreading1.getText().toString().trim();
                String rep2 = String.valueOf(charSequence);
                if (!rep2.equalsIgnoreCase("")) {
                    double moist = 0;
                    if (!rep2.equals("") && !rep2.equals("0") && !rep1.equals("") && !rep1.equals("0")) {
                        double REP1 = Double.parseDouble(rep1);
                        double REP2 = Double.parseDouble(rep2);
                        if (REP1 < 4.5 || REP1 > 20) {
                            et_mmreading1.setError("It cannot be less than 4.5% or more than 20%");
                            et_mmreading1.requestFocus();
                        } else {
                            moist = (REP1 + REP2) / 2;
                            et_mm_moistureper.setText(String.format(Locale.US, "%.1f", moist));
                            //et_mm_moistureper.setText(String.valueOf(moist));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_mmreading3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String rep1 = et_mmreading1.getText().toString().trim();
                String rep2 = et_mmreading2.getText().toString().trim();
                String rep3 = String.valueOf(charSequence);
                if (!rep3.equalsIgnoreCase("")) {
                    double moist = 0;
                    if (!rep3.equals("") && !rep3.equals("0") && !rep2.equals("") && !rep2.equals("0") && !rep1.equals("") && !rep1.equals("0")) {
                        double REP1 = Double.parseDouble(rep1);
                        double REP2 = Double.parseDouble(rep2);
                        double REP3 = Double.parseDouble(rep3);
                        if (REP1 < 4.5 || REP1 > 20) {
                            et_mmreading2.setError("It cannot be less than 4.5% or more than 20%");
                            et_mmreading2.requestFocus();
                        } else {
                            moist = (REP1 + REP2 + REP3) / 3;
                            if (moist < 4.5 || moist > 20) {
                                et_mmreading3.setError("It cannot be less than 4.5% or more than 20%");
                                et_mm_moistureper.setText("");
                            } else {
                                et_mm_moistureper.setText(String.format(Locale.US, "%.1f", moist));
                                //et_mm_moistureper.setText(String.valueOf(moist));
                            }

                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void finalSubmitAlert(final String type, final String remarks) {
        final Dialog dialog = new Dialog(MoistureReadingFormActivity.this);
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
        alerttitle.setText("Review Data Before Submission. Once you have submit, you cannot edit the data of moisture metere as wll as HAOM. Do you want submit?");
        //message.setText(R.string.confirm_alert);

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
                finalSubmit(type,remarks);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void confirm_alert(final String m1rep1, final String m1rep2, final String m1rep3, final String m1rep4, final String formname) {
        final Dialog dialog = new Dialog(MoistureReadingFormActivity.this);
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
                if (formname.equals("M3")){
                    String moistureper1 = et_moistureper1.getText().toString().trim();
                    String moistureper2 = et_moistureper2.getText().toString().trim();
                    String moistureper3 = et_moistureper3.getText().toString().trim();
                    String moistureper4 = et_moistureper4.getText().toString().trim();
                    formSubmitM3(m1rep1,m1rep2,m1rep3,m1rep4,moistureper1,moistureper2,moistureper3,moistureper4,formname);
                }else if(formname.equals("MM")){
                    formSubmit(m1rep1,m1rep2,m1rep3,m1rep4,formname);
                }else {
                    String m1rep1 = et_m1rep1.getText().toString().trim();
                    String m1rep2 = et_m1rep2.getText().toString().trim();
                    String m1rep3 = et_m1rep3.getText().toString().trim();
                    String m1rep4 = et_m1rep4.getText().toString().trim();
                    formSubmit1(m1rep1,m1rep2,m1rep3,m1rep4,"M1");
                    //formSubmit(m1rep1,m1rep2,m1rep3,m1rep4,formname);
                }
            }
        });
    }

    private void finalSubmit_old(String type, String remarks){
        String sampleno = tv_sampleno.getText().toString().trim();
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
        RequestBody userid1 = RequestBody.create(MediaType.parse("text/plain"), userid);
        RequestBody scode1 = RequestBody.create(MediaType.parse("text/plain"), scode);
        RequestBody sampleno1 = RequestBody.create(MediaType.parse("text/plain"), sampleno);
        RequestBody type1 = RequestBody.create(MediaType.parse("text/plain"), type);
        RequestBody remarks1 = RequestBody.create(MediaType.parse("text/plain"), remarks);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Call<SubmitSuccessResponse> call =apiInterface.submitMoistureData(userid1, scode1, sampleno1, type1, remarks1, list);
        call.enqueue(new Callback<SubmitSuccessResponse>() {
            @SuppressLint({"NotifyDataSetChanged", "LongLogTag"})
            @Override
            public void onResponse(@NonNull Call<SubmitSuccessResponse> call, @NonNull retrofit2.Response<SubmitSuccessResponse> response) {
                if (response.isSuccessful()) {
                    progressDialog.cancel();
                    Log.e(TAG, "Response : " + response.body());
                    SubmitSuccessResponse submitSuccessResponse = response.body();
                    assert submitSuccessResponse != null;
                    if (submitSuccessResponse.getStatus()){
                        Toast.makeText(getApplicationContext(), submitSuccessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MoistureReadingFormActivity.this, PPHomeActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(), submitSuccessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.cancel();
                    if (response.errorBody() != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            String msg = jsonObj.getString("message");
                            Toast.makeText(MoistureReadingFormActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MoistureReadingFormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Call<SubmitSuccessResponse> call, @NonNull Throwable t) {
                progressDialog.cancel();
                Log.e(TAG, "RetrofitError : " + t.getMessage());
            }
        });

    }

    private void finalSubmit(String type, String remarks) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
        String sampleno = tv_sampleno.getText().toString().trim();
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("scode", scode);
        params.put("sampleno", sampleno);
        params.put("type", type);
        params.put("remarks", remarks);
        params.put("image", "");

        new SendVolleyCall().executeProgram(MoistureReadingFormActivity.this, AppConfig.SAMPLEMOISTUREFINALSUBMIT_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitGermpData(response);
                if (message.equalsIgnoreCase("Success")){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MoistureReadingFormActivity.this, MoistureHomeActivity.class);
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

    private void formSubmitM3(String m1rep1, String m1rep2, String m1rep3, String m1rep4, String moistureper1, String moistureper2, String moistureper3, String moistureper4, String formname) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
        String sampleno = tv_sampleno.getText().toString().trim();
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("hmtype", formname);
        params.put("scode", scode);
        params.put("sampleno", sampleno);
        params.put("m1rep1", "");
        params.put("m1rep2", "");
        params.put("m1rep3", "");
        params.put("m1rep4", "");
        params.put("m2rep1", "");
        params.put("m2rep2", "");
        params.put("m2rep3", "");
        params.put("m2rep4", "");
        params.put("m3rep1", m1rep1);
        params.put("m3rep2", m1rep2);
        params.put("m3rep3", m1rep3);
        params.put("m3rep4", m1rep4);
        params.put("rep1moistper", moistureper1);
        params.put("rep2moistper", moistureper2);
        params.put("rep3moistper", moistureper3);
        params.put("rep4moistper", moistureper4);
        params.put("haommoistper", "");
        params.put("mmrep1", "");
        params.put("mmrep2", "");
        params.put("mmrep3", "");
        params.put("mmrmoistper", "");

        new SendVolleyCall().executeProgram(MoistureReadingFormActivity.this, AppConfig.SAMPLEMOISTUREUPDATE_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitData(response);
                if (message.equalsIgnoreCase("Success")){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MoistureReadingFormActivity.this, MoistureHomeActivity.class);
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

    /*private void formSubmitM3(String m1rep1, String m1rep2, String m1rep3, String m1rep4, String moistureper1, String moistureper2, String moistureper3, String moistureper4, String formname) {
        String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
        String sampleno = tv_sampleno.getText().toString().trim();
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        RequestBody userid1 = RequestBody.create(MediaType.parse("text/plain"), userid);
        RequestBody scode1 = RequestBody.create(MediaType.parse("text/plain"), scode);
        RequestBody sampleno1 = RequestBody.create(MediaType.parse("text/plain"), sampleno);
        RequestBody m1rep11 = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody m1rep21 = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody m1rep31 = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody m1rep41 = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody m2rep11 = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody m2rep21 = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody m2rep31 = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody m2rep41 = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody m3rep11 = RequestBody.create(MediaType.parse("text/plain"), m1rep1);
        RequestBody m3rep21 = RequestBody.create(MediaType.parse("text/plain"), m1rep2);
        RequestBody m3rep31 = RequestBody.create(MediaType.parse("text/plain"), m1rep3);
        RequestBody m3rep41 = RequestBody.create(MediaType.parse("text/plain"), m1rep4);
        RequestBody moistureper11 = RequestBody.create(MediaType.parse("text/plain"), moistureper1);
        RequestBody moistureper21 = RequestBody.create(MediaType.parse("text/plain"), moistureper2);
        RequestBody moistureper31 = RequestBody.create(MediaType.parse("text/plain"), moistureper3);
        RequestBody moistureper41 = RequestBody.create(MediaType.parse("text/plain"), moistureper4);
        RequestBody formname1 = RequestBody.create(MediaType.parse("text/plain"), formname);
        RequestBody haommoistper = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody mmrep1 = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody mmrep2 = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody mmrep3 = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody mmrmoistper = RequestBody.create(MediaType.parse("text/plain"), "");
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Call<SubmitSuccessResponse> call =apiInterface.submitMoistureData(userid1,scode1,sampleno1,formname1,m1rep11,m1rep21,m1rep31,m1rep41,m2rep11,m2rep21,m2rep31,m2rep41,m3rep11,m3rep21,m3rep31,m3rep41,moistureper11,moistureper21,moistureper31,moistureper41,
                haommoistper,mmrep1,mmrep2,mmrep3,mmrmoistper,list);
        call.enqueue(new Callback<SubmitSuccessResponse>() {
            @SuppressLint({"NotifyDataSetChanged", "LongLogTag"})
            @Override
            public void onResponse(@NonNull Call<SubmitSuccessResponse> call, @NonNull retrofit2.Response<SubmitSuccessResponse> response) {
                if (response.isSuccessful()) {
                    progressDialog.cancel();
                    Log.e(TAG, "Response : " + response.body());
                    SubmitSuccessResponse submitSuccessResponse = response.body();
                    assert submitSuccessResponse != null;
                    if (submitSuccessResponse.getStatus()){
                        Toast.makeText(getApplicationContext(), submitSuccessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MoistureReadingFormActivity.this, PPHomeActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(), submitSuccessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.cancel();
                    if (response.errorBody() != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            String msg = jsonObj.getString("message");
                            Toast.makeText(MoistureReadingFormActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Call<SubmitSuccessResponse> call, @NonNull Throwable t) {
                progressDialog.cancel();
                Log.e(TAG, "RetrofitError : " + t.getMessage());
            }
        });
    }*/

    private void formSubmit1(String m1rep1, String m1rep2, String m1rep3, String m1rep4, final String m1) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        String sampleno = tv_sampleno.getText().toString().trim();
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("hmtype", m1);
        params.put("scode", scode);
        params.put("sampleno", sampleno);
        params.put("m1rep1", m1rep1);
        params.put("m1rep2", m1rep2);
        params.put("m1rep3", m1rep3);
        params.put("m1rep4", m1rep4);
        params.put("m2rep1", m1rep1);
        params.put("m2rep2", m1rep2);
        params.put("m2rep3", m1rep3);
        params.put("m2rep4", m1rep4);
        params.put("m3rep1", "");
        params.put("m3rep2", "");
        params.put("m3rep3", "");
        params.put("m3rep4", "");
        params.put("rep1moistper", "");
        params.put("rep2moistper", "");
        params.put("rep3moistper", "");
        params.put("rep4moistper", "");
        params.put("haommoistper", "");
        params.put("mmrep1", m1rep1);
        params.put("mmrep2", m1rep2);
        params.put("mmrep3", m1rep3);
        params.put("mmrmoistper", m1rep4);

        new SendVolleyCall().executeProgram(MoistureReadingFormActivity.this, AppConfig.SAMPLEMOISTUREUPDATE_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitData(response);
                if (message.equalsIgnoreCase("Success")){
                    String m2rep1 = et_m2rep1.getText().toString().trim();
                    String m2rep2 = et_m2rep2.getText().toString().trim();
                    String m2rep3 = et_m2rep3.getText().toString().trim();
                    String m2rep4 = et_m2rep4.getText().toString().trim();
                    formSubmit(m2rep1,m2rep2,m2rep3,m2rep4,"M2");
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

    private void formSubmit(String m1rep1, String m1rep2, String m1rep3, String m1rep4, final String formname) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        String sampleno = tv_sampleno.getText().toString().trim();
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("hmtype", formname);
        params.put("scode", scode);
        params.put("sampleno", sampleno);
        params.put("m1rep1", m1rep1);
        params.put("m1rep2", m1rep2);
        params.put("m1rep3", m1rep3);
        params.put("m1rep4", m1rep4);
        params.put("m2rep1", m1rep1);
        params.put("m2rep2", m1rep2);
        params.put("m2rep3", m1rep3);
        params.put("m2rep4", m1rep4);
        params.put("m3rep1", "");
        params.put("m3rep2", "");
        params.put("m3rep3", "");
        params.put("m3rep4", "");
        params.put("rep1moistper", "");
        params.put("rep2moistper", "");
        params.put("rep3moistper", "");
        params.put("rep4moistper", "");
        params.put("haommoistper", "");
        params.put("mmrep1", m1rep1);
        params.put("mmrep2", m1rep2);
        params.put("mmrep3", m1rep3);
        params.put("mmrmoistper", m1rep4);

        new SendVolleyCall().executeProgram(MoistureReadingFormActivity.this, AppConfig.SAMPLEMOISTUREUPDATE_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitData(response);
               if (message.equalsIgnoreCase("Success")){
                     /*if (formname.equals("MM")){
                        finalSubmit(formname);
                    }else {*/
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MoistureReadingFormActivity.this, MoistureHomeActivity.class);
                        startActivity(intent);
                    //}
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

    public void moisture_meter(View v)
    {
        ll_hotairmethod.setVisibility(View.GONE);
        ll_moisturemeter.setVisibility(View.VISIBLE);
    }

    public void hot_air_method(View v)
    {
        ll_hotairmethod.setVisibility(View.VISIBLE);
        ll_moisturemeter.setVisibility(View.GONE);
    }

    private void selectImage() {
        final CharSequence[] items = {"Click here to open camera", "Choose from gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MoistureReadingFormActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(MoistureReadingFormActivity.this);

                if (items[item].equals("Click here to open camera")) {
                    userChoosenTask = "Click here to open camera";
                    if (result) {
                        cameraIntent();
                    }

                } else if (items[item].equals("Choose from gallery")) {
                    userChoosenTask = "Choose from gallery";
                    if (result) {
                        galleryIntent();
                    }

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);
            } else {
                openCamera();
            }
        }else {
            openCamera();
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"new_image");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent camintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camintent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(camintent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        if (imageString.equalsIgnoreCase("MM")){
            iv_retphoto.setImageURI(image_uri);
        }else {
            iv_retphoto_haom.setImageURI(image_uri);
        }
        path_billcopy = getPath(image_uri);
        list.add(prepareFilePart("image", Uri.parse(path_billcopy)));
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        Bitmap selectedBitmap = null;
        if (data != null) {
            try {
                image_uri = data.getData();
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (imageString.equalsIgnoreCase("MM")){
            iv_retphoto.setImageBitmap(bm);
        }else {
            iv_retphoto_haom.setImageBitmap(bm);
        }
        assert bm != null;
        String absolutePath = saveImage(bm);
        list.add(prepareFilePart("image", Uri.parse(absolutePath)));
    }

    public String getPath(Uri image_uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(image_uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 25, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Samadhaan/");
        wallpaperDirectory.mkdirs();

        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {

            BILL_COPY = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            BILL_COPY.createNewFile();
            FileOutputStream fo = new FileOutputStream(BILL_COPY);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(MoistureReadingFormActivity.this, new String[]{BILL_COPY.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            Log.e("TAG", "File Saved::--->" + BILL_COPY.getAbsolutePath());
            return BILL_COPY.getAbsolutePath();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = new File(fileUri.getPath());
        Log.i("here is error", file.getAbsolutePath());
        //create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

}