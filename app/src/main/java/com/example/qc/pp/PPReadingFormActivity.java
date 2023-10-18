package com.example.qc.pp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import android.graphics.BitmapFactory;
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
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qc.R;
import com.example.qc.got.TransplantingPendingReportActivity;
import com.example.qc.moisture.MoistureHomeActivity;
import com.example.qc.moisture.MoistureReadingFormActivity;
import com.example.qc.parser.Gotrepdetailsarray;
import com.example.qc.parser.JsonParser;
import com.example.qc.parser.PendingReportResponse;
import com.example.qc.parser.SubmitSuccessResponse;
import com.example.qc.pojo.GotReportDetailedPojo;
import com.example.qc.pojo.LoginResponse;
import com.example.qc.pojo.SampleInfo;
import com.example.qc.pojo.SamplePPInfo;
import com.example.qc.retrofit.ApiInterface;
import com.example.qc.retrofit.RetrofitClient;
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

import kotlin.io.TextStreamsKt;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class PPReadingFormActivity extends AppCompatActivity {

    private static final String TAG = "PPReadingFormActivity";
    private ImageButton back_nav;

    private TextView tv_sampleno;
    private Button btn_submit;
    private EditText et_samplewt;
    private EditText et_pureseed;
    private EditText et_pureseedper;
    private EditText et_inertmattert;
    private EditText et_inertmattertper;
    private EditText et_lightseed;
    private EditText et_lightseedper;
    private EditText et_totalnoinsample;
    private EditText et_tobeexnoperkg;
    private EditText et_odv_totalnoinsample;
    private EditText et_odv_tobeexnoperkg;
    private EditText et_discoloredseed;
    private EditText et_discoloredper;
    private EditText et_pinholenumber;
    private EditText et_pinhole_tobeexnoperkg;

    //Add Photo
    private ImageView iv_photo;
    private Button button_photo;
    private static final int PERMISSION_CODE = 1234;
    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    private String path_billcopy;
    private final List<MultipartBody.Part> list = new ArrayList<>();
    private File BILL_COPY;
    private Uri image_uri;
    private String userChoosenTask;


    private EditText et_pinhole_remarks;
    private EditText et_odv_wt;
    private EditText et_odv_wt_per;
    private EditText et_totalnoinsample_germp;
    private EditText et_tobeexnoperkg_germp;
    private EditText et_germp_wt;
    private EditText et_germp_wt_per;
    private EditText et_germp_remarks;
    private EditText et_ocs_wt;
    private EditText et_ocs_wt_per;
    private EditText et_ocs_remarks;
    private EditText et_odv_remarks;
    private EditText et_pinhole_wt;
    private EditText et_pinhole_wt_per;
    private LinearLayout ll_1010;
    private EditText et_odv_1010;
    private EditText et_odv_fineGrain;
    private LinearLayout ll_boldGrain;
    private EditText et_odv_boldGrain;
    private EditText et_odv_longGrain;
    private LinearLayout ll_otherType;
    private EditText et_odv_otherTyp;
    private LinearLayout ll_odv_total;
    private EditText et_odv_total;
    private EditText et_odv_total_per;
    private LinearLayout ll_odv_total_no;
    private LinearLayout ll_odv_wt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_p_reading_form);
        getSupportActionBar().hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        back_nav = findViewById(R.id.back_nav);
        btn_submit = findViewById(R.id.btn_submit);

        et_samplewt = findViewById(R.id.et_samplewt);
        et_pureseed = findViewById(R.id.et_pureseed);
        et_pureseedper = findViewById(R.id.et_pureseedper);
        et_inertmattert = findViewById(R.id.et_inertmattert);
        et_inertmattertper = findViewById(R.id.et_inertmattertper);
        et_lightseed = findViewById(R.id.et_lightseed);
        et_lightseedper = findViewById(R.id.et_lightseedper);


        et_totalnoinsample = findViewById(R.id.et_totalnoinsample);
        et_tobeexnoperkg = findViewById(R.id.et_tobeexnoperkg);
        et_ocs_wt = findViewById(R.id.et_ocs_wt);
        et_ocs_wt_per = findViewById(R.id.et_ocs_wt_per);
        et_ocs_remarks = findViewById(R.id.et_ocs_remarks);

        et_totalnoinsample_germp = findViewById(R.id.et_totalnoinsample_germp);
        et_tobeexnoperkg_germp = findViewById(R.id.et_tobeexnoperkg_germp);
        et_germp_wt = findViewById(R.id.et_germp_wt);
        et_germp_wt_per = findViewById(R.id.et_germp_wt_per);
        et_germp_remarks = findViewById(R.id.et_germp_remarks);

        et_odv_totalnoinsample = findViewById(R.id.et_odv_totalnoinsample);
        et_odv_tobeexnoperkg = findViewById(R.id.et_odv_tobeexnoperkg);
        et_odv_wt = findViewById(R.id.et_odv_wt);
        et_odv_wt_per = findViewById(R.id.et_odv_wt_par);
        et_odv_remarks = findViewById(R.id.et_odv_remarks);
        ll_odv_total_no = findViewById(R.id.ll_odv_total_no);
        ll_odv_wt = findViewById(R.id.ll_odv_wt);

        ll_1010 = findViewById(R.id.ll_1010);
        et_odv_1010 = findViewById(R.id.et_odv_1010);
        et_odv_fineGrain = findViewById(R.id.et_odv_fineGrain);
        ll_boldGrain = findViewById(R.id.ll_boldGrain);
        et_odv_boldGrain = findViewById(R.id.et_odv_boldGrain);
        et_odv_longGrain = findViewById(R.id.et_odv_longGrain);
        ll_otherType = findViewById(R.id.ll_otherType);
        et_odv_otherTyp = findViewById(R.id.et_odv_otherTyp);
        ll_odv_total = findViewById(R.id.ll_odv_total);
        et_odv_total = findViewById(R.id.et_odv_total);
        et_odv_total_per = findViewById(R.id.et_odv_total_per);

        et_discoloredseed = findViewById(R.id.et_discoloredseed);
        et_discoloredper = findViewById(R.id.et_discoloredper);

        et_pinholenumber = findViewById(R.id.et_pinholenumber);
        et_pinhole_tobeexnoperkg = findViewById(R.id.et_pinhole_tobeexnoperkg);
        et_pinhole_wt = findViewById(R.id.et_pinhole_wt);
        et_pinhole_wt_per = findViewById(R.id.et_pinhole_wt_per);
        et_pinhole_remarks = findViewById(R.id.et_pinhole_remarks);

        TextView tv_crop = findViewById(R.id.tv_crop);
        TextView tv_variety = findViewById(R.id.tv_variety);
        TextView tv_lono = findViewById(R.id.tv_lono);
        TextView tv_qtykg = findViewById(R.id.tv_qtykg);
        TextView tv_stage = findViewById(R.id.tv_stage);
        tv_sampleno = findViewById(R.id.tv_sampleno);
        TextView tv_textmsg = findViewById(R.id.tv_textmsg);
        TextView tv_srfNo = findViewById(R.id.tv_srfNo);

        iv_photo = findViewById(R.id.iv_retphoto);
        button_photo = findViewById(R.id.button_photo);

        SamplePPInfo samplePPInfo = (SamplePPInfo) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_SAMPLEPPINFO_OBJ,SamplePPInfo.class);
        tv_crop.setText(samplePPInfo.getCrop());
        tv_variety.setText(samplePPInfo.getVariety());
        tv_lono.setText(samplePPInfo.getLotno());
        tv_stage.setText(samplePPInfo.getTrstage());
        tv_qtykg.setText(samplePPInfo.getQty());
        tv_sampleno.setText(samplePPInfo.getSampleno());
        tv_srfNo.setText(samplePPInfo.getSrfno());

        if (samplePPInfo.getCrop().equalsIgnoreCase("Paddy Seed")){
            ll_odv_total_no.setVisibility(View.GONE);
            //ll_odv_wt.setVisibility(View.GONE);
            ll_1010.setVisibility(View.VISIBLE);
            ll_boldGrain.setVisibility(View.VISIBLE);
            ll_otherType.setVisibility(View.VISIBLE);
            ll_odv_total.setVisibility(View.VISIBLE);
        }else {
            ll_odv_total_no.setVisibility(View.VISIBLE);
            //ll_odv_wt.setVisibility(View.VISIBLE);
            ll_1010.setVisibility(View.GONE);
            ll_boldGrain.setVisibility(View.GONE);
            ll_otherType.setVisibility(View.GONE);
            ll_odv_total.setVisibility(View.GONE);
        }

        if (!samplePPInfo.getQcp_ppdataflg().equalsIgnoreCase("0")){
            et_samplewt.setText(samplePPInfo.getQcp_samplewt());
            et_pureseed.setText(samplePPInfo.getQcp_pureseed());
            et_pureseedper.setText(samplePPInfo.getQcp_pureseedper());
            et_inertmattert.setText(samplePPInfo.getQcp_imseed());
            et_inertmattertper.setText(samplePPInfo.getQcp_imseedper());
            et_lightseed.setText(samplePPInfo.getQcp_lightseed());
            et_lightseedper.setText(samplePPInfo.getQcp_lightseedper());
            et_totalnoinsample.setText(samplePPInfo.getQcp_ocseedno());
            et_tobeexnoperkg.setText(samplePPInfo.getQcp_ocseedinkg());
            et_odv_totalnoinsample.setText(samplePPInfo.getQcp_odvseedno());
            et_odv_tobeexnoperkg.setText(samplePPInfo.getQcp_odvseedinkg());
            et_discoloredseed.setText(samplePPInfo.getQcp_dcseed());
            et_discoloredper.setText(samplePPInfo.getQcp_dcseedper());
            et_pinholenumber.setText(samplePPInfo.getQcp_phseedno());
            et_pinhole_tobeexnoperkg.setText(samplePPInfo.getQcp_phseedinkg());
            et_totalnoinsample_germp.setText(samplePPInfo.getQcpGstotnosmp());
            et_tobeexnoperkg_germp.setText(samplePPInfo.getQcpGsnoperkg());
            et_germp_wt.setText(samplePPInfo.getQcpGswtgms());
            et_germp_wt_per.setText(samplePPInfo.getQcpGsper());
            et_germp_remarks.setText(samplePPInfo.getQcpGsremark());
            et_ocs_wt.setText(samplePPInfo.getQcpOcwt());
            et_ocs_wt_per.setText(samplePPInfo.getQcpOcper());
            et_ocs_remarks.setText(samplePPInfo.getQcpOcremark());
            et_odv_wt.setText(samplePPInfo.getQcpOdvwt());
            et_odv_wt_per.setText(samplePPInfo.getQcpOdvper());
            et_odv_1010.setText(samplePPInfo.getQcpOdv1010());
            et_odv_fineGrain.setText(samplePPInfo.getQcpOdvfgrain());
            et_odv_boldGrain.setText(samplePPInfo.getQcpOdvbgrain());
            et_odv_longGrain.setText(samplePPInfo.getQcpOdvlonggrain());
            et_odv_otherTyp.setText(samplePPInfo.getQcpOdvothertype());
            et_odv_total.setText(samplePPInfo.getQcpOdvtotal());
            et_odv_total_per.setText(samplePPInfo.getQcpOdvtotalper());
            et_odv_remarks.setText(samplePPInfo.getQcpOdvremark());
            et_pinhole_wt.setText(samplePPInfo.getQcpPhwt());
            et_pinhole_wt_per.setText(samplePPInfo.getQcpPhper());
            et_pinhole_remarks.setText(samplePPInfo.getQcpPhremark());

            /*byte[] imageBytes = Base64.decode(samplePPInfo.getQcp_ppphoto(), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            iv_photo.setImageBitmap(decodedImage);*/

            Glide.with(PPReadingFormActivity.this)
                    .load(Uri.parse(AppConfig.IMAGE_URL + samplePPInfo.getQcp_ppphoto()))
                    .into(iv_photo);

            et_samplewt.setEnabled(false);
            et_pureseed.setEnabled(false);
            et_pureseedper.setEnabled(false);
            et_inertmattert.setEnabled(false);
            et_inertmattertper.setEnabled(false);
            et_lightseed.setEnabled(false);
            et_lightseedper.setEnabled(false);
            et_totalnoinsample.setEnabled(false);
            et_tobeexnoperkg.setEnabled(false);
            et_odv_totalnoinsample.setEnabled(false);
            et_odv_tobeexnoperkg.setEnabled(false);
            et_discoloredseed.setEnabled(false);
            et_discoloredper.setEnabled(false);
            et_pinholenumber.setEnabled(false);
            et_pinhole_tobeexnoperkg.setEnabled(false);
            et_pinhole_remarks.setEnabled(false);

            et_totalnoinsample_germp.setEnabled(false);
            et_tobeexnoperkg_germp.setEnabled(false);
            et_germp_wt.setEnabled(false);
            et_germp_wt_per.setEnabled(false);
            et_germp_remarks.setEnabled(false);
            et_ocs_wt.setEnabled(false);
            et_ocs_wt_per.setEnabled(false);
            et_ocs_remarks.setEnabled(false);
            et_odv_wt.setEnabled(false);
            et_odv_wt_per.setEnabled(false);
            et_odv_1010.setEnabled(false);
            et_odv_fineGrain.setEnabled(false);
            et_odv_boldGrain.setEnabled(false);
            et_odv_longGrain.setEnabled(false);
            et_odv_otherTyp.setEnabled(false);
            et_odv_total.setEnabled(false);
            et_odv_total_per.setEnabled(false);
            et_odv_remarks.setEnabled(false);
            et_pinhole_wt.setEnabled(false);
            et_pinhole_wt_per.setEnabled(false);
            et_pinhole_remarks.setEnabled(false);
            btn_submit.setVisibility(View.GONE);
            button_photo.setVisibility(View.GONE);
            tv_textmsg.setVisibility(View.VISIBLE);
        }else {
            et_samplewt.setEnabled(true);
            et_pureseed.setEnabled(true);
            //et_pureseedper.setEnabled(true);
            et_inertmattert.setEnabled(true);
            //et_inertmattertper.setEnabled(true);
            et_lightseed.setEnabled(true);
            //et_lightseedper.setEnabled(true);
            et_totalnoinsample.setEnabled(true);
            //et_tobeexnoperkg.setEnabled(true);
            et_odv_totalnoinsample.setEnabled(true);
            //et_odv_tobeexnoperkg.setEnabled(true);
            et_discoloredseed.setEnabled(true);
            //et_discoloredper.setEnabled(true);
            et_pinholenumber.setEnabled(true);
            et_pinhole_remarks.setEnabled(true);
            //et_pinhole_tobeexnoperkg.setEnabled(true);

            et_totalnoinsample_germp.setEnabled(true);
            et_tobeexnoperkg_germp.setEnabled(true);
            et_germp_wt.setEnabled(true);
            et_germp_wt_per.setEnabled(true);
            et_germp_remarks.setEnabled(true);
            et_ocs_wt.setEnabled(true);
            et_ocs_wt_per.setEnabled(true);
            et_ocs_remarks.setEnabled(true);
            et_odv_wt.setEnabled(true);
            et_odv_wt_per.setEnabled(true);
            et_odv_1010.setEnabled(true);
            et_odv_fineGrain.setEnabled(true);
            et_odv_boldGrain.setEnabled(true);
            et_odv_longGrain.setEnabled(true);
            et_odv_otherTyp.setEnabled(true);
            et_odv_total.setEnabled(true);
            et_odv_total_per.setEnabled(true);
            et_odv_remarks.setEnabled(true);
            et_pinhole_wt.setEnabled(true);
            et_pinhole_wt_per.setEnabled(true);
            et_pinhole_remarks.setEnabled(true);

            btn_submit.setVisibility(View.VISIBLE);
            tv_textmsg.setVisibility(View.GONE);
        }
    }

    private void init() {
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PPReadingFormActivity.this, PPHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        et_pureseed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samplewt_cal = et_samplewt.getText().toString().trim();
                if (!samplewt_cal.equals("0") && !samplewt_cal.equals("")){
                    String pureseed = et_pureseed.getText().toString().trim();
                    double pureseedper;
                    if (!pureseed.equals("0") && !pureseed.equals("")) {
                        double samplewt_cal1 = Double.parseDouble(samplewt_cal);
                        double pureseed1 = Double.parseDouble(pureseed);
                        pureseedper = (pureseed1*100)/samplewt_cal1;
                        et_pureseedper.setText(String.format(Locale.US, "%.2f", pureseedper));
                    }
                }else {
                    et_samplewt.setError("Enter Sample Weight");
                    et_samplewt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_inertmattert.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samplewt_cal = et_samplewt.getText().toString().trim();
                if (!samplewt_cal.equals("0") && !samplewt_cal.equals("")){
                    String inertmattert = et_inertmattert.getText().toString().trim();
                    double inertmattertper;
                    if (!inertmattert.equals("0") && !inertmattert.equals("")) {
                        double samplewt_cal1 = Double.parseDouble(samplewt_cal);
                        double inertmattert1 = Double.parseDouble(inertmattert);
                        inertmattertper = (inertmattert1*100)/samplewt_cal1;
                        et_inertmattertper.setText(String.format(Locale.US, "%.2f", inertmattertper));
                    }
                }else {
                    et_samplewt.setError("Enter Sample Weight");
                    et_samplewt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_lightseed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samplewt_cal = et_samplewt.getText().toString().trim();
                if (!samplewt_cal.equals("0") && !samplewt_cal.equals("")){
                    String lightseed = et_lightseed.getText().toString().trim();
                    double lightseedper;
                    if (!lightseed.equals("0") && !lightseed.equals("")) {
                        double samplewt_cal1 = Double.parseDouble(samplewt_cal);
                        double lightseed1 = Double.parseDouble(lightseed);
                        lightseedper = (lightseed1*100)/samplewt_cal1;
                        et_lightseedper.setText(String.format(Locale.US, "%.2f", lightseedper));
                    }
                }else {
                    et_samplewt.setError("Enter Sample Weight");
                    et_samplewt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_discoloredseed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samplewt_cal = et_samplewt.getText().toString().trim();
                if (!samplewt_cal.equals("0") && !samplewt_cal.equals("")){
                    String discoloredseed = et_discoloredseed.getText().toString().trim();
                    double discoloredseedper;
                    if (!discoloredseed.equals("0") && !discoloredseed.equals("")) {
                        double samplewt_cal1 = Double.parseDouble(samplewt_cal);
                        double discoloredseed1 = Double.parseDouble(discoloredseed);
                        discoloredseedper = (discoloredseed1*100)/samplewt_cal1;
                        et_discoloredper.setText(String.format(Locale.US, "%.2f", discoloredseedper));
                    }
                }else {
                    et_samplewt.setError("Enter Sample Weight");
                    et_samplewt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_totalnoinsample_germp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samplewt_cal = et_samplewt.getText().toString().trim();
                if (!samplewt_cal.equals("0") && !samplewt_cal.equals("")){
                    String totalnoinsample_germp = et_totalnoinsample_germp.getText().toString().trim();
                    double totalnoinsample_germp_per;
                    if (!totalnoinsample_germp.equals("0") && !totalnoinsample_germp.equals("")) {
                        double samplewt_cal1 = Double.parseDouble(samplewt_cal);
                        double totalnoinsample_germp_per1 = Double.parseDouble(totalnoinsample_germp);
                        totalnoinsample_germp_per = (totalnoinsample_germp_per1*1000)/samplewt_cal1;
                        et_tobeexnoperkg_germp.setText(String.valueOf(Math.toIntExact(Math.round(totalnoinsample_germp_per))));
                    }
                }else {
                    et_samplewt.setError("Enter Sample Weight");
                    et_samplewt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_germp_wt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samplewt_cal = et_samplewt.getText().toString().trim();
                if (!samplewt_cal.equals("0") && !samplewt_cal.equals("")){
                    String germp_wt = et_germp_wt.getText().toString().trim();
                    double germp_wt_per;
                    if (!germp_wt.equals("0") && !germp_wt.equals("")) {
                        double samplewt_cal1 = Double.parseDouble(samplewt_cal);
                        double germp_wt1 = Double.parseDouble(germp_wt);
                        germp_wt_per = (germp_wt1*100)/samplewt_cal1;
                        et_germp_wt_per.setText(String.format(Locale.US, "%.2f", germp_wt_per));
                    }
                }else {
                    et_samplewt.setError("Enter Sample Weight");
                    et_samplewt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_ocs_wt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samplewt_cal = et_samplewt.getText().toString().trim();
                if (!samplewt_cal.equals("0") && !samplewt_cal.equals("")){
                    String ocs_wt = et_ocs_wt.getText().toString().trim();
                    double ocs_wt_per;
                    if (!ocs_wt.equals("0") && !ocs_wt.equals("")) {
                        double samplewt_cal1 = Double.parseDouble(samplewt_cal);
                        double ocs_wt1 = Double.parseDouble(ocs_wt);
                        ocs_wt_per = (ocs_wt1*100)/samplewt_cal1;
                        et_ocs_wt_per.setText(String.format(Locale.US, "%.2f", ocs_wt_per));
                    }
                }else {
                    et_samplewt.setError("Enter Sample Weight");
                    et_samplewt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_odv_wt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samplewt_cal = et_samplewt.getText().toString().trim();
                if (!samplewt_cal.equals("0") && !samplewt_cal.equals("")){
                    String odv_wt = et_odv_wt.getText().toString().trim();
                    double odv_wt_per;
                    if (!odv_wt.equals("0") && !odv_wt.equals("")) {
                        double samplewt_cal1 = Double.parseDouble(samplewt_cal);
                        double odv_wt1 = Double.parseDouble(odv_wt);
                        odv_wt_per = (odv_wt1*100)/samplewt_cal1;
                        et_odv_wt_per.setText(String.format(Locale.US, "%.2f", odv_wt_per));
                    }
                }else {
                    et_samplewt.setError("Enter Sample Weight");
                    et_samplewt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_pinhole_wt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samplewt_cal = et_samplewt.getText().toString().trim();
                if (!samplewt_cal.equals("0") && !samplewt_cal.equals("")){
                    String pinhole_wt = et_pinhole_wt.getText().toString().trim();
                    double pinhole_wt_per;
                    if (!pinhole_wt.equals("0") && !pinhole_wt.equals("")) {
                        double samplewt_cal1 = Double.parseDouble(samplewt_cal);
                        double pinhole_wt1 = Double.parseDouble(pinhole_wt);
                        pinhole_wt_per = (pinhole_wt1*100)/samplewt_cal1;
                        et_pinhole_wt_per.setText(String.format(Locale.US, "%.2f", pinhole_wt_per));
                    }
                }else {
                    et_samplewt.setError("Enter Sample Weight");
                    et_samplewt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_totalnoinsample.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samplewt_cal = et_samplewt.getText().toString().trim();
                if (!samplewt_cal.equals("")){
                    String totalnoinsample = et_totalnoinsample.getText().toString().trim();
                    double tobeexnoperkg;
                    if (!totalnoinsample.equals("")) {
                        if (!totalnoinsample.equals("0")) {
                            double samplewt_cal1 = Double.parseDouble(samplewt_cal);
                            double totalnoinsample1 = Double.parseDouble(totalnoinsample);
                            tobeexnoperkg = (totalnoinsample1 * 1000) / samplewt_cal1;
                            et_tobeexnoperkg.setText(String.valueOf(Math.round(tobeexnoperkg)));
                        } else {
                            et_tobeexnoperkg.setText("0");
                        }
                    }else {
                        et_totalnoinsample.setError("Enter Total Number");
                    }
                }else {
                    et_samplewt.setError("Enter Sample Weight");
                    et_samplewt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_odv_totalnoinsample.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samplewt_cal = et_samplewt.getText().toString().trim();
                if (!samplewt_cal.equals("")){
                    String odv_totalnoinsample = et_odv_totalnoinsample.getText().toString().trim();
                    double tobeexnoperkg;
                    if (!odv_totalnoinsample.equals("")) {
                        if (!odv_totalnoinsample.equals("0")) {
                            double samplewt_cal1 = Double.parseDouble(samplewt_cal);
                            double odv_totalnoinsample1 = Double.parseDouble(odv_totalnoinsample);
                            tobeexnoperkg = (odv_totalnoinsample1 * 1000) / samplewt_cal1;
                            et_odv_tobeexnoperkg.setText(String.valueOf(Math.round(tobeexnoperkg)));
                        } else {
                            et_odv_tobeexnoperkg.setText("0");
                        }
                    }else {
                        et_odv_totalnoinsample.setError("Enter Total Number");
                    }
                }else {
                    et_samplewt.setError("Enter Sample Weight");
                    et_samplewt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_odv_1010.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samplewt_cal = et_samplewt.getText().toString().trim();
                if (!samplewt_cal.equals("")){
                    String odv_1010 = et_odv_1010.getText().toString().trim();
                    double odv_total;
                    double odv_total_per;
                    if (!odv_1010.equals("")) {
                        if (!odv_1010.equals("0")) {
                            double samplewt_cal1 = Double.parseDouble(samplewt_cal);
                            odv_total = Double.parseDouble(odv_1010);
                            if (!et_odv_fineGrain.getText().toString().isEmpty() && !et_odv_fineGrain.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_fineGrain.getText().toString());
                            }
                            if (!et_odv_boldGrain.getText().toString().isEmpty() && !et_odv_boldGrain.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_boldGrain.getText().toString());
                            }
                            if (!et_odv_longGrain.getText().toString().isEmpty() && !et_odv_longGrain.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_longGrain.getText().toString());
                            }
                            if (!et_odv_otherTyp.getText().toString().isEmpty() && !et_odv_otherTyp.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_otherTyp.getText().toString());
                            }
                            odv_total_per = (odv_total * 1000) / samplewt_cal1;
                            et_odv_total.setText(String.valueOf(odv_total));
                            et_odv_total_per.setText(String.valueOf(Math.round(odv_total_per)));
                        }
                    }else {
                        et_odv_totalnoinsample.setError("Enter Total Number");
                    }

                }else {
                    et_samplewt.setError("Enter Sample Weight");
                    et_samplewt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_odv_fineGrain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samplewt_cal = et_samplewt.getText().toString().trim();
                if (!samplewt_cal.equals("")){
                    String odv_fineGrain = et_odv_fineGrain.getText().toString().trim();
                    double odv_total;
                    double odv_total_per;
                    if (!odv_fineGrain.equals("")) {
                        if (!odv_fineGrain.equals("0")) {
                            double samplewt_cal1 = Double.parseDouble(samplewt_cal);
                            odv_total = Double.parseDouble(odv_fineGrain);
                            if (!et_odv_1010.getText().toString().isEmpty() && !et_odv_1010.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_1010.getText().toString());
                            }
                            if (!et_odv_boldGrain.getText().toString().isEmpty() && !et_odv_boldGrain.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_boldGrain.getText().toString());
                            }
                            if (!et_odv_longGrain.getText().toString().isEmpty() && !et_odv_longGrain.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_longGrain.getText().toString());
                            }
                            if (!et_odv_otherTyp.getText().toString().isEmpty() && !et_odv_otherTyp.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_otherTyp.getText().toString());
                            }
                            odv_total_per = (odv_total * 1000) / samplewt_cal1;
                            et_odv_total.setText(String.valueOf(odv_total));
                            et_odv_total_per.setText(String.valueOf(Math.round(odv_total_per)));
                        }
                    }else {
                        et_odv_totalnoinsample.setError("Enter Total Number");
                    }

                }else {
                    et_samplewt.setError("Enter Sample Weight");
                    et_samplewt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_odv_boldGrain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samplewt_cal = et_samplewt.getText().toString().trim();
                if (!samplewt_cal.equals("")){
                    String odv_boldGrain = et_odv_boldGrain.getText().toString().trim();
                    double odv_total;
                    double odv_total_per;
                    if (!odv_boldGrain.equals("")) {
                        if (!odv_boldGrain.equals("0")) {
                            double samplewt_cal1 = Double.parseDouble(samplewt_cal);
                            odv_total = Double.parseDouble(odv_boldGrain);
                            if (!et_odv_1010.getText().toString().isEmpty() && !et_odv_1010.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_1010.getText().toString());
                            }
                            if (!et_odv_fineGrain.getText().toString().isEmpty() && !et_odv_fineGrain.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_fineGrain.getText().toString());
                            }
                            if (!et_odv_longGrain.getText().toString().isEmpty() && !et_odv_longGrain.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_longGrain.getText().toString());
                            }
                            if (!et_odv_otherTyp.getText().toString().isEmpty() &&!et_odv_otherTyp.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_otherTyp.getText().toString());
                            }
                            odv_total_per = (odv_total * 1000) / samplewt_cal1;
                            et_odv_total.setText(String.valueOf(odv_total));
                            et_odv_total_per.setText(String.valueOf(Math.round(odv_total_per)));
                        }
                    }else {
                        et_odv_totalnoinsample.setError("Enter Total Number");
                    }

                }else {
                    et_samplewt.setError("Enter Sample Weight");
                    et_samplewt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_odv_longGrain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samplewt_cal = et_samplewt.getText().toString().trim();
                if (!samplewt_cal.equals("")){
                    String odv_longGrain = et_odv_longGrain.getText().toString().trim();
                    double odv_total;
                    double odv_total_per;
                    if (!odv_longGrain.equals("")) {
                        if (!odv_longGrain.equals("0")) {
                            double samplewt_cal1 = Double.parseDouble(samplewt_cal);
                            odv_total = Double.parseDouble(odv_longGrain);
                            if (!et_odv_1010.getText().toString().isEmpty() && !et_odv_1010.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_1010.getText().toString());
                            }
                            if (!et_odv_fineGrain.getText().toString().isEmpty() && !et_odv_fineGrain.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_fineGrain.getText().toString());
                            }
                            if (!et_odv_boldGrain.getText().toString().isEmpty() && !et_odv_boldGrain.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_boldGrain.getText().toString());
                            }
                            if (!et_odv_otherTyp.getText().toString().isEmpty() && !et_odv_otherTyp.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_otherTyp.getText().toString());
                            }
                            odv_total_per = (odv_total * 1000) / samplewt_cal1;
                            et_odv_total.setText(String.valueOf(odv_total));
                            et_odv_total_per.setText(String.valueOf(Math.round(odv_total_per)));
                        }
                    }else {
                        et_odv_totalnoinsample.setError("Enter Total Number");
                    }

                }else {
                    et_samplewt.setError("Enter Sample Weight");
                    et_samplewt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_odv_otherTyp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samplewt_cal = et_samplewt.getText().toString().trim();
                if (!samplewt_cal.equals("")){
                    String odv_otherTyp = et_odv_otherTyp.getText().toString().trim();
                    double odv_total;
                    double odv_total_per;
                    if (!odv_otherTyp.equals("")) {
                        if (!odv_otherTyp.equals("0")) {
                            double samplewt_cal1 = Double.parseDouble(samplewt_cal);
                            odv_total = Double.parseDouble(odv_otherTyp);
                            if (!et_odv_1010.getText().toString().isEmpty() && !et_odv_1010.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_1010.getText().toString());
                            }
                            if (!et_odv_fineGrain.getText().toString().isEmpty() && !et_odv_fineGrain.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_fineGrain.getText().toString());
                            }
                            if (!et_odv_boldGrain.getText().toString().isEmpty() && !et_odv_boldGrain.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_boldGrain.getText().toString());
                            }
                            if (!et_odv_longGrain.getText().toString().isEmpty() && !et_odv_longGrain.getText().toString().equals("0")){
                                odv_total=odv_total+Double.parseDouble(et_odv_longGrain.getText().toString());
                            }
                            odv_total_per = (odv_total * 1000) / samplewt_cal1;
                            et_odv_total.setText(String.valueOf(odv_total));
                            et_odv_total_per.setText(String.valueOf(Math.round(odv_total_per)));
                        }
                    }else {
                        et_odv_totalnoinsample.setError("Enter Total Number");
                    }

                }else {
                    et_samplewt.setError("Enter Sample Weight");
                    et_samplewt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_pinholenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samplewt_cal = et_samplewt.getText().toString().trim();
                if (!samplewt_cal.equals("")){
                    String pinholenumber = et_pinholenumber.getText().toString().trim();
                    double tobeexnoperkg;
                    if (!pinholenumber.equals("")) {
                        if (!pinholenumber.equals("0")) {
                            double samplewt_cal1 = Double.parseDouble(samplewt_cal);
                            double pinholenumber1 = Double.parseDouble(pinholenumber);
                            tobeexnoperkg = (pinholenumber1 * 1000) / samplewt_cal1;
                            et_pinhole_tobeexnoperkg.setText(String.valueOf(Math.round(tobeexnoperkg)));
                        } else {
                            et_pinhole_tobeexnoperkg.setText("0");
                        }
                    }else {
                        et_pinholenumber.setError("Enter Total Number");
                    }
                }else {
                    et_samplewt.setError("Enter Sample Weight");
                    et_samplewt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sampleno = tv_sampleno.getText().toString().trim();
                String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
                String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
                String samplewt = et_samplewt.getText().toString().trim();
                String pureseed = et_pureseed.getText().toString().trim();
                String pureseedper = et_pureseedper.getText().toString().trim();
                String inertmattert = et_inertmattert.getText().toString().trim();
                String inertmattertper = et_inertmattertper.getText().toString().trim();
                String lightseed = et_lightseed.getText().toString().trim();
                String lightseedper = et_lightseedper.getText().toString().trim();
                String totalnoinsample = et_totalnoinsample.getText().toString().trim();
                String tobeexnoperkg = et_tobeexnoperkg.getText().toString().trim();
                String odv_totalnoinsample = et_odv_totalnoinsample.getText().toString().trim();
                String odv_tobeexnoperkg = et_odv_tobeexnoperkg.getText().toString().trim();
                String discoloredseed = et_discoloredseed.getText().toString().trim();
                String discoloredper = et_discoloredper.getText().toString().trim();
                String pinholenumber = et_pinholenumber.getText().toString().trim();
                String pinhole_tobeexnoperkg = et_pinhole_tobeexnoperkg.getText().toString().trim();
                String pinhole_remarks = et_pinhole_remarks.getText().toString().trim();
                String pinhole_wt = et_pinhole_wt.getText().toString().trim();
                String pinhole_wt_per = et_pinhole_wt_per.getText().toString().trim();
                String germp_remarks = et_germp_remarks.getText().toString().trim();
                String germp_wt = et_germp_wt.getText().toString().trim();
                String germp_wt_per = et_germp_wt_per.getText().toString().trim();
                String totalnoinsample_germp = et_totalnoinsample_germp.getText().toString().trim();
                String tobeexnoperkg_germp = et_tobeexnoperkg_germp.getText().toString().trim();
                String ocs_remarks = et_ocs_remarks.getText().toString().trim();
                String ocs_wt = et_ocs_wt.getText().toString().trim();
                String ocs_wt_per = et_ocs_wt_per.getText().toString().trim();
                String odv_wt = et_odv_wt.getText().toString().trim();
                String odv_wt_per = et_odv_wt_per.getText().toString().trim();
                String odv_remarks = et_odv_remarks.getText().toString().trim();
                String odv_1010 = et_odv_1010.getText().toString().trim();
                String odv_fineGrain = et_odv_fineGrain.getText().toString().trim();
                String odv_boldGrain = et_odv_boldGrain.getText().toString().trim();
                String odv_longGrain = et_odv_longGrain.getText().toString().trim();
                String odv_otherTyp = et_odv_otherTyp.getText().toString().trim();
                String odv_total = et_odv_total.getText().toString().trim();
                String odv_total_per = et_odv_total_per.getText().toString().trim();

                if (!samplewt.equals("0") && !samplewt.equals("")){
                    if (!pureseed.equals("0") && !pureseed.equals("")){
                        confirmAlert(sampleno,userid,scode,samplewt,pureseed,pureseedper,inertmattert,inertmattertper,lightseed,lightseedper,
                                totalnoinsample,tobeexnoperkg,odv_totalnoinsample,odv_tobeexnoperkg,discoloredseed,discoloredper,pinholenumber,
                                pinhole_tobeexnoperkg,pinhole_remarks, pinhole_wt, pinhole_wt_per, germp_remarks, germp_wt, germp_wt_per,
                                totalnoinsample_germp, tobeexnoperkg_germp, ocs_remarks, ocs_wt, ocs_wt_per, odv_wt, odv_wt_per, odv_remarks,
                                odv_1010, odv_fineGrain, odv_boldGrain, odv_longGrain, odv_otherTyp, odv_total, odv_total_per);
                    }else {
                        Toast.makeText(getApplicationContext(), "Ente Pure Seed", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Enter Sample weight", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = {"Click here to open camera", "Choose from gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PPReadingFormActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(PPReadingFormActivity.this);

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
        iv_photo.setImageURI(image_uri);
        //Log.e(TAG, String.valueOf(image_uri));
        path_billcopy = getPath(image_uri);
        //mArrayUri.add(image_uri);
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

        iv_photo.setImageBitmap(bm);
        assert bm != null;
        String absolutePath = saveImage(bm);
        list.add(prepareFilePart("image", Uri.parse(absolutePath)));
        /*assert bm != null;
        path_billcopy = getPath(image_uri);
        Log.e(TAG, String.valueOf(image_uri));
        list.add(prepareFilePart("ticket_raise_files", image_uri));*/
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
            MediaScannerConnection.scanFile(PPReadingFormActivity.this, new String[]{BILL_COPY.getPath()}, new String[]{"image/jpeg"}, null);
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
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    @SuppressLint("SetTextI18n")
    private void confirmAlert(final String sampleno, final String userid, final String scode, final String samplewt, final String pureseed, final String pureseedper, final String inertmattert, final String inertmattertper, final String lightseed, final String lightseedper, final String totalnoinsample, final String tobeexnoperkg, final String odv_totalnoinsample, final String odv_tobeexnoperkg, final String discoloredseed, final String discoloredper, final String pinholenumber, final String pinhole_tobeexnoperkg, final String pinhole_remarks, String pinhole_wt, String pinhole_wt_per, String germp_remarks, String germp_wt, String germp_wt_per, String totalnoinsample_germp, String tobeexnoperkg_germp, String ocs_remarks, String ocs_wt, String ocs_wt_per, String odv_wt, String odv_wt_per, String odv_remarks, String odv_1010, String odv_fineGrain, String odv_boldGrain, String odv_longGrain, String odv_otherTyp, String odv_total, String odv_total_per) {
        final Dialog dialog = new Dialog(PPReadingFormActivity.this);
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
                finalSubmit(sampleno,userid,scode,samplewt,pureseed,pureseedper,inertmattert,inertmattertper,lightseed,lightseedper,
                        totalnoinsample,tobeexnoperkg,odv_totalnoinsample,odv_tobeexnoperkg,discoloredseed,discoloredper,pinholenumber,
                        pinhole_tobeexnoperkg,pinhole_remarks, pinhole_wt, pinhole_wt_per, germp_remarks, germp_wt, germp_wt_per,
                        totalnoinsample_germp, tobeexnoperkg_germp, ocs_remarks, ocs_wt, ocs_wt_per, odv_wt, odv_wt_per, odv_remarks,
                        odv_1010, odv_fineGrain, odv_boldGrain, odv_longGrain, odv_otherTyp, odv_total, odv_total_per);
            }
        });
    }

    private void finalSubmit(String sampleno, String userid, String scode, String samplewt, String pureseed, String pureseedper, String inertmattert,
                             String inertmattertper, String lightseed, String lightseedper, String totalnoinsample, String tobeexnoperkg, String odv_totalnoinsample,
                             String odv_tobeexnoperkg, String discoloredseed, String discoloredper, String pinholenumber, String pinhole_tobeexnoperkg,
                             String pinhole_remarks, String pinhole_wt, String pinhole_wt_per, String germp_remarks, String germp_wt, String germp_wt_per,
                             String totalnoinsample_germp, String tobeexnoperkg_germp, String ocs_remarks, String ocs_wt, String ocs_wt_per, String odv_wt,
                             String odv_wt_per, String odv_remarks, String odv_1010, String odv_fineGrain, String odv_boldGrain, String odv_longGrain,
                             String odv_otherTyp, String odv_total, String odv_total_per) {
        RequestBody userid1 = RequestBody.create(MediaType.parse("text/plain"), userid);
        RequestBody scode1 = RequestBody.create(MediaType.parse("text/plain"), scode);
        RequestBody sampleno1 = RequestBody.create(MediaType.parse("text/plain"), sampleno);
        RequestBody samplewt1 = RequestBody.create(MediaType.parse("text/plain"), samplewt);
        RequestBody pureseed1 = RequestBody.create(MediaType.parse("text/plain"), pureseed);
        RequestBody pureseedper1 = RequestBody.create(MediaType.parse("text/plain"), pureseedper);
        RequestBody inertmattert1 = RequestBody.create(MediaType.parse("text/plain"), inertmattert);
        RequestBody inertmattertper1 = RequestBody.create(MediaType.parse("text/plain"), inertmattertper);
        RequestBody lightseed1 = RequestBody.create(MediaType.parse("text/plain"), lightseed);
        RequestBody lightseedper1 = RequestBody.create(MediaType.parse("text/plain"), lightseedper);
        RequestBody totalnoinsample1 = RequestBody.create(MediaType.parse("text/plain"), totalnoinsample);
        RequestBody tobeexnoperkg1 = RequestBody.create(MediaType.parse("text/plain"), tobeexnoperkg);
        RequestBody odv_totalnoinsample1 = RequestBody.create(MediaType.parse("text/plain"), odv_totalnoinsample);
        RequestBody odv_tobeexnoperkg1 = RequestBody.create(MediaType.parse("text/plain"), odv_tobeexnoperkg);
        RequestBody discoloredseed1 = RequestBody.create(MediaType.parse("text/plain"), discoloredseed);
        RequestBody discoloredper1 = RequestBody.create(MediaType.parse("text/plain"), discoloredper);
        RequestBody pinholenumber1 = RequestBody.create(MediaType.parse("text/plain"), pinholenumber);
        RequestBody pinhole_tobeexnoperkg1 = RequestBody.create(MediaType.parse("text/plain"), pinhole_tobeexnoperkg);
        RequestBody pinhole_remarks1 = RequestBody.create(MediaType.parse("text/plain"), pinhole_remarks);
        RequestBody pinhole_wt1 = RequestBody.create(MediaType.parse("text/plain"), pinhole_wt);
        RequestBody pinhole_wt_per1 = RequestBody.create(MediaType.parse("text/plain"), pinhole_wt_per);
        RequestBody germp_remarks1 = RequestBody.create(MediaType.parse("text/plain"), germp_remarks);
        RequestBody germp_wt1 = RequestBody.create(MediaType.parse("text/plain"), germp_wt);
        RequestBody germp_wt_per1 = RequestBody.create(MediaType.parse("text/plain"), germp_wt_per);
        RequestBody totalnoinsample_germp1 = RequestBody.create(MediaType.parse("text/plain"), totalnoinsample_germp);
        RequestBody tobeexnoperkg_germp1 = RequestBody.create(MediaType.parse("text/plain"), tobeexnoperkg_germp);
        RequestBody ocs_remarks1 = RequestBody.create(MediaType.parse("text/plain"), ocs_remarks);
        RequestBody ocs_wt1 = RequestBody.create(MediaType.parse("text/plain"), ocs_wt);
        RequestBody ocs_wt_per1 = RequestBody.create(MediaType.parse("text/plain"), ocs_wt_per);
        RequestBody odv_wt1 = RequestBody.create(MediaType.parse("text/plain"), odv_wt);
        RequestBody odv_wt_per1 = RequestBody.create(MediaType.parse("text/plain"), odv_wt_per);
        RequestBody odv_remarks1 = RequestBody.create(MediaType.parse("text/plain"), odv_remarks);
        RequestBody odv_10101 = RequestBody.create(MediaType.parse("text/plain"), odv_1010);
        RequestBody odv_fineGrain1 = RequestBody.create(MediaType.parse("text/plain"), odv_fineGrain);
        RequestBody odv_boldGrain1 = RequestBody.create(MediaType.parse("text/plain"), odv_boldGrain);
        RequestBody odv_longGrain1 = RequestBody.create(MediaType.parse("text/plain"), odv_longGrain);
        RequestBody odv_otherTyp1 = RequestBody.create(MediaType.parse("text/plain"), odv_otherTyp);
        RequestBody odv_total1 = RequestBody.create(MediaType.parse("text/plain"), odv_total);
        RequestBody odv_total_per1 = RequestBody.create(MediaType.parse("text/plain"), odv_total_per);
        Log.e("Params", userid+"=="+scode+"=="+sampleno+"=="+samplewt+"=="+pureseed+"=="+pureseedper+"=="+inertmattert+"=="+inertmattertper+"=="+lightseed+"=="+lightseedper+"=="+totalnoinsample+"=="+tobeexnoperkg+"=="+odv_totalnoinsample+"=="+odv_tobeexnoperkg+"=="+discoloredseed+"=="+discoloredper+"=="+pinholenumber+"=="+pinhole_tobeexnoperkg+"=="+pinhole_remarks+"=="+pinhole_wt+"=="+pinhole_wt_per+"=="+germp_remarks+"=="+germp_wt+"=="+germp_wt_per+"=="+totalnoinsample_germp+"=="+tobeexnoperkg_germp+"=="+ocs_remarks+"=="+ocs_wt+"=="+ocs_wt_per+"=="+odv_wt+"=="+odv_wt_per+"=="+odv_remarks+"=="+odv_1010+"=="+odv_fineGrain+"=="+odv_boldGrain+"=="+odv_longGrain+"=="+odv_otherTyp+"=="+odv_total+"=="+odv_total_per);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Call<SubmitSuccessResponse> call =apiInterface.submitPPData(userid1, scode1, sampleno1, samplewt1, pureseed1, pureseedper1, inertmattert1, inertmattertper1,
                lightseed1, lightseedper1, totalnoinsample1, tobeexnoperkg1, odv_totalnoinsample1, odv_tobeexnoperkg1, discoloredseed1, discoloredper1,
                pinholenumber1, pinhole_tobeexnoperkg1, pinhole_remarks1, pinhole_wt1, pinhole_wt_per1, germp_remarks1, germp_wt1, germp_wt_per1,
                totalnoinsample_germp1, tobeexnoperkg_germp1, ocs_remarks1, ocs_wt1, ocs_wt_per1, odv_wt1, odv_wt_per1, odv_remarks1,
                odv_10101, odv_fineGrain1, odv_boldGrain1, odv_longGrain1, odv_otherTyp1, odv_total1, odv_total_per1, list);
        call.enqueue(new Callback<SubmitSuccessResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<SubmitSuccessResponse> call, @NonNull retrofit2.Response<SubmitSuccessResponse> response) {
                if (response.isSuccessful()) {
                    progressDialog.cancel();
                    Log.e(TAG, "Response : " + response.body());
                    SubmitSuccessResponse submitSuccessResponse = response.body();
                    assert submitSuccessResponse != null;
                    if (submitSuccessResponse.getStatus()){
                        Toast.makeText(getApplicationContext(), submitSuccessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PPReadingFormActivity.this, PPHomeActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(), submitSuccessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.cancel();
                    if (response.errorBody() != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            String msg = jsonObj.getString("msg");
                            Toast.makeText(PPReadingFormActivity.this, msg, Toast.LENGTH_SHORT).show();
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
    }

    /*private void finalSubmit1(String sampleno, String userid, String scode, String samplewt, String pureseed, String pureseedper, String inertmattert, String inertmattertper, String lightseed, String lightseedper, String totalnoinsample, String tobeexnoperkg, String odv_totalnoinsample, String odv_tobeexnoperkg, String discoloredseed, String discoloredper, String pinholenumber, String pinhole_tobeexnoperkg, String pinhole_remarks, String pinhole_wt, String pinhole_wt_per, String germp_remarks, String germp_wt, String germp_wt_per, String totalnoinsample_germp, String tobeexnoperkg_germp, String ocs_remarks, String ocs_wt, String ocs_wt_per, String odv_wt, String odv_wt_per, String odv_remarks, String odv_1010, String odv_fineGrain, String odv_boldGrain, String odv_longGrain, String odv_otherTyp, String odv_total, String odv_total_per) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("scode", scode);
        params.put("sampleno", sampleno);
        params.put("samplewt", samplewt);
        params.put("pureseed", pureseed);
        params.put("pureseedper", pureseedper);
        params.put("imseed", inertmattert);
        params.put("imseedper", inertmattertper);
        params.put("lightseed", lightseed);
        params.put("lightseedper", lightseedper);
        params.put("ocseedno", totalnoinsample);
        params.put("ocseedinkg", tobeexnoperkg);
        params.put("odvseedno", odv_totalnoinsample);
        params.put("odvseedinkg", odv_tobeexnoperkg);
        params.put("dcseed", discoloredseed);
        params.put("dcseedper", discoloredper);
        params.put("phseedno", pinholenumber);
        params.put("phseedinkg", pinhole_tobeexnoperkg);
        params.put("pinhole_remarks", pinhole_remarks);

        new SendVolleyCall().executeProgram(PPReadingFormActivity.this, AppConfig.PHISICALPURITYFINALSUBMIT_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitData(response);
                if (message.equalsIgnoreCase("Success")){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PPReadingFormActivity.this, PPHomeActivity.class);
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
    }*/
}