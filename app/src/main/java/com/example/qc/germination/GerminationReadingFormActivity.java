package com.example.qc.germination;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.qc.R;
import com.example.qc.parser.JsonParser;
import com.example.qc.parser.SubmitSuccessResponse;
import com.example.qc.parser.germpsampleinfo.Samplegemparray;
import com.example.qc.pp.Utility;
import com.example.qc.retrofit.ApiInterface;
import com.example.qc.retrofit.RetrofitClient;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.Utils;
import com.example.qc.volley.SendVolleyCall;
import com.example.qc.volley.volleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

public class GerminationReadingFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "GerminationReadingFormActivity";
    private LinearLayout ll_oneobs;
    private LinearLayout ll_both;
    private LinearLayout ll_medium;
    private LinearLayout ll_sgtdoe;
    private LinearLayout ll_sgtddoe;
    private LinearLayout ll_fgtdoe;
    private LinearLayout ll_block34;
    private LinearLayout ll_block5678;
    private LinearLayout ll_normalrep2;
    private LinearLayout ll_normalrep34;
    private LinearLayout ll_normalrep56;
    private LinearLayout ll_normalrep78;
    private LinearLayout ll_observation;
    private LinearLayout ll_noofrepsgt;
    private LinearLayout ll_noofrepfgt;
    private LinearLayout ll_noofrepsgtd;

    private LinearLayout ll_replication2;
    private LinearLayout ll_replication2_1;
    private LinearLayout ll_replication2_2;

    private LinearLayout ll_hardfug_rep1;
    private LinearLayout ll_hardfug_rep2;
    private LinearLayout ll_hardfug_rep3;
    private LinearLayout ll_hardfug_rep4;
    private LinearLayout ll_hardfug_rep5;
    private LinearLayout ll_hardfug_rep6;
    private LinearLayout ll_hardfug_rep7;
    private LinearLayout ll_hardfug_rep8;
    private LinearLayout ll_meadofhardseeds;

    private Button btn_submit;
    private EditText et_noofrepsgt;
    private EditText et_noofrepsgtd;
    private EditText et_noofrepfgt;
    private EditText et_media;
    private EditText et_doesgt;
    private EditText et_doefgt;
    private EditText et_doesgtd;

    private RadioGroup radioGrpGerm;
    private RadioGroup radioGrpObrs;
    private EditText et_totalnoofseeds;
    private String rep_countsgt;
    private String rep_countsgtd;
    private String rep_countfgt;
    private ImageButton back_nav;
    private Spinner spinner_vremark;

    private EditText et_normalseed_rep1;
    private EditText et_normalseed_rep2;
    private EditText et_normalseed_rep3;
    private EditText et_normalseed_rep4;
    private EditText et_normalseed_rep5;
    private EditText et_normalseed_rep6;
    private EditText et_normalseed_rep7;
    private EditText et_normalseed_rep8;

    private EditText et_normal_rep1;
    private EditText et_abnormal_rep1;
    private EditText et_hardfug_rep1;
    private EditText et_dead_rep1;

    private EditText et_normal_rep2;
    private EditText et_abnormal_rep2;
    private EditText et_hardfug_rep2;
    private EditText et_dead_rep2;

    private EditText et_normal_rep3;
    private EditText et_abnormal_rep3;
    private EditText et_hardfug_rep3;
    private EditText et_dead_rep3;

    private EditText et_normal_rep4;
    private EditText et_abnormal_rep4;
    private EditText et_hardfug_rep4;
    private EditText et_dead_rep4;

    private EditText et_normal_rep5;
    private EditText et_abnormal_rep5;
    private EditText et_hardfug_rep5;
    private EditText et_dead_rep5;

    private EditText et_normal_rep6;
    private EditText et_abnormal_rep6;
    private EditText et_hardfug_rep6;
    private EditText et_dead_rep6;

    private EditText et_normal_rep7;
    private EditText et_abnormal_rep7;
    private EditText et_hardfug_rep7;
    private EditText et_dead_rep7;

    private EditText et_normal_rep8;
    private EditText et_abnormal_rep8;
    private EditText et_hardfug_rep8;
    private EditText et_dead_rep8;

    private EditText et_normalseedper;
    private EditText et_abnormalseedper;
    private EditText et_hardseedper;
    private EditText et_deadseedper;

    private CheckBox checkBox_rssc;
    private CheckBox checkBox_prm;
    private CheckBox checkBox_vss;

    private TextView tv_sampleno;
    private Samplegemparray sampleGerminationInfo;
    private LinearLayout ll_techremark;
    private EditText et_vremark;
    private float finalNoofseedonerep = 0;

    private CheckBox checkBox_retest;
    private LinearLayout ll_retest_reason;
    private LinearLayout ll_withsample;
    private RadioGroup radiosample;
    private String retest = "No";
    private EditText et_retest_reason;
    private EditText et_remark;
    private String remarks;

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
    private LinearLayout ll_germpPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_germination_reading_form);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setTheme();
        init();
    }

    @SuppressLint("SetTextI18n")
    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        back_nav = findViewById(R.id.back_nav);

        checkBox_retest = findViewById(R.id.checkBox_retest);
        ll_retest_reason = findViewById(R.id.ll_retest_reason);
        ll_withsample = findViewById(R.id.ll_withsample);
        radiosample = findViewById(R.id.radiosample);
        et_retest_reason = findViewById(R.id.et_retest_reason);
        et_remark = findViewById(R.id.et_remark);

        //Photo Upload
        iv_photo = findViewById(R.id.iv_photo);
        button_photo = findViewById(R.id.button_photo);
        ll_germpPhoto = findViewById(R.id.ll_germpPhoto);

        et_noofrepsgt = findViewById(R.id.et_noofrepsgt);
        et_noofrepsgtd = findViewById(R.id.et_noofrepsgtd);
        et_noofrepfgt = findViewById(R.id.et_noofrepfgt);
        et_media = findViewById(R.id.et_media);
        et_doesgt = findViewById(R.id.et_doesgt);
        et_doesgtd = findViewById(R.id.et_doesgtd);
        et_doefgt = findViewById(R.id.et_doefgt);

        radioGrpGerm = findViewById(R.id.radioGrpGerm);
        RadioButton radiosgt = findViewById(R.id.radiosgt);
        RadioButton radiofgt = findViewById(R.id.radiofgt);
        RadioButton radiodbt = findViewById(R.id.radiodbt);
        radioGrpObrs = findViewById(R.id.radioGrpObrs);
        RadioButton radioOneObs = findViewById(R.id.radioOneObs);
        RadioButton radioFinalObs = findViewById(R.id.radioFinalObs);

        //First Count
        et_normalseed_rep1 = findViewById(R.id.et_normalseed_rep1);
        et_normalseed_rep2 = findViewById(R.id.et_normalseed_rep2);
        et_normalseed_rep3 = findViewById(R.id.et_normalseed_rep3);
        et_normalseed_rep4 = findViewById(R.id.et_normalseed_rep4);
        et_normalseed_rep5 = findViewById(R.id.et_normalseed_rep5);
        et_normalseed_rep6 = findViewById(R.id.et_normalseed_rep6);
        et_normalseed_rep7 = findViewById(R.id.et_normalseed_rep7);
        et_normalseed_rep8 = findViewById(R.id.et_normalseed_rep8);

        //Rep1
        et_normal_rep1 = findViewById(R.id.et_normal_rep1);
        et_abnormal_rep1 = findViewById(R.id.et_abnormal_rep1);
        et_hardfug_rep1 = findViewById(R.id.et_hardfug_rep1);
        et_dead_rep1 = findViewById(R.id.et_dead_rep1);

        //Rep2
        et_normal_rep2 = findViewById(R.id.et_normal_rep2);
        et_abnormal_rep2 = findViewById(R.id.et_abnormal_rep2);
        et_hardfug_rep2 = findViewById(R.id.et_hardfug_rep2);
        et_dead_rep2 = findViewById(R.id.et_dead_rep2);

        //Rep3
        et_normal_rep3 = findViewById(R.id.et_normal_rep3);
        et_abnormal_rep3 = findViewById(R.id.et_abnormal_rep3);
        et_hardfug_rep3 = findViewById(R.id.et_hardfug_rep3);
        et_dead_rep3 = findViewById(R.id.et_dead_rep3);

        //Rep4
        et_normal_rep4 = findViewById(R.id.et_normal_rep4);
        et_abnormal_rep4 = findViewById(R.id.et_abnormal_rep4);
        et_hardfug_rep4 = findViewById(R.id.et_hardfug_rep4);
        et_dead_rep4 = findViewById(R.id.et_dead_rep4);

        //Rep5
        et_normal_rep5 = findViewById(R.id.et_normal_rep5);
        et_abnormal_rep5 = findViewById(R.id.et_abnormal_rep5);
        et_hardfug_rep5 = findViewById(R.id.et_hardfug_rep5);
        et_dead_rep5 = findViewById(R.id.et_dead_rep5);

        //Rep6
        et_normal_rep6 = findViewById(R.id.et_normal_rep6);
        et_abnormal_rep6 = findViewById(R.id.et_abnormal_rep6);
        et_hardfug_rep6 = findViewById(R.id.et_hardfug_rep6);
        et_dead_rep6 = findViewById(R.id.et_dead_rep6);

        //Rep7
        et_normal_rep7 = findViewById(R.id.et_normal_rep7);
        et_abnormal_rep7 = findViewById(R.id.et_abnormal_rep7);
        et_hardfug_rep7 = findViewById(R.id.et_hardfug_rep7);
        et_dead_rep7 = findViewById(R.id.et_dead_rep7);

        //Rep8
        et_normal_rep8 = findViewById(R.id.et_normal_rep8);
        et_abnormal_rep8 = findViewById(R.id.et_abnormal_rep8);
        et_hardfug_rep8 = findViewById(R.id.et_hardfug_rep8);
        et_dead_rep8 = findViewById(R.id.et_dead_rep8);

        //Mean
        et_normalseedper = findViewById(R.id.et_normalseedper);
        et_abnormalseedper = findViewById(R.id.et_abnormalseedper);
        et_hardseedper = findViewById(R.id.et_hardseedper);
        et_deadseedper = findViewById(R.id.et_deadseedper);

        et_totalnoofseeds = findViewById(R.id.et_totalnoofseeds);
        ll_oneobs = findViewById(R.id.ll_oneobs);
        ll_both = findViewById(R.id.ll_both);
        ll_medium = findViewById(R.id.ll_medium);
        ll_sgtdoe = findViewById(R.id.ll_sgtdoe);
        ll_sgtddoe = findViewById(R.id.ll_sgtddoe);
        ll_fgtdoe = findViewById(R.id.ll_fgtdoe);
        ll_noofrepsgt = findViewById(R.id.ll_noofrepsgt);
        ll_noofrepsgtd = findViewById(R.id.ll_noofrepsgtd);
        ll_noofrepfgt = findViewById(R.id.ll_noofrepfgt);

        ll_hardfug_rep1 = findViewById(R.id.ll_hardfug_rep1);
        ll_hardfug_rep2 = findViewById(R.id.ll_hardfug_rep2);
        ll_hardfug_rep3 = findViewById(R.id.ll_hardfug_rep3);
        ll_hardfug_rep4 = findViewById(R.id.ll_hardfug_rep4);
        ll_hardfug_rep5 = findViewById(R.id.ll_hardfug_rep5);
        ll_hardfug_rep6 = findViewById(R.id.ll_hardfug_rep6);
        ll_hardfug_rep7 = findViewById(R.id.ll_hardfug_rep7);
        ll_hardfug_rep8 = findViewById(R.id.ll_hardfug_rep8);
        ll_meadofhardseeds = findViewById(R.id.ll_meadofhardseeds);

        ll_block34 = findViewById(R.id.ll_block34);
        ll_block5678 = findViewById(R.id.ll_block5678);
        ll_normalrep2 = findViewById(R.id.ll_normalrep2);
        ll_normalrep34 = findViewById(R.id.ll_normalrep34);
        ll_normalrep56 = findViewById(R.id.ll_normalrep56);
        ll_normalrep78 = findViewById(R.id.ll_normalrep78);
        ll_observation = findViewById(R.id.ll_observation);

        ll_replication2 = findViewById(R.id.ll_replication2);
        ll_replication2_1 = findViewById(R.id.ll_replication2_1);
        ll_replication2_2 = findViewById(R.id.ll_replication2_2);
        ll_techremark = findViewById(R.id.ll_techremark);

        btn_submit = findViewById(R.id.btn_submit);
        radioGrpGerm = findViewById(R.id.radioGrpGerm);
        radiofgt = findViewById(R.id.radiofgt);
        radiosgt = findViewById(R.id.radiosgt);

        spinner_vremark = findViewById(R.id.spinner_vremark);
        checkBox_rssc = findViewById(R.id.checkBox_rssc);
        checkBox_prm = findViewById(R.id.checkBox_prm);
        checkBox_vss = findViewById(R.id.checkBox_vss);

        String currentdate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        et_doesgt.setText(currentdate);
        et_doesgtd.setText(currentdate);
        et_doefgt.setText(currentdate);

        TextView tv_crop = findViewById(R.id.tv_crop);
        TextView tv_variety = findViewById(R.id.tv_variety);
        TextView tv_lono = findViewById(R.id.tv_lono);
        TextView tv_qtykg = findViewById(R.id.tv_qtykg);
        TextView tv_stage = findViewById(R.id.tv_stage);
        tv_sampleno = findViewById(R.id.tv_sampleno);
        TextView tv_noofseedsinonerep = findViewById(R.id.tv_noofseedsinonerep);
        TextView tv_sizeofseeds = findViewById(R.id.tv_sizeofseeds);
        TextView tv_srfNo = findViewById(R.id.tv_srfNo);

        et_vremark = findViewById(R.id.et_vremark);

        sampleGerminationInfo = (Samplegemparray) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_SAMPLEGERMINATIONINFO_OBJ,Samplegemparray.class);
        //SampleInfoData = (Samplegemparray) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_SAMPLEGERMINATIONINFO_OBJ,Samplegemparray.class);

        String gmm = sampleGerminationInfo.getQcgTesttype();
        String sgtnoofrep = String.valueOf(sampleGerminationInfo.getQcgSgtnoofrep());
        String fgtnoofrep = String.valueOf(sampleGerminationInfo.getQcgVignoofrep());
        String dbtnoofrep = String.valueOf(sampleGerminationInfo.getQcgSgtdnoofrep());
        String mediatype = sampleGerminationInfo.getQcgVigtesttype();

        et_noofrepsgt.setText(sgtnoofrep);
        et_noofrepsgtd.setText(dbtnoofrep);
        et_noofrepfgt.setText(fgtnoofrep);
        et_media.setText(mediatype);

        rep_countsgt = et_noofrepsgt.getText().toString().trim();
        rep_countsgtd = et_noofrepsgtd.getText().toString().trim();
        rep_countfgt = et_noofrepfgt.getText().toString().trim();

        tv_crop.setText(sampleGerminationInfo.getCrop());
        tv_variety.setText(sampleGerminationInfo.getVariety());
        tv_lono.setText(sampleGerminationInfo.getLotno());
        tv_qtykg.setText(String.valueOf(sampleGerminationInfo.getQty()));
        tv_stage.setText(sampleGerminationInfo.getTrstage());
        tv_sampleno.setText(sampleGerminationInfo.getSampleno());
        tv_noofseedsinonerep.setText(String.valueOf(sampleGerminationInfo.getNoofseedinonerep())+"/"+String.valueOf(sampleGerminationInfo.getNoofseedinonerepfgt()));
        tv_sizeofseeds.setText(sampleGerminationInfo.getSeedsize());
        tv_srfNo.setText(sampleGerminationInfo.getSrfno());
        if (gmm.equalsIgnoreCase("SGT")){
            int noofseedsinonerep = sampleGerminationInfo.getNoofseedinonerep();
            radiosgt.setVisibility(View.VISIBLE);
            radiodbt.setVisibility(View.GONE);
            radiofgt.setVisibility(View.GONE);
            ll_sgtdoe.setVisibility(View.VISIBLE);
            ll_sgtddoe.setVisibility(View.GONE);
            ll_fgtdoe.setVisibility(View.GONE);
            ll_hardfug_rep1.setVisibility(View.VISIBLE);
            ll_hardfug_rep2.setVisibility(View.VISIBLE);
            ll_hardfug_rep3.setVisibility(View.VISIBLE);
            ll_hardfug_rep4.setVisibility(View.VISIBLE);
            ll_hardfug_rep5.setVisibility(View.VISIBLE);
            ll_hardfug_rep6.setVisibility(View.VISIBLE);
            ll_hardfug_rep7.setVisibility(View.VISIBLE);
            ll_hardfug_rep8.setVisibility(View.VISIBLE);
            ll_meadofhardseeds.setVisibility(View.VISIBLE);
            ll_noofrepsgt.setVisibility(View.VISIBLE);
            ll_noofrepsgtd.setVisibility(View.GONE);
            ll_noofrepfgt.setVisibility(View.GONE);

            if (sampleGerminationInfo.getQcgSgtflg()==1){
                et_normalseed_rep1.setText(sampleGerminationInfo.getQcgSgtoobnormal1());
                et_normalseed_rep2.setText(sampleGerminationInfo.getQcgSgtoobnormal2());
                et_normalseed_rep3.setText(sampleGerminationInfo.getQcgSgtoobnormal3());
                et_normalseed_rep4.setText(sampleGerminationInfo.getQcgSgtoobnormal4());
                et_normalseed_rep5.setText(sampleGerminationInfo.getQcgSgtoobnormal5());
                et_normalseed_rep6.setText(sampleGerminationInfo.getQcgSgtoobnormal6());
                et_normalseed_rep7.setText(sampleGerminationInfo.getQcgSgtoobnormal7());
                et_normalseed_rep8.setText(sampleGerminationInfo.getQcgSgtoobnormal8());

                et_normal_rep1.setText(sampleGerminationInfo.getQcgSgtnormal1());
                et_abnormal_rep1.setText(sampleGerminationInfo.getQcgSgtabnormal1());
                et_hardfug_rep1.setText(sampleGerminationInfo.getQcgSgthardfug1());
                et_dead_rep1.setText(sampleGerminationInfo.getQcgSgthardfug1());

                et_normal_rep2.setText(sampleGerminationInfo.getQcgSgtnormal2());
                et_abnormal_rep2.setText(sampleGerminationInfo.getQcgSgtabnormal2());
                et_hardfug_rep2.setText(sampleGerminationInfo.getQcgSgthardfug2());
                et_dead_rep2.setText(sampleGerminationInfo.getQcgSgtdead2());

                et_normal_rep3.setText(sampleGerminationInfo.getQcgSgtnormal3());
                et_abnormal_rep3.setText(sampleGerminationInfo.getQcgSgtabnormal3());
                et_hardfug_rep3.setText(sampleGerminationInfo.getQcgSgthardfug3());
                et_dead_rep3.setText(sampleGerminationInfo.getQcgSgtdead3());

                et_normal_rep4.setText(sampleGerminationInfo.getQcgSgtnormal4());
                et_abnormal_rep4.setText(sampleGerminationInfo.getQcgSgtabnormal4());
                et_hardfug_rep4.setText(sampleGerminationInfo.getQcgSgthardfug4());
                et_dead_rep4.setText(sampleGerminationInfo.getQcgSgtdead4());

                et_normal_rep5.setText(sampleGerminationInfo.getQcgSgtnormal5());
                et_abnormal_rep5.setText(sampleGerminationInfo.getQcgSgtabnormal5());
                et_hardfug_rep5.setText(sampleGerminationInfo.getQcgSgthardfug5());
                et_dead_rep5.setText(sampleGerminationInfo.getQcgSgtdead5());

                et_normal_rep6.setText(sampleGerminationInfo.getQcgSgtnormal6());
                et_abnormal_rep6.setText(sampleGerminationInfo.getQcgSgtabnormal6());
                et_hardfug_rep6.setText(sampleGerminationInfo.getQcgSgthardfug6());
                et_dead_rep6.setText(sampleGerminationInfo.getQcgSgtdead6());

                et_normal_rep7.setText(sampleGerminationInfo.getQcgSgtnormal7());
                et_abnormal_rep7.setText(sampleGerminationInfo.getQcgSgtabnormal7());
                et_hardfug_rep7.setText(sampleGerminationInfo.getQcgSgthardfug7());
                et_dead_rep7.setText(sampleGerminationInfo.getQcgSgtdead7());

                et_normal_rep8.setText(sampleGerminationInfo.getQcgSgtnormal8());
                et_abnormal_rep8.setText(sampleGerminationInfo.getQcgSgtabnormal8());
                et_hardfug_rep8.setText(sampleGerminationInfo.getQcgSgthardfug8());
                et_dead_rep8.setText(sampleGerminationInfo.getQcgSgtdead8());

                et_normalseedper.setText(sampleGerminationInfo.getQcgSgtnormalavg());
                et_abnormalseedper.setText(sampleGerminationInfo.getQcgSgtabnormalavg());
                et_hardseedper.setText(sampleGerminationInfo.getQcgSgthardfugavg());
                et_deadseedper.setText(sampleGerminationInfo.getQcgSgtdeadavg());

                checkBox_rssc.setEnabled(false);
                checkBox_prm.setEnabled(false);
                checkBox_vss.setEnabled(false);

                if (sampleGerminationInfo.getQcgOprremark().contains("PRM")){
                    checkBox_prm.setChecked(true);
                } else {
                    checkBox_prm.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgOprremark().contains("VSS")){
                    checkBox_vss.setChecked(true);
                }else {
                    checkBox_vss.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgOprremark().contains("RSSC")){
                    checkBox_rssc.setChecked(true);
                }else {
                    checkBox_rssc.setChecked(false);
                }

                et_normalseed_rep1.setEnabled(false);
                et_normalseed_rep2.setEnabled(false);
                et_normalseed_rep3.setEnabled(false);
                et_normalseed_rep4.setEnabled(false);
                et_normalseed_rep5.setEnabled(false);
                et_normalseed_rep6.setEnabled(false);
                et_normalseed_rep7.setEnabled(false);
                et_normalseed_rep8.setEnabled(false);
                et_normal_rep1.setEnabled(false);
                et_abnormal_rep1.setEnabled(false);
                et_hardfug_rep1.setEnabled(false);
                et_dead_rep1.setEnabled(false);
                et_normal_rep2.setEnabled(false);
                et_abnormal_rep2.setEnabled(false);
                et_hardfug_rep2.setEnabled(false);
                et_dead_rep2.setEnabled(false);
                et_normal_rep3.setEnabled(false);
                et_abnormal_rep3.setEnabled(false);
                et_hardfug_rep3.setEnabled(false);
                et_dead_rep3.setEnabled(false);
                et_normal_rep4.setEnabled(false);
                et_abnormal_rep4.setEnabled(false);
                et_hardfug_rep4.setEnabled(false);
                et_dead_rep4.setEnabled(false);
                et_normal_rep5.setEnabled(false);
                et_abnormal_rep5.setEnabled(false);
                et_hardfug_rep5.setEnabled(false);
                et_dead_rep5.setEnabled(false);
                et_normal_rep6.setEnabled(false);
                et_abnormal_rep6.setEnabled(false);
                et_hardfug_rep6.setEnabled(false);
                et_dead_rep6.setEnabled(false);
                et_normal_rep7.setEnabled(false);
                et_abnormal_rep7.setEnabled(false);
                et_hardfug_rep7.setEnabled(false);
                et_dead_rep7.setEnabled(false);
                et_normal_rep8.setEnabled(false);
                et_abnormal_rep8.setEnabled(false);
                et_hardfug_rep8.setEnabled(false);
                et_dead_rep8.setEnabled(false);
                et_normalseedper.setEnabled(false);
                et_abnormalseedper.setEnabled(false);
                et_hardseedper.setEnabled(false);
                et_deadseedper.setEnabled(false);

                spinner_vremark.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                et_vremark.setText(sampleGerminationInfo.getQcgSgtvremark());
                et_vremark.setVisibility(View.VISIBLE);
                et_remark.setEnabled(false);
                if (sampleGerminationInfo.getQcgSgtremark()!=null){
                    et_remark.setText(sampleGerminationInfo.getQcgSgtremark());
                }

                if (sampleGerminationInfo.getQcgSgtimage()!=null && !sampleGerminationInfo.getQcgSgtimage().equalsIgnoreCase("")){
                    Glide.with(GerminationReadingFormActivity.this)
                            .load(Uri.parse(AppConfig.IMAGE_URL + sampleGerminationInfo.getQcgSgtimage()))
                            .into(iv_photo);
                    button_photo.setVisibility(View.GONE);
                }else {
                    ll_germpPhoto.setVisibility(View.GONE);
                }
            }else {
                et_normalseed_rep1.setEnabled(true);
                et_normalseed_rep2.setEnabled(true);
                et_normalseed_rep3.setEnabled(true);
                et_normalseed_rep4.setEnabled(true);
                et_normalseed_rep5.setEnabled(true);
                et_normalseed_rep6.setEnabled(true);
                et_normalseed_rep7.setEnabled(true);
                et_normalseed_rep8.setEnabled(true);
                et_normal_rep1.setEnabled(true);
                et_abnormal_rep1.setEnabled(true);
                et_hardfug_rep1.setEnabled(true);
                et_dead_rep1.setEnabled(true);
                et_normal_rep2.setEnabled(true);
                et_abnormal_rep2.setEnabled(true);
                et_hardfug_rep2.setEnabled(true);
                et_dead_rep2.setEnabled(true);
                et_normal_rep3.setEnabled(true);
                et_abnormal_rep3.setEnabled(true);
                et_hardfug_rep3.setEnabled(true);
                et_dead_rep3.setEnabled(true);
                et_normal_rep4.setEnabled(true);
                et_abnormal_rep4.setEnabled(true);
                et_hardfug_rep4.setEnabled(true);
                et_dead_rep4.setEnabled(true);
                et_normal_rep5.setEnabled(true);
                et_abnormal_rep5.setEnabled(true);
                et_hardfug_rep5.setEnabled(true);
                et_dead_rep5.setEnabled(true);
                et_normal_rep6.setEnabled(true);
                et_abnormal_rep6.setEnabled(true);
                et_hardfug_rep6.setEnabled(true);
                et_dead_rep6.setEnabled(true);
                et_normal_rep7.setEnabled(true);
                et_abnormal_rep7.setEnabled(true);
                et_hardfug_rep7.setEnabled(true);
                et_dead_rep7.setEnabled(true);
                et_normal_rep8.setEnabled(true);
                et_abnormal_rep8.setEnabled(true);
                et_hardfug_rep8.setEnabled(true);
                et_dead_rep8.setEnabled(true);
                spinner_vremark.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                button_photo.setVisibility(View.VISIBLE);
                ll_germpPhoto.setVisibility(View.VISIBLE);
                et_vremark.setVisibility(View.GONE);
                String[] vremark_list = {"Good","Average","Poor"};
                ArrayAdapter<String> adapter_vremark = new ArrayAdapter<>(GerminationReadingFormActivity.this, android.R.layout.simple_spinner_item, vremark_list);
                adapter_vremark.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_vremark.setAdapter(adapter_vremark);
            }

            ll_medium.setVisibility(View.GONE);
            if (rep_countsgt.equalsIgnoreCase("1")) {
                ll_replication2.setVisibility(View.GONE);
                ll_replication2_1.setVisibility(View.GONE);
                ll_replication2_2.setVisibility(View.GONE);
                ll_block34.setVisibility(View.GONE);
                ll_block5678.setVisibility(View.GONE);
                ll_normalrep2.setVisibility(View.GONE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                et_totalnoofseeds.setText(String.valueOf(sampleGerminationInfo.getNoofseedinonerep()));
            }else if (rep_countsgt.equalsIgnoreCase("2")){
                ll_replication2.setVisibility(View.VISIBLE);
                ll_replication2_1.setVisibility(View.VISIBLE);
                ll_replication2_2.setVisibility(View.VISIBLE);
                ll_block34.setVisibility(View.GONE);
                ll_block5678.setVisibility(View.GONE);
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep*2;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }else if (rep_countsgt.equalsIgnoreCase("4")){
                ll_replication2.setVisibility(View.VISIBLE);
                ll_replication2_1.setVisibility(View.VISIBLE);
                ll_replication2_2.setVisibility(View.VISIBLE);
                ll_block34.setVisibility(View.VISIBLE);
                ll_block5678.setVisibility(View.GONE);
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep*4;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }else if (rep_countsgt.equalsIgnoreCase("8")){
                ll_replication2.setVisibility(View.VISIBLE);
                ll_replication2_1.setVisibility(View.VISIBLE);
                ll_replication2_2.setVisibility(View.VISIBLE);
                ll_block34.setVisibility(View.VISIBLE);
                ll_block5678.setVisibility(View.VISIBLE);
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.VISIBLE);
                ll_normalrep78.setVisibility(View.VISIBLE);
                int totalrep = noofseedsinonerep*8;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }
        }else if (gmm.equalsIgnoreCase("FGT")){
            int noofseedsinonerep = sampleGerminationInfo.getNoofseedinonerepfgt();
            radiosgt.setVisibility(View.GONE);
            radiodbt.setVisibility(View.GONE);
            radiofgt.setVisibility(View.VISIBLE);
            radiofgt.setChecked(true);
            ll_sgtdoe.setVisibility(View.GONE);
            ll_sgtddoe.setVisibility(View.GONE);
            ll_fgtdoe.setVisibility(View.VISIBLE);
            ll_hardfug_rep1.setVisibility(View.GONE);
            ll_hardfug_rep2.setVisibility(View.GONE);
            ll_hardfug_rep3.setVisibility(View.GONE);
            ll_hardfug_rep4.setVisibility(View.GONE);
            ll_hardfug_rep5.setVisibility(View.GONE);
            ll_hardfug_rep6.setVisibility(View.GONE);
            ll_hardfug_rep7.setVisibility(View.GONE);
            ll_hardfug_rep8.setVisibility(View.GONE);
            ll_meadofhardseeds.setVisibility(View.GONE);
            ll_noofrepsgt.setVisibility(View.GONE);
            ll_noofrepsgtd.setVisibility(View.GONE);
            ll_noofrepfgt.setVisibility(View.VISIBLE);
            ll_medium.setVisibility(View.VISIBLE);
            if (rep_countfgt.equalsIgnoreCase("1")) {
                ll_replication2.setVisibility(View.GONE);
                ll_replication2_1.setVisibility(View.GONE);
                ll_replication2_2.setVisibility(View.GONE);
                ll_block34.setVisibility(View.GONE);
                ll_block5678.setVisibility(View.GONE);
                ll_normalrep2.setVisibility(View.GONE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                et_totalnoofseeds.setText(String.valueOf(noofseedsinonerep));
            }else if (rep_countfgt.equalsIgnoreCase("2")){
                ll_replication2.setVisibility(View.VISIBLE);
                ll_replication2_1.setVisibility(View.VISIBLE);
                ll_replication2_2.setVisibility(View.VISIBLE);
                ll_block34.setVisibility(View.GONE);
                ll_block5678.setVisibility(View.GONE);
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep*2;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }else if (rep_countfgt.equalsIgnoreCase("4")){
                ll_replication2.setVisibility(View.VISIBLE);
                ll_replication2_1.setVisibility(View.VISIBLE);
                ll_replication2_2.setVisibility(View.VISIBLE);
                ll_block34.setVisibility(View.VISIBLE);
                ll_block5678.setVisibility(View.GONE);
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep*4;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }else if (rep_countfgt.equalsIgnoreCase("8")){
                ll_replication2.setVisibility(View.VISIBLE);
                ll_replication2_1.setVisibility(View.VISIBLE);
                ll_replication2_2.setVisibility(View.VISIBLE);
                ll_block34.setVisibility(View.VISIBLE);
                ll_block5678.setVisibility(View.VISIBLE);
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.VISIBLE);
                ll_normalrep78.setVisibility(View.VISIBLE);
                int totalrep = noofseedsinonerep*8;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }
            if (sampleGerminationInfo.getQcgVigflg()==1){
                et_normalseed_rep1.setText(sampleGerminationInfo.getQcgVigoobnormal1());
                et_normalseed_rep2.setText(sampleGerminationInfo.getQcgVigoobnormal2());
                et_normalseed_rep3.setText(sampleGerminationInfo.getQcgVigoobnormal3());
                et_normalseed_rep4.setText(sampleGerminationInfo.getQcgVigoobnormal4());
                et_normalseed_rep5.setText(sampleGerminationInfo.getQcgVigoobnormal5());
                et_normalseed_rep6.setText(sampleGerminationInfo.getQcgVigoobnormal6());
                et_normalseed_rep7.setText(sampleGerminationInfo.getQcgVigoobnormal7());
                et_normalseed_rep8.setText(sampleGerminationInfo.getQcgVigoobnormal8());

                et_normal_rep1.setText(sampleGerminationInfo.getQcgVignormal1());
                et_abnormal_rep1.setText(sampleGerminationInfo.getQcgVigabnormal1());
                et_dead_rep1.setText(sampleGerminationInfo.getQcgVigdead1());

                et_normal_rep2.setText(sampleGerminationInfo.getQcgVignormal2());
                et_abnormal_rep2.setText(sampleGerminationInfo.getQcgVigabnormal2());
                et_dead_rep2.setText(sampleGerminationInfo.getQcgVigdead2());

                et_normal_rep3.setText(sampleGerminationInfo.getQcgVignormal3());
                et_abnormal_rep3.setText(sampleGerminationInfo.getQcgVigabnormal3());
                et_dead_rep3.setText(sampleGerminationInfo.getQcgVigdead3());

                et_normal_rep4.setText(sampleGerminationInfo.getQcgVignormal4());
                et_abnormal_rep4.setText(sampleGerminationInfo.getQcgVigabnormal4());
                et_dead_rep4.setText(sampleGerminationInfo.getQcgVigdead4());

                et_normal_rep5.setText(sampleGerminationInfo.getQcgVignormal5());
                et_abnormal_rep5.setText(sampleGerminationInfo.getQcgVigabnormal5());
                et_dead_rep5.setText(sampleGerminationInfo.getQcgVigdead5());

                et_normal_rep6.setText(sampleGerminationInfo.getQcgVignormal6());
                et_abnormal_rep6.setText(sampleGerminationInfo.getQcgVigabnormal6());
                et_dead_rep6.setText(sampleGerminationInfo.getQcgVigdead6());

                et_normal_rep7.setText(sampleGerminationInfo.getQcgVignormal7());
                et_abnormal_rep7.setText(sampleGerminationInfo.getQcgVigabnormal7());
                et_dead_rep7.setText(sampleGerminationInfo.getQcgVigdead7());

                et_normal_rep8.setText(sampleGerminationInfo.getQcgVignormal8());
                et_abnormal_rep8.setText(sampleGerminationInfo.getQcgVigabnormal8());
                et_dead_rep8.setText(sampleGerminationInfo.getQcgVigdead8());

                et_normalseedper.setText(sampleGerminationInfo.getQcgVignormalavg());
                et_abnormalseedper.setText(sampleGerminationInfo.getQcgVigabnormalavg());
                et_deadseedper.setText(sampleGerminationInfo.getQcgVigdeadavg());

                checkBox_rssc.setEnabled(false);
                checkBox_prm.setEnabled(false);
                checkBox_vss.setEnabled(false);

                if (sampleGerminationInfo.getQcgFgtoprremark().contains("PRM")){
                    checkBox_prm.setChecked(true);
                }else {
                    checkBox_prm.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgFgtoprremark().contains("VSS")){
                    checkBox_vss.setChecked(true);
                }else {
                    checkBox_vss.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgFgtoprremark().contains("RSSC")){
                    checkBox_rssc.setChecked(true);
                }else {
                    checkBox_rssc.setChecked(false);
                }

                et_normalseed_rep1.setEnabled(false);
                et_normalseed_rep2.setEnabled(false);
                et_normalseed_rep3.setEnabled(false);
                et_normalseed_rep4.setEnabled(false);
                et_normalseed_rep5.setEnabled(false);
                et_normalseed_rep6.setEnabled(false);
                et_normalseed_rep7.setEnabled(false);
                et_normalseed_rep8.setEnabled(false);
                et_normal_rep1.setEnabled(false);
                et_abnormal_rep1.setEnabled(false);
                et_hardfug_rep1.setEnabled(false);
                et_dead_rep1.setEnabled(false);
                et_normal_rep2.setEnabled(false);
                et_abnormal_rep2.setEnabled(false);
                et_hardfug_rep2.setEnabled(false);
                et_dead_rep2.setEnabled(false);
                et_normal_rep3.setEnabled(false);
                et_abnormal_rep3.setEnabled(false);
                et_hardfug_rep3.setEnabled(false);
                et_dead_rep3.setEnabled(false);
                et_normal_rep4.setEnabled(false);
                et_abnormal_rep4.setEnabled(false);
                et_hardfug_rep4.setEnabled(false);
                et_dead_rep4.setEnabled(false);
                et_normal_rep5.setEnabled(false);
                et_abnormal_rep5.setEnabled(false);
                et_hardfug_rep5.setEnabled(false);
                et_dead_rep5.setEnabled(false);
                et_normal_rep6.setEnabled(false);
                et_abnormal_rep6.setEnabled(false);
                et_hardfug_rep6.setEnabled(false);
                et_dead_rep6.setEnabled(false);
                et_normal_rep7.setEnabled(false);
                et_abnormal_rep7.setEnabled(false);
                et_hardfug_rep7.setEnabled(false);
                et_dead_rep7.setEnabled(false);
                et_normal_rep8.setEnabled(false);
                et_abnormal_rep8.setEnabled(false);
                et_hardfug_rep8.setEnabled(false);
                et_dead_rep8.setEnabled(false);
                et_normalseedper.setEnabled(false);
                et_abnormalseedper.setEnabled(false);
                et_hardseedper.setEnabled(false);
                et_deadseedper.setEnabled(false);
                et_remark.setEnabled(false);
                spinner_vremark.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                et_vremark.setText(sampleGerminationInfo.getQcgVigvremark());
                et_vremark.setVisibility(View.VISIBLE);
                if (sampleGerminationInfo.getQcgFgtfcremark()!=null){
                    et_remark.setText(sampleGerminationInfo.getQcgFgtremark());
                }

                if (sampleGerminationInfo.getQcgFgtfcimage()!=null && !sampleGerminationInfo.getQcgFgtimage().equalsIgnoreCase("")){
                    Glide.with(GerminationReadingFormActivity.this)
                            .load(Uri.parse(AppConfig.IMAGE_URL + sampleGerminationInfo.getQcgFgtimage()))
                            .into(iv_photo);
                    button_photo.setVisibility(View.GONE);
                }else {
                    ll_germpPhoto.setVisibility(View.GONE);
                }
            }else {
                et_normalseed_rep1.setEnabled(true);
                et_normalseed_rep2.setEnabled(true);
                et_normalseed_rep3.setEnabled(true);
                et_normalseed_rep4.setEnabled(true);
                et_normalseed_rep5.setEnabled(true);
                et_normalseed_rep6.setEnabled(true);
                et_normalseed_rep7.setEnabled(true);
                et_normalseed_rep8.setEnabled(true);
                et_normal_rep1.setEnabled(true);
                et_abnormal_rep1.setEnabled(true);
                et_hardfug_rep1.setEnabled(true);
                et_dead_rep1.setEnabled(true);
                et_normal_rep2.setEnabled(true);
                et_abnormal_rep2.setEnabled(true);
                et_hardfug_rep2.setEnabled(true);
                et_dead_rep2.setEnabled(true);
                et_normal_rep3.setEnabled(true);
                et_abnormal_rep3.setEnabled(true);
                et_hardfug_rep3.setEnabled(true);
                et_dead_rep3.setEnabled(true);
                et_normal_rep4.setEnabled(true);
                et_abnormal_rep4.setEnabled(true);
                et_hardfug_rep4.setEnabled(true);
                et_dead_rep4.setEnabled(true);
                et_normal_rep5.setEnabled(true);
                et_abnormal_rep5.setEnabled(true);
                et_hardfug_rep5.setEnabled(true);
                et_dead_rep5.setEnabled(true);
                et_normal_rep6.setEnabled(true);
                et_abnormal_rep6.setEnabled(true);
                et_hardfug_rep6.setEnabled(true);
                et_dead_rep6.setEnabled(true);
                et_normal_rep7.setEnabled(true);
                et_abnormal_rep7.setEnabled(true);
                et_hardfug_rep7.setEnabled(true);
                et_dead_rep7.setEnabled(true);
                et_normal_rep8.setEnabled(true);
                et_abnormal_rep8.setEnabled(true);
                et_hardfug_rep8.setEnabled(true);
                et_dead_rep8.setEnabled(true);

                spinner_vremark.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                button_photo.setVisibility(View.VISIBLE);
                ll_germpPhoto.setVisibility(View.VISIBLE);
                et_vremark.setVisibility(View.GONE);
                String[] vremark_list = {"Good","Average","Poor"};
                ArrayAdapter<String> adapter_vremark = new ArrayAdapter<>(GerminationReadingFormActivity.this, android.R.layout.simple_spinner_item, vremark_list);
                adapter_vremark.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_vremark.setAdapter(adapter_vremark);
            }
        } else if (gmm.equalsIgnoreCase("SGT and FGT")){
            int noofseedsinonerep = sampleGerminationInfo.getNoofseedinonerep();
            radiosgt.setVisibility(View.VISIBLE);
            radiodbt.setVisibility(View.GONE);
            radiofgt.setVisibility(View.VISIBLE);
            ll_sgtdoe.setVisibility(View.VISIBLE);
            ll_sgtddoe.setVisibility(View.GONE);
            ll_fgtdoe.setVisibility(View.GONE);
            ll_hardfug_rep1.setVisibility(View.VISIBLE);
            ll_hardfug_rep2.setVisibility(View.VISIBLE);
            ll_hardfug_rep3.setVisibility(View.VISIBLE);
            ll_hardfug_rep4.setVisibility(View.VISIBLE);
            ll_hardfug_rep5.setVisibility(View.VISIBLE);
            ll_hardfug_rep6.setVisibility(View.VISIBLE);
            ll_hardfug_rep7.setVisibility(View.VISIBLE);
            ll_hardfug_rep8.setVisibility(View.VISIBLE);
            ll_meadofhardseeds.setVisibility(View.VISIBLE);
            ll_noofrepsgt.setVisibility(View.VISIBLE);
            ll_noofrepsgtd.setVisibility(View.GONE);
            ll_noofrepfgt.setVisibility(View.GONE);
            ll_medium.setVisibility(View.GONE);
            if (rep_countsgt.equalsIgnoreCase("1")) {
                ll_replication2.setVisibility(View.GONE);
                ll_replication2_1.setVisibility(View.GONE);
                ll_replication2_2.setVisibility(View.GONE);
                ll_block34.setVisibility(View.GONE);
                ll_block5678.setVisibility(View.GONE);
                ll_normalrep2.setVisibility(View.GONE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                et_totalnoofseeds.setText(String.valueOf(noofseedsinonerep));
            }else if (rep_countsgt.equalsIgnoreCase("2")){
                ll_replication2.setVisibility(View.VISIBLE);
                ll_replication2_1.setVisibility(View.VISIBLE);
                ll_replication2_2.setVisibility(View.VISIBLE);
                ll_block34.setVisibility(View.GONE);
                ll_block5678.setVisibility(View.GONE);
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep*2;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }else if (rep_countsgt.equalsIgnoreCase("4")){
                ll_replication2.setVisibility(View.VISIBLE);
                ll_replication2_1.setVisibility(View.VISIBLE);
                ll_replication2_2.setVisibility(View.VISIBLE);
                ll_block34.setVisibility(View.VISIBLE);
                ll_block5678.setVisibility(View.GONE);
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep*4;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }else if (rep_countsgt.equalsIgnoreCase("8")){
                ll_replication2.setVisibility(View.VISIBLE);
                ll_replication2_1.setVisibility(View.VISIBLE);
                ll_replication2_2.setVisibility(View.VISIBLE);
                ll_block34.setVisibility(View.VISIBLE);
                ll_block5678.setVisibility(View.VISIBLE);
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.VISIBLE);
                ll_normalrep78.setVisibility(View.VISIBLE);
                int totalrep = noofseedsinonerep*8;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }
            if (sampleGerminationInfo.getQcgSgtflg()==1){
                et_normalseed_rep1.setText(sampleGerminationInfo.getQcgSgtoobnormal1());
                et_normalseed_rep2.setText(sampleGerminationInfo.getQcgSgtoobnormal2());
                et_normalseed_rep3.setText(sampleGerminationInfo.getQcgSgtoobnormal3());
                et_normalseed_rep4.setText(sampleGerminationInfo.getQcgSgtoobnormal4());
                et_normalseed_rep5.setText(sampleGerminationInfo.getQcgSgtoobnormal5());
                et_normalseed_rep6.setText(sampleGerminationInfo.getQcgSgtoobnormal6());
                et_normalseed_rep7.setText(sampleGerminationInfo.getQcgSgtoobnormal7());
                et_normalseed_rep8.setText(sampleGerminationInfo.getQcgSgtoobnormal8());

                et_normal_rep1.setText(sampleGerminationInfo.getQcgSgtnormal1());
                et_abnormal_rep1.setText(sampleGerminationInfo.getQcgSgtabnormal1());
                et_hardfug_rep1.setText(sampleGerminationInfo.getQcgSgthardfug1());
                et_dead_rep1.setText(sampleGerminationInfo.getQcgSgthardfug1());

                et_normal_rep2.setText(sampleGerminationInfo.getQcgSgtnormal2());
                et_abnormal_rep2.setText(sampleGerminationInfo.getQcgSgtabnormal2());
                et_hardfug_rep2.setText(sampleGerminationInfo.getQcgSgthardfug2());
                et_dead_rep2.setText(sampleGerminationInfo.getQcgSgtdead2());

                et_normal_rep3.setText(sampleGerminationInfo.getQcgSgtnormal3());
                et_abnormal_rep3.setText(sampleGerminationInfo.getQcgSgtabnormal3());
                et_hardfug_rep3.setText(sampleGerminationInfo.getQcgSgthardfug3());
                et_dead_rep3.setText(sampleGerminationInfo.getQcgSgtdead3());

                et_normal_rep4.setText(sampleGerminationInfo.getQcgSgtnormal4());
                et_abnormal_rep4.setText(sampleGerminationInfo.getQcgSgtabnormal4());
                et_hardfug_rep4.setText(sampleGerminationInfo.getQcgSgthardfug4());
                et_dead_rep4.setText(sampleGerminationInfo.getQcgSgtdead4());

                et_normal_rep5.setText(sampleGerminationInfo.getQcgSgtnormal5());
                et_abnormal_rep5.setText(sampleGerminationInfo.getQcgSgtabnormal5());
                et_hardfug_rep5.setText(sampleGerminationInfo.getQcgSgthardfug5());
                et_dead_rep5.setText(sampleGerminationInfo.getQcgSgtdead5());

                et_normal_rep6.setText(sampleGerminationInfo.getQcgSgtnormal6());
                et_abnormal_rep6.setText(sampleGerminationInfo.getQcgSgtabnormal6());
                et_hardfug_rep6.setText(sampleGerminationInfo.getQcgSgthardfug6());
                et_dead_rep6.setText(sampleGerminationInfo.getQcgSgtdead6());

                et_normal_rep7.setText(sampleGerminationInfo.getQcgSgtnormal7());
                et_abnormal_rep7.setText(sampleGerminationInfo.getQcgSgtabnormal7());
                et_hardfug_rep7.setText(sampleGerminationInfo.getQcgSgthardfug7());
                et_dead_rep7.setText(sampleGerminationInfo.getQcgSgtdead7());

                et_normal_rep8.setText(sampleGerminationInfo.getQcgSgtnormal8());
                et_abnormal_rep8.setText(sampleGerminationInfo.getQcgSgtabnormal8());
                et_hardfug_rep8.setText(sampleGerminationInfo.getQcgSgthardfug8());
                et_dead_rep8.setText(sampleGerminationInfo.getQcgSgtdead8());

                et_normalseedper.setText(sampleGerminationInfo.getQcgSgtnormalavg());
                et_abnormalseedper.setText(sampleGerminationInfo.getQcgSgtabnormalavg());
                et_hardseedper.setText(sampleGerminationInfo.getQcgSgthardfugavg());
                et_deadseedper.setText(sampleGerminationInfo.getQcgSgtdeadavg());

                checkBox_rssc.setEnabled(false);
                checkBox_prm.setEnabled(false);
                checkBox_vss.setEnabled(false);

                if (sampleGerminationInfo.getQcgOprremark().contains("PRM")){
                    checkBox_prm.setChecked(true);
                }else {
                    checkBox_prm.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgOprremark().contains("VSS")){
                    checkBox_vss.setChecked(true);
                }else {
                    checkBox_vss.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgOprremark().contains("RSSC")){
                    checkBox_rssc.setChecked(true);
                }else {
                    checkBox_rssc.setChecked(false);
                }

                et_normalseed_rep1.setEnabled(false);
                et_normalseed_rep2.setEnabled(false);
                et_normalseed_rep3.setEnabled(false);
                et_normalseed_rep4.setEnabled(false);
                et_normalseed_rep5.setEnabled(false);
                et_normalseed_rep6.setEnabled(false);
                et_normalseed_rep7.setEnabled(false);
                et_normalseed_rep8.setEnabled(false);
                et_normal_rep1.setEnabled(false);
                et_abnormal_rep1.setEnabled(false);
                et_hardfug_rep1.setEnabled(false);
                et_dead_rep1.setEnabled(false);
                et_normal_rep2.setEnabled(false);
                et_abnormal_rep2.setEnabled(false);
                et_hardfug_rep2.setEnabled(false);
                et_dead_rep2.setEnabled(false);
                et_normal_rep3.setEnabled(false);
                et_abnormal_rep3.setEnabled(false);
                et_hardfug_rep3.setEnabled(false);
                et_dead_rep3.setEnabled(false);
                et_normal_rep4.setEnabled(false);
                et_abnormal_rep4.setEnabled(false);
                et_hardfug_rep4.setEnabled(false);
                et_dead_rep4.setEnabled(false);
                et_normal_rep5.setEnabled(false);
                et_abnormal_rep5.setEnabled(false);
                et_hardfug_rep5.setEnabled(false);
                et_dead_rep5.setEnabled(false);
                et_normal_rep6.setEnabled(false);
                et_abnormal_rep6.setEnabled(false);
                et_hardfug_rep6.setEnabled(false);
                et_dead_rep6.setEnabled(false);
                et_normal_rep7.setEnabled(false);
                et_abnormal_rep7.setEnabled(false);
                et_hardfug_rep7.setEnabled(false);
                et_dead_rep7.setEnabled(false);
                et_normal_rep8.setEnabled(false);
                et_abnormal_rep8.setEnabled(false);
                et_hardfug_rep8.setEnabled(false);
                et_dead_rep8.setEnabled(false);
                et_normalseedper.setEnabled(false);
                et_abnormalseedper.setEnabled(false);
                et_hardseedper.setEnabled(false);
                et_deadseedper.setEnabled(false);
                et_remark.setEnabled(false);
                spinner_vremark.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                et_vremark.setText(sampleGerminationInfo.getQcgSgtdvremark());
                et_vremark.setVisibility(View.VISIBLE);
                if (sampleGerminationInfo.getQcgSgtremark()!=null){
                    et_remark.setText(sampleGerminationInfo.getQcgSgtremark());
                }
                if (sampleGerminationInfo.getQcgSgtimage()!=null && !sampleGerminationInfo.getQcgSgtimage().equalsIgnoreCase("")){
                    Glide.with(GerminationReadingFormActivity.this)
                            .load(Uri.parse(AppConfig.IMAGE_URL + sampleGerminationInfo.getQcgSgtimage()))
                            .into(iv_photo);
                    button_photo.setVisibility(View.GONE);
                }else {
                    ll_germpPhoto.setVisibility(View.GONE);
                }

            }else {
                et_normalseed_rep1.setEnabled(true);
                et_normalseed_rep2.setEnabled(true);
                et_normalseed_rep3.setEnabled(true);
                et_normalseed_rep4.setEnabled(true);
                et_normalseed_rep5.setEnabled(true);
                et_normalseed_rep6.setEnabled(true);
                et_normalseed_rep7.setEnabled(true);
                et_normalseed_rep8.setEnabled(true);
                et_normal_rep1.setEnabled(true);
                et_abnormal_rep1.setEnabled(true);
                et_hardfug_rep1.setEnabled(true);
                et_dead_rep1.setEnabled(true);
                et_normal_rep2.setEnabled(true);
                et_abnormal_rep2.setEnabled(true);
                et_hardfug_rep2.setEnabled(true);
                et_dead_rep2.setEnabled(true);
                et_normal_rep3.setEnabled(true);
                et_abnormal_rep3.setEnabled(true);
                et_hardfug_rep3.setEnabled(true);
                et_dead_rep3.setEnabled(true);
                et_normal_rep4.setEnabled(true);
                et_abnormal_rep4.setEnabled(true);
                et_hardfug_rep4.setEnabled(true);
                et_dead_rep4.setEnabled(true);
                et_normal_rep5.setEnabled(true);
                et_abnormal_rep5.setEnabled(true);
                et_hardfug_rep5.setEnabled(true);
                et_dead_rep5.setEnabled(true);
                et_normal_rep6.setEnabled(true);
                et_abnormal_rep6.setEnabled(true);
                et_hardfug_rep6.setEnabled(true);
                et_dead_rep6.setEnabled(true);
                et_normal_rep7.setEnabled(true);
                et_abnormal_rep7.setEnabled(true);
                et_hardfug_rep7.setEnabled(true);
                et_dead_rep7.setEnabled(true);
                et_normal_rep8.setEnabled(true);
                et_abnormal_rep8.setEnabled(true);
                et_hardfug_rep8.setEnabled(true);
                et_dead_rep8.setEnabled(true);
                spinner_vremark.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                button_photo.setVisibility(View.VISIBLE);
                ll_germpPhoto.setVisibility(View.VISIBLE);
                et_vremark.setVisibility(View.GONE);
                String[] vremark_list = {"Good","Average","Poor"};
                ArrayAdapter<String> adapter_vremark = new ArrayAdapter<>(GerminationReadingFormActivity.this, android.R.layout.simple_spinner_item, vremark_list);
                adapter_vremark.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_vremark.setAdapter(adapter_vremark);
            }
        } else if (gmm.equalsIgnoreCase("SGT with SGTD")){
            int noofseedsinonerep = sampleGerminationInfo.getNoofseedinonerep();
            radiosgt.setVisibility(View.VISIBLE);
            radiodbt.setVisibility(View.VISIBLE);
            radiofgt.setVisibility(View.GONE);
            ll_sgtdoe.setVisibility(View.VISIBLE);
            ll_sgtddoe.setVisibility(View.GONE);
            ll_fgtdoe.setVisibility(View.GONE);
            ll_hardfug_rep1.setVisibility(View.VISIBLE);
            ll_hardfug_rep2.setVisibility(View.VISIBLE);
            ll_hardfug_rep3.setVisibility(View.VISIBLE);
            ll_hardfug_rep4.setVisibility(View.VISIBLE);
            ll_hardfug_rep5.setVisibility(View.VISIBLE);
            ll_hardfug_rep6.setVisibility(View.VISIBLE);
            ll_hardfug_rep7.setVisibility(View.VISIBLE);
            ll_hardfug_rep8.setVisibility(View.VISIBLE);
            ll_meadofhardseeds.setVisibility(View.VISIBLE);
            ll_noofrepsgt.setVisibility(View.VISIBLE);
            ll_noofrepsgtd.setVisibility(View.GONE);
            ll_noofrepfgt.setVisibility(View.GONE);

            if (sampleGerminationInfo.getQcgSgtflg()==1){
                et_normalseed_rep1.setText(sampleGerminationInfo.getQcgSgtoobnormal1());
                et_normalseed_rep2.setText(sampleGerminationInfo.getQcgSgtoobnormal2());
                et_normalseed_rep3.setText(sampleGerminationInfo.getQcgSgtoobnormal3());
                et_normalseed_rep4.setText(sampleGerminationInfo.getQcgSgtoobnormal4());
                et_normalseed_rep5.setText(sampleGerminationInfo.getQcgSgtoobnormal5());
                et_normalseed_rep6.setText(sampleGerminationInfo.getQcgSgtoobnormal6());
                et_normalseed_rep7.setText(sampleGerminationInfo.getQcgSgtoobnormal7());
                et_normalseed_rep8.setText(sampleGerminationInfo.getQcgSgtoobnormal8());

                et_normal_rep1.setText(sampleGerminationInfo.getQcgSgtnormal1());
                et_abnormal_rep1.setText(sampleGerminationInfo.getQcgSgtabnormal1());
                et_hardfug_rep1.setText(sampleGerminationInfo.getQcgSgthardfug1());
                et_dead_rep1.setText(sampleGerminationInfo.getQcgSgthardfug1());

                et_normal_rep2.setText(sampleGerminationInfo.getQcgSgtnormal2());
                et_abnormal_rep2.setText(sampleGerminationInfo.getQcgSgtabnormal2());
                et_hardfug_rep2.setText(sampleGerminationInfo.getQcgSgthardfug2());
                et_dead_rep2.setText(sampleGerminationInfo.getQcgSgtdead2());

                et_normal_rep3.setText(sampleGerminationInfo.getQcgSgtnormal3());
                et_abnormal_rep3.setText(sampleGerminationInfo.getQcgSgtabnormal3());
                et_hardfug_rep3.setText(sampleGerminationInfo.getQcgSgthardfug3());
                et_dead_rep3.setText(sampleGerminationInfo.getQcgSgtdead3());

                et_normal_rep4.setText(sampleGerminationInfo.getQcgSgtnormal4());
                et_abnormal_rep4.setText(sampleGerminationInfo.getQcgSgtabnormal4());
                et_hardfug_rep4.setText(sampleGerminationInfo.getQcgSgthardfug4());
                et_dead_rep4.setText(sampleGerminationInfo.getQcgSgtdead4());

                et_normal_rep5.setText(sampleGerminationInfo.getQcgSgtnormal5());
                et_abnormal_rep5.setText(sampleGerminationInfo.getQcgSgtabnormal5());
                et_hardfug_rep5.setText(sampleGerminationInfo.getQcgSgthardfug5());
                et_dead_rep5.setText(sampleGerminationInfo.getQcgSgtdead5());

                et_normal_rep6.setText(sampleGerminationInfo.getQcgSgtnormal6());
                et_abnormal_rep6.setText(sampleGerminationInfo.getQcgSgtabnormal6());
                et_hardfug_rep6.setText(sampleGerminationInfo.getQcgSgthardfug6());
                et_dead_rep6.setText(sampleGerminationInfo.getQcgSgtdead6());

                et_normal_rep7.setText(sampleGerminationInfo.getQcgSgtnormal7());
                et_abnormal_rep7.setText(sampleGerminationInfo.getQcgSgtabnormal7());
                et_hardfug_rep7.setText(sampleGerminationInfo.getQcgSgthardfug7());
                et_dead_rep7.setText(sampleGerminationInfo.getQcgSgtdead7());

                et_normal_rep8.setText(sampleGerminationInfo.getQcgSgtnormal8());
                et_abnormal_rep8.setText(sampleGerminationInfo.getQcgSgtabnormal8());
                et_hardfug_rep8.setText(sampleGerminationInfo.getQcgSgthardfug8());
                et_dead_rep8.setText(sampleGerminationInfo.getQcgSgtdead8());

                et_normalseedper.setText(sampleGerminationInfo.getQcgSgtnormalavg());
                et_abnormalseedper.setText(sampleGerminationInfo.getQcgSgtabnormalavg());
                et_hardseedper.setText(sampleGerminationInfo.getQcgSgthardfugavg());
                et_deadseedper.setText(sampleGerminationInfo.getQcgSgtdeadavg());

                checkBox_rssc.setEnabled(false);
                checkBox_prm.setEnabled(false);
                checkBox_vss.setEnabled(false);

                if (sampleGerminationInfo.getQcgOprremark().contains("PRM")){
                    checkBox_prm.setChecked(true);
                }else {
                    checkBox_prm.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgOprremark().contains("VSS")){
                    checkBox_vss.setChecked(true);
                }else {
                    checkBox_vss.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgOprremark().contains("RSSC")){
                    checkBox_rssc.setChecked(true);
                }else {
                    checkBox_rssc.setChecked(false);
                }

                et_normalseed_rep1.setEnabled(false);
                et_normalseed_rep2.setEnabled(false);
                et_normalseed_rep3.setEnabled(false);
                et_normalseed_rep4.setEnabled(false);
                et_normalseed_rep5.setEnabled(false);
                et_normalseed_rep6.setEnabled(false);
                et_normalseed_rep7.setEnabled(false);
                et_normalseed_rep8.setEnabled(false);
                et_normal_rep1.setEnabled(false);
                et_abnormal_rep1.setEnabled(false);
                et_hardfug_rep1.setEnabled(false);
                et_dead_rep1.setEnabled(false);
                et_normal_rep2.setEnabled(false);
                et_abnormal_rep2.setEnabled(false);
                et_hardfug_rep2.setEnabled(false);
                et_dead_rep2.setEnabled(false);
                et_normal_rep3.setEnabled(false);
                et_abnormal_rep3.setEnabled(false);
                et_hardfug_rep3.setEnabled(false);
                et_dead_rep3.setEnabled(false);
                et_normal_rep4.setEnabled(false);
                et_abnormal_rep4.setEnabled(false);
                et_hardfug_rep4.setEnabled(false);
                et_dead_rep4.setEnabled(false);
                et_normal_rep5.setEnabled(false);
                et_abnormal_rep5.setEnabled(false);
                et_hardfug_rep5.setEnabled(false);
                et_dead_rep5.setEnabled(false);
                et_normal_rep6.setEnabled(false);
                et_abnormal_rep6.setEnabled(false);
                et_hardfug_rep6.setEnabled(false);
                et_dead_rep6.setEnabled(false);
                et_normal_rep7.setEnabled(false);
                et_abnormal_rep7.setEnabled(false);
                et_hardfug_rep7.setEnabled(false);
                et_dead_rep7.setEnabled(false);
                et_normal_rep8.setEnabled(false);
                et_abnormal_rep8.setEnabled(false);
                et_hardfug_rep8.setEnabled(false);
                et_dead_rep8.setEnabled(false);
                et_normalseedper.setEnabled(false);
                et_abnormalseedper.setEnabled(false);
                et_hardseedper.setEnabled(false);
                et_deadseedper.setEnabled(false);
                et_remark.setEnabled(false);

                spinner_vremark.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                et_vremark.setText(sampleGerminationInfo.getQcgSgtvremark());
                et_vremark.setVisibility(View.VISIBLE);
                if (sampleGerminationInfo.getQcgSgtremark()!=null){
                    et_remark.setText(sampleGerminationInfo.getQcgSgtremark());
                }
                if (sampleGerminationInfo.getQcgSgtimage()!=null && !sampleGerminationInfo.getQcgSgtimage().equalsIgnoreCase("")){
                    Glide.with(GerminationReadingFormActivity.this)
                            .load(Uri.parse(AppConfig.IMAGE_URL + sampleGerminationInfo.getQcgSgtimage()))
                            .into(iv_photo);
                    button_photo.setVisibility(View.GONE);
                }else {
                    ll_germpPhoto.setVisibility(View.GONE);
                }
            }else {
                et_normalseed_rep1.setEnabled(true);
                et_normalseed_rep2.setEnabled(true);
                et_normalseed_rep3.setEnabled(true);
                et_normalseed_rep4.setEnabled(true);
                et_normalseed_rep5.setEnabled(true);
                et_normalseed_rep6.setEnabled(true);
                et_normalseed_rep7.setEnabled(true);
                et_normalseed_rep8.setEnabled(true);
                et_normal_rep1.setEnabled(true);
                et_abnormal_rep1.setEnabled(true);
                et_hardfug_rep1.setEnabled(true);
                et_dead_rep1.setEnabled(true);
                et_normal_rep2.setEnabled(true);
                et_abnormal_rep2.setEnabled(true);
                et_hardfug_rep2.setEnabled(true);
                et_dead_rep2.setEnabled(true);
                et_normal_rep3.setEnabled(true);
                et_abnormal_rep3.setEnabled(true);
                et_hardfug_rep3.setEnabled(true);
                et_dead_rep3.setEnabled(true);
                et_normal_rep4.setEnabled(true);
                et_abnormal_rep4.setEnabled(true);
                et_hardfug_rep4.setEnabled(true);
                et_dead_rep4.setEnabled(true);
                et_normal_rep5.setEnabled(true);
                et_abnormal_rep5.setEnabled(true);
                et_hardfug_rep5.setEnabled(true);
                et_dead_rep5.setEnabled(true);
                et_normal_rep6.setEnabled(true);
                et_abnormal_rep6.setEnabled(true);
                et_hardfug_rep6.setEnabled(true);
                et_dead_rep6.setEnabled(true);
                et_normal_rep7.setEnabled(true);
                et_abnormal_rep7.setEnabled(true);
                et_hardfug_rep7.setEnabled(true);
                et_dead_rep7.setEnabled(true);
                et_normal_rep8.setEnabled(true);
                et_abnormal_rep8.setEnabled(true);
                et_hardfug_rep8.setEnabled(true);
                et_dead_rep8.setEnabled(true);
                spinner_vremark.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                button_photo.setVisibility(View.VISIBLE);
                ll_germpPhoto.setVisibility(View.VISIBLE);
                et_vremark.setVisibility(View.GONE);
                String[] vremark_list = {"Good","Average","Poor"};
                ArrayAdapter<String> adapter_vremark = new ArrayAdapter<>(GerminationReadingFormActivity.this, android.R.layout.simple_spinner_item, vremark_list);
                adapter_vremark.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_vremark.setAdapter(adapter_vremark);
            }

            ll_medium.setVisibility(View.GONE);
            if (rep_countsgt.equalsIgnoreCase("1")) {
                ll_replication2.setVisibility(View.GONE);
                ll_replication2_1.setVisibility(View.GONE);
                ll_replication2_2.setVisibility(View.GONE);
                ll_block34.setVisibility(View.GONE);
                ll_block5678.setVisibility(View.GONE);
                ll_normalrep2.setVisibility(View.GONE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                et_totalnoofseeds.setText(String.valueOf(sampleGerminationInfo.getNoofseedinonerep()));
            }else if (rep_countsgt.equalsIgnoreCase("2")){
                ll_replication2.setVisibility(View.VISIBLE);
                ll_replication2_1.setVisibility(View.VISIBLE);
                ll_replication2_2.setVisibility(View.VISIBLE);
                ll_block34.setVisibility(View.GONE);
                ll_block5678.setVisibility(View.GONE);
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep*2;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }else if (rep_countsgt.equalsIgnoreCase("4")){
                ll_replication2.setVisibility(View.VISIBLE);
                ll_replication2_1.setVisibility(View.VISIBLE);
                ll_replication2_2.setVisibility(View.VISIBLE);
                ll_block34.setVisibility(View.VISIBLE);
                ll_block5678.setVisibility(View.GONE);
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep*4;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }else if (rep_countsgt.equalsIgnoreCase("8")){
                ll_replication2.setVisibility(View.VISIBLE);
                ll_replication2_1.setVisibility(View.VISIBLE);
                ll_replication2_2.setVisibility(View.VISIBLE);
                ll_block34.setVisibility(View.VISIBLE);
                ll_block5678.setVisibility(View.VISIBLE);
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.VISIBLE);
                ll_normalrep78.setVisibility(View.VISIBLE);
                int totalrep = noofseedsinonerep*8;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }
        } else if (gmm.equalsIgnoreCase("ALL")){
            int noofseedsinonerep = sampleGerminationInfo.getNoofseedinonerep();
            radiosgt.setVisibility(View.VISIBLE);
            radiodbt.setVisibility(View.VISIBLE);
            radiofgt.setVisibility(View.VISIBLE);
            ll_sgtdoe.setVisibility(View.VISIBLE);
            ll_sgtddoe.setVisibility(View.GONE);
            ll_fgtdoe.setVisibility(View.GONE);
            ll_hardfug_rep1.setVisibility(View.VISIBLE);
            ll_hardfug_rep2.setVisibility(View.VISIBLE);
            ll_hardfug_rep3.setVisibility(View.VISIBLE);
            ll_hardfug_rep4.setVisibility(View.VISIBLE);
            ll_hardfug_rep5.setVisibility(View.VISIBLE);
            ll_hardfug_rep6.setVisibility(View.VISIBLE);
            ll_hardfug_rep7.setVisibility(View.VISIBLE);
            ll_hardfug_rep8.setVisibility(View.VISIBLE);
            ll_meadofhardseeds.setVisibility(View.VISIBLE);
            ll_noofrepsgt.setVisibility(View.VISIBLE);
            ll_noofrepsgtd.setVisibility(View.VISIBLE);
            ll_noofrepfgt.setVisibility(View.VISIBLE);

            if (sampleGerminationInfo.getQcgSgtflg()==1){
                et_normalseed_rep1.setText(sampleGerminationInfo.getQcgSgtoobnormal1());
                et_normalseed_rep2.setText(sampleGerminationInfo.getQcgSgtoobnormal2());
                et_normalseed_rep3.setText(sampleGerminationInfo.getQcgSgtoobnormal3());
                et_normalseed_rep4.setText(sampleGerminationInfo.getQcgSgtoobnormal4());
                et_normalseed_rep5.setText(sampleGerminationInfo.getQcgSgtoobnormal5());
                et_normalseed_rep6.setText(sampleGerminationInfo.getQcgSgtoobnormal6());
                et_normalseed_rep7.setText(sampleGerminationInfo.getQcgSgtoobnormal7());
                et_normalseed_rep8.setText(sampleGerminationInfo.getQcgSgtoobnormal8());

                et_normal_rep1.setText(sampleGerminationInfo.getQcgSgtnormal1());
                et_abnormal_rep1.setText(sampleGerminationInfo.getQcgSgtabnormal1());
                et_hardfug_rep1.setText(sampleGerminationInfo.getQcgSgthardfug1());
                et_dead_rep1.setText(sampleGerminationInfo.getQcgSgthardfug1());

                et_normal_rep2.setText(sampleGerminationInfo.getQcgSgtnormal2());
                et_abnormal_rep2.setText(sampleGerminationInfo.getQcgSgtabnormal2());
                et_hardfug_rep2.setText(sampleGerminationInfo.getQcgSgthardfug2());
                et_dead_rep2.setText(sampleGerminationInfo.getQcgSgtdead2());

                et_normal_rep3.setText(sampleGerminationInfo.getQcgSgtnormal3());
                et_abnormal_rep3.setText(sampleGerminationInfo.getQcgSgtabnormal3());
                et_hardfug_rep3.setText(sampleGerminationInfo.getQcgSgthardfug3());
                et_dead_rep3.setText(sampleGerminationInfo.getQcgSgtdead3());

                et_normal_rep4.setText(sampleGerminationInfo.getQcgSgtnormal4());
                et_abnormal_rep4.setText(sampleGerminationInfo.getQcgSgtabnormal4());
                et_hardfug_rep4.setText(sampleGerminationInfo.getQcgSgthardfug4());
                et_dead_rep4.setText(sampleGerminationInfo.getQcgSgtdead4());

                et_normal_rep5.setText(sampleGerminationInfo.getQcgSgtnormal5());
                et_abnormal_rep5.setText(sampleGerminationInfo.getQcgSgtabnormal5());
                et_hardfug_rep5.setText(sampleGerminationInfo.getQcgSgthardfug5());
                et_dead_rep5.setText(sampleGerminationInfo.getQcgSgtdead5());

                et_normal_rep6.setText(sampleGerminationInfo.getQcgSgtnormal6());
                et_abnormal_rep6.setText(sampleGerminationInfo.getQcgSgtabnormal6());
                et_hardfug_rep6.setText(sampleGerminationInfo.getQcgSgthardfug6());
                et_dead_rep6.setText(sampleGerminationInfo.getQcgSgtdead6());

                et_normal_rep7.setText(sampleGerminationInfo.getQcgSgtnormal7());
                et_abnormal_rep7.setText(sampleGerminationInfo.getQcgSgtabnormal7());
                et_hardfug_rep7.setText(sampleGerminationInfo.getQcgSgthardfug7());
                et_dead_rep7.setText(sampleGerminationInfo.getQcgSgtdead7());

                et_normal_rep8.setText(sampleGerminationInfo.getQcgSgtnormal8());
                et_abnormal_rep8.setText(sampleGerminationInfo.getQcgSgtabnormal8());
                et_hardfug_rep8.setText(sampleGerminationInfo.getQcgSgthardfug8());
                et_dead_rep8.setText(sampleGerminationInfo.getQcgSgtdead8());

                et_normalseedper.setText(sampleGerminationInfo.getQcgSgtnormalavg());
                et_abnormalseedper.setText(sampleGerminationInfo.getQcgSgtabnormalavg());
                et_hardseedper.setText(sampleGerminationInfo.getQcgSgthardfugavg());
                et_deadseedper.setText(sampleGerminationInfo.getQcgSgtdeadavg());

                et_normalseed_rep1.setEnabled(false);
                et_normalseed_rep2.setEnabled(false);
                et_normalseed_rep3.setEnabled(false);
                et_normalseed_rep4.setEnabled(false);
                et_normalseed_rep5.setEnabled(false);
                et_normalseed_rep6.setEnabled(false);
                et_normalseed_rep7.setEnabled(false);
                et_normalseed_rep8.setEnabled(false);
                et_normal_rep1.setEnabled(false);
                et_abnormal_rep1.setEnabled(false);
                et_hardfug_rep1.setEnabled(false);
                et_dead_rep1.setEnabled(false);
                et_normal_rep2.setEnabled(false);
                et_abnormal_rep2.setEnabled(false);
                et_hardfug_rep2.setEnabled(false);
                et_dead_rep2.setEnabled(false);
                et_normal_rep3.setEnabled(false);
                et_abnormal_rep3.setEnabled(false);
                et_hardfug_rep3.setEnabled(false);
                et_dead_rep3.setEnabled(false);
                et_normal_rep4.setEnabled(false);
                et_abnormal_rep4.setEnabled(false);
                et_hardfug_rep4.setEnabled(false);
                et_dead_rep4.setEnabled(false);
                et_normal_rep5.setEnabled(false);
                et_abnormal_rep5.setEnabled(false);
                et_hardfug_rep5.setEnabled(false);
                et_dead_rep5.setEnabled(false);
                et_normal_rep6.setEnabled(false);
                et_abnormal_rep6.setEnabled(false);
                et_hardfug_rep6.setEnabled(false);
                et_dead_rep6.setEnabled(false);
                et_normal_rep7.setEnabled(false);
                et_abnormal_rep7.setEnabled(false);
                et_hardfug_rep7.setEnabled(false);
                et_dead_rep7.setEnabled(false);
                et_normal_rep8.setEnabled(false);
                et_abnormal_rep8.setEnabled(false);
                et_hardfug_rep8.setEnabled(false);
                et_dead_rep8.setEnabled(false);
                et_normalseedper.setEnabled(false);
                et_abnormalseedper.setEnabled(false);
                et_hardseedper.setEnabled(false);
                et_deadseedper.setEnabled(false);
                et_remark.setEnabled(false);

                checkBox_rssc.setEnabled(false);
                checkBox_prm.setEnabled(false);
                checkBox_vss.setEnabled(false);

                if (sampleGerminationInfo.getQcgOprremark().contains("PRM")){
                    checkBox_prm.setChecked(true);
                }else {
                    checkBox_prm.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgOprremark().contains("VSS")){
                    checkBox_vss.setChecked(true);
                }else {
                    checkBox_vss.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgOprremark().contains("RSSC")){
                    checkBox_rssc.setChecked(true);
                }else {
                    checkBox_rssc.setChecked(false);
                }

                spinner_vremark.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                et_vremark.setText(sampleGerminationInfo.getQcgSgtvremark());
                et_vremark.setVisibility(View.VISIBLE);
                if (sampleGerminationInfo.getQcgSgtremark()!=null){
                    et_remark.setText(sampleGerminationInfo.getQcgSgtremark());
                }

                if (sampleGerminationInfo.getQcgSgtimage()!=null && !sampleGerminationInfo.getQcgSgtimage().equalsIgnoreCase("")){
                    Glide.with(GerminationReadingFormActivity.this)
                            .load(Uri.parse(AppConfig.IMAGE_URL + sampleGerminationInfo.getQcgSgtimage()))
                            .into(iv_photo);
                    button_photo.setVisibility(View.GONE);
                }else {
                    ll_germpPhoto.setVisibility(View.GONE);
                }
            }else {
                et_normalseed_rep1.setEnabled(true);
                et_normalseed_rep2.setEnabled(true);
                et_normalseed_rep3.setEnabled(true);
                et_normalseed_rep4.setEnabled(true);
                et_normalseed_rep5.setEnabled(true);
                et_normalseed_rep6.setEnabled(true);
                et_normalseed_rep7.setEnabled(true);
                et_normalseed_rep8.setEnabled(true);
                et_normal_rep1.setEnabled(true);
                et_abnormal_rep1.setEnabled(true);
                et_hardfug_rep1.setEnabled(true);
                et_dead_rep1.setEnabled(true);
                et_normal_rep2.setEnabled(true);
                et_abnormal_rep2.setEnabled(true);
                et_hardfug_rep2.setEnabled(true);
                et_dead_rep2.setEnabled(true);
                et_normal_rep3.setEnabled(true);
                et_abnormal_rep3.setEnabled(true);
                et_hardfug_rep3.setEnabled(true);
                et_dead_rep3.setEnabled(true);
                et_normal_rep4.setEnabled(true);
                et_abnormal_rep4.setEnabled(true);
                et_hardfug_rep4.setEnabled(true);
                et_dead_rep4.setEnabled(true);
                et_normal_rep5.setEnabled(true);
                et_abnormal_rep5.setEnabled(true);
                et_hardfug_rep5.setEnabled(true);
                et_dead_rep5.setEnabled(true);
                et_normal_rep6.setEnabled(true);
                et_abnormal_rep6.setEnabled(true);
                et_hardfug_rep6.setEnabled(true);
                et_dead_rep6.setEnabled(true);
                et_normal_rep7.setEnabled(true);
                et_abnormal_rep7.setEnabled(true);
                et_hardfug_rep7.setEnabled(true);
                et_dead_rep7.setEnabled(true);
                et_normal_rep8.setEnabled(true);
                et_abnormal_rep8.setEnabled(true);
                et_hardfug_rep8.setEnabled(true);
                et_dead_rep8.setEnabled(true);
                spinner_vremark.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                button_photo.setVisibility(View.VISIBLE);
                ll_germpPhoto.setVisibility(View.VISIBLE);
                et_vremark.setVisibility(View.GONE);
                String[] vremark_list = {"Good","Average","Poor"};
                ArrayAdapter<String> adapter_vremark = new ArrayAdapter<>(GerminationReadingFormActivity.this, android.R.layout.simple_spinner_item, vremark_list);
                adapter_vremark.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_vremark.setAdapter(adapter_vremark);


            }

            ll_medium.setVisibility(View.GONE);
            if (rep_countsgt.equalsIgnoreCase("1")) {
                ll_replication2.setVisibility(View.GONE);
                ll_replication2_1.setVisibility(View.GONE);
                ll_replication2_2.setVisibility(View.GONE);
                ll_block34.setVisibility(View.GONE);
                ll_block5678.setVisibility(View.GONE);
                ll_normalrep2.setVisibility(View.GONE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                et_totalnoofseeds.setText(String.valueOf(sampleGerminationInfo.getNoofseedinonerep()));
            }else if (rep_countsgt.equalsIgnoreCase("2")){
                ll_replication2.setVisibility(View.VISIBLE);
                ll_replication2_1.setVisibility(View.VISIBLE);
                ll_replication2_2.setVisibility(View.VISIBLE);
                ll_block34.setVisibility(View.GONE);
                ll_block5678.setVisibility(View.GONE);
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep*2;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }else if (rep_countsgt.equalsIgnoreCase("4")){
                ll_replication2.setVisibility(View.VISIBLE);
                ll_replication2_1.setVisibility(View.VISIBLE);
                ll_replication2_2.setVisibility(View.VISIBLE);
                ll_block34.setVisibility(View.VISIBLE);
                ll_block5678.setVisibility(View.GONE);
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep*4;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }else if (rep_countsgt.equalsIgnoreCase("8")){
                ll_replication2.setVisibility(View.VISIBLE);
                ll_replication2_1.setVisibility(View.VISIBLE);
                ll_replication2_2.setVisibility(View.VISIBLE);
                ll_block34.setVisibility(View.VISIBLE);
                ll_block5678.setVisibility(View.VISIBLE);
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.VISIBLE);
                ll_normalrep78.setVisibility(View.VISIBLE);
                int totalrep = noofseedsinonerep*8;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }
        }
    }

    private void init() {
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GerminationReadingFormActivity.this, GerminationHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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

        String noofrepsgt = et_noofrepsgt.getText().toString().trim();
        String noofrepsgtd = et_noofrepsgtd.getText().toString().trim();
        String noofrepfgt = et_noofrepfgt.getText().toString().trim();
        int germtype = radioGrpGerm.getCheckedRadioButtonId();
        RadioButton type = findViewById(germtype);
        String gmmtype = type.getText().toString();
        if (gmmtype.equalsIgnoreCase("FGT")) {
            finalNoofseedonerep = sampleGerminationInfo.getNoofseedinonerepfgt();
        }else {
            finalNoofseedonerep = sampleGerminationInfo.getNoofseedinonerep();
        }
            et_normal_rep1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                       String normal_rep1 = String.valueOf(charSequence);
                        if (!normal_rep1.equals("")) {
                            cal_normalper();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
            });
            et_abnormal_rep1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String abnormal_rep1 = String.valueOf(charSequence);
                    if (!abnormal_rep1.equals("")) {
                        cal_abnormalper();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            if (!gmmtype.equalsIgnoreCase("FGT")) {
                et_hardfug_rep1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String hardfug_rep1 = String.valueOf(charSequence);
                        if (!hardfug_rep1.equals("")) {
                            cal_hardfugper();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
            et_dead_rep1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String dead_rep1 = String.valueOf(charSequence);
                    if (!dead_rep1.equals("")) {
                        cal_deadper();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            et_normal_rep2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String normal_rep1 = et_normal_rep1.getText().toString().trim();
                    String normal_rep2 = String.valueOf(charSequence);
                    if(!normal_rep1.equals("") && !normal_rep2.equals("")) {
                        cal_normalper();
                    }/*else {
                        Toast.makeText(getApplicationContext(), "Enter previous all replication's data", Toast.LENGTH_SHORT).show();
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            et_abnormal_rep2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String abnormal_rep1 = et_abnormal_rep1.getText().toString().trim();
                    String abnormal_rep2 = String.valueOf(charSequence);
                    if(!abnormal_rep1.equals("") && !abnormal_rep2.equals("")) {
                        cal_abnormalper();
                    }/*else {
                        Toast.makeText(getApplicationContext(), "Enter previous all replication's data", Toast.LENGTH_LONG).show();
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            if (!gmmtype.equalsIgnoreCase("FGT")) {
                et_hardfug_rep2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String hardfug_rep1 = et_hardfug_rep1.getText().toString().trim();
                        String hardfug_rep2 = String.valueOf(charSequence);
                        if (!hardfug_rep1.equals("") && !hardfug_rep2.equals("")) {
                            cal_hardfugper();
                        } /*else {
                            Toast.makeText(getApplicationContext(), "Enter previous all replication's data", Toast.LENGTH_LONG).show();
                        }*/
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
            et_dead_rep2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String dead_rep1 = et_dead_rep1.getText().toString().trim();
                    String dead_rep2 = String.valueOf(charSequence);
                    if(!dead_rep1.equals("") && !dead_rep2.equals("")) {
                        cal_deadper();
                    }/*else {
                        Toast.makeText(getApplicationContext(), "Enter previous all replication's data", Toast.LENGTH_LONG).show();
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            et_normal_rep3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String normal_rep1 = et_normal_rep1.getText().toString().trim();
                    String normal_rep2 = et_normal_rep2.getText().toString().trim();
                    String normal_rep3 = et_normal_rep3.getText().toString().trim();
                    if (!normal_rep1.equals("") && !normal_rep2.equals("") && !normal_rep3.equals("")) {
                        cal_normalper();
                    } /*else {
                        Toast.makeText(getApplicationContext(), "Enter previous all replication's data", Toast.LENGTH_LONG).show();
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            et_abnormal_rep3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String abnormal_rep1 = et_abnormal_rep1.getText().toString().trim();
                    String abnormal_rep2 = et_abnormal_rep2.getText().toString().trim();
                    String abnormal_rep3 = et_abnormal_rep3.getText().toString().trim();

                    if(!abnormal_rep1.equals("") && !abnormal_rep2.equals("") && !abnormal_rep3.equals("")) {
                        cal_abnormalper();
                    } /*else {
                        Toast.makeText(getApplicationContext(), "Enter previous all replication's data", Toast.LENGTH_LONG).show();
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        if (!gmmtype.equalsIgnoreCase("FGT")) {
            et_hardfug_rep3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String hardfug_rep1 = et_hardfug_rep1.getText().toString().trim();
                    String hardfug_rep2 = et_hardfug_rep2.getText().toString().trim();
                    String hardfug_rep3 = et_hardfug_rep3.getText().toString().trim();

                    if (!hardfug_rep1.equals("") && !hardfug_rep2.equals("") && !hardfug_rep3.equals("")) {
                        cal_hardfugper();
                    } /*else {
                        Toast.makeText(getApplicationContext(), "Enter previous all replication's data", Toast.LENGTH_LONG).show();
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

            et_dead_rep3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String dead_rep1 = et_dead_rep1.getText().toString().trim();
                    String dead_rep2 = et_dead_rep2.getText().toString().trim();
                    String dead_rep3 = et_dead_rep3.getText().toString().trim();
                    if(!dead_rep1.equals("") && !dead_rep2.equals("") && !dead_rep3.equals("")) {
                        cal_deadper();
                    } /*else {
                        Toast.makeText(getApplicationContext(), "Enter previous all replication's data", Toast.LENGTH_LONG).show();
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            et_normal_rep4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String normal_rep4 = String.valueOf(charSequence);
                    if (!normal_rep4.equals("")){
                        String normal_rep1 = et_normal_rep1.getText().toString().trim();
                        String normal_rep2 = et_normal_rep2.getText().toString().trim();
                        String normal_rep3 = et_normal_rep3.getText().toString().trim();
                        if (!normal_rep1.equals("") && !normal_rep2.equals("") && !normal_rep3.equals("")) {
                            cal_normalper();
                        } /*else {
                            Toast.makeText(getApplicationContext(), "Enter previous all replication's data", Toast.LENGTH_LONG).show();
                        }*/
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            et_abnormal_rep4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String abnormal_rep1 = et_abnormal_rep1.getText().toString().trim();
                    String abnormal_rep2 = et_abnormal_rep2.getText().toString().trim();
                    String abnormal_rep3 = et_abnormal_rep3.getText().toString().trim();
                    String abnormal_rep4 = String.valueOf(charSequence);
                    if(!abnormal_rep1.equals("") && !abnormal_rep2.equals("") && !abnormal_rep3.equals("") && !abnormal_rep4.equals("")) {
                        cal_abnormalper();
                    } /*else {
                        Toast.makeText(getApplicationContext(), "Enter previous all replication's data", Toast.LENGTH_LONG).show();
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            if (!gmmtype.equalsIgnoreCase("FGT")) {
                et_hardfug_rep4.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String hardfug_rep1 = et_hardfug_rep1.getText().toString().trim();
                        String hardfug_rep2 = et_hardfug_rep2.getText().toString().trim();
                        String hardfug_rep3 = et_hardfug_rep3.getText().toString().trim();
                        String hardfug_rep4 = String.valueOf(charSequence);
                        if (!hardfug_rep1.equals("") && !hardfug_rep2.equals("") && !hardfug_rep3.equals("") && !hardfug_rep4.equals("")) {
                            cal_hardfugper();
                        } /*else {
                            Toast.makeText(getApplicationContext(), "Enter previous all replication's data", Toast.LENGTH_LONG).show();
                        }*/
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
            et_dead_rep4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String dead_rep1 = et_dead_rep1.getText().toString().trim();
                    String dead_rep2 = et_dead_rep2.getText().toString().trim();
                    String dead_rep3 = et_dead_rep3.getText().toString().trim();
                    String dead_rep4 = String.valueOf(charSequence);
                    if(!dead_rep1.equals("") && !dead_rep2.equals("") && !dead_rep3.equals("") && !dead_rep4.equals("")) {
                        cal_deadper();
                    } /*else {
                        Toast.makeText(getApplicationContext(), "Enter previous all replication's data", Toast.LENGTH_LONG).show();
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            et_normal_rep8.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String normal_rep1 = et_normal_rep1.getText().toString().trim();
                    String normal_rep2 = et_normal_rep2.getText().toString().trim();
                    String normal_rep3 = et_normal_rep3.getText().toString().trim();
                    String normal_rep4 = et_normal_rep4.getText().toString().trim();
                    String normal_rep5 = et_normal_rep5.getText().toString().trim();
                    String normal_rep6 = et_normal_rep6.getText().toString().trim();
                    String normal_rep7 = et_normal_rep7.getText().toString().trim();
                    String normal_rep8 = String.valueOf(charSequence);
                    if(!normal_rep1.equals("") && !normal_rep2.equals("") && !normal_rep3.equals("") && !normal_rep4.equals("") && !normal_rep5.equals("") && !normal_rep6.equals("") && !normal_rep7.equals("") && !normal_rep8.equals("")) {
                        float NORREP1 = Float.parseFloat(normal_rep1);
                        float NORREP2 = Float.parseFloat(normal_rep2);
                        float NORREP3 = Float.parseFloat(normal_rep3);
                        float NORREP4 = Float.parseFloat(normal_rep4);
                        float NORREP5 = Float.parseFloat(normal_rep5);
                        float NORREP6 = Float.parseFloat(normal_rep6);
                        float NORREP7 = Float.parseFloat(normal_rep7);
                        float NORREP8 = Float.parseFloat(normal_rep8);
                        float NORPER = (NORREP1+NORREP2+NORREP3+NORREP4+NORREP5+NORREP6+NORREP7+NORREP8) / 8;
                        float meanper = (NORPER*100)/finalNoofseedonerep;
                        int perround = Math.round(meanper);
                        et_normalseedper.setText(String.valueOf(perround));
                    } /*else {
                        Toast.makeText(getApplicationContext(), "Enter previous all replication's data", Toast.LENGTH_LONG).show();
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            et_abnormal_rep8.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String abnormal_rep1 = et_abnormal_rep1.getText().toString().trim();
                    String abnormal_rep2 = et_abnormal_rep2.getText().toString().trim();
                    String abnormal_rep3 = et_abnormal_rep3.getText().toString().trim();
                    String abnormal_rep4 = et_abnormal_rep4.getText().toString().trim();
                    String abnormal_rep5 = et_abnormal_rep5.getText().toString().trim();
                    String abnormal_rep6 = et_abnormal_rep6.getText().toString().trim();
                    String abnormal_rep7 = et_abnormal_rep7.getText().toString().trim();
                    String abnormal_rep8 = String.valueOf(charSequence);
                    if(!abnormal_rep1.equals("") && !abnormal_rep2.equals("") && !abnormal_rep3.equals("") && !abnormal_rep4.equals("") && !abnormal_rep5.equals("") && !abnormal_rep6.equals("") && !abnormal_rep7.equals("") && !abnormal_rep8.equals("")) {
                        float ABNORREP1 = Float.parseFloat(abnormal_rep1);
                        float ABNORREP2 = Float.parseFloat(abnormal_rep2);
                        float ABNORREP3 = Float.parseFloat(abnormal_rep3);
                        float ABNORREP4 = Float.parseFloat(abnormal_rep4);
                        float ABNORREP5 = Float.parseFloat(abnormal_rep5);
                        float ABNORREP6 = Float.parseFloat(abnormal_rep6);
                        float ABNORREP7 = Float.parseFloat(abnormal_rep7);
                        float ABNORREP8 = Float.parseFloat(abnormal_rep8);
                        float ABNORPER = (ABNORREP1+ABNORREP2+ABNORREP3+ABNORREP4+ABNORREP5+ABNORREP6+ABNORREP7+ABNORREP8) / 8;
                        float meanper = (ABNORPER*100)/finalNoofseedonerep;
                        int perround = Math.round(meanper);
                        et_abnormalseedper.setText(String.valueOf(perround));
                    } /*else {
                        Toast.makeText(getApplicationContext(), "Enter previous all replication's data", Toast.LENGTH_LONG).show();
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            if (!gmmtype.equalsIgnoreCase("FGT")) {
                et_hardfug_rep8.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String hardfug_rep1 = et_hardfug_rep1.getText().toString().trim();
                        String hardfug_rep2 = et_hardfug_rep2.getText().toString().trim();
                        String hardfug_rep3 = et_hardfug_rep3.getText().toString().trim();
                        String hardfug_rep4 = et_hardfug_rep4.getText().toString().trim();
                        String hardfug_rep5 = et_hardfug_rep5.getText().toString().trim();
                        String hardfug_rep6 = et_hardfug_rep6.getText().toString().trim();
                        String hardfug_rep7 = et_hardfug_rep7.getText().toString().trim();
                        String hardfug_rep8 = String.valueOf(charSequence);
                        if (!hardfug_rep1.equals("") && !hardfug_rep2.equals("") && !hardfug_rep3.equals("") && !hardfug_rep4.equals("") && !hardfug_rep5.equals("") && !hardfug_rep6.equals("") && !hardfug_rep7.equals("") && !hardfug_rep8.equals("")) {
                            float HARDREP1 = Float.parseFloat(hardfug_rep1);
                            float HARDREP2 = Float.parseFloat(hardfug_rep2);
                            float HARDREP3 = Float.parseFloat(hardfug_rep3);
                            float HARDREP4 = Float.parseFloat(hardfug_rep4);
                            float HARDREP5 = Float.parseFloat(hardfug_rep5);
                            float HARDREP6 = Float.parseFloat(hardfug_rep6);
                            float HARDREP7 = Float.parseFloat(hardfug_rep7);
                            float HARDREP8 = Float.parseFloat(hardfug_rep8);
                            float HARDPER = (HARDREP1 + HARDREP2 + HARDREP3 + HARDREP4 + HARDREP5 + HARDREP6 + HARDREP7 + HARDREP8) / 8;
                            float noofseedonerep = sampleGerminationInfo.getNoofseedinonerepfgt();
                            float meanper = (HARDPER*100)/noofseedonerep;
                            int perround = Math.round(meanper);
                            et_hardseedper.setText(String.valueOf(perround));
                        } /*else {
                            Toast.makeText(getApplicationContext(), "Enter previous all replication's data", Toast.LENGTH_LONG).show();
                        }*/
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
            et_dead_rep8.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String dead_rep1 = et_dead_rep1.getText().toString().trim();
                    String dead_rep2 = et_dead_rep2.getText().toString().trim();
                    String dead_rep3 = et_dead_rep3.getText().toString().trim();
                    String dead_rep4 = et_dead_rep4.getText().toString().trim();
                    String dead_rep5 = et_dead_rep5.getText().toString().trim();
                    String dead_rep6 = et_dead_rep6.getText().toString().trim();
                    String dead_rep7 = et_dead_rep7.getText().toString().trim();
                    String dead_rep8 = String.valueOf(charSequence);
                    if(!dead_rep1.equals("") && !dead_rep2.equals("") && !dead_rep3.equals("") && !dead_rep4.equals("") && !dead_rep5.equals("") && !dead_rep6.equals("") && !dead_rep7.equals("") && !dead_rep8.equals("")) {
                        float DEADREP1 = Float.parseFloat(dead_rep1);
                        float DEADREP2 = Float.parseFloat(dead_rep2);
                        float DEADREP3 = Float.parseFloat(dead_rep3);
                        float DEADREP4 = Float.parseFloat(dead_rep4);
                        float DEADREP5 = Float.parseFloat(dead_rep5);
                        float DEADREP6 = Float.parseFloat(dead_rep6);
                        float DEADREP7 = Float.parseFloat(dead_rep7);
                        float DEADREP8 = Float.parseFloat(dead_rep8);
                        float DEADPER = (DEADREP1 + DEADREP2 + DEADREP3 + DEADREP4 + DEADREP5 + DEADREP6 + DEADREP7 + DEADREP8) / 8;
                        float meanper = (DEADPER*100)/finalNoofseedonerep;
                        int perround = Math.round(meanper);
                        et_deadseedper.setText(String.valueOf(perround));
                    } /*else {
                        Toast.makeText(getApplicationContext(), "Enter previous all replication's data", Toast.LENGTH_LONG).show();
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
                String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
                String sampleno = tv_sampleno.getText().toString().trim();

                int germtype = radioGrpGerm.getCheckedRadioButtonId();
                RadioButton type = findViewById(germtype);
                String gmmtype = type.getText().toString();

                int germsobrstype = radioGrpObrs.getCheckedRadioButtonId();
                RadioButton obrstype = findViewById(germsobrstype);
                String obrstype1 = obrstype.getText().toString();

                String totalnoofseeds = et_totalnoofseeds.getText().toString().trim();
                String doesgt = et_doesgt.getText().toString().trim();
                String doesgtd = et_doesgtd.getText().toString().trim();
                String doefgt = et_doefgt.getText().toString().trim();
                String noofrepsgt = et_noofrepsgt.getText().toString().trim();
                String noofrepsgtd = et_noofrepsgtd.getText().toString().trim();
                String noofrepfgt = et_noofrepfgt.getText().toString().trim();
                String fgtmedia = et_media.getText().toString().trim();

                String normalseed_rep1 = et_normalseed_rep1.getText().toString().trim();
                String normalseed_rep2 = et_normalseed_rep2.getText().toString().trim();
                String normalseed_rep3 = et_normalseed_rep3.getText().toString().trim();
                String normalseed_rep4 = et_normalseed_rep4.getText().toString().trim();
                String normalseed_rep5 = et_normalseed_rep5.getText().toString().trim();
                String normalseed_rep6 = et_normalseed_rep6.getText().toString().trim();
                String normalseed_rep7 = et_normalseed_rep7.getText().toString().trim();
                String normalseed_rep8 = et_normalseed_rep8.getText().toString().trim();

                String normal_rep1 = et_normal_rep1.getText().toString().trim();
                String abnormal_rep1 = et_abnormal_rep1.getText().toString().trim();
                String hardfug_rep1 = et_hardfug_rep1.getText().toString().trim();
                String dead_rep1 = et_dead_rep1.getText().toString().trim();

                String normal_rep2 = et_normal_rep2.getText().toString().trim();
                String abnormal_rep2 = et_abnormal_rep2.getText().toString().trim();
                String hardfug_rep2 = et_hardfug_rep2.getText().toString().trim();
                String dead_rep2 = et_dead_rep2.getText().toString().trim();

                String normal_rep3 = et_normal_rep3.getText().toString().trim();
                String abnormal_rep3 = et_abnormal_rep3.getText().toString().trim();
                String hardfug_rep3 = et_hardfug_rep3.getText().toString().trim();
                String dead_rep3 = et_dead_rep3.getText().toString().trim();

                String normal_rep4 = et_normal_rep4.getText().toString().trim();
                String abnormal_rep4 = et_abnormal_rep4.getText().toString().trim();
                String hardfug_rep4 = et_hardfug_rep4.getText().toString().trim();
                String dead_rep4 = et_dead_rep4.getText().toString().trim();

                String normal_rep5 = et_normal_rep5.getText().toString().trim();
                String abnormal_rep5 = et_abnormal_rep5.getText().toString().trim();
                String hardfug_rep5 = et_hardfug_rep5.getText().toString().trim();
                String dead_rep5 = et_dead_rep5.getText().toString().trim();

                String normal_rep6 = et_normal_rep6.getText().toString().trim();
                String abnormal_rep6 = et_abnormal_rep6.getText().toString().trim();
                String hardfug_rep6 = et_hardfug_rep6.getText().toString().trim();
                String dead_rep6 = et_dead_rep6.getText().toString().trim();

                String normal_rep7 = et_normal_rep7.getText().toString().trim();
                String abnormal_rep7 = et_abnormal_rep7.getText().toString().trim();
                String hardfug_rep7 = et_hardfug_rep7.getText().toString().trim();
                String dead_rep7 = et_dead_rep7.getText().toString().trim();

                String normal_rep8 = et_normal_rep8.getText().toString().trim();
                String abnormal_rep8 = et_abnormal_rep8.getText().toString().trim();
                String hardfug_rep8 = et_hardfug_rep8.getText().toString().trim();
                String dead_rep8 = et_dead_rep8.getText().toString().trim();

                String normalseedper = et_normalseedper.getText().toString().trim();
                String abnormalseedper = et_abnormalseedper.getText().toString().trim();
                String hardseedper = et_hardseedper.getText().toString().trim();
                String deadseedper = et_deadseedper.getText().toString().trim();

                String vegremark = spinner_vremark.getSelectedItem().toString().trim();
                String techremark = "";

                String retest_reason = et_retest_reason.getText().toString().trim();
                int rdsample = radiosample.getCheckedRadioButtonId();
                RadioButton rdsamp = findViewById(rdsample);
                String retesttype = rdsamp.getText().toString();

                if (checkBox_rssc.isChecked()) {
                    if (techremark.equalsIgnoreCase("")) {
                        techremark = "RSSC";
                    }else {
                        techremark = techremark+",RSSC";
                    }
                }
                if (checkBox_prm.isChecked()) {
                    if (techremark.equalsIgnoreCase("")) {
                        techremark = "PRM";
                    }else {
                        techremark = techremark+",PRM";
                    }

                }
                if (checkBox_vss.isChecked()) {
                    if (techremark.equalsIgnoreCase("")) {
                        techremark = "VSS";
                    }else {
                        techremark = techremark+",VSS";
                    }
                }

                if(checkBox_retest.isChecked()) {
                    if (retest_reason.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(), "Enter Re-Test Reason", Toast.LENGTH_LONG).show();
                    }else {
                        retest = "Yes";
                        confirmAlertSGT(userid, scode, sampleno, noofrepsgt, gmmtype, obrstype1, normalseed_rep1, normalseed_rep2, normalseed_rep3, normalseed_rep4, normalseed_rep5, normalseed_rep6, normalseed_rep7, normalseed_rep8, doesgt);
                    }
                } else {
                    retest = "No";
                    if (gmmtype.equalsIgnoreCase("SGT")) {
                        if (noofrepsgt.equals("1")) {
                            if (obrstype1.equalsIgnoreCase("First Count")) {
                                if (normalseed_rep1.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Enter all data", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertSGT(userid, scode, sampleno, noofrepsgt, gmmtype, obrstype1, normalseed_rep1, normalseed_rep2, normalseed_rep3, normalseed_rep4, normalseed_rep5, normalseed_rep6, normalseed_rep7, normalseed_rep8, doesgt);
                                    //submitSGTFC(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,normalseed_rep1,normalseed_rep2,normalseed_rep3,normalseed_rep4,normalseed_rep5,normalseed_rep6,normalseed_rep7,normalseed_rep8,doesgt);
                                }
                            } else {
                                double reptotalseeds = Double.parseDouble(normal_rep1) + Double.parseDouble(abnormal_rep1) + Double.parseDouble(dead_rep1) + Double.parseDouble(hardfug_rep1);
                                if (normal_rep1.equals("") || abnormal_rep1.equals("") || dead_rep1.equals("") || hardfug_rep1.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertSGTFC(userid, scode, sampleno, noofrepsgt, gmmtype, obrstype1, vegremark, techremark, normal_rep1, abnormal_rep1, hardfug_rep1, dead_rep1, normal_rep2, abnormal_rep2, hardfug_rep2, dead_rep2, normal_rep3, abnormal_rep3, hardfug_rep3, dead_rep3, normal_rep4, abnormal_rep4, hardfug_rep4, dead_rep4, normal_rep5, abnormal_rep5, hardfug_rep5, dead_rep5, normal_rep6, abnormal_rep6, hardfug_rep6, dead_rep6, normal_rep7, abnormal_rep7, hardfug_rep7, dead_rep7, normal_rep8, abnormal_rep8, hardfug_rep8, dead_rep8, normalseedper, abnormalseedper, hardseedper, deadseedper, doesgt);
                                    //submitSGTFinalCount(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,vegremark,techremark,normal_rep1,abnormal_rep1,hardfug_rep1,dead_rep1,normal_rep2,abnormal_rep2,hardfug_rep2,dead_rep2,normal_rep3,abnormal_rep3,hardfug_rep3,dead_rep3,normal_rep4,abnormal_rep4,hardfug_rep4,dead_rep4,normal_rep5,abnormal_rep5,hardfug_rep5,dead_rep5,normal_rep6,abnormal_rep6,hardfug_rep6,dead_rep6,normal_rep7,abnormal_rep7,hardfug_rep7,dead_rep7,normal_rep8,abnormal_rep8,hardfug_rep8,dead_rep8,normalseedper,abnormalseedper,hardseedper,deadseedper,doesgt);
                                }
                            }
                        } else if (noofrepsgt.equals("2")) {
                            if (obrstype1.equalsIgnoreCase("First Count")) {
                                if (normalseed_rep1.equals("") || normalseed_rep2.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Enter all data", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertSGT(userid, scode, sampleno, noofrepsgt, gmmtype, obrstype1, normalseed_rep1, normalseed_rep2, normalseed_rep3, normalseed_rep4, normalseed_rep5, normalseed_rep6, normalseed_rep7, normalseed_rep8, doesgt);
                                    //submitSGTFC(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,normalseed_rep1,normalseed_rep2,normalseed_rep3,normalseed_rep4,normalseed_rep5,normalseed_rep6,normalseed_rep7,normalseed_rep8,doesgt);
                                }
                            } else {
                                double reptotalseeds = Double.parseDouble(normal_rep1) + Double.parseDouble(abnormal_rep1) + Double.parseDouble(dead_rep1) + Double.parseDouble(hardfug_rep1);
                                double reptotalseeds2 = Double.parseDouble(normal_rep2) + Double.parseDouble(abnormal_rep2) + Double.parseDouble(dead_rep2) + Double.parseDouble(hardfug_rep2);
                                if (normal_rep1.equals("") || abnormal_rep1.equals("") || dead_rep1.equals("") || hardfug_rep1.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep2.equals("") || abnormal_rep2.equals("") || dead_rep2.equals("") || hardfug_rep2.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #2 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds2 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #2 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertSGTFC(userid, scode, sampleno, noofrepsgt, gmmtype, obrstype1, vegremark, techremark, normal_rep1, abnormal_rep1, hardfug_rep1, dead_rep1, normal_rep2, abnormal_rep2, hardfug_rep2, dead_rep2, normal_rep3, abnormal_rep3, hardfug_rep3, dead_rep3, normal_rep4, abnormal_rep4, hardfug_rep4, dead_rep4, normal_rep5, abnormal_rep5, hardfug_rep5, dead_rep5, normal_rep6, abnormal_rep6, hardfug_rep6, dead_rep6, normal_rep7, abnormal_rep7, hardfug_rep7, dead_rep7, normal_rep8, abnormal_rep8, hardfug_rep8, dead_rep8, normalseedper, abnormalseedper, hardseedper, deadseedper, doesgt);
                                    //submitSGTFinalCount(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,vegremark,techremark,normal_rep1,abnormal_rep1,hardfug_rep1,dead_rep1,normal_rep2,abnormal_rep2,hardfug_rep2,dead_rep2,normal_rep3,abnormal_rep3,hardfug_rep3,dead_rep3,normal_rep4,abnormal_rep4,hardfug_rep4,dead_rep4,normal_rep5,abnormal_rep5,hardfug_rep5,dead_rep5,normal_rep6,abnormal_rep6,hardfug_rep6,dead_rep6,normal_rep7,abnormal_rep7,hardfug_rep7,dead_rep7,normal_rep8,abnormal_rep8,hardfug_rep8,dead_rep8,normalseedper,abnormalseedper,hardseedper,deadseedper,doesgt);
                                }
                            }
                        } else if (noofrepsgt.equals("4")) {
                            if (obrstype1.equalsIgnoreCase("First Count")) {
                                if (normalseed_rep1.equals("") || normalseed_rep2.equals("") || normalseed_rep3.equals("") || normalseed_rep4.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Enter all data", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertSGT(userid, scode, sampleno, noofrepsgt, gmmtype, obrstype1, normalseed_rep1, normalseed_rep2, normalseed_rep3, normalseed_rep4, normalseed_rep5, normalseed_rep6, normalseed_rep7, normalseed_rep8, doesgt);
                                    //submitSGTFC(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,normalseed_rep1,normalseed_rep2,normalseed_rep3,normalseed_rep4,normalseed_rep5,normalseed_rep6,normalseed_rep7,normalseed_rep8,doesgt);
                                }
                            } else {
                                double reptotalseeds = Double.parseDouble(normal_rep1) + Double.parseDouble(abnormal_rep1) + Double.parseDouble(dead_rep1) + Double.parseDouble(hardfug_rep1);
                                double reptotalseeds2 = Double.parseDouble(normal_rep2) + Double.parseDouble(abnormal_rep2) + Double.parseDouble(dead_rep2) + Double.parseDouble(hardfug_rep2);
                                double reptotalseeds3 = Double.parseDouble(normal_rep3) + Double.parseDouble(abnormal_rep3) + Double.parseDouble(dead_rep3) + Double.parseDouble(hardfug_rep3);
                                double reptotalseeds4 = Double.parseDouble(normal_rep4) + Double.parseDouble(abnormal_rep4) + Double.parseDouble(dead_rep4) + Double.parseDouble(hardfug_rep4);
                                if (normal_rep1.equals("") || abnormal_rep1.equals("") || dead_rep1.equals("") || hardfug_rep1.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep2.equals("") || abnormal_rep2.equals("") || dead_rep2.equals("") || hardfug_rep2.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #2 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds2 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #2 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep3.equals("") || abnormal_rep3.equals("") || dead_rep3.equals("") || hardfug_rep3.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #3 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds3 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #3 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep4.equals("") || abnormal_rep4.equals("") || dead_rep4.equals("") || hardfug_rep4.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #4 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds4 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #4 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertSGTFC(userid, scode, sampleno, noofrepsgt, gmmtype, obrstype1, vegremark, techremark, normal_rep1, abnormal_rep1, hardfug_rep1, dead_rep1, normal_rep2, abnormal_rep2, hardfug_rep2, dead_rep2, normal_rep3, abnormal_rep3, hardfug_rep3, dead_rep3, normal_rep4, abnormal_rep4, hardfug_rep4, dead_rep4, normal_rep5, abnormal_rep5, hardfug_rep5, dead_rep5, normal_rep6, abnormal_rep6, hardfug_rep6, dead_rep6, normal_rep7, abnormal_rep7, hardfug_rep7, dead_rep7, normal_rep8, abnormal_rep8, hardfug_rep8, dead_rep8, normalseedper, abnormalseedper, hardseedper, deadseedper, doesgt);
                                    //submitSGTFinalCount(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,vegremark,techremark,normal_rep1,abnormal_rep1,hardfug_rep1,dead_rep1,normal_rep2,abnormal_rep2,hardfug_rep2,dead_rep2,normal_rep3,abnormal_rep3,hardfug_rep3,dead_rep3,normal_rep4,abnormal_rep4,hardfug_rep4,dead_rep4,normal_rep5,abnormal_rep5,hardfug_rep5,dead_rep5,normal_rep6,abnormal_rep6,hardfug_rep6,dead_rep6,normal_rep7,abnormal_rep7,hardfug_rep7,dead_rep7,normal_rep8,abnormal_rep8,hardfug_rep8,dead_rep8,normalseedper,abnormalseedper,hardseedper,deadseedper,doesgt);
                                }
                            }
                        } else if (noofrepsgt.equals("8")) {
                            if (obrstype1.equalsIgnoreCase("First Count")) {
                                if (normalseed_rep1.equals("") || normalseed_rep2.equals("") || normalseed_rep3.equals("") || normalseed_rep4.equals("") || normalseed_rep5.equals("") || normalseed_rep6.equals("") || normalseed_rep7.equals("") || normalseed_rep8.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Enter all data", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertSGT(userid, scode, sampleno, noofrepsgt, gmmtype, obrstype1, normalseed_rep1, normalseed_rep2, normalseed_rep3, normalseed_rep4, normalseed_rep5, normalseed_rep6, normalseed_rep7, normalseed_rep8, doesgt);
                                    //submitSGTFC(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,normalseed_rep1,normalseed_rep2,normalseed_rep3,normalseed_rep4,normalseed_rep5,normalseed_rep6,normalseed_rep7,normalseed_rep8,doesgt);
                                }
                            } else {
                                double reptotalseeds = Double.parseDouble(normal_rep1) + Double.parseDouble(abnormal_rep1) + Double.parseDouble(dead_rep1) + Double.parseDouble(hardfug_rep1);
                                double reptotalseeds2 = Double.parseDouble(normal_rep2) + Double.parseDouble(abnormal_rep2) + Double.parseDouble(dead_rep2) + Double.parseDouble(hardfug_rep2);
                                double reptotalseeds3 = Double.parseDouble(normal_rep3) + Double.parseDouble(abnormal_rep3) + Double.parseDouble(dead_rep3) + Double.parseDouble(hardfug_rep3);
                                double reptotalseeds4 = Double.parseDouble(normal_rep4) + Double.parseDouble(abnormal_rep4) + Double.parseDouble(dead_rep4) + Double.parseDouble(hardfug_rep4);
                                double reptotalseeds5 = Double.parseDouble(normal_rep5) + Double.parseDouble(abnormal_rep5) + Double.parseDouble(dead_rep5) + Double.parseDouble(hardfug_rep5);
                                double reptotalseeds6 = Double.parseDouble(normal_rep6) + Double.parseDouble(abnormal_rep6) + Double.parseDouble(dead_rep6) + Double.parseDouble(hardfug_rep6);
                                double reptotalseeds7 = Double.parseDouble(normal_rep7) + Double.parseDouble(abnormal_rep7) + Double.parseDouble(dead_rep7) + Double.parseDouble(hardfug_rep7);
                                double reptotalseeds8 = Double.parseDouble(normal_rep8) + Double.parseDouble(abnormal_rep8) + Double.parseDouble(dead_rep8) + Double.parseDouble(hardfug_rep8);
                                if (normal_rep1.equals("") || abnormal_rep1.equals("") || dead_rep1.equals("") || hardfug_rep1.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep2.equals("") || abnormal_rep2.equals("") || dead_rep2.equals("") || hardfug_rep2.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #2 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds2 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #2 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep3.equals("") || abnormal_rep3.equals("") || dead_rep3.equals("") || hardfug_rep3.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #3 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds3 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #3 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep4.equals("") || abnormal_rep4.equals("") || dead_rep4.equals("") || hardfug_rep4.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #4 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds4 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #4 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep5.equals("") || abnormal_rep5.equals("") || dead_rep5.equals("") || hardfug_rep5.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #5 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds5 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #5 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep6.equals("") || abnormal_rep6.equals("") || dead_rep6.equals("") || hardfug_rep6.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #6 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds6 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #6 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep7.equals("") || abnormal_rep7.equals("") || dead_rep7.equals("") || hardfug_rep7.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #7 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds7 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #7 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep8.equals("") || abnormal_rep8.equals("") || dead_rep8.equals("") || hardfug_rep8.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #8 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds8 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #8 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertSGTFC(userid, scode, sampleno, noofrepsgt, gmmtype, obrstype1, vegremark, techremark, normal_rep1, abnormal_rep1, hardfug_rep1, dead_rep1, normal_rep2, abnormal_rep2, hardfug_rep2, dead_rep2, normal_rep3, abnormal_rep3, hardfug_rep3, dead_rep3, normal_rep4, abnormal_rep4, hardfug_rep4, dead_rep4, normal_rep5, abnormal_rep5, hardfug_rep5, dead_rep5, normal_rep6, abnormal_rep6, hardfug_rep6, dead_rep6, normal_rep7, abnormal_rep7, hardfug_rep7, dead_rep7, normal_rep8, abnormal_rep8, hardfug_rep8, dead_rep8, normalseedper, abnormalseedper, hardseedper, deadseedper, doesgt);
                                    //submitSGTFinalCount(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,vegremark,techremark,normal_rep1,abnormal_rep1,hardfug_rep1,dead_rep1,normal_rep2,abnormal_rep2,hardfug_rep2,dead_rep2,normal_rep3,abnormal_rep3,hardfug_rep3,dead_rep3,normal_rep4,abnormal_rep4,hardfug_rep4,dead_rep4,normal_rep5,abnormal_rep5,hardfug_rep5,dead_rep5,normal_rep6,abnormal_rep6,hardfug_rep6,dead_rep6,normal_rep7,abnormal_rep7,hardfug_rep7,dead_rep7,normal_rep8,abnormal_rep8,hardfug_rep8,dead_rep8,normalseedper,abnormalseedper,hardseedper,deadseedper,doesgt);
                                }
                            }
                        }
                    }

                    if (gmmtype.equalsIgnoreCase("SGT with SGTD")) {
                        if (noofrepsgtd.equals("1")) {
                            if (obrstype1.equalsIgnoreCase("First Count")) {
                                if (normalseed_rep1.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Enter all data", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertSGT(userid, scode, sampleno, noofrepsgt, gmmtype, obrstype1, normalseed_rep1, normalseed_rep2, normalseed_rep3, normalseed_rep4, normalseed_rep5, normalseed_rep6, normalseed_rep7, normalseed_rep8, doesgt);
                                    //submitSGTFC(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,normalseed_rep1,normalseed_rep2,normalseed_rep3,normalseed_rep4,normalseed_rep5,normalseed_rep6,normalseed_rep7,normalseed_rep8,doesgt);
                                }
                            } else {
                                double reptotalseeds = Double.parseDouble(normal_rep1) + Double.parseDouble(abnormal_rep1) + Double.parseDouble(dead_rep1) + Double.parseDouble(hardfug_rep1);
                                if (normal_rep1.equals("") || abnormal_rep1.equals("") || dead_rep1.equals("") || hardfug_rep1.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertSGTFC(userid, scode, sampleno, noofrepsgt, gmmtype, obrstype1, vegremark, techremark, normal_rep1, abnormal_rep1, hardfug_rep1, dead_rep1, normal_rep2, abnormal_rep2, hardfug_rep2, dead_rep2, normal_rep3, abnormal_rep3, hardfug_rep3, dead_rep3, normal_rep4, abnormal_rep4, hardfug_rep4, dead_rep4, normal_rep5, abnormal_rep5, hardfug_rep5, dead_rep5, normal_rep6, abnormal_rep6, hardfug_rep6, dead_rep6, normal_rep7, abnormal_rep7, hardfug_rep7, dead_rep7, normal_rep8, abnormal_rep8, hardfug_rep8, dead_rep8, normalseedper, abnormalseedper, hardseedper, deadseedper, doesgt);
                                    //submitSGTFinalCount(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,vegremark,techremark,normal_rep1,abnormal_rep1,hardfug_rep1,dead_rep1,normal_rep2,abnormal_rep2,hardfug_rep2,dead_rep2,normal_rep3,abnormal_rep3,hardfug_rep3,dead_rep3,normal_rep4,abnormal_rep4,hardfug_rep4,dead_rep4,normal_rep5,abnormal_rep5,hardfug_rep5,dead_rep5,normal_rep6,abnormal_rep6,hardfug_rep6,dead_rep6,normal_rep7,abnormal_rep7,hardfug_rep7,dead_rep7,normal_rep8,abnormal_rep8,hardfug_rep8,dead_rep8,normalseedper,abnormalseedper,hardseedper,deadseedper,doesgt);
                                }
                            }
                        } else if (noofrepsgtd.equals("2")) {
                            if (obrstype1.equalsIgnoreCase("First Count")) {
                                if (normalseed_rep1.equals("") || normalseed_rep2.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Enter all data", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertSGT(userid, scode, sampleno, noofrepsgt, gmmtype, obrstype1, normalseed_rep1, normalseed_rep2, normalseed_rep3, normalseed_rep4, normalseed_rep5, normalseed_rep6, normalseed_rep7, normalseed_rep8, doesgt);
                                    //submitSGTFC(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,normalseed_rep1,normalseed_rep2,normalseed_rep3,normalseed_rep4,normalseed_rep5,normalseed_rep6,normalseed_rep7,normalseed_rep8,doesgt);
                                }
                            } else {
                                double reptotalseeds = Double.parseDouble(normal_rep1) + Double.parseDouble(abnormal_rep1) + Double.parseDouble(dead_rep1) + Double.parseDouble(hardfug_rep1);
                                double reptotalseeds2 = Double.parseDouble(normal_rep2) + Double.parseDouble(abnormal_rep2) + Double.parseDouble(dead_rep2) + Double.parseDouble(hardfug_rep2);
                                if (normal_rep1.equals("") || abnormal_rep1.equals("") || dead_rep1.equals("") || hardfug_rep1.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep2.equals("") || abnormal_rep2.equals("") || dead_rep2.equals("") || hardfug_rep2.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #2 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds2 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #2 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertSGTFC(userid, scode, sampleno, noofrepsgt, gmmtype, obrstype1, vegremark, techremark, normal_rep1, abnormal_rep1, hardfug_rep1, dead_rep1, normal_rep2, abnormal_rep2, hardfug_rep2, dead_rep2, normal_rep3, abnormal_rep3, hardfug_rep3, dead_rep3, normal_rep4, abnormal_rep4, hardfug_rep4, dead_rep4, normal_rep5, abnormal_rep5, hardfug_rep5, dead_rep5, normal_rep6, abnormal_rep6, hardfug_rep6, dead_rep6, normal_rep7, abnormal_rep7, hardfug_rep7, dead_rep7, normal_rep8, abnormal_rep8, hardfug_rep8, dead_rep8, normalseedper, abnormalseedper, hardseedper, deadseedper, doesgt);
                                    //submitSGTFinalCount(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,vegremark,techremark,normal_rep1,abnormal_rep1,hardfug_rep1,dead_rep1,normal_rep2,abnormal_rep2,hardfug_rep2,dead_rep2,normal_rep3,abnormal_rep3,hardfug_rep3,dead_rep3,normal_rep4,abnormal_rep4,hardfug_rep4,dead_rep4,normal_rep5,abnormal_rep5,hardfug_rep5,dead_rep5,normal_rep6,abnormal_rep6,hardfug_rep6,dead_rep6,normal_rep7,abnormal_rep7,hardfug_rep7,dead_rep7,normal_rep8,abnormal_rep8,hardfug_rep8,dead_rep8,normalseedper,abnormalseedper,hardseedper,deadseedper,doesgt);
                                }
                            }
                        } else if (noofrepsgtd.equals("4")) {
                            if (obrstype1.equalsIgnoreCase("First Count")) {
                                if (normalseed_rep1.equals("") || normalseed_rep2.equals("") || normalseed_rep3.equals("") || normalseed_rep4.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Enter all data", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertSGT(userid, scode, sampleno, noofrepsgt, gmmtype, obrstype1, normalseed_rep1, normalseed_rep2, normalseed_rep3, normalseed_rep4, normalseed_rep5, normalseed_rep6, normalseed_rep7, normalseed_rep8, doesgt);
                                    //submitSGTFC(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,normalseed_rep1,normalseed_rep2,normalseed_rep3,normalseed_rep4,normalseed_rep5,normalseed_rep6,normalseed_rep7,normalseed_rep8,doesgt);
                                }
                            } else {
                                double reptotalseeds = Double.parseDouble(normal_rep1) + Double.parseDouble(abnormal_rep1) + Double.parseDouble(dead_rep1) + Double.parseDouble(hardfug_rep1);
                                double reptotalseeds2 = Double.parseDouble(normal_rep2) + Double.parseDouble(abnormal_rep2) + Double.parseDouble(dead_rep2) + Double.parseDouble(hardfug_rep2);
                                double reptotalseeds3 = Double.parseDouble(normal_rep3) + Double.parseDouble(abnormal_rep3) + Double.parseDouble(dead_rep3) + Double.parseDouble(hardfug_rep3);
                                double reptotalseeds4 = Double.parseDouble(normal_rep4) + Double.parseDouble(abnormal_rep4) + Double.parseDouble(dead_rep4) + Double.parseDouble(hardfug_rep4);
                                if (normal_rep1.equals("") || abnormal_rep1.equals("") || dead_rep1.equals("") || hardfug_rep1.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep2.equals("") || abnormal_rep2.equals("") || dead_rep2.equals("") || hardfug_rep2.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #2 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds2 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #2 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep3.equals("") || abnormal_rep3.equals("") || dead_rep3.equals("") || hardfug_rep3.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #3 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds3 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #3 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep4.equals("") || abnormal_rep4.equals("") || dead_rep4.equals("") || hardfug_rep4.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #4 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds4 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #4 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertSGTFC(userid, scode, sampleno, noofrepsgt, gmmtype, obrstype1, vegremark, techremark, normal_rep1, abnormal_rep1, hardfug_rep1, dead_rep1, normal_rep2, abnormal_rep2, hardfug_rep2, dead_rep2, normal_rep3, abnormal_rep3, hardfug_rep3, dead_rep3, normal_rep4, abnormal_rep4, hardfug_rep4, dead_rep4, normal_rep5, abnormal_rep5, hardfug_rep5, dead_rep5, normal_rep6, abnormal_rep6, hardfug_rep6, dead_rep6, normal_rep7, abnormal_rep7, hardfug_rep7, dead_rep7, normal_rep8, abnormal_rep8, hardfug_rep8, dead_rep8, normalseedper, abnormalseedper, hardseedper, deadseedper, doesgt);
                                    //submitSGTFinalCount(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,vegremark,techremark,normal_rep1,abnormal_rep1,hardfug_rep1,dead_rep1,normal_rep2,abnormal_rep2,hardfug_rep2,dead_rep2,normal_rep3,abnormal_rep3,hardfug_rep3,dead_rep3,normal_rep4,abnormal_rep4,hardfug_rep4,dead_rep4,normal_rep5,abnormal_rep5,hardfug_rep5,dead_rep5,normal_rep6,abnormal_rep6,hardfug_rep6,dead_rep6,normal_rep7,abnormal_rep7,hardfug_rep7,dead_rep7,normal_rep8,abnormal_rep8,hardfug_rep8,dead_rep8,normalseedper,abnormalseedper,hardseedper,deadseedper,doesgt);
                                }
                            }
                        } else if (noofrepsgtd.equals("8")) {
                            if (obrstype1.equalsIgnoreCase("First Count")) {
                                if (normalseed_rep1.equals("") || normalseed_rep2.equals("") || normalseed_rep3.equals("") || normalseed_rep4.equals("") || normalseed_rep5.equals("") || normalseed_rep6.equals("") || normalseed_rep7.equals("") || normalseed_rep8.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Enter all data", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertSGT(userid, scode, sampleno, noofrepsgt, gmmtype, obrstype1, normalseed_rep1, normalseed_rep2, normalseed_rep3, normalseed_rep4, normalseed_rep5, normalseed_rep6, normalseed_rep7, normalseed_rep8, doesgt);
                                    //submitSGTFC(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,normalseed_rep1,normalseed_rep2,normalseed_rep3,normalseed_rep4,normalseed_rep5,normalseed_rep6,normalseed_rep7,normalseed_rep8,doesgt);
                                }
                            } else {
                                double reptotalseeds = Double.parseDouble(normal_rep1) + Double.parseDouble(abnormal_rep1) + Double.parseDouble(dead_rep1) + Double.parseDouble(hardfug_rep1);
                                double reptotalseeds2 = Double.parseDouble(normal_rep2) + Double.parseDouble(abnormal_rep2) + Double.parseDouble(dead_rep2) + Double.parseDouble(hardfug_rep2);
                                double reptotalseeds3 = Double.parseDouble(normal_rep3) + Double.parseDouble(abnormal_rep3) + Double.parseDouble(dead_rep3) + Double.parseDouble(hardfug_rep3);
                                double reptotalseeds4 = Double.parseDouble(normal_rep4) + Double.parseDouble(abnormal_rep4) + Double.parseDouble(dead_rep4) + Double.parseDouble(hardfug_rep4);
                                double reptotalseeds5 = Double.parseDouble(normal_rep5) + Double.parseDouble(abnormal_rep5) + Double.parseDouble(dead_rep5) + Double.parseDouble(hardfug_rep5);
                                double reptotalseeds6 = Double.parseDouble(normal_rep6) + Double.parseDouble(abnormal_rep6) + Double.parseDouble(dead_rep6) + Double.parseDouble(hardfug_rep6);
                                double reptotalseeds7 = Double.parseDouble(normal_rep7) + Double.parseDouble(abnormal_rep7) + Double.parseDouble(dead_rep7) + Double.parseDouble(hardfug_rep7);
                                double reptotalseeds8 = Double.parseDouble(normal_rep8) + Double.parseDouble(abnormal_rep8) + Double.parseDouble(dead_rep8) + Double.parseDouble(hardfug_rep8);
                                if (normal_rep1.equals("") || abnormal_rep1.equals("") || dead_rep1.equals("") || hardfug_rep1.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep2.equals("") || abnormal_rep2.equals("") || dead_rep2.equals("") || hardfug_rep2.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #2 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds2 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #2 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep3.equals("") || abnormal_rep3.equals("") || dead_rep3.equals("") || hardfug_rep3.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #3 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds3 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #3 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep4.equals("") || abnormal_rep4.equals("") || dead_rep4.equals("") || hardfug_rep4.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #4 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds4 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #4 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep5.equals("") || abnormal_rep5.equals("") || dead_rep5.equals("") || hardfug_rep5.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #5 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds5 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #5 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep6.equals("") || abnormal_rep6.equals("") || dead_rep6.equals("") || hardfug_rep6.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #6 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds6 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #6 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep7.equals("") || abnormal_rep7.equals("") || dead_rep7.equals("") || hardfug_rep7.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #7 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds7 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #7 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep8.equals("") || abnormal_rep8.equals("") || dead_rep8.equals("") || hardfug_rep8.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #8 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds8 != sampleGerminationInfo.getNoofseedinonerep()) {
                                    Toast.makeText(getApplicationContext(), "Replication #8 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertSGTFC(userid, scode, sampleno, noofrepsgt, gmmtype, obrstype1, vegremark, techremark, normal_rep1, abnormal_rep1, hardfug_rep1, dead_rep1, normal_rep2, abnormal_rep2, hardfug_rep2, dead_rep2, normal_rep3, abnormal_rep3, hardfug_rep3, dead_rep3, normal_rep4, abnormal_rep4, hardfug_rep4, dead_rep4, normal_rep5, abnormal_rep5, hardfug_rep5, dead_rep5, normal_rep6, abnormal_rep6, hardfug_rep6, dead_rep6, normal_rep7, abnormal_rep7, hardfug_rep7, dead_rep7, normal_rep8, abnormal_rep8, hardfug_rep8, dead_rep8, normalseedper, abnormalseedper, hardseedper, deadseedper, doesgt);
                                    //submitSGTFinalCount(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,vegremark,techremark,normal_rep1,abnormal_rep1,hardfug_rep1,dead_rep1,normal_rep2,abnormal_rep2,hardfug_rep2,dead_rep2,normal_rep3,abnormal_rep3,hardfug_rep3,dead_rep3,normal_rep4,abnormal_rep4,hardfug_rep4,dead_rep4,normal_rep5,abnormal_rep5,hardfug_rep5,dead_rep5,normal_rep6,abnormal_rep6,hardfug_rep6,dead_rep6,normal_rep7,abnormal_rep7,hardfug_rep7,dead_rep7,normal_rep8,abnormal_rep8,hardfug_rep8,dead_rep8,normalseedper,abnormalseedper,hardseedper,deadseedper,doesgt);
                                }
                            }
                        }
                    }

                    if (gmmtype.equalsIgnoreCase("FGT")) {
                        if (noofrepfgt.equals("1")) {
                            if (obrstype1.equalsIgnoreCase("First Count")) {
                                if (normalseed_rep1.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Enter all data", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertFGT(userid, scode, sampleno, noofrepfgt, gmmtype, obrstype1, normalseed_rep1, normalseed_rep2, normalseed_rep3, normalseed_rep4, normalseed_rep5, normalseed_rep6, normalseed_rep7, normalseed_rep8, doefgt, fgtmedia);
                                    //submitFGTFC(userid,scode,sampleno,noofrepfgt,gmmtype,obrstype1,normalseed_rep1,normalseed_rep2,normalseed_rep3,normalseed_rep4,normalseed_rep5,normalseed_rep6,normalseed_rep7,normalseed_rep8,doefgt,fgtmedia);
                                }
                            } else {
                                double reptotalseeds = Double.parseDouble(normal_rep1) + Double.parseDouble(abnormal_rep1) + Double.parseDouble(dead_rep1);
                                if (normal_rep1.equals("") || abnormal_rep1.equals("") || dead_rep1.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds != sampleGerminationInfo.getNoofseedinonerepfgt()) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertFGTFC(userid, scode, sampleno, noofrepfgt, gmmtype, obrstype1, vegremark, techremark, normal_rep1, abnormal_rep1, dead_rep1, normal_rep2, abnormal_rep2, dead_rep2, normal_rep3, abnormal_rep3, dead_rep3, normal_rep4, abnormal_rep4, dead_rep4, normal_rep5, abnormal_rep5, dead_rep5, normal_rep6, abnormal_rep6, dead_rep6, normal_rep7, abnormal_rep7, dead_rep7, normal_rep8, abnormal_rep8, dead_rep8, normalseedper, abnormalseedper, deadseedper, doefgt, fgtmedia);
                                    //submitFGTFinalCount(userid,scode,sampleno,noofrepfgt,gmmtype,obrstype1,vegremark,techremark,normal_rep1,abnormal_rep1,dead_rep1,normal_rep2,abnormal_rep2,dead_rep2,normal_rep3,abnormal_rep3,dead_rep3,normal_rep4,abnormal_rep4,dead_rep4,normal_rep5,abnormal_rep5,dead_rep5,normal_rep6,abnormal_rep6,dead_rep6,normal_rep7,abnormal_rep7,dead_rep7,normal_rep8,abnormal_rep8,dead_rep8,normalseedper,abnormalseedper,deadseedper,doefgt,fgtmedia);
                                }
                            }
                        } else if (noofrepfgt.equals("2")) {
                            if (obrstype1.equalsIgnoreCase("First Count")) {
                                if (normalseed_rep1.equals("") || normalseed_rep2.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Enter all data", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertFGT(userid, scode, sampleno, noofrepfgt, gmmtype, obrstype1, normalseed_rep1, normalseed_rep2, normalseed_rep3, normalseed_rep4, normalseed_rep5, normalseed_rep6, normalseed_rep7, normalseed_rep8, doefgt, fgtmedia);
                                    //submitFGTFC(userid,scode,sampleno,noofrepfgt,gmmtype,obrstype1,normalseed_rep1,normalseed_rep2,normalseed_rep3,normalseed_rep4,normalseed_rep5,normalseed_rep6,normalseed_rep7,normalseed_rep8,doefgt,fgtmedia);
                                }
                            } else {
                                if (!normal_rep1.equalsIgnoreCase("") && !abnormal_rep1.equalsIgnoreCase("") && !dead_rep1.equalsIgnoreCase("")) {
                                    if (!normal_rep2.equalsIgnoreCase("") && !abnormal_rep2.equalsIgnoreCase("") && !dead_rep2.equalsIgnoreCase("")) {
                                        double reptotalseeds = Double.parseDouble(normal_rep1) + Double.parseDouble(abnormal_rep1) + Double.parseDouble(dead_rep1);
                                        double reptotalseeds2 = Double.parseDouble(normal_rep2) + Double.parseDouble(abnormal_rep2) + Double.parseDouble(dead_rep2);
                                        if (normal_rep1.equals("") || abnormal_rep1.equals("") || dead_rep1.equals("")) {
                                            Toast.makeText(getApplicationContext(), "Replication #1 : Enter all data", Toast.LENGTH_LONG).show();
                                        } else if (reptotalseeds != sampleGerminationInfo.getNoofseedinonerepfgt()) {
                                            Toast.makeText(getApplicationContext(), "Replication #1 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                        } else if (normal_rep2.equals("") || abnormal_rep2.equals("") || dead_rep2.equals("")) {
                                            Toast.makeText(getApplicationContext(), "Replication #2 : Enter all data", Toast.LENGTH_LONG).show();
                                        } else if (reptotalseeds2 != sampleGerminationInfo.getNoofseedinonerepfgt()) {
                                            Toast.makeText(getApplicationContext(), "Replication #2 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                        } else {
                                            confirmAlertFGTFC(userid, scode, sampleno, noofrepfgt, gmmtype, obrstype1, vegremark, techremark, normal_rep1, abnormal_rep1, dead_rep1, normal_rep2, abnormal_rep2, dead_rep2, normal_rep3, abnormal_rep3, dead_rep3, normal_rep4, abnormal_rep4, dead_rep4, normal_rep5, abnormal_rep5, dead_rep5, normal_rep6, abnormal_rep6, dead_rep6, normal_rep7, abnormal_rep7, dead_rep7, normal_rep8, abnormal_rep8, dead_rep8, normalseedper, abnormalseedper, deadseedper, doefgt, fgtmedia);
                                            //submitFGTFinalCount(userid,scode,sampleno,noofrepfgt,gmmtype,obrstype1,vegremark,techremark,normal_rep1,abnormal_rep1,dead_rep1,normal_rep2,abnormal_rep2,dead_rep2,normal_rep3,abnormal_rep3,dead_rep3,normal_rep4,abnormal_rep4,dead_rep4,normal_rep5,abnormal_rep5,dead_rep5,normal_rep6,abnormal_rep6,dead_rep6,normal_rep7,abnormal_rep7,dead_rep7,normal_rep8,abnormal_rep8,dead_rep8,normalseedper,abnormalseedper,deadseedper,doefgt,fgtmedia);
                                        }
                                    }
                                }
                            }
                        } else if (noofrepfgt.equals("4")) {
                            if (obrstype1.equalsIgnoreCase("First Count")) {
                                if (normalseed_rep1.equals("") || normalseed_rep2.equals("") || normalseed_rep3.equals("") || normalseed_rep4.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Enter all data", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertFGT(userid, scode, sampleno, noofrepfgt, gmmtype, obrstype1, normalseed_rep1, normalseed_rep2, normalseed_rep3, normalseed_rep4, normalseed_rep5, normalseed_rep6, normalseed_rep7, normalseed_rep8, doefgt, fgtmedia);
                                    //submitFGTFC(userid,scode,sampleno,noofrepfgt,gmmtype,obrstype1,normalseed_rep1,normalseed_rep2,normalseed_rep3,normalseed_rep4,normalseed_rep5,normalseed_rep6,normalseed_rep7,normalseed_rep8,doefgt,fgtmedia);
                                }
                            } else {
                                if (!normal_rep1.equalsIgnoreCase("") && !abnormal_rep1.equalsIgnoreCase("") && !dead_rep1.equalsIgnoreCase("")) {
                                    if (!normal_rep2.equalsIgnoreCase("") && !abnormal_rep2.equalsIgnoreCase("") && !dead_rep2.equalsIgnoreCase("")) {
                                        if (!normal_rep3.equalsIgnoreCase("") && !abnormal_rep3.equalsIgnoreCase("") && !dead_rep3.equalsIgnoreCase("")) {
                                            if (!normal_rep4.equalsIgnoreCase("") && !abnormal_rep4.equalsIgnoreCase("") && !dead_rep4.equalsIgnoreCase("")) {
                                                double reptotalseeds = Double.parseDouble(normal_rep1) + Double.parseDouble(abnormal_rep1) + Double.parseDouble(dead_rep1);
                                                double reptotalseeds2 = Double.parseDouble(normal_rep2) + Double.parseDouble(abnormal_rep2) + Double.parseDouble(dead_rep2);
                                                double reptotalseeds3 = Double.parseDouble(normal_rep3) + Double.parseDouble(abnormal_rep3) + Double.parseDouble(dead_rep3);
                                                double reptotalseeds4 = Double.parseDouble(normal_rep4) + Double.parseDouble(abnormal_rep4) + Double.parseDouble(dead_rep4);
                                                if (normal_rep1.equals("") || abnormal_rep1.equals("") || dead_rep1.equals("")) {
                                                    Toast.makeText(getApplicationContext(), "Replication #1 : Enter all data", Toast.LENGTH_LONG).show();
                                                } else if (reptotalseeds != sampleGerminationInfo.getNoofseedinonerepfgt()) {
                                                    Toast.makeText(getApplicationContext(), "Replication #1 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                } else if (normal_rep2.equals("") || abnormal_rep2.equals("") || dead_rep2.equals("")) {
                                                    Toast.makeText(getApplicationContext(), "Replication #2 : Enter all data", Toast.LENGTH_LONG).show();
                                                } else if (reptotalseeds2 != sampleGerminationInfo.getNoofseedinonerepfgt()) {
                                                    Toast.makeText(getApplicationContext(), "Replication #2 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                } else if (normal_rep3.equals("") || abnormal_rep3.equals("") || dead_rep3.equals("")) {
                                                    Toast.makeText(getApplicationContext(), "Replication #3 : Enter all data", Toast.LENGTH_LONG).show();
                                                } else if (reptotalseeds3 != sampleGerminationInfo.getNoofseedinonerepfgt()) {
                                                    Toast.makeText(getApplicationContext(), "Replication #3 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                } else if (normal_rep4.equals("") || abnormal_rep4.equals("") || dead_rep4.equals("")) {
                                                    Toast.makeText(getApplicationContext(), "Replication #4 : Enter all data", Toast.LENGTH_LONG).show();
                                                } else if (reptotalseeds4 != sampleGerminationInfo.getNoofseedinonerepfgt()) {
                                                    Toast.makeText(getApplicationContext(), "Replication #4 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                } else {
                                                    confirmAlertFGTFC(userid, scode, sampleno, noofrepfgt, gmmtype, obrstype1, vegremark, techremark, normal_rep1, abnormal_rep1, dead_rep1, normal_rep2, abnormal_rep2, dead_rep2, normal_rep3, abnormal_rep3, dead_rep3, normal_rep4, abnormal_rep4, dead_rep4, normal_rep5, abnormal_rep5, dead_rep5, normal_rep6, abnormal_rep6, dead_rep6, normal_rep7, abnormal_rep7, dead_rep7, normal_rep8, abnormal_rep8, dead_rep8, normalseedper, abnormalseedper, deadseedper, doefgt, fgtmedia);
                                                    //submitFGTFinalCount(userid,scode,sampleno,noofrepfgt,gmmtype,obrstype1,vegremark,techremark,normal_rep1,abnormal_rep1,dead_rep1,normal_rep2,abnormal_rep2,dead_rep2,normal_rep3,abnormal_rep3,dead_rep3,normal_rep4,abnormal_rep4,dead_rep4,normal_rep5,abnormal_rep5,dead_rep5,normal_rep6,abnormal_rep6,dead_rep6,normal_rep7,abnormal_rep7,dead_rep7,normal_rep8,abnormal_rep8,dead_rep8,normalseedper,abnormalseedper,deadseedper,doefgt,fgtmedia);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (noofrepfgt.equals("8")) {
                            if (obrstype1.equalsIgnoreCase("First Count")) {
                                if (normalseed_rep1.equals("") || normalseed_rep2.equals("") || normalseed_rep3.equals("") || normalseed_rep4.equals("") || normalseed_rep5.equals("") || normalseed_rep6.equals("") || normalseed_rep7.equals("") || normalseed_rep8.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Enter all data", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertFGT(userid, scode, sampleno, noofrepfgt, gmmtype, obrstype1, normalseed_rep1, normalseed_rep2, normalseed_rep3, normalseed_rep4, normalseed_rep5, normalseed_rep6, normalseed_rep7, normalseed_rep8, doefgt, fgtmedia);
                                    //submitFGTFC(userid,scode,sampleno,noofrepfgt,gmmtype,obrstype1,normalseed_rep1,normalseed_rep2,normalseed_rep3,normalseed_rep4,normalseed_rep5,normalseed_rep6,normalseed_rep7,normalseed_rep8,doefgt,fgtmedia);
                                }
                            } else {
                                if (!normal_rep1.equalsIgnoreCase("") && !abnormal_rep1.equalsIgnoreCase("") && !dead_rep1.equalsIgnoreCase("")) {
                                    if (!normal_rep2.equalsIgnoreCase("") && !abnormal_rep2.equalsIgnoreCase("") && !dead_rep2.equalsIgnoreCase("")) {
                                        if (!normal_rep3.equalsIgnoreCase("") && !abnormal_rep3.equalsIgnoreCase("") && !dead_rep3.equalsIgnoreCase("")) {
                                            if (!normal_rep4.equalsIgnoreCase("") && !abnormal_rep4.equalsIgnoreCase("") && !dead_rep4.equalsIgnoreCase("")) {
                                                if (!normal_rep5.equalsIgnoreCase("") && !abnormal_rep5.equalsIgnoreCase("") && !dead_rep5.equalsIgnoreCase("")) {
                                                    if (!normal_rep6.equalsIgnoreCase("") && !abnormal_rep6.equalsIgnoreCase("") && !dead_rep6.equalsIgnoreCase("")) {
                                                        if (!normal_rep7.equalsIgnoreCase("") && !abnormal_rep7.equalsIgnoreCase("") && !dead_rep7.equalsIgnoreCase("")) {
                                                            if (!normal_rep8.equalsIgnoreCase("") && !abnormal_rep8.equalsIgnoreCase("") && !dead_rep8.equalsIgnoreCase("")) {
                                                                double reptotalseeds = Double.parseDouble(normal_rep1) + Double.parseDouble(abnormal_rep1) + Double.parseDouble(dead_rep1);
                                                                double reptotalseeds2 = Double.parseDouble(normal_rep2) + Double.parseDouble(abnormal_rep2) + Double.parseDouble(dead_rep2);
                                                                double reptotalseeds3 = Double.parseDouble(normal_rep3) + Double.parseDouble(abnormal_rep3) + Double.parseDouble(dead_rep3);
                                                                double reptotalseeds4 = Double.parseDouble(normal_rep4) + Double.parseDouble(abnormal_rep4) + Double.parseDouble(dead_rep4);
                                                                double reptotalseeds5 = Double.parseDouble(normal_rep5) + Double.parseDouble(abnormal_rep5) + Double.parseDouble(dead_rep5);
                                                                double reptotalseeds6 = Double.parseDouble(normal_rep6) + Double.parseDouble(abnormal_rep6) + Double.parseDouble(dead_rep6);
                                                                double reptotalseeds7 = Double.parseDouble(normal_rep7) + Double.parseDouble(abnormal_rep7) + Double.parseDouble(dead_rep7);
                                                                double reptotalseeds8 = Double.parseDouble(normal_rep8) + Double.parseDouble(abnormal_rep8) + Double.parseDouble(dead_rep8);
                                                                if (normal_rep1.equals("") || abnormal_rep1.equals("") || dead_rep1.equals("")) {
                                                                    Toast.makeText(getApplicationContext(), "Replication #1 : Enter all data", Toast.LENGTH_LONG).show();
                                                                } else if (reptotalseeds != sampleGerminationInfo.getNoofseedinonerepfgt()) {
                                                                    Toast.makeText(getApplicationContext(), "Replication #1 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                                } else if (normal_rep2.equals("") || abnormal_rep2.equals("") || dead_rep2.equals("")) {
                                                                    Toast.makeText(getApplicationContext(), "Replication #2 : Enter all data", Toast.LENGTH_LONG).show();
                                                                } else if (reptotalseeds2 != sampleGerminationInfo.getNoofseedinonerepfgt()) {
                                                                    Toast.makeText(getApplicationContext(), "Replication #2 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                                } else if (normal_rep3.equals("") || abnormal_rep3.equals("") || dead_rep3.equals("")) {
                                                                    Toast.makeText(getApplicationContext(), "Replication #3 : Enter all data", Toast.LENGTH_LONG).show();
                                                                } else if (reptotalseeds3 != sampleGerminationInfo.getNoofseedinonerepfgt()) {
                                                                    Toast.makeText(getApplicationContext(), "Replication #3 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                                } else if (normal_rep4.equals("") || abnormal_rep4.equals("") || dead_rep4.equals("")) {
                                                                    Toast.makeText(getApplicationContext(), "Replication #4 : Enter all data", Toast.LENGTH_LONG).show();
                                                                } else if (reptotalseeds4 != sampleGerminationInfo.getNoofseedinonerepfgt()) {
                                                                    Toast.makeText(getApplicationContext(), "Replication #4 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                                } else if (normal_rep5.equals("") || abnormal_rep5.equals("") || dead_rep5.equals("")) {
                                                                    Toast.makeText(getApplicationContext(), "Replication #5 : Enter all data", Toast.LENGTH_LONG).show();
                                                                } else if (reptotalseeds5 != sampleGerminationInfo.getNoofseedinonerepfgt()) {
                                                                    Toast.makeText(getApplicationContext(), "Replication #5 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                                } else if (normal_rep6.equals("") || abnormal_rep6.equals("") || dead_rep6.equals("")) {
                                                                    Toast.makeText(getApplicationContext(), "Replication #6 : Enter all data", Toast.LENGTH_LONG).show();
                                                                } else if (reptotalseeds6 != sampleGerminationInfo.getNoofseedinonerepfgt()) {
                                                                    Toast.makeText(getApplicationContext(), "Replication #6 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                                } else if (normal_rep7.equals("") || abnormal_rep7.equals("") || dead_rep7.equals("")) {
                                                                    Toast.makeText(getApplicationContext(), "Replication #7 : Enter all data", Toast.LENGTH_LONG).show();
                                                                } else if (reptotalseeds7 != sampleGerminationInfo.getNoofseedinonerepfgt()) {
                                                                    Toast.makeText(getApplicationContext(), "Replication #7 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                                } else if (normal_rep8.equals("") || abnormal_rep8.equals("") || dead_rep8.equals("")) {
                                                                    Toast.makeText(getApplicationContext(), "Replication #8 : Enter all data", Toast.LENGTH_LONG).show();
                                                                } else if (reptotalseeds8 != sampleGerminationInfo.getNoofseedinonerepfgt()) {
                                                                    Toast.makeText(getApplicationContext(), "Replication #8 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                                } else {
                                                                    confirmAlertFGTFC(userid, scode, sampleno, noofrepfgt, gmmtype, obrstype1, vegremark, techremark, normal_rep1, abnormal_rep1, dead_rep1, normal_rep2, abnormal_rep2, dead_rep2, normal_rep3, abnormal_rep3, dead_rep3, normal_rep4, abnormal_rep4, dead_rep4, normal_rep5, abnormal_rep5, dead_rep5, normal_rep6, abnormal_rep6, dead_rep6, normal_rep7, abnormal_rep7, dead_rep7, normal_rep8, abnormal_rep8, dead_rep8, normalseedper, abnormalseedper, deadseedper, doefgt, fgtmedia);
                                                                    //submitFGTFinalCount(userid,scode,sampleno,noofrepfgt,gmmtype,obrstype1,vegremark,techremark,normal_rep1,abnormal_rep1,dead_rep1,normal_rep2,abnormal_rep2,dead_rep2,normal_rep3,abnormal_rep3,dead_rep3,normal_rep4,abnormal_rep4,dead_rep4,normal_rep5,abnormal_rep5,dead_rep5,normal_rep6,abnormal_rep6,dead_rep6,normal_rep7,abnormal_rep7,dead_rep7,normal_rep8,abnormal_rep8,dead_rep8,normalseedper,abnormalseedper,deadseedper,doefgt,fgtmedia);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void cal_deadper() {
        String dead_rep1 = "0";
        String dead_rep2 = "0";
        String dead_rep3 = "0";
        String dead_rep4 = "0";
        if (!et_dead_rep1.getText().toString().equalsIgnoreCase("")){
            dead_rep1 = et_dead_rep1.getText().toString().trim();
        }
        if (!et_dead_rep2.getText().toString().equalsIgnoreCase("")){
            dead_rep2 = et_dead_rep2.getText().toString().trim();
        }
        if (!et_dead_rep3.getText().toString().equalsIgnoreCase("")){
            dead_rep3 = et_dead_rep3.getText().toString().trim();
        }
        if (!et_dead_rep4.getText().toString().equalsIgnoreCase("")){
            dead_rep4 = et_dead_rep4.getText().toString().trim();
        }

        float dead_rep14 = Float.parseFloat(dead_rep1);
        float dead_rep24 = Float.parseFloat(dead_rep2);
        float dead_rep34 = Float.parseFloat(dead_rep3);
        float dead_rep44 = Float.parseFloat(dead_rep4);
        int perround = 0;
        if (dead_rep14>0 && dead_rep24==0 && dead_rep34==0 && dead_rep44==0){
            float deadmeanper = (dead_rep14 * 100) / finalNoofseedonerep;
            perround = Math.round(deadmeanper);
        }else if (dead_rep14>0 && dead_rep24>0 && dead_rep34==0 && dead_rep44==0){
            float DEADPER = (dead_rep14 + dead_rep24) / 2;
            float meanper = (DEADPER*100)/finalNoofseedonerep;
            perround = Math.round(meanper);
        }else if (dead_rep14>0 && dead_rep24>0 && dead_rep34>0 && dead_rep44==0){
            float DEADPER = (dead_rep14 + dead_rep24+dead_rep34) / 3;
            float meanper = (DEADPER*100)/finalNoofseedonerep;
            perround = Math.round(meanper);
        }else if (dead_rep14>0 && dead_rep24>0 && dead_rep34>0 && dead_rep44>0){
            float DEADPER = (dead_rep14 + dead_rep24+dead_rep34+dead_rep44) / 4;
            float meanper = (DEADPER*100)/finalNoofseedonerep;
            perround = Math.round(meanper);
        }
        et_deadseedper.setText(String.valueOf(perround));
    }

    private void cal_hardfugper() {
        String hardfug_rep1 = "0";
        String hardfug_rep2 = "0";
        String hardfug_rep3 = "0";
        String hardfug_rep4 = "0";
        if (!et_hardfug_rep1.getText().toString().equalsIgnoreCase("")){
            hardfug_rep1 = et_hardfug_rep1.getText().toString().trim();
        }
        if (!et_hardfug_rep2.getText().toString().equalsIgnoreCase("")){
            hardfug_rep2 = et_hardfug_rep2.getText().toString().trim();
        }
        if (!et_hardfug_rep3.getText().toString().equalsIgnoreCase("")){
            hardfug_rep3 = et_hardfug_rep3.getText().toString().trim();
        }
        if (!et_hardfug_rep4.getText().toString().equalsIgnoreCase("")){
            hardfug_rep4 = et_hardfug_rep4.getText().toString().trim();
        }

        float noofseedonerep = sampleGerminationInfo.getNoofseedinonerepfgt();
        float hardfug_rep31 = Float.parseFloat(hardfug_rep1);
        float hardfug_rep32 = Float.parseFloat(hardfug_rep2);
        float hardfug_rep33 = Float.parseFloat(hardfug_rep3);
        float hardfug_rep34 = Float.parseFloat(hardfug_rep4);
        int perround = 0;
        if (hardfug_rep31>0 && hardfug_rep32==0 && hardfug_rep33==0 && hardfug_rep34==0){
            float hardfugmeanper = (hardfug_rep31 * 100) / noofseedonerep;
            perround = Math.round(hardfugmeanper);
        }else if (hardfug_rep31>0 && hardfug_rep32>0 && hardfug_rep33==0 && hardfug_rep34==0){
            float HARDPER = (hardfug_rep31 + hardfug_rep32) / 2;
            float meanper = (HARDPER*100)/finalNoofseedonerep;
            perround = Math.round(meanper);
        }else if (hardfug_rep31>0 && hardfug_rep32>0 && hardfug_rep33>0 && hardfug_rep34==0){
            float HARDPER = (hardfug_rep31 + hardfug_rep32 + hardfug_rep33) / 3;
            float meanper = (HARDPER*100)/finalNoofseedonerep;
            perround = Math.round(meanper);
        }else if (hardfug_rep31>0 && hardfug_rep32>0 && hardfug_rep33>0 && hardfug_rep34>0){
            float HARDPER = (hardfug_rep31 + hardfug_rep32 + hardfug_rep33 + hardfug_rep34) / 3;
            float meanper = (HARDPER*100)/finalNoofseedonerep;
            perround = Math.round(meanper);
        }
        et_hardseedper.setText(String.valueOf(perround));
    }

    private void cal_abnormalper() {
        String abnormal_rep1 = "0";
        String abnormal_rep2 = "0";
        String abnormal_rep3 = "0";
        String abnormal_rep4 = "0";
        if (!et_abnormal_rep1.getText().toString().equalsIgnoreCase("")){
            abnormal_rep1 = et_abnormal_rep1.getText().toString().trim();
        }
        if (!et_abnormal_rep2.getText().toString().equalsIgnoreCase("")){
            abnormal_rep2 = et_abnormal_rep2.getText().toString().trim();
        }
        if (!et_abnormal_rep3.getText().toString().equalsIgnoreCase("")){
            abnormal_rep3 = et_abnormal_rep3.getText().toString().trim();
        }
        if (!et_normal_rep4.getText().toString().equalsIgnoreCase("")){
            abnormal_rep4 = et_abnormal_rep4.getText().toString().trim();
        }

        float abnormal_rep12 = Float.parseFloat(abnormal_rep1);
        float abnormal_rep22 = Float.parseFloat(abnormal_rep2);
        float abnormal_rep32 = Float.parseFloat(abnormal_rep3);
        float abnormal_rep42 = Float.parseFloat(abnormal_rep4);

        int perround = 0;
        if (abnormal_rep12>0 && abnormal_rep22==0 && abnormal_rep32==0 && abnormal_rep42==0){
            float abnormalmeanper = (abnormal_rep12 * 100) / finalNoofseedonerep;
            perround = Math.round(abnormalmeanper);
        }else if (abnormal_rep12>0 && abnormal_rep22>0 && abnormal_rep32==0 && abnormal_rep42==0){
            float ABNORPER = (abnormal_rep12 + abnormal_rep22) / 2;
            float meanper = (ABNORPER*100)/finalNoofseedonerep;
            perround = Math.round(meanper);
        }else if (abnormal_rep12>0 && abnormal_rep22>0 && abnormal_rep32>0 && abnormal_rep42==0){
            float ABNORPER = (abnormal_rep12 + abnormal_rep22 + abnormal_rep32) / 3;
            float meanper = (ABNORPER*100)/finalNoofseedonerep;
            perround = Math.round(meanper);
        }else if (abnormal_rep12>0 && abnormal_rep22>0 && abnormal_rep32>0 && abnormal_rep42>0){
            float ABNORPER = (abnormal_rep12 + abnormal_rep22 + abnormal_rep32 + abnormal_rep42) / 4;
            float meanper = (ABNORPER*100)/finalNoofseedonerep;
            perround = Math.round(meanper);
        }

        et_abnormalseedper.setText(String.valueOf(perround));
    }

    private void cal_normalper() {
        String normal_rep1 = "0";
        String normal_rep2 = "0";
        String normal_rep3 = "0";
        String normal_rep4 = "0";
        if (!et_normal_rep1.getText().toString().equalsIgnoreCase("")){
            normal_rep1 = et_normal_rep1.getText().toString().trim();
        }
        if (!et_normal_rep2.getText().toString().equalsIgnoreCase("")){
            normal_rep2 = et_normal_rep2.getText().toString().trim();
        }
        if (!et_normal_rep3.getText().toString().equalsIgnoreCase("")){
            normal_rep3 = et_normal_rep3.getText().toString().trim();
        }
        if (!et_normal_rep4.getText().toString().equalsIgnoreCase("")){
            normal_rep4 = et_normal_rep4.getText().toString().trim();
        }

        float normal_rep12 = Float.parseFloat(normal_rep1);
        float normal_rep21 = Float.parseFloat(normal_rep2);
        float normal_rep31 = Float.parseFloat(normal_rep3);
        float normal_rep41 = Float.parseFloat(normal_rep4);
        int perround = 0;
        if (normal_rep12>0 && normal_rep21==0 && normal_rep31==0 && normal_rep41==0){
            float normalmeanper = (normal_rep12 * 100) / finalNoofseedonerep;
            perround = Math.round(normalmeanper);
        } else if (normal_rep12>0 && normal_rep21>0 && normal_rep31==0 && normal_rep41==0) {
            float NORREP1 = Float.parseFloat(normal_rep1);
            float NORPER = (NORREP1 + normal_rep21) / 2;
            float meanper = (NORPER*100)/finalNoofseedonerep;
            perround = Math.round(meanper);
        } else if (normal_rep12>0 && normal_rep21>0 && normal_rep31>0 && normal_rep41==0) {
            float NORREP1 = Float.parseFloat(normal_rep1);
            float NORPER = (NORREP1 + normal_rep21 + normal_rep31) / 3;
            float meanper = (NORPER*100)/finalNoofseedonerep;
            perround = Math.round(meanper);
        } else if (normal_rep12>0 && normal_rep21>0 && normal_rep31>0 && normal_rep41>0) {
            float NORREP1 = Float.parseFloat(normal_rep1);
            float NORPER = (NORREP1 + normal_rep21 + normal_rep31 + normal_rep41) / 4;
            float meanper = (NORPER*100)/finalNoofseedonerep;
            perround = Math.round(meanper);
        }
        et_normalseedper.setText(String.valueOf(perround));
    }

    @SuppressLint("SetTextI18n")
    private void confirmAlertFGTFC(final String userid, final String scode, final String sampleno, final String noofrepfgt, final String gmmtype, final String obrstype1, final String vegremark, final String techremark, final String normal_rep1, final String abnormal_rep1, final String dead_rep1, final String normal_rep2, final String abnormal_rep2, final String dead_rep2, final String normal_rep3, final String abnormal_rep3, final String dead_rep3, final String normal_rep4, final String abnormal_rep4, final String dead_rep4, final String normal_rep5, final String abnormal_rep5, final String dead_rep5, final String normal_rep6, final String abnormal_rep6, final String dead_rep6, final String normal_rep7, final String abnormal_rep7, final String dead_rep7, final String normal_rep8, final String abnormal_rep8, final String dead_rep8, final String normalseedper, final String abnormalseedper, final String deadseedper, final String doefgt, final String fgtmedia) {
        final Dialog dialog = new Dialog(GerminationReadingFormActivity.this);
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
                submitFGTFinalCount(userid,scode,sampleno,noofrepfgt,gmmtype,obrstype1,vegremark,techremark,normal_rep1,abnormal_rep1,dead_rep1,normal_rep2,abnormal_rep2,dead_rep2,normal_rep3,abnormal_rep3,dead_rep3,normal_rep4,abnormal_rep4,dead_rep4,normal_rep5,abnormal_rep5,dead_rep5,normal_rep6,abnormal_rep6,dead_rep6,normal_rep7,abnormal_rep7,dead_rep7,normal_rep8,abnormal_rep8,dead_rep8,normalseedper,abnormalseedper,deadseedper,doefgt,fgtmedia);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void confirmAlertSGTFC(final String userid, final String scode, final String sampleno, final String noofrepsgt, final String gmmtype, final String obrstype1, final String vegremark, final String techremark, final String normal_rep1, final String abnormal_rep1, final String hardfug_rep1, final String dead_rep1, final String normal_rep2, final String abnormal_rep2, final String hardfug_rep2, final String dead_rep2, final String normal_rep3, final String abnormal_rep3, final String hardfug_rep3, final String dead_rep3, final String normal_rep4, final String abnormal_rep4, final String hardfug_rep4, final String dead_rep4, final String normal_rep5, final String abnormal_rep5, final String hardfug_rep5, final String dead_rep5, final String normal_rep6, final String abnormal_rep6, final String hardfug_rep6, final String dead_rep6, final String normal_rep7, final String abnormal_rep7, final String hardfug_rep7, final String dead_rep7, final String normal_rep8, final String abnormal_rep8, final String hardfug_rep8, final String dead_rep8, final String normalseedper, final String abnormalseedper, final String hardseedper, final String deadseedper, final String doesgt) {
        final Dialog dialog = new Dialog(GerminationReadingFormActivity.this);
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
                submitSGTFinalCount(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,vegremark,techremark,normal_rep1,abnormal_rep1,hardfug_rep1,dead_rep1,normal_rep2,abnormal_rep2,hardfug_rep2,dead_rep2,normal_rep3,abnormal_rep3,hardfug_rep3,dead_rep3,normal_rep4,abnormal_rep4,hardfug_rep4,dead_rep4,normal_rep5,abnormal_rep5,hardfug_rep5,dead_rep5,normal_rep6,abnormal_rep6,hardfug_rep6,dead_rep6,normal_rep7,abnormal_rep7,hardfug_rep7,dead_rep7,normal_rep8,abnormal_rep8,hardfug_rep8,dead_rep8,normalseedper,abnormalseedper,hardseedper,deadseedper,doesgt);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void confirmAlertFGT(final String userid, final String scode, final String sampleno, final String noofrepfgt, final String gmmtype, final String obrstype1, final String normalseed_rep1, final String normalseed_rep2, final String normalseed_rep3, final String normalseed_rep4, final String normalseed_rep5, final String normalseed_rep6, final String normalseed_rep7, final String normalseed_rep8, final String doefgt, final String fgtmedia) {
        final Dialog dialog = new Dialog(GerminationReadingFormActivity.this);
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
                submitFGTFC(userid,scode,sampleno,noofrepfgt,gmmtype,obrstype1,normalseed_rep1,normalseed_rep2,normalseed_rep3,normalseed_rep4,normalseed_rep5,normalseed_rep6,normalseed_rep7,normalseed_rep8,doefgt,fgtmedia);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void confirmAlertSGT(final String userid, final String scode, final String sampleno, final String noofrepsgt, final String gmmtype, final String obrstype1, final String normalseed_rep1, final String normalseed_rep2, final String normalseed_rep3, final String normalseed_rep4, final String normalseed_rep5, final String normalseed_rep6, final String normalseed_rep7, final String normalseed_rep8, final String doesgt) {
        final Dialog dialog = new Dialog(GerminationReadingFormActivity.this);
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
                submitSGTFC(userid,scode,sampleno,noofrepsgt,gmmtype,obrstype1,normalseed_rep1,normalseed_rep2,normalseed_rep3,normalseed_rep4,normalseed_rep5,normalseed_rep6,normalseed_rep7,normalseed_rep8,doesgt);
            }
        });
    }

    private void submitFGTFinalCount(String userid, String scode, String sampleno, String noofrepsgt, String gmmtype, String obrstype1, String vegremark, String techremark,
                                     String normal_rep1, String abnormal_rep1, String dead_rep1, String normal_rep2, String abnormal_rep2, String dead_rep2, String normal_rep3,
                                     String abnormal_rep3, String dead_rep3, String normal_rep4, String abnormal_rep4, String dead_rep4, String normal_rep5, String abnormal_rep5,
                                     String dead_rep5, String normal_rep6, String abnormal_rep6, String dead_rep6, String normal_rep7, String abnormal_rep7, String dead_rep7,
                                     String normal_rep8, String abnormal_rep8, String dead_rep8, String normalseedper, String abnormalseedper, String deadseedper, String doefgt,
                                     String fgtmedia) {
        RequestBody userid1 = RequestBody.create(MediaType.parse("text/plain"), userid);
        RequestBody scode1 = RequestBody.create(MediaType.parse("text/plain"), scode);
        RequestBody sampleno1 = RequestBody.create(MediaType.parse("text/plain"), sampleno);
        RequestBody noofrepsgt1 = RequestBody.create(MediaType.parse("text/plain"), noofrepsgt);
        RequestBody gmmtype1 = RequestBody.create(MediaType.parse("text/plain"), gmmtype);
        RequestBody obrstype11 = RequestBody.create(MediaType.parse("text/plain"), obrstype1);
        RequestBody fgtmedia1 = RequestBody.create(MediaType.parse("text/plain"), fgtmedia);
        RequestBody vegremark1 = RequestBody.create(MediaType.parse("text/plain"), vegremark);
        RequestBody techremark1 = RequestBody.create(MediaType.parse("text/plain"), techremark);
        RequestBody normal_rep11 = RequestBody.create(MediaType.parse("text/plain"), normal_rep1);
        RequestBody abnormal_rep11 = RequestBody.create(MediaType.parse("text/plain"), abnormal_rep1);
        RequestBody dead_rep11 = RequestBody.create(MediaType.parse("text/plain"), dead_rep1);
        RequestBody normal_rep21 = RequestBody.create(MediaType.parse("text/plain"), normal_rep2);
        RequestBody abnormal_rep21 = RequestBody.create(MediaType.parse("text/plain"), abnormal_rep2);
        RequestBody dead_rep21 = RequestBody.create(MediaType.parse("text/plain"), dead_rep2);
        RequestBody normal_rep31 = RequestBody.create(MediaType.parse("text/plain"), normal_rep3);
        RequestBody abnormal_rep31 = RequestBody.create(MediaType.parse("text/plain"), abnormal_rep3);
        RequestBody dead_rep31 = RequestBody.create(MediaType.parse("text/plain"), dead_rep3);
        RequestBody normal_rep41 = RequestBody.create(MediaType.parse("text/plain"), normal_rep4);
        RequestBody abnormal_rep41 = RequestBody.create(MediaType.parse("text/plain"), abnormal_rep4);
        RequestBody dead_rep41 = RequestBody.create(MediaType.parse("text/plain"), dead_rep4);
        RequestBody normal_rep51 = RequestBody.create(MediaType.parse("text/plain"), normal_rep5);
        RequestBody abnormal_rep51 = RequestBody.create(MediaType.parse("text/plain"), abnormal_rep5);
        RequestBody dead_rep51 = RequestBody.create(MediaType.parse("text/plain"), dead_rep5);
        RequestBody normal_rep61 = RequestBody.create(MediaType.parse("text/plain"), normal_rep6);
        RequestBody abnormal_rep61 = RequestBody.create(MediaType.parse("text/plain"), abnormal_rep6);
        RequestBody dead_rep61 = RequestBody.create(MediaType.parse("text/plain"), dead_rep6);
        RequestBody normal_rep71 = RequestBody.create(MediaType.parse("text/plain"), normal_rep7);
        RequestBody abnormal_rep71 = RequestBody.create(MediaType.parse("text/plain"), abnormal_rep7);
        RequestBody dead_rep71 = RequestBody.create(MediaType.parse("text/plain"), dead_rep7);
        RequestBody normal_rep81 = RequestBody.create(MediaType.parse("text/plain"), normal_rep8);
        RequestBody abnormal_rep81 = RequestBody.create(MediaType.parse("text/plain"), abnormal_rep8);
        RequestBody dead_rep81 = RequestBody.create(MediaType.parse("text/plain"), dead_rep8);
        RequestBody normalseedper1 = RequestBody.create(MediaType.parse("text/plain"), normalseedper);
        RequestBody abnormalseedper1 = RequestBody.create(MediaType.parse("text/plain"), abnormalseedper);
        RequestBody deadseedper1 = RequestBody.create(MediaType.parse("text/plain"), deadseedper);
        RequestBody doefgt1 = RequestBody.create(MediaType.parse("text/plain"), doefgt);
        RequestBody retest1 = RequestBody.create(MediaType.parse("text/plain"), retest);
        String retest_reason = et_retest_reason.getText().toString().trim();
        RequestBody retest_reason1 = RequestBody.create(MediaType.parse("text/plain"), retest_reason);
        remarks = et_remark.getText().toString().trim();
        RequestBody remarks1 = RequestBody.create(MediaType.parse("text/plain"), remarks);
        int rdsample = radiosample.getCheckedRadioButtonId();
        RadioButton rdsamp = findViewById(rdsample);
        String retesttype = rdsamp.getText().toString();
        RequestBody retesttype1 = RequestBody.create(MediaType.parse("text/plain"), retesttype);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Call<SubmitSuccessResponse> call =apiInterface.submitFGTFinalCount(userid1,scode1,sampleno1,noofrepsgt1,gmmtype1,obrstype11,fgtmedia1,normal_rep11,normal_rep21,normal_rep31,normal_rep41,normal_rep51,normal_rep61,normal_rep71,normal_rep81,normalseedper1,abnormal_rep11,abnormal_rep21,abnormal_rep31,abnormal_rep41,abnormal_rep51,abnormal_rep61,abnormal_rep71,abnormal_rep81,abnormalseedper1,dead_rep11,dead_rep21,dead_rep31,dead_rep41,dead_rep51,dead_rep61,dead_rep71,dead_rep81,deadseedper1,doefgt1,vegremark1,techremark1,retest_reason1,retest1,retesttype1,remarks1, list);
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
                        Intent intent = new Intent(GerminationReadingFormActivity.this, GerminationHomeActivity.class);
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
                            Toast.makeText(GerminationReadingFormActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GerminationReadingFormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void submitFGTFinalCount_old(String userid, String scode, String sampleno, String noofrepsgt, String gmmtype, String obrstype1, String vegremark, String techremark, String normal_rep1, String abnormal_rep1, String dead_rep1, String normal_rep2, String abnormal_rep2, String dead_rep2, String normal_rep3, String abnormal_rep3, String dead_rep3, String normal_rep4, String abnormal_rep4, String dead_rep4, String normal_rep5, String abnormal_rep5, String dead_rep5, String normal_rep6, String abnormal_rep6, String dead_rep6, String normal_rep7, String abnormal_rep7, String dead_rep7, String normal_rep8, String abnormal_rep8, String dead_rep8, String normalseedper, String abnormalseedper, String deadseedper, String doefgt, String fgtmedia) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String retest_reason = et_retest_reason.getText().toString().trim();
        int rdsample = radiosample.getCheckedRadioButtonId();
        remarks = et_remark.getText().toString().trim();
        RadioButton rdsamp = findViewById(rdsample);
        String retesttype = rdsamp.getText().toString();
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("scode", scode);
        params.put("sampleno", sampleno);
        params.put("noofrepsgt", noofrepsgt);
        params.put("testtype", gmmtype);
        params.put("fgtfcfltype", obrstype1);
        params.put("fgtmtype", fgtmedia);
        params.put("fgtnormal1", normal_rep1);
        params.put("fgtnormal2", normal_rep2);
        params.put("fgtnormal3", normal_rep3);
        params.put("fgtnormal4", normal_rep4);
        params.put("fgtnormal5", normal_rep5);
        params.put("fgtnormal6", normal_rep6);
        params.put("fgtnormal7", normal_rep7);
        params.put("fgtnormal8", normal_rep8);
        params.put("fgtnormalavg", normalseedper);
        params.put("fgtabnormal1", abnormal_rep1);
        params.put("fgtabnormal2", abnormal_rep2);
        params.put("fgtabnormal3", abnormal_rep3);
        params.put("fgtabnormal4", abnormal_rep4);
        params.put("fgtabnormal5", abnormal_rep5);
        params.put("fgtabnormal6", abnormal_rep6);
        params.put("fgtabnormal7", abnormal_rep7);
        params.put("fgtabnormal8", abnormal_rep8);
        params.put("fgtabnormalavg", abnormalseedper);
        params.put("fgtdead1", dead_rep1);
        params.put("fgtdead2", dead_rep2);
        params.put("fgtdead3", dead_rep3);
        params.put("fgtdead4", dead_rep4);
        params.put("fgtdead5", dead_rep5);
        params.put("fgtdead6", dead_rep6);
        params.put("fgtdead7", dead_rep7);
        params.put("fgtdead8", dead_rep8);
        params.put("fgtdeadavg", deadseedper);
        params.put("fgtdt", doefgt);
        params.put("fgtvremark", vegremark);
        params.put("oprremark", techremark);
        params.put("retest_reason", retest_reason);
        params.put("retest", retest);
        params.put("retesttype", retesttype);
        params.put("remarks", remarks);

        new SendVolleyCall().executeProgram(GerminationReadingFormActivity.this, AppConfig.SAMPLEGERMINATIONSUBMIT_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitGermpData(response);
                if (message.equalsIgnoreCase("Success")){
                    Intent intent = new Intent(GerminationReadingFormActivity.this, GerminationHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
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

    private void submitFGTFC(String userid, String scode, String sampleno, String noofrepsgt, String gmmtype, String obrstype1, String normalseed_rep1, String normalseed_rep2, String normalseed_rep3, String normalseed_rep4, String normalseed_rep5, String normalseed_rep6, String normalseed_rep7, String normalseed_rep8, String doefgt, String fgtmedia) {
        RequestBody userid1 = RequestBody.create(MediaType.parse("text/plain"), userid);
        RequestBody scode1 = RequestBody.create(MediaType.parse("text/plain"), scode);
        RequestBody sampleno1 = RequestBody.create(MediaType.parse("text/plain"), sampleno);
        RequestBody noofrepsgt1 = RequestBody.create(MediaType.parse("text/plain"), noofrepsgt);
        RequestBody gmmtype1 = RequestBody.create(MediaType.parse("text/plain"), gmmtype);
        RequestBody obrstype11 = RequestBody.create(MediaType.parse("text/plain"), obrstype1);
        RequestBody normalseed_rep11 = RequestBody.create(MediaType.parse("text/plain"), normalseed_rep1);
        RequestBody normalseed_rep21 = RequestBody.create(MediaType.parse("text/plain"), normalseed_rep2);
        RequestBody normalseed_rep31 = RequestBody.create(MediaType.parse("text/plain"), normalseed_rep3);
        RequestBody normalseed_rep41 = RequestBody.create(MediaType.parse("text/plain"), normalseed_rep4);
        RequestBody normalseed_rep51 = RequestBody.create(MediaType.parse("text/plain"), normalseed_rep5);
        RequestBody normalseed_rep61 = RequestBody.create(MediaType.parse("text/plain"), normalseed_rep6);
        RequestBody normalseed_rep71 = RequestBody.create(MediaType.parse("text/plain"), normalseed_rep7);
        RequestBody normalseed_rep81 = RequestBody.create(MediaType.parse("text/plain"), normalseed_rep8);
        RequestBody doefgt1 = RequestBody.create(MediaType.parse("text/plain"), doefgt);
        RequestBody fgtmedia1 = RequestBody.create(MediaType.parse("text/plain"), fgtmedia);
        RequestBody retest1 = RequestBody.create(MediaType.parse("text/plain"), retest);
        RequestBody fgtoobnormalavg1 = RequestBody.create(MediaType.parse("text/plain"), "");
        String retest_reason = et_retest_reason.getText().toString().trim();
        RequestBody retest_reason1 = RequestBody.create(MediaType.parse("text/plain"), retest_reason);
        remarks = et_remark.getText().toString().trim();
        RequestBody remarks1 = RequestBody.create(MediaType.parse("text/plain"), remarks);
        int rdsample = radiosample.getCheckedRadioButtonId();
        RadioButton rdsamp = findViewById(rdsample);
        String retesttype = rdsamp.getText().toString();
        RequestBody retesttype1 = RequestBody.create(MediaType.parse("text/plain"), retesttype);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Call<SubmitSuccessResponse> call =apiInterface.submitFGTFC(userid1,scode1,sampleno1,noofrepsgt1,gmmtype1,obrstype11,fgtmedia1,normalseed_rep11,normalseed_rep21,normalseed_rep31,normalseed_rep41,normalseed_rep51,normalseed_rep61,normalseed_rep71,normalseed_rep81,fgtoobnormalavg1,doefgt1,retest_reason1,retest1,retesttype1,remarks1, list);
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
                        Intent intent = new Intent(GerminationReadingFormActivity.this, GerminationHomeActivity.class);
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
                            Toast.makeText(GerminationReadingFormActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GerminationReadingFormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void submitFGTFC_old(String userid, String scode, String sampleno, String noofrepsgt, String gmmtype, String obrstype1, String normalseed_rep1, String normalseed_rep2, String normalseed_rep3, String normalseed_rep4, String normalseed_rep5, String normalseed_rep6, String normalseed_rep7, String normalseed_rep8, String doefgt, String fgtmedia) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String retest_reason = et_retest_reason.getText().toString().trim();
        remarks = et_remark.getText().toString().trim();
        int rdsample = radiosample.getCheckedRadioButtonId();
        RadioButton rdsamp = findViewById(rdsample);
        String retesttype = rdsamp.getText().toString();
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("scode", scode);
        params.put("sampleno", sampleno);
        params.put("noofrepsgt", noofrepsgt);
        params.put("testtype", gmmtype);
        params.put("fgtfcfltype", obrstype1);
        params.put("fgtmtype", fgtmedia);
        params.put("fgtoobnormal1", normalseed_rep1);
        params.put("fgtoobnormal2", normalseed_rep2);
        params.put("fgtoobnormal3", normalseed_rep3);
        params.put("fgtoobnormal4", normalseed_rep4);
        params.put("fgtoobnormal5", normalseed_rep5);
        params.put("fgtoobnormal6", normalseed_rep6);
        params.put("fgtoobnormal7", normalseed_rep7);
        params.put("fgtoobnormal8", normalseed_rep8);
        params.put("fgtoobnormalavg", "");
        params.put("fgtoobnormaldt", doefgt);
        params.put("retest_reason", retest_reason);
        params.put("retest", retest);
        params.put("retesttype", retesttype);
        params.put("remarks", remarks);
        params.put("image", "");

        new SendVolleyCall().executeProgram(GerminationReadingFormActivity.this, AppConfig.SAMPLEGERMINATIONSUBMIT_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitGermpData(response);
                if (message.equalsIgnoreCase("Success")){
                    Intent intent = new Intent(GerminationReadingFormActivity.this, GerminationHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
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

    private void submitSGTFinalCount(String userid, String scode, String sampleno, String noofrepsgt, String gmmtype, String obrstype1, String vegremark, String techremark, String normal_rep1, String abnormal_rep1, String hardfug_rep1, String dead_rep1, String normal_rep2, String abnormal_rep2, String hardfug_rep2, String dead_rep2, String normal_rep3, String abnormal_rep3, String hardfug_rep3, String dead_rep3, String normal_rep4, String abnormal_rep4, String hardfug_rep4, String dead_rep4, String normal_rep5, String abnormal_rep5, String hardfug_rep5, String dead_rep5, String normal_rep6, String abnormal_rep6, String hardfug_rep6, String dead_rep6, String normal_rep7, String abnormal_rep7, String hardfug_rep7, String dead_rep7, String normal_rep8, String abnormal_rep8, String hardfug_rep8, String dead_rep8, String normalseedper, String abnormalseedper, String hardseedper, String deadseedper, String doesgt) {
        RequestBody userid1 = RequestBody.create(MediaType.parse("text/plain"), userid);
        RequestBody scode1 = RequestBody.create(MediaType.parse("text/plain"), scode);
        RequestBody sampleno1 = RequestBody.create(MediaType.parse("text/plain"), sampleno);
        RequestBody noofrepsgt1 = RequestBody.create(MediaType.parse("text/plain"), noofrepsgt);
        RequestBody gmmtype1 = RequestBody.create(MediaType.parse("text/plain"), gmmtype);
        RequestBody obrstype11 = RequestBody.create(MediaType.parse("text/plain"), obrstype1);
        RequestBody vegremark1 = RequestBody.create(MediaType.parse("text/plain"), vegremark);
        RequestBody techremark1 = RequestBody.create(MediaType.parse("text/plain"), techremark);
        RequestBody normal_rep11 = RequestBody.create(MediaType.parse("text/plain"), normal_rep1);
        RequestBody abnormal_rep11 = RequestBody.create(MediaType.parse("text/plain"), abnormal_rep1);
        RequestBody hardfug_rep11 = RequestBody.create(MediaType.parse("text/plain"), hardfug_rep1);
        RequestBody dead_rep11 = RequestBody.create(MediaType.parse("text/plain"), dead_rep1);
        RequestBody normal_rep21 = RequestBody.create(MediaType.parse("text/plain"), normal_rep2);
        RequestBody abnormal_rep21 = RequestBody.create(MediaType.parse("text/plain"), abnormal_rep2);
        RequestBody hardfug_rep21 = RequestBody.create(MediaType.parse("text/plain"), hardfug_rep2);
        RequestBody dead_rep21 = RequestBody.create(MediaType.parse("text/plain"), dead_rep2);
        RequestBody normal_rep31 = RequestBody.create(MediaType.parse("text/plain"), normal_rep3);
        RequestBody abnormal_rep31 = RequestBody.create(MediaType.parse("text/plain"), abnormal_rep3);
        RequestBody hardfug_rep31 = RequestBody.create(MediaType.parse("text/plain"), hardfug_rep3);
        RequestBody dead_rep31 = RequestBody.create(MediaType.parse("text/plain"), dead_rep3);
        RequestBody normal_rep41 = RequestBody.create(MediaType.parse("text/plain"), normal_rep4);
        RequestBody abnormal_rep41 = RequestBody.create(MediaType.parse("text/plain"), abnormal_rep4);
        RequestBody hardfug_rep41 = RequestBody.create(MediaType.parse("text/plain"), hardfug_rep4);
        RequestBody dead_rep41 = RequestBody.create(MediaType.parse("text/plain"), dead_rep4);
        RequestBody normal_rep51 = RequestBody.create(MediaType.parse("text/plain"), normal_rep5);
        RequestBody abnormal_rep51 = RequestBody.create(MediaType.parse("text/plain"), abnormal_rep5);
        RequestBody hardfug_rep51 = RequestBody.create(MediaType.parse("text/plain"), hardfug_rep5);
        RequestBody dead_rep51 = RequestBody.create(MediaType.parse("text/plain"), dead_rep5);
        RequestBody normal_rep61 = RequestBody.create(MediaType.parse("text/plain"), normal_rep6);
        RequestBody abnormal_rep61 = RequestBody.create(MediaType.parse("text/plain"), abnormal_rep6);
        RequestBody hardfug_rep61 = RequestBody.create(MediaType.parse("text/plain"), hardfug_rep6);
        RequestBody dead_rep61 = RequestBody.create(MediaType.parse("text/plain"), dead_rep6);
        RequestBody normal_rep71 = RequestBody.create(MediaType.parse("text/plain"), normal_rep7);
        RequestBody abnormal_rep71 = RequestBody.create(MediaType.parse("text/plain"), abnormal_rep7);
        RequestBody hardfug_rep71 = RequestBody.create(MediaType.parse("text/plain"), hardfug_rep7);
        RequestBody dead_rep71 = RequestBody.create(MediaType.parse("text/plain"), dead_rep7);
        RequestBody normal_rep81 = RequestBody.create(MediaType.parse("text/plain"), normal_rep8);
        RequestBody abnormal_rep81 = RequestBody.create(MediaType.parse("text/plain"), abnormal_rep8);
        RequestBody hardfug_rep81 = RequestBody.create(MediaType.parse("text/plain"), hardfug_rep8);
        RequestBody dead_rep81 = RequestBody.create(MediaType.parse("text/plain"), dead_rep8);
        RequestBody normalseedper1 = RequestBody.create(MediaType.parse("text/plain"), normalseedper);
        RequestBody abnormalseedper1 = RequestBody.create(MediaType.parse("text/plain"), abnormalseedper);
        RequestBody hardseedper1 = RequestBody.create(MediaType.parse("text/plain"), hardseedper);
        RequestBody deadseedper1 = RequestBody.create(MediaType.parse("text/plain"), deadseedper);
        RequestBody doesgt1 = RequestBody.create(MediaType.parse("text/plain"), doesgt);
        RequestBody retest1 = RequestBody.create(MediaType.parse("text/plain"), retest);
        String retest_reason = et_retest_reason.getText().toString().trim();
        RequestBody retest_reason1 = RequestBody.create(MediaType.parse("text/plain"), retest_reason);
        remarks = et_remark.getText().toString().trim();
        RequestBody remarks1 = RequestBody.create(MediaType.parse("text/plain"), remarks);
        int rdsample = radiosample.getCheckedRadioButtonId();
        RadioButton rdsamp = findViewById(rdsample);
        String retesttype = rdsamp.getText().toString();
        RequestBody retesttype1 = RequestBody.create(MediaType.parse("text/plain"), retesttype);

        Log.e("Params", userid+"="+scode+"="+sampleno+"="+noofrepsgt+"="+gmmtype+"="+obrstype1+"="+vegremark+"="+techremark+"="+normal_rep1+"="+abnormal_rep1+"="+hardfug_rep1+"="+dead_rep1+"="+normal_rep2+"="+abnormal_rep2+"="+hardfug_rep2+"="+dead_rep2+"="+normal_rep3+"="+abnormal_rep3+"="+hardfug_rep3+"="+dead_rep3+"="+normal_rep4+"="+abnormal_rep4+"="+hardfug_rep4+"="+dead_rep4+"="+normal_rep5+"="+abnormal_rep5+"="+hardfug_rep5+"="+dead_rep5+"="+normal_rep6+"="+abnormal_rep6+"="+hardfug_rep6+"="+dead_rep6+"="+normal_rep7+"="+abnormal_rep7+"="+hardfug_rep7+"="+dead_rep7+"="+normal_rep8+"="+abnormal_rep8+"="+hardfug_rep8+"="+dead_rep8+"="+normalseedper+"="+abnormalseedper+"="+hardseedper+"="+deadseedper+"="+doesgt);

        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Call<SubmitSuccessResponse> call =apiInterface.submitSGTFinalCount(userid1,scode1,sampleno1,noofrepsgt1,gmmtype1,obrstype11,normal_rep11,normal_rep21,normal_rep31,normal_rep41,normal_rep51,normal_rep61,normal_rep71,normal_rep81,normalseedper1,abnormal_rep11,abnormal_rep21,abnormal_rep31,abnormal_rep41,abnormal_rep51,abnormal_rep61,abnormal_rep71,abnormal_rep81,abnormalseedper1,hardfug_rep11,hardfug_rep21,hardfug_rep31,hardfug_rep41,hardfug_rep51,hardfug_rep61,hardfug_rep71,hardfug_rep81,hardseedper1,dead_rep11,dead_rep21,dead_rep31,dead_rep41,dead_rep51,dead_rep61,dead_rep71,dead_rep81,deadseedper1,doesgt1,vegremark1,techremark1,retest_reason1,retest1,retesttype1,remarks1, list);
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
                        Intent intent = new Intent(GerminationReadingFormActivity.this, GerminationHomeActivity.class);
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
                            Toast.makeText(GerminationReadingFormActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GerminationReadingFormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void submitSGTFinalCount_old(String userid, String scode, String sampleno, String noofrepsgt, String gmmtype, String obrstype1, String vegremark, String techremark, String normal_rep1, String abnormal_rep1, String hardfug_rep1, String dead_rep1, String normal_rep2, String abnormal_rep2, String hardfug_rep2, String dead_rep2, String normal_rep3, String abnormal_rep3, String hardfug_rep3, String dead_rep3, String normal_rep4, String abnormal_rep4, String hardfug_rep4, String dead_rep4, String normal_rep5, String abnormal_rep5, String hardfug_rep5, String dead_rep5, String normal_rep6, String abnormal_rep6, String hardfug_rep6, String dead_rep6, String normal_rep7, String abnormal_rep7, String hardfug_rep7, String dead_rep7, String normal_rep8, String abnormal_rep8, String hardfug_rep8, String dead_rep8, String normalseedper, String abnormalseedper, String hardseedper, String deadseedper, String doesgt) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String retest_reason = et_retest_reason.getText().toString().trim();
        remarks = et_remark.getText().toString().trim();
        int rdsample = radiosample.getCheckedRadioButtonId();
        RadioButton rdsamp = findViewById(rdsample);
        String retesttype = rdsamp.getText().toString();
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("scode", scode);
        params.put("sampleno", sampleno);
        params.put("noofrepsgt", noofrepsgt);
        params.put("testtype", gmmtype);
        params.put("sgtfcfltype", obrstype1);
        params.put("sgtnormal1", normal_rep1);
        params.put("sgtnormal2", normal_rep2);
        params.put("sgtnormal3", normal_rep3);
        params.put("sgtnormal4", normal_rep4);
        params.put("sgtnormal5", normal_rep5);
        params.put("sgtnormal6", normal_rep6);
        params.put("sgtnormal7", normal_rep7);
        params.put("sgtnormal8", normal_rep8);
        params.put("sgtnormalavg", normalseedper);
        params.put("sgtabnormal1", abnormal_rep1);
        params.put("sgtabnormal2", abnormal_rep2);
        params.put("sgtabnormal3", abnormal_rep3);
        params.put("sgtabnormal4", abnormal_rep4);
        params.put("sgtabnormal5", abnormal_rep5);
        params.put("sgtabnormal6", abnormal_rep6);
        params.put("sgtabnormal7", abnormal_rep7);
        params.put("sgtabnormal8", abnormal_rep8);
        params.put("sgtabnormalavg", abnormalseedper);
        params.put("sgthardfug1", hardfug_rep1);
        params.put("sgthardfug2", hardfug_rep2);
        params.put("sgthardfug3", hardfug_rep3);
        params.put("sgthardfug4", hardfug_rep4);
        params.put("sgthardfug5", hardfug_rep5);
        params.put("sgthardfug6", hardfug_rep6);
        params.put("sgthardfug7", hardfug_rep7);
        params.put("sgthardfug8", hardfug_rep8);
        params.put("sgthardfugavg", hardseedper);
        params.put("sgtdead1", dead_rep1);
        params.put("sgtdead2", dead_rep2);
        params.put("sgtdead3", dead_rep3);
        params.put("sgtdead4", dead_rep4);
        params.put("sgtdead5", dead_rep5);
        params.put("sgtdead6", dead_rep6);
        params.put("sgtdead7", dead_rep7);
        params.put("sgtdead8", dead_rep8);
        params.put("sgtdeadavg", deadseedper);
        params.put("sgtdt", doesgt);
        params.put("sgtvremark", vegremark);
        params.put("oprremark", techremark);
        params.put("retest_reason", retest_reason);
        params.put("retest", retest);
        params.put("retesttype", retesttype);
        params.put("remarks", remarks);

        new SendVolleyCall().executeProgram(GerminationReadingFormActivity.this, AppConfig.SAMPLEGERMINATIONSUBMIT_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitGermpData(response);
                if (message.equalsIgnoreCase("Success")){
                    Intent intent = new Intent(GerminationReadingFormActivity.this, GerminationHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
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

    private void submitSGTFC(String userid, String scode, String sampleno, String noofrepsgt, String gmmtype, String obrstype1, String normalseed_rep1, String normalseed_rep2, String normalseed_rep3, String normalseed_rep4, String normalseed_rep5, String normalseed_rep6, String normalseed_rep7, String normalseed_rep8, String doesgt) {
        RequestBody userid1 = RequestBody.create(MediaType.parse("text/plain"), userid);
        RequestBody scode1 = RequestBody.create(MediaType.parse("text/plain"), scode);
        RequestBody sampleno1 = RequestBody.create(MediaType.parse("text/plain"), sampleno);
        RequestBody noofrepsgt1 = RequestBody.create(MediaType.parse("text/plain"), noofrepsgt);
        RequestBody gmmtype1 = RequestBody.create(MediaType.parse("text/plain"), gmmtype);
        RequestBody obrstype11 = RequestBody.create(MediaType.parse("text/plain"), obrstype1);
        RequestBody normalseed_rep11 = RequestBody.create(MediaType.parse("text/plain"), normalseed_rep1);
        RequestBody normalseed_rep21 = RequestBody.create(MediaType.parse("text/plain"), normalseed_rep2);
        RequestBody normalseed_rep31 = RequestBody.create(MediaType.parse("text/plain"), normalseed_rep3);
        RequestBody normalseed_rep41 = RequestBody.create(MediaType.parse("text/plain"), normalseed_rep4);
        RequestBody normalseed_rep51 = RequestBody.create(MediaType.parse("text/plain"), normalseed_rep5);
        RequestBody normalseed_rep61 = RequestBody.create(MediaType.parse("text/plain"), normalseed_rep6);
        RequestBody normalseed_rep71 = RequestBody.create(MediaType.parse("text/plain"), normalseed_rep7);
        RequestBody normalseed_rep81 = RequestBody.create(MediaType.parse("text/plain"), normalseed_rep8);
        RequestBody doesgt1 = RequestBody.create(MediaType.parse("text/plain"), doesgt);
        RequestBody retest1 = RequestBody.create(MediaType.parse("text/plain"), retest);
        RequestBody sgtoobnormalavg1 = RequestBody.create(MediaType.parse("text/plain"), "");
        String retest_reason = et_retest_reason.getText().toString().trim();
        RequestBody retest_reason1 = RequestBody.create(MediaType.parse("text/plain"), retest_reason);
        remarks = et_remark.getText().toString().trim();
        RequestBody remarks1 = RequestBody.create(MediaType.parse("text/plain"), remarks);
        int rdsample = radiosample.getCheckedRadioButtonId();
        RadioButton rdsamp = findViewById(rdsample);
        String retesttype = rdsamp.getText().toString();
        RequestBody retesttype1 = RequestBody.create(MediaType.parse("text/plain"), retesttype);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Call<SubmitSuccessResponse> call =apiInterface.submitSGTFC(userid1,scode1,sampleno1,noofrepsgt1,gmmtype1,obrstype11,normalseed_rep11,normalseed_rep21,normalseed_rep31,normalseed_rep41,normalseed_rep51,normalseed_rep61,normalseed_rep71,normalseed_rep81,sgtoobnormalavg1,doesgt1,retest_reason1,retest1,retesttype1,remarks1, list);
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
                        Intent intent = new Intent(GerminationReadingFormActivity.this, GerminationHomeActivity.class);
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
                            Toast.makeText(GerminationReadingFormActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GerminationReadingFormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void submitSGTFC_old(String userid, String scode, String sampleno, String noofrepsgt, String gmmtype, String obrstype1, String normalseed_rep1, String normalseed_rep2, String normalseed_rep3, String normalseed_rep4, String normalseed_rep5, String normalseed_rep6, String normalseed_rep7, String normalseed_rep8, String doesgt) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        pDialog.setCancelable(false);
        String retest_reason = et_retest_reason.getText().toString().trim();
        remarks = et_remark.getText().toString().trim();
        int rdsample = radiosample.getCheckedRadioButtonId();
        RadioButton rdsamp = findViewById(rdsample);
        String retesttype = rdsamp.getText().toString();
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("scode", scode);
        params.put("sampleno", sampleno);
        params.put("noofrepsgt", noofrepsgt);
        params.put("testtype", gmmtype);
        params.put("sgtfcfltype", obrstype1);
        params.put("sgtoobnormal1", normalseed_rep1);
        params.put("sgtoobnormal2", normalseed_rep2);
        params.put("sgtoobnormal3", normalseed_rep3);
        params.put("sgtoobnormal4", normalseed_rep4);
        params.put("sgtoobnormal5", normalseed_rep5);
        params.put("sgtoobnormal6", normalseed_rep6);
        params.put("sgtoobnormal7", normalseed_rep7);
        params.put("sgtoobnormal8", normalseed_rep8);
        params.put("sgtoobnormalavg", "");
        params.put("sgtoobnormaldt", doesgt);
        params.put("retest_reason", retest_reason);
        params.put("retest", retest);
        params.put("retesttype", retesttype);
        params.put("remarks", remarks);
        params.put("image", "");

        new SendVolleyCall().executeProgram(GerminationReadingFormActivity.this, AppConfig.SAMPLEGERMINATIONSUBMIT_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitGermpData(response);
                if (message.equalsIgnoreCase("Success")){
                    Intent intent = new Intent(GerminationReadingFormActivity.this, GerminationHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void between_paper(View v)
    {
        ll_hardfug_rep1.setVisibility(View.VISIBLE);
        ll_hardfug_rep2.setVisibility(View.VISIBLE);
        ll_hardfug_rep3.setVisibility(View.VISIBLE);
        ll_hardfug_rep4.setVisibility(View.VISIBLE);
        ll_hardfug_rep5.setVisibility(View.VISIBLE);
        ll_hardfug_rep6.setVisibility(View.VISIBLE);
        ll_hardfug_rep7.setVisibility(View.VISIBLE);
        ll_hardfug_rep8.setVisibility(View.VISIBLE);
        ll_meadofhardseeds.setVisibility(View.VISIBLE);
        ll_medium.setVisibility(View.GONE);
        ll_observation.setVisibility(View.VISIBLE);
        ll_sgtdoe.setVisibility(View.VISIBLE);
        ll_sgtddoe.setVisibility(View.GONE);
        ll_fgtdoe.setVisibility(View.GONE);
        ll_noofrepsgt.setVisibility(View.VISIBLE);
        ll_noofrepsgtd.setVisibility(View.GONE);
        ll_noofrepfgt.setVisibility(View.GONE);
        finalNoofseedonerep = sampleGerminationInfo.getNoofseedinonerep();

        int rdGrpObrs = radioGrpObrs.getCheckedRadioButtonId();
        RadioButton germpObr = findViewById(rdGrpObrs);
        String obrType = germpObr.getText().toString();

        if (obrType.equalsIgnoreCase("First Count")){
            if (sampleGerminationInfo.getQcgSgtoneobflg()==1) {
                et_normalseed_rep1.setText(sampleGerminationInfo.getQcgSgtoobnormal1());
                et_normalseed_rep2.setText(sampleGerminationInfo.getQcgSgtoobnormal2());
                et_normalseed_rep3.setText(sampleGerminationInfo.getQcgSgtoobnormal3());
                et_normalseed_rep4.setText(sampleGerminationInfo.getQcgSgtoobnormal4());
                et_normalseed_rep5.setText(sampleGerminationInfo.getQcgSgtoobnormal5());
                et_normalseed_rep6.setText(sampleGerminationInfo.getQcgSgtoobnormal6());
                et_normalseed_rep7.setText(sampleGerminationInfo.getQcgSgtoobnormal7());
                et_normalseed_rep8.setText(sampleGerminationInfo.getQcgSgtoobnormal8());

                et_remark.setEnabled(false);
                et_normalseed_rep1.setEnabled(false);
                et_normalseed_rep2.setEnabled(false);
                et_normalseed_rep3.setEnabled(false);
                et_normalseed_rep4.setEnabled(false);
                et_normalseed_rep5.setEnabled(false);
                et_normalseed_rep6.setEnabled(false);
                et_normalseed_rep7.setEnabled(false);
                et_normalseed_rep8.setEnabled(false);
                if (sampleGerminationInfo.getQcgSgtremark()!=null){
                    et_remark.setText(sampleGerminationInfo.getQcgSgtremark());
                }

                btn_submit.setVisibility(View.GONE);
                button_photo.setVisibility(View.GONE);
                if (sampleGerminationInfo.getQcgSgtfcimage()!=null && !sampleGerminationInfo.getQcgSgtfcimage().equalsIgnoreCase("")){
                    Glide.with(GerminationReadingFormActivity.this)
                            .load(Uri.parse(AppConfig.IMAGE_URL + sampleGerminationInfo.getQcgSgtfcimage()))
                            .into(iv_photo);
                    button_photo.setVisibility(View.GONE);
                } else {
                    ll_germpPhoto.setVisibility(View.GONE);
                }
            }else {
                et_normalseed_rep1.setText("");
                et_normalseed_rep2.setText("");
                et_normalseed_rep3.setText("");
                et_normalseed_rep4.setText("");
                et_normalseed_rep5.setText("");
                et_normalseed_rep6.setText("");
                et_normalseed_rep7.setText("");
                et_normalseed_rep8.setText("");
                et_remark.setText("");

                if (sampleGerminationInfo.getQcgSgtflg()==1){
                    button_photo.setVisibility(View.GONE);
                    ll_germpPhoto.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.GONE);

                    et_normalseed_rep1.setEnabled(false);
                    et_normalseed_rep2.setEnabled(false);
                    et_normalseed_rep3.setEnabled(false);
                    et_normalseed_rep4.setEnabled(false);
                    et_normalseed_rep5.setEnabled(false);
                    et_normalseed_rep6.setEnabled(false);
                    et_normalseed_rep7.setEnabled(false);
                    et_normalseed_rep8.setEnabled(false);
                    et_remark.setEnabled(false);
                }else {
                    button_photo.setVisibility(View.VISIBLE);
                    ll_germpPhoto.setVisibility(View.VISIBLE);
                    btn_submit.setVisibility(View.VISIBLE);
                    iv_photo.setImageDrawable(null);

                    et_normalseed_rep1.setEnabled(true);
                    et_normalseed_rep2.setEnabled(true);
                    et_normalseed_rep3.setEnabled(true);
                    et_normalseed_rep4.setEnabled(true);
                    et_normalseed_rep5.setEnabled(true);
                    et_normalseed_rep6.setEnabled(true);
                    et_normalseed_rep7.setEnabled(true);
                    et_normalseed_rep8.setEnabled(true);
                    et_remark.setEnabled(true);
                }
            }
        } else {
            if(sampleGerminationInfo.getQcgSgtflg()==1) {
                et_normal_rep1.setText(sampleGerminationInfo.getQcgSgtnormal1());
                et_abnormal_rep1.setText(sampleGerminationInfo.getQcgSgtabnormal1());
                et_hardfug_rep1.setText(sampleGerminationInfo.getQcgSgthardfug1());
                et_dead_rep1.setText(sampleGerminationInfo.getQcgSgthardfug1());

                et_normal_rep2.setText(sampleGerminationInfo.getQcgSgtnormal2());
                et_abnormal_rep2.setText(sampleGerminationInfo.getQcgSgtabnormal2());
                et_hardfug_rep2.setText(sampleGerminationInfo.getQcgSgthardfug2());
                et_dead_rep2.setText(sampleGerminationInfo.getQcgSgtdead2());

                et_normal_rep3.setText(sampleGerminationInfo.getQcgSgtnormal3());
                et_abnormal_rep3.setText(sampleGerminationInfo.getQcgSgtabnormal3());
                et_hardfug_rep3.setText(sampleGerminationInfo.getQcgSgthardfug3());
                et_dead_rep3.setText(sampleGerminationInfo.getQcgSgtdead3());

                et_normal_rep4.setText(sampleGerminationInfo.getQcgSgtnormal4());
                et_abnormal_rep4.setText(sampleGerminationInfo.getQcgSgtabnormal4());
                et_hardfug_rep4.setText(sampleGerminationInfo.getQcgSgthardfug4());
                et_dead_rep4.setText(sampleGerminationInfo.getQcgSgtdead4());

                et_normal_rep5.setText(sampleGerminationInfo.getQcgSgtnormal5());
                et_abnormal_rep5.setText(sampleGerminationInfo.getQcgSgtabnormal5());
                et_hardfug_rep5.setText(sampleGerminationInfo.getQcgSgthardfug5());
                et_dead_rep5.setText(sampleGerminationInfo.getQcgSgtdead5());

                et_normal_rep6.setText(sampleGerminationInfo.getQcgSgtnormal6());
                et_abnormal_rep6.setText(sampleGerminationInfo.getQcgSgtabnormal6());
                et_hardfug_rep6.setText(sampleGerminationInfo.getQcgSgthardfug6());
                et_dead_rep6.setText(sampleGerminationInfo.getQcgSgtdead6());

                et_normal_rep7.setText(sampleGerminationInfo.getQcgSgtnormal7());
                et_abnormal_rep7.setText(sampleGerminationInfo.getQcgSgtabnormal7());
                et_hardfug_rep7.setText(sampleGerminationInfo.getQcgSgthardfug7());
                et_dead_rep7.setText(sampleGerminationInfo.getQcgSgtdead7());

                et_normal_rep8.setText(sampleGerminationInfo.getQcgSgtnormal8());
                et_abnormal_rep8.setText(sampleGerminationInfo.getQcgSgtabnormal8());
                et_hardfug_rep8.setText(sampleGerminationInfo.getQcgSgthardfug8());
                et_dead_rep8.setText(sampleGerminationInfo.getQcgSgtdead8());

                et_normalseedper.setText(sampleGerminationInfo.getQcgSgtnormalavg());
                et_abnormalseedper.setText(sampleGerminationInfo.getQcgSgtabnormalavg());
                et_hardseedper.setText(sampleGerminationInfo.getQcgSgthardfugavg());
                et_deadseedper.setText(sampleGerminationInfo.getQcgSgtdeadavg());

                checkBox_rssc.setEnabled(false);
                checkBox_prm.setEnabled(false);
                checkBox_vss.setEnabled(false);

                if (sampleGerminationInfo.getQcgOprremark().contains("PRM")){
                    checkBox_prm.setChecked(true);
                }else {
                    checkBox_prm.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgOprremark().contains("VSS")){
                    checkBox_vss.setChecked(true);
                }else {
                    checkBox_vss.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgOprremark().contains("RSSC")){
                    checkBox_rssc.setChecked(true);
                }else {
                    checkBox_rssc.setChecked(false);
                }

                spinner_vremark.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                et_vremark.setText(sampleGerminationInfo.getQcgSgtvremark());
                et_vremark.setVisibility(View.VISIBLE);
                et_remark.setEnabled(false);
                if (sampleGerminationInfo.getQcgSgtremark()!=null){
                    et_remark.setText(sampleGerminationInfo.getQcgSgtremark());
                }

                if (sampleGerminationInfo.getQcgSgtimage()!=null && !sampleGerminationInfo.getQcgSgtimage().equalsIgnoreCase("")){
                    Glide.with(GerminationReadingFormActivity.this)
                            .load(Uri.parse(AppConfig.IMAGE_URL + sampleGerminationInfo.getQcgSgtimage()))
                            .into(iv_photo);
                    button_photo.setVisibility(View.GONE);
                }else {
                    ll_germpPhoto.setVisibility(View.GONE);
                }
            }else {

                et_normal_rep1.setText("");
                et_abnormal_rep1.setText("");
                et_hardfug_rep1.setText("");
                et_dead_rep1.setText("");

                et_normal_rep2.setText("");
                et_abnormal_rep2.setText("");
                et_hardfug_rep2.setText("");
                et_dead_rep2.setText("");

                et_normal_rep3.setText("");
                et_abnormal_rep3.setText("");
                et_hardfug_rep3.setText("");
                et_dead_rep3.setText("");

                et_normal_rep4.setText("");
                et_abnormal_rep4.setText("");
                et_hardfug_rep4.setText("");
                et_dead_rep4.setText("");

                et_normal_rep5.setText("");
                et_abnormal_rep5.setText("");
                et_hardfug_rep5.setText("");
                et_dead_rep5.setText("");

                et_normal_rep6.setText("");
                et_abnormal_rep6.setText("");
                et_hardfug_rep6.setText("");
                et_dead_rep6.setText("");

                et_normal_rep7.setText("");
                et_abnormal_rep7.setText("");
                et_hardfug_rep7.setText("");
                et_dead_rep7.setText("");

                et_normal_rep8.setText("");
                et_abnormal_rep8.setText("");
                et_hardfug_rep8.setText("");
                et_dead_rep8.setText("");

                et_normalseedper.setText("");
                et_abnormalseedper.setText("");
                et_hardseedper.setText("");
                et_deadseedper.setText("");

                et_remark.setText("");

                et_normal_rep1.setEnabled(true);
                et_abnormal_rep1.setEnabled(true);
                et_hardfug_rep1.setEnabled(true);
                et_dead_rep1.setEnabled(true);
                et_normal_rep2.setEnabled(true);
                et_abnormal_rep2.setEnabled(true);
                et_hardfug_rep2.setEnabled(true);
                et_dead_rep2.setEnabled(true);
                et_normal_rep3.setEnabled(true);
                et_abnormal_rep3.setEnabled(true);
                et_hardfug_rep3.setEnabled(true);
                et_dead_rep3.setEnabled(true);
                et_normal_rep4.setEnabled(true);
                et_abnormal_rep4.setEnabled(true);
                et_hardfug_rep4.setEnabled(true);
                et_dead_rep4.setEnabled(true);
                et_normal_rep5.setEnabled(true);
                et_abnormal_rep5.setEnabled(true);
                et_hardfug_rep5.setEnabled(true);
                et_dead_rep5.setEnabled(true);
                et_normal_rep6.setEnabled(true);
                et_abnormal_rep6.setEnabled(true);
                et_hardfug_rep6.setEnabled(true);
                et_dead_rep6.setEnabled(true);
                et_normal_rep7.setEnabled(true);
                et_abnormal_rep7.setEnabled(true);
                et_hardfug_rep7.setEnabled(true);
                et_dead_rep7.setEnabled(true);
                et_normal_rep8.setEnabled(true);
                et_abnormal_rep8.setEnabled(true);
                et_hardfug_rep8.setEnabled(true);
                et_dead_rep8.setEnabled(true);
                et_remark.setEnabled(true);
                spinner_vremark.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                button_photo.setVisibility(View.VISIBLE);
                ll_germpPhoto.setVisibility(View.VISIBLE);
                et_vremark.setVisibility(View.GONE);
                String[] vremark_list = {"Good","Average","Poor"};
                ArrayAdapter<String> adapter_vremark = new ArrayAdapter<>(GerminationReadingFormActivity.this, android.R.layout.simple_spinner_item, vremark_list);
                adapter_vremark.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_vremark.setAdapter(adapter_vremark);

                iv_photo.setImageDrawable(null);

                checkBox_rssc.setChecked(false);
                checkBox_prm.setChecked(false);
                checkBox_vss.setChecked(false);
                checkBox_rssc.setEnabled(true);
                checkBox_prm.setEnabled(true);
                checkBox_vss.setEnabled(true);
            }
        }


        int noofseedsinonerep = sampleGerminationInfo.getNoofseedinonerep();
        if (rep_countsgt.equalsIgnoreCase("1")) {
            ll_replication2.setVisibility(View.GONE);
            ll_replication2_1.setVisibility(View.GONE);
            ll_replication2_2.setVisibility(View.GONE);
            ll_block34.setVisibility(View.GONE);
            ll_block5678.setVisibility(View.GONE);
            ll_normalrep2.setVisibility(View.GONE);
            ll_normalrep34.setVisibility(View.GONE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);
            et_totalnoofseeds.setText(String.valueOf(noofseedsinonerep));
        }else if (rep_countsgt.equalsIgnoreCase("2")){
            ll_replication2.setVisibility(View.VISIBLE);
            ll_replication2_1.setVisibility(View.VISIBLE);
            ll_replication2_2.setVisibility(View.VISIBLE);
            ll_block34.setVisibility(View.GONE);
            ll_block5678.setVisibility(View.GONE);
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.GONE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);
            int totalrep = noofseedsinonerep*2;
            et_totalnoofseeds.setText(String.valueOf(totalrep));
        }else if (rep_countsgt.equalsIgnoreCase("4")){
            ll_replication2.setVisibility(View.VISIBLE);
            ll_replication2_1.setVisibility(View.VISIBLE);
            ll_replication2_2.setVisibility(View.VISIBLE);
            ll_block34.setVisibility(View.VISIBLE);
            ll_block5678.setVisibility(View.GONE);
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.VISIBLE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);
            int totalrep = noofseedsinonerep*4;
            et_totalnoofseeds.setText(String.valueOf(totalrep));
        }else if (rep_countsgt.equalsIgnoreCase("8")){
            ll_replication2.setVisibility(View.VISIBLE);
            ll_replication2_1.setVisibility(View.VISIBLE);
            ll_replication2_2.setVisibility(View.VISIBLE);
            ll_block34.setVisibility(View.VISIBLE);
            ll_block5678.setVisibility(View.VISIBLE);
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.VISIBLE);
            ll_normalrep56.setVisibility(View.VISIBLE);
            ll_normalrep78.setVisibility(View.VISIBLE);
            int totalrep = noofseedsinonerep*8;
            et_totalnoofseeds.setText(String.valueOf(totalrep));
        }
    }

    public void sgt_with_sgtd(View v){
        ll_hardfug_rep1.setVisibility(View.VISIBLE);
        ll_hardfug_rep2.setVisibility(View.VISIBLE);
        ll_hardfug_rep3.setVisibility(View.VISIBLE);
        ll_hardfug_rep4.setVisibility(View.VISIBLE);
        ll_hardfug_rep5.setVisibility(View.VISIBLE);
        ll_hardfug_rep6.setVisibility(View.VISIBLE);
        ll_hardfug_rep7.setVisibility(View.VISIBLE);
        ll_hardfug_rep8.setVisibility(View.VISIBLE);
        ll_meadofhardseeds.setVisibility(View.VISIBLE);
        ll_medium.setVisibility(View.GONE);
        ll_observation.setVisibility(View.VISIBLE);
        ll_sgtdoe.setVisibility(View.GONE);
        ll_sgtddoe.setVisibility(View.VISIBLE);
        ll_fgtdoe.setVisibility(View.GONE);
        ll_noofrepsgt.setVisibility(View.GONE);
        ll_noofrepsgtd.setVisibility(View.VISIBLE);
        ll_noofrepfgt.setVisibility(View.GONE);
        finalNoofseedonerep = sampleGerminationInfo.getNoofseedinonerep();

        int rdGrpObrs = radioGrpObrs.getCheckedRadioButtonId();
        RadioButton germpObr = findViewById(rdGrpObrs);
        String obrType = germpObr.getText().toString();

        if (obrType.equalsIgnoreCase("First Count")){
            if (sampleGerminationInfo.getQcgSgtdoneobflg()==1){
                et_normalseed_rep1.setText(sampleGerminationInfo.getQcgSgtdoobnormal1());
                et_normalseed_rep2.setText(sampleGerminationInfo.getQcgSgtdoobnormal2());
                et_normalseed_rep3.setText(sampleGerminationInfo.getQcgSgtdoobnormal3());
                et_normalseed_rep4.setText(sampleGerminationInfo.getQcgSgtdoobnormal4());
                et_normalseed_rep5.setText(sampleGerminationInfo.getQcgSgtdoobnormal5());
                et_normalseed_rep6.setText(sampleGerminationInfo.getQcgSgdtoobnormal6());
                et_normalseed_rep7.setText(sampleGerminationInfo.getQcgSgtdoobnormal7());
                et_normalseed_rep8.setText(sampleGerminationInfo.getQcgSgdtoobnormal8());

                et_normalseed_rep1.setEnabled(false);
                et_normalseed_rep2.setEnabled(false);
                et_normalseed_rep3.setEnabled(false);
                et_normalseed_rep4.setEnabled(false);
                et_normalseed_rep5.setEnabled(false);
                et_normalseed_rep6.setEnabled(false);
                et_normalseed_rep7.setEnabled(false);
                et_normalseed_rep8.setEnabled(false);
                et_remark.setEnabled(false);

                btn_submit.setVisibility(View.GONE);
                button_photo.setVisibility(View.GONE);
                if (sampleGerminationInfo.getQcgSgtdfcremark()!=null){
                    et_remark.setText(sampleGerminationInfo.getQcgSgtdfcremark());
                }

                if (sampleGerminationInfo.getQcgSgtdfcimage()!=null && !sampleGerminationInfo.getQcgSgtdfcimage().equalsIgnoreCase("")){
                    Glide.with(GerminationReadingFormActivity.this)
                            .load(Uri.parse(AppConfig.IMAGE_URL + sampleGerminationInfo.getQcgSgtdfcimage()))
                            .into(iv_photo);
                }else {
                    ll_germpPhoto.setVisibility(View.GONE);
                }
            } else {
                et_normalseed_rep1.setText("");
                et_normalseed_rep2.setText("");
                et_normalseed_rep3.setText("");
                et_normalseed_rep4.setText("");
                et_normalseed_rep5.setText("");
                et_normalseed_rep6.setText("");
                et_normalseed_rep7.setText("");
                et_normalseed_rep8.setText("");
                et_remark.setText("");

                if (sampleGerminationInfo.getQcgSgtdflg()==1){
                    button_photo.setVisibility(View.GONE);
                    ll_germpPhoto.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.GONE);

                    et_normalseed_rep1.setEnabled(false);
                    et_normalseed_rep2.setEnabled(false);
                    et_normalseed_rep3.setEnabled(false);
                    et_normalseed_rep4.setEnabled(false);
                    et_normalseed_rep5.setEnabled(false);
                    et_normalseed_rep6.setEnabled(false);
                    et_normalseed_rep7.setEnabled(false);
                    et_normalseed_rep8.setEnabled(false);
                    et_remark.setEnabled(false);
                }else {
                    button_photo.setVisibility(View.VISIBLE);
                    ll_germpPhoto.setVisibility(View.VISIBLE);
                    btn_submit.setVisibility(View.VISIBLE);
                    iv_photo.setImageDrawable(null);

                    et_normalseed_rep1.setEnabled(true);
                    et_normalseed_rep2.setEnabled(true);
                    et_normalseed_rep3.setEnabled(true);
                    et_normalseed_rep4.setEnabled(true);
                    et_normalseed_rep5.setEnabled(true);
                    et_normalseed_rep6.setEnabled(true);
                    et_normalseed_rep7.setEnabled(true);
                    et_normalseed_rep8.setEnabled(true);
                    et_remark.setEnabled(true);
                }
            }
        } else {
            if(sampleGerminationInfo.getQcgSgtdflg()==1) {
                et_normal_rep1.setText(sampleGerminationInfo.getQcgSgtdnormal1());
                et_abnormal_rep1.setText(sampleGerminationInfo.getQcgSgtdabnormal1());
                et_hardfug_rep1.setText(sampleGerminationInfo.getQcgSgtdhardfug1());
                et_dead_rep1.setText(sampleGerminationInfo.getQcgSgtddead1());

                et_normal_rep2.setText(sampleGerminationInfo.getQcgSgtdnormal2());
                et_abnormal_rep2.setText(sampleGerminationInfo.getQcgSgtdabnormal2());
                et_hardfug_rep2.setText(sampleGerminationInfo.getQcgSgtdhardfug2());
                et_dead_rep2.setText(sampleGerminationInfo.getQcgSgtddead2());

                et_normal_rep3.setText(sampleGerminationInfo.getQcgSgtdnormal3());
                et_abnormal_rep3.setText(sampleGerminationInfo.getQcgSgtdabnormal3());
                et_hardfug_rep3.setText(sampleGerminationInfo.getQcgSgtdhardfug3());
                et_dead_rep3.setText(sampleGerminationInfo.getQcgSgtddead3());

                et_normal_rep4.setText(sampleGerminationInfo.getQcgSgtdnormal4());
                et_abnormal_rep4.setText(sampleGerminationInfo.getQcgSgtdabnormal4());
                et_hardfug_rep4.setText(sampleGerminationInfo.getQcgSgtdhardfug4());
                et_dead_rep4.setText(sampleGerminationInfo.getQcgSgtddead4());

                et_normal_rep5.setText(sampleGerminationInfo.getQcgSgtdnormal5());
                et_abnormal_rep5.setText(sampleGerminationInfo.getQcgSgtdabnormal5());
                et_hardfug_rep5.setText(sampleGerminationInfo.getQcgSgtdhardfug5());
                et_dead_rep5.setText(sampleGerminationInfo.getQcgSgtddead5());

                et_normal_rep6.setText(sampleGerminationInfo.getQcgSgtdnormal6());
                et_abnormal_rep6.setText(sampleGerminationInfo.getQcgSgtdabnormal6());
                et_hardfug_rep6.setText(sampleGerminationInfo.getQcgSgtdhardfug6());
                et_dead_rep6.setText(sampleGerminationInfo.getQcgSgtddead6());

                et_normal_rep7.setText(sampleGerminationInfo.getQcgSgtdnormal7());
                et_abnormal_rep7.setText(sampleGerminationInfo.getQcgSgtdabnormal7());
                et_hardfug_rep7.setText(sampleGerminationInfo.getQcgSgtdhardfug7());
                et_dead_rep7.setText(sampleGerminationInfo.getQcgSgtddead7());

                et_normal_rep8.setText(sampleGerminationInfo.getQcgSgtdnormal8());
                et_abnormal_rep8.setText(sampleGerminationInfo.getQcgSgtdabnormal8());
                et_hardfug_rep8.setText(sampleGerminationInfo.getQcgSgtdhardfug8());
                et_dead_rep8.setText(sampleGerminationInfo.getQcgSgtddead8());

                et_normalseedper.setText(sampleGerminationInfo.getQcgSgtdnormalavg());
                et_abnormalseedper.setText(sampleGerminationInfo.getQcgSgtdabnormalavg());
                et_hardseedper.setText(sampleGerminationInfo.getQcgSgtdhardfugavg());
                et_deadseedper.setText(sampleGerminationInfo.getQcgSgtddeadavg());

                checkBox_rssc.setEnabled(false);
                checkBox_prm.setEnabled(false);
                checkBox_vss.setEnabled(false);

                if (sampleGerminationInfo.getQcgSgtdoprremark().contains("PRM")){
                    checkBox_prm.setChecked(true);
                }else {
                    checkBox_prm.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgSgtdoprremark().contains("VSS")){
                    checkBox_vss.setChecked(true);
                }else {
                    checkBox_vss.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgSgtdoprremark().contains("RSSC")){
                    checkBox_rssc.setChecked(true);
                }else {
                    checkBox_rssc.setChecked(false);
                }

                spinner_vremark.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                et_vremark.setText(sampleGerminationInfo.getQcgSgtvremark());
                et_vremark.setVisibility(View.VISIBLE);
                et_remark.setEnabled(false);
                if (sampleGerminationInfo.getQcgSgtdremark()!=null){
                    et_remark.setText(sampleGerminationInfo.getQcgSgtdremark());
                }

                if (sampleGerminationInfo.getQcgSgtdimage()!=null && !sampleGerminationInfo.getQcgSgtdimage().equalsIgnoreCase("")){
                    Glide.with(GerminationReadingFormActivity.this)
                            .load(Uri.parse(AppConfig.IMAGE_URL + sampleGerminationInfo.getQcgSgtdimage()))
                            .into(iv_photo);
                    button_photo.setVisibility(View.GONE);
                }else {
                    ll_germpPhoto.setVisibility(View.GONE);
                }
            }else {
                et_normal_rep1.setText("");
                et_abnormal_rep1.setText("");
                et_hardfug_rep1.setText("");
                et_dead_rep1.setText("");

                et_normal_rep2.setText("");
                et_abnormal_rep2.setText("");
                et_hardfug_rep2.setText("");
                et_dead_rep2.setText("");

                et_normal_rep3.setText("");
                et_abnormal_rep3.setText("");
                et_hardfug_rep3.setText("");
                et_dead_rep3.setText("");

                et_normal_rep4.setText("");
                et_abnormal_rep4.setText("");
                et_hardfug_rep4.setText("");
                et_dead_rep4.setText("");

                et_normal_rep5.setText("");
                et_abnormal_rep5.setText("");
                et_hardfug_rep5.setText("");
                et_dead_rep5.setText("");

                et_normal_rep6.setText("");
                et_abnormal_rep6.setText("");
                et_hardfug_rep6.setText("");
                et_dead_rep6.setText("");

                et_normal_rep7.setText("");
                et_abnormal_rep7.setText("");
                et_hardfug_rep7.setText("");
                et_dead_rep7.setText("");

                et_normal_rep8.setText("");
                et_abnormal_rep8.setText("");
                et_hardfug_rep8.setText("");
                et_dead_rep8.setText("");

                et_normalseedper.setText("");
                et_abnormalseedper.setText("");
                et_hardseedper.setText("");
                et_deadseedper.setText("");

                et_remark.setText("");

                et_normal_rep1.setEnabled(true);
                et_abnormal_rep1.setEnabled(true);
                et_hardfug_rep1.setEnabled(true);
                et_dead_rep1.setEnabled(true);
                et_normal_rep2.setEnabled(true);
                et_abnormal_rep2.setEnabled(true);
                et_hardfug_rep2.setEnabled(true);
                et_dead_rep2.setEnabled(true);
                et_normal_rep3.setEnabled(true);
                et_abnormal_rep3.setEnabled(true);
                et_hardfug_rep3.setEnabled(true);
                et_dead_rep3.setEnabled(true);
                et_normal_rep4.setEnabled(true);
                et_abnormal_rep4.setEnabled(true);
                et_hardfug_rep4.setEnabled(true);
                et_dead_rep4.setEnabled(true);
                et_normal_rep5.setEnabled(true);
                et_abnormal_rep5.setEnabled(true);
                et_hardfug_rep5.setEnabled(true);
                et_dead_rep5.setEnabled(true);
                et_normal_rep6.setEnabled(true);
                et_abnormal_rep6.setEnabled(true);
                et_hardfug_rep6.setEnabled(true);
                et_dead_rep6.setEnabled(true);
                et_normal_rep7.setEnabled(true);
                et_abnormal_rep7.setEnabled(true);
                et_hardfug_rep7.setEnabled(true);
                et_dead_rep7.setEnabled(true);
                et_normal_rep8.setEnabled(true);
                et_abnormal_rep8.setEnabled(true);
                et_hardfug_rep8.setEnabled(true);
                et_dead_rep8.setEnabled(true);
                et_remark.setEnabled(true);
                spinner_vremark.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                button_photo.setVisibility(View.VISIBLE);
                ll_germpPhoto.setVisibility(View.VISIBLE);
                et_vremark.setVisibility(View.GONE);
                String[] vremark_list = {"Good","Average","Poor"};
                ArrayAdapter<String> adapter_vremark = new ArrayAdapter<>(GerminationReadingFormActivity.this, android.R.layout.simple_spinner_item, vremark_list);
                adapter_vremark.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_vremark.setAdapter(adapter_vremark);
                iv_photo.setImageDrawable(null);
                checkBox_rssc.setChecked(false);
                checkBox_prm.setChecked(false);
                checkBox_vss.setChecked(false);
                checkBox_rssc.setEnabled(true);
                checkBox_prm.setEnabled(true);
                checkBox_vss.setEnabled(true);
            }
        }

        int noofseedsinonerep = sampleGerminationInfo.getNoofseedinonerep();
        if (rep_countsgtd.equalsIgnoreCase("1")) {
            ll_replication2.setVisibility(View.GONE);
            ll_replication2_1.setVisibility(View.GONE);
            ll_replication2_2.setVisibility(View.GONE);
            ll_block34.setVisibility(View.GONE);
            ll_block5678.setVisibility(View.GONE);
            ll_normalrep2.setVisibility(View.GONE);
            ll_normalrep34.setVisibility(View.GONE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);
            et_totalnoofseeds.setText(String.valueOf(noofseedsinonerep));
        }else if (rep_countsgtd.equalsIgnoreCase("2")){
            ll_replication2.setVisibility(View.VISIBLE);
            ll_replication2_1.setVisibility(View.VISIBLE);
            ll_replication2_2.setVisibility(View.VISIBLE);
            ll_block34.setVisibility(View.GONE);
            ll_block5678.setVisibility(View.GONE);
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.GONE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);
            int totalrep = noofseedsinonerep*2;
            et_totalnoofseeds.setText(String.valueOf(totalrep));
        }else if (rep_countsgtd.equalsIgnoreCase("4")){
            ll_replication2.setVisibility(View.VISIBLE);
            ll_replication2_1.setVisibility(View.VISIBLE);
            ll_replication2_2.setVisibility(View.VISIBLE);
            ll_block34.setVisibility(View.VISIBLE);
            ll_block5678.setVisibility(View.GONE);
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.VISIBLE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);
            int totalrep = noofseedsinonerep*4;
            et_totalnoofseeds.setText(String.valueOf(totalrep));
        }else if (rep_countsgtd.equalsIgnoreCase("8")){
            ll_replication2.setVisibility(View.VISIBLE);
            ll_replication2_1.setVisibility(View.VISIBLE);
            ll_replication2_2.setVisibility(View.VISIBLE);
            ll_block34.setVisibility(View.VISIBLE);
            ll_block5678.setVisibility(View.VISIBLE);
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.VISIBLE);
            ll_normalrep56.setVisibility(View.VISIBLE);
            ll_normalrep78.setVisibility(View.VISIBLE);
            int totalrep = noofseedsinonerep*8;
            et_totalnoofseeds.setText(String.valueOf(totalrep));
        }
    }

    public void cocopeat_soil(View v)
    {
        ll_hardfug_rep1.setVisibility(View.GONE);
        ll_hardfug_rep2.setVisibility(View.GONE);
        ll_hardfug_rep3.setVisibility(View.GONE);
        ll_hardfug_rep4.setVisibility(View.GONE);
        ll_hardfug_rep5.setVisibility(View.GONE);
        ll_hardfug_rep6.setVisibility(View.GONE);
        ll_hardfug_rep7.setVisibility(View.GONE);
        ll_hardfug_rep8.setVisibility(View.GONE);
        ll_meadofhardseeds.setVisibility(View.GONE);
        ll_medium.setVisibility(View.VISIBLE);
        ll_sgtdoe.setVisibility(View.GONE);
        ll_sgtddoe.setVisibility(View.GONE);
        ll_fgtdoe.setVisibility(View.VISIBLE);
        ll_noofrepsgt.setVisibility(View.GONE);
        ll_noofrepsgtd.setVisibility(View.GONE);
        ll_noofrepfgt.setVisibility(View.VISIBLE);
        finalNoofseedonerep = sampleGerminationInfo.getNoofseedinonerepfgt();

        int rdGrpObrs = radioGrpObrs.getCheckedRadioButtonId();
        RadioButton germpObr = findViewById(rdGrpObrs);
        String obrType = germpObr.getText().toString();

        if (obrType.equalsIgnoreCase("First Count")){
            if (sampleGerminationInfo.getQcgVigoneobflg()==1){
                et_normalseed_rep1.setText(sampleGerminationInfo.getQcgVigoobnormal1());
                et_normalseed_rep2.setText(sampleGerminationInfo.getQcgVigoobnormal2());
                et_normalseed_rep3.setText(sampleGerminationInfo.getQcgVigoobnormal3());
                et_normalseed_rep4.setText(sampleGerminationInfo.getQcgVigoobnormal4());
                et_normalseed_rep5.setText(sampleGerminationInfo.getQcgVigoobnormal5());
                et_normalseed_rep6.setText(sampleGerminationInfo.getQcgVigoobnormal6());
                et_normalseed_rep7.setText(sampleGerminationInfo.getQcgVigoobnormal7());
                et_normalseed_rep8.setText(sampleGerminationInfo.getQcgVigoobnormal8());

                et_normalseed_rep1.setEnabled(false);
                et_normalseed_rep2.setEnabled(false);
                et_normalseed_rep3.setEnabled(false);
                et_normalseed_rep4.setEnabled(false);
                et_normalseed_rep5.setEnabled(false);
                et_normalseed_rep6.setEnabled(false);
                et_normalseed_rep7.setEnabled(false);
                et_normalseed_rep8.setEnabled(false);
                et_remark.setEnabled(false);

                btn_submit.setVisibility(View.GONE);
                button_photo.setVisibility(View.GONE);
                if (sampleGerminationInfo.getQcgFgtfcremark()!=null) {
                    et_remark.setText(sampleGerminationInfo.getQcgFgtfcremark());
                }

                if (sampleGerminationInfo.getQcgFgtfcimage()!=null && !sampleGerminationInfo.getQcgFgtfcimage().equalsIgnoreCase("")){
                    Glide.with(GerminationReadingFormActivity.this)
                            .load(Uri.parse(AppConfig.IMAGE_URL + sampleGerminationInfo.getQcgFgtfcimage()))
                            .into(iv_photo);
                }else {
                    ll_germpPhoto.setVisibility(View.GONE);
                }
            } else {
                et_normalseed_rep1.setText("");
                et_normalseed_rep2.setText("");
                et_normalseed_rep3.setText("");
                et_normalseed_rep4.setText("");
                et_normalseed_rep5.setText("");
                et_normalseed_rep6.setText("");
                et_normalseed_rep7.setText("");
                et_normalseed_rep8.setText("");
                et_remark.setText("");

                if (sampleGerminationInfo.getQcgVigflg()==1){
                    button_photo.setVisibility(View.GONE);
                    ll_germpPhoto.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.GONE);

                    et_normalseed_rep1.setEnabled(false);
                    et_normalseed_rep2.setEnabled(false);
                    et_normalseed_rep3.setEnabled(false);
                    et_normalseed_rep4.setEnabled(false);
                    et_normalseed_rep5.setEnabled(false);
                    et_normalseed_rep6.setEnabled(false);
                    et_normalseed_rep7.setEnabled(false);
                    et_normalseed_rep8.setEnabled(false);
                    et_remark.setEnabled(false);
                }else {
                    button_photo.setVisibility(View.VISIBLE);
                    ll_germpPhoto.setVisibility(View.VISIBLE);
                    btn_submit.setVisibility(View.VISIBLE);
                    iv_photo.setImageDrawable(null);

                    et_normalseed_rep1.setEnabled(true);
                    et_normalseed_rep2.setEnabled(true);
                    et_normalseed_rep3.setEnabled(true);
                    et_normalseed_rep4.setEnabled(true);
                    et_normalseed_rep5.setEnabled(true);
                    et_normalseed_rep6.setEnabled(true);
                    et_normalseed_rep7.setEnabled(true);
                    et_normalseed_rep8.setEnabled(true);
                    et_remark.setEnabled(true);
                }
            }
        } else {
            if(sampleGerminationInfo.getQcgVigflg()==1){
                et_normal_rep1.setText(sampleGerminationInfo.getQcgVignormal1());
                et_abnormal_rep1.setText(sampleGerminationInfo.getQcgVigabnormal1());
                et_dead_rep1.setText(sampleGerminationInfo.getQcgVigdead1());

                et_normal_rep2.setText(sampleGerminationInfo.getQcgVignormal2());
                et_abnormal_rep2.setText(sampleGerminationInfo.getQcgVigabnormal2());
                et_dead_rep2.setText(sampleGerminationInfo.getQcgVigdead2());

                et_normal_rep3.setText(sampleGerminationInfo.getQcgVignormal3());
                et_abnormal_rep3.setText(sampleGerminationInfo.getQcgVigabnormal3());
                et_dead_rep3.setText(sampleGerminationInfo.getQcgVigdead3());

                et_normal_rep4.setText(sampleGerminationInfo.getQcgVignormal4());
                et_abnormal_rep4.setText(sampleGerminationInfo.getQcgVigabnormal4());
                et_dead_rep4.setText(sampleGerminationInfo.getQcgVigdead4());

                et_normal_rep5.setText(sampleGerminationInfo.getQcgVignormal5());
                et_abnormal_rep5.setText(sampleGerminationInfo.getQcgVigabnormal5());
                et_dead_rep5.setText(sampleGerminationInfo.getQcgVigdead5());

                et_normal_rep6.setText(sampleGerminationInfo.getQcgVignormal6());
                et_abnormal_rep6.setText(sampleGerminationInfo.getQcgVigabnormal6());
                et_dead_rep6.setText(sampleGerminationInfo.getQcgVigdead6());

                et_normal_rep7.setText(sampleGerminationInfo.getQcgVignormal7());
                et_abnormal_rep7.setText(sampleGerminationInfo.getQcgVigabnormal7());
                et_dead_rep7.setText(sampleGerminationInfo.getQcgVigdead7());

                et_normal_rep8.setText(sampleGerminationInfo.getQcgVignormal8());
                et_abnormal_rep8.setText(sampleGerminationInfo.getQcgVigabnormal8());
                et_dead_rep8.setText(sampleGerminationInfo.getQcgVigdead8());

                et_normalseedper.setText(sampleGerminationInfo.getQcgVignormalavg());
                et_abnormalseedper.setText(sampleGerminationInfo.getQcgVigabnormalavg());
                et_deadseedper.setText(sampleGerminationInfo.getQcgVigdeadavg());

                checkBox_rssc.setEnabled(false);
                checkBox_prm.setEnabled(false);
                checkBox_vss.setEnabled(false);

                if (sampleGerminationInfo.getQcgFgtoprremark().contains("PRM")){
                    checkBox_prm.setChecked(true);
                }else {
                    checkBox_prm.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgFgtoprremark().contains("VSS")){
                    checkBox_vss.setChecked(true);
                }else {
                    checkBox_vss.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgFgtoprremark().contains("RSSC")){
                    checkBox_rssc.setChecked(true);
                }else {
                    checkBox_rssc.setChecked(false);
                }

                spinner_vremark.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                et_vremark.setText(sampleGerminationInfo.getQcgVigvremark());
                et_vremark.setVisibility(View.VISIBLE);
                if (sampleGerminationInfo.getQcgFgtremark()!=null) {
                    et_remark.setText(sampleGerminationInfo.getQcgFgtremark());
                }
                et_remark.setEnabled(false);

                if (sampleGerminationInfo.getQcgFgtimage()!=null && !sampleGerminationInfo.getQcgFgtimage().equalsIgnoreCase("")){
                    Glide.with(GerminationReadingFormActivity.this)
                            .load(Uri.parse(AppConfig.IMAGE_URL + sampleGerminationInfo.getQcgFgtimage()))
                            .into(iv_photo);
                    button_photo.setVisibility(View.GONE);
                }else {
                    ll_germpPhoto.setVisibility(View.GONE);
                }
            }else if(sampleGerminationInfo.getQcgVigflg()!=1) {
                et_normal_rep1.setText("");
                et_abnormal_rep1.setText("");
                et_hardfug_rep1.setText("");
                et_dead_rep1.setText("");

                et_normal_rep2.setText("");
                et_abnormal_rep2.setText("");
                et_hardfug_rep2.setText("");
                et_dead_rep2.setText("");

                et_normal_rep3.setText("");
                et_abnormal_rep3.setText("");
                et_hardfug_rep3.setText("");
                et_dead_rep3.setText("");

                et_normal_rep4.setText("");
                et_abnormal_rep4.setText("");
                et_hardfug_rep4.setText("");
                et_dead_rep4.setText("");

                et_normal_rep5.setText("");
                et_abnormal_rep5.setText("");
                et_hardfug_rep5.setText("");
                et_dead_rep5.setText("");

                et_normal_rep6.setText("");
                et_abnormal_rep6.setText("");
                et_hardfug_rep6.setText("");
                et_dead_rep6.setText("");

                et_normal_rep7.setText("");
                et_abnormal_rep7.setText("");
                et_hardfug_rep7.setText("");
                et_dead_rep7.setText("");

                et_normal_rep8.setText("");
                et_abnormal_rep8.setText("");
                et_hardfug_rep8.setText("");
                et_dead_rep8.setText("");

                et_normalseedper.setText("");
                et_abnormalseedper.setText("");
                et_hardseedper.setText("");
                et_deadseedper.setText("");

                et_remark.setText("");

                et_normal_rep1.setEnabled(true);
                et_abnormal_rep1.setEnabled(true);
                et_hardfug_rep1.setEnabled(true);
                et_dead_rep1.setEnabled(true);
                et_normal_rep2.setEnabled(true);
                et_abnormal_rep2.setEnabled(true);
                et_hardfug_rep2.setEnabled(true);
                et_dead_rep2.setEnabled(true);
                et_normal_rep3.setEnabled(true);
                et_abnormal_rep3.setEnabled(true);
                et_hardfug_rep3.setEnabled(true);
                et_dead_rep3.setEnabled(true);
                et_normal_rep4.setEnabled(true);
                et_abnormal_rep4.setEnabled(true);
                et_hardfug_rep4.setEnabled(true);
                et_dead_rep4.setEnabled(true);
                et_normal_rep5.setEnabled(true);
                et_abnormal_rep5.setEnabled(true);
                et_hardfug_rep5.setEnabled(true);
                et_dead_rep5.setEnabled(true);
                et_normal_rep6.setEnabled(true);
                et_abnormal_rep6.setEnabled(true);
                et_hardfug_rep6.setEnabled(true);
                et_dead_rep6.setEnabled(true);
                et_normal_rep7.setEnabled(true);
                et_abnormal_rep7.setEnabled(true);
                et_hardfug_rep7.setEnabled(true);
                et_dead_rep7.setEnabled(true);
                et_normal_rep8.setEnabled(true);
                et_abnormal_rep8.setEnabled(true);
                et_hardfug_rep8.setEnabled(true);
                et_dead_rep8.setEnabled(true);
                et_remark.setEnabled(true);

                spinner_vremark.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                et_vremark.setVisibility(View.GONE);
                button_photo.setVisibility(View.VISIBLE);
                ll_germpPhoto.setVisibility(View.VISIBLE);
                String[] vremark_list = {"Good","Average","Poor"};
                ArrayAdapter<String> adapter_vremark = new ArrayAdapter<>(GerminationReadingFormActivity.this, android.R.layout.simple_spinner_item, vremark_list);
                adapter_vremark.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_vremark.setAdapter(adapter_vremark);

                iv_photo.setImageDrawable(null);

                checkBox_rssc.setChecked(false);
                checkBox_prm.setChecked(false);
                checkBox_vss.setChecked(false);
                checkBox_rssc.setEnabled(true);
                checkBox_prm.setEnabled(true);
                checkBox_vss.setEnabled(true);
            }
        }

        int noofseedsinonerep = sampleGerminationInfo.getNoofseedinonerepfgt();
        if (rep_countfgt.equalsIgnoreCase("1")) {
            ll_replication2.setVisibility(View.GONE);
            ll_replication2_1.setVisibility(View.GONE);
            ll_replication2_2.setVisibility(View.GONE);
            ll_block34.setVisibility(View.GONE);
            ll_block5678.setVisibility(View.GONE);
            ll_normalrep2.setVisibility(View.GONE);
            ll_normalrep34.setVisibility(View.GONE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);
            et_totalnoofseeds.setText(String.valueOf(noofseedsinonerep));
        }else if (rep_countfgt.equalsIgnoreCase("2")){
            ll_replication2.setVisibility(View.VISIBLE);
            ll_replication2_1.setVisibility(View.VISIBLE);
            ll_replication2_2.setVisibility(View.VISIBLE);
            ll_block34.setVisibility(View.GONE);
            ll_block5678.setVisibility(View.GONE);
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.GONE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);
            int totalrep = noofseedsinonerep*2;
            et_totalnoofseeds.setText(String.valueOf(totalrep));
        }else if (rep_countfgt.equalsIgnoreCase("4")){
            ll_replication2.setVisibility(View.VISIBLE);
            ll_replication2_1.setVisibility(View.VISIBLE);
            ll_replication2_2.setVisibility(View.VISIBLE);
            ll_block34.setVisibility(View.VISIBLE);
            ll_block5678.setVisibility(View.GONE);
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.VISIBLE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);
            int totalrep = noofseedsinonerep*4;
            et_totalnoofseeds.setText(String.valueOf(totalrep));
        }else if (rep_countfgt.equalsIgnoreCase("8")){
            ll_replication2.setVisibility(View.VISIBLE);
            ll_replication2_1.setVisibility(View.VISIBLE);
            ll_replication2_2.setVisibility(View.VISIBLE);
            ll_block34.setVisibility(View.VISIBLE);
            ll_block5678.setVisibility(View.VISIBLE);
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.VISIBLE);
            ll_normalrep56.setVisibility(View.VISIBLE);
            ll_normalrep78.setVisibility(View.VISIBLE);
            int totalrep = noofseedsinonerep*8;
            et_totalnoofseeds.setText(String.valueOf(totalrep));
        }
    }

    public void final_observation(View v)
    {
        ll_both.setVisibility(View.VISIBLE);
        ll_germpPhoto.setVisibility(View.VISIBLE);
        ll_oneobs.setVisibility(View.GONE);
        ll_techremark.setVisibility(View.VISIBLE);
        int germmethod = radioGrpGerm.getCheckedRadioButtonId();
        RadioButton mois = findViewById(germmethod);
        String gmmtype = mois.getText().toString();
        if (gmmtype.equalsIgnoreCase("SGT")) {
            int noofseedsinonerep = sampleGerminationInfo.getNoofseedinonerep();
            if (rep_countsgt.equalsIgnoreCase("1")) {
                ll_normalrep2.setVisibility(View.GONE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                et_totalnoofseeds.setText(String.valueOf(noofseedsinonerep));
            } else if (rep_countsgt.equalsIgnoreCase("2")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep * 2;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            } else if (rep_countsgt.equalsIgnoreCase("4")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep * 4;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            } else if (rep_countsgt.equalsIgnoreCase("8")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.VISIBLE);
                ll_normalrep78.setVisibility(View.VISIBLE);
                int totalrep = noofseedsinonerep * 8;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }

            if (sampleGerminationInfo.getQcgSgtflg()==1){
                button_photo.setVisibility(View.GONE);
                if (sampleGerminationInfo.getQcgSgtimage()!=null && !sampleGerminationInfo.getQcgSgtimage().equalsIgnoreCase("")){
                    Glide.with(GerminationReadingFormActivity.this)
                            .load(Uri.parse(AppConfig.IMAGE_URL + sampleGerminationInfo.getQcgSgtimage()))
                            .into(iv_photo);
                }else {
                    ll_germpPhoto.setVisibility(View.GONE);
                }

                et_normal_rep1.setText(sampleGerminationInfo.getQcgSgtnormal1());
                et_abnormal_rep1.setText(sampleGerminationInfo.getQcgSgtabnormal1());
                et_hardfug_rep1.setText(sampleGerminationInfo.getQcgSgthardfug1());
                et_dead_rep1.setText(sampleGerminationInfo.getQcgSgthardfug1());

                et_normal_rep2.setText(sampleGerminationInfo.getQcgSgtnormal2());
                et_abnormal_rep2.setText(sampleGerminationInfo.getQcgSgtabnormal2());
                et_hardfug_rep2.setText(sampleGerminationInfo.getQcgSgthardfug2());
                et_dead_rep2.setText(sampleGerminationInfo.getQcgSgtdead2());

                et_normal_rep3.setText(sampleGerminationInfo.getQcgSgtnormal3());
                et_abnormal_rep3.setText(sampleGerminationInfo.getQcgSgtabnormal3());
                et_hardfug_rep3.setText(sampleGerminationInfo.getQcgSgthardfug3());
                et_dead_rep3.setText(sampleGerminationInfo.getQcgSgtdead3());

                et_normal_rep4.setText(sampleGerminationInfo.getQcgSgtnormal4());
                et_abnormal_rep4.setText(sampleGerminationInfo.getQcgSgtabnormal4());
                et_hardfug_rep4.setText(sampleGerminationInfo.getQcgSgthardfug4());
                et_dead_rep4.setText(sampleGerminationInfo.getQcgSgtdead4());

                et_normal_rep5.setText(sampleGerminationInfo.getQcgSgtnormal5());
                et_abnormal_rep5.setText(sampleGerminationInfo.getQcgSgtabnormal5());
                et_hardfug_rep5.setText(sampleGerminationInfo.getQcgSgthardfug5());
                et_dead_rep5.setText(sampleGerminationInfo.getQcgSgtdead5());

                et_normal_rep6.setText(sampleGerminationInfo.getQcgSgtnormal6());
                et_abnormal_rep6.setText(sampleGerminationInfo.getQcgSgtabnormal6());
                et_hardfug_rep6.setText(sampleGerminationInfo.getQcgSgthardfug6());
                et_dead_rep6.setText(sampleGerminationInfo.getQcgSgtdead6());

                et_normal_rep7.setText(sampleGerminationInfo.getQcgSgtnormal7());
                et_abnormal_rep7.setText(sampleGerminationInfo.getQcgSgtabnormal7());
                et_hardfug_rep7.setText(sampleGerminationInfo.getQcgSgthardfug7());
                et_dead_rep7.setText(sampleGerminationInfo.getQcgSgtdead7());

                et_normal_rep8.setText(sampleGerminationInfo.getQcgSgtnormal8());
                et_abnormal_rep8.setText(sampleGerminationInfo.getQcgSgtabnormal8());
                et_hardfug_rep8.setText(sampleGerminationInfo.getQcgSgthardfug8());
                et_dead_rep8.setText(sampleGerminationInfo.getQcgSgtdead8());

                et_normalseedper.setText(sampleGerminationInfo.getQcgSgtnormalavg());
                et_abnormalseedper.setText(sampleGerminationInfo.getQcgSgtabnormalavg());
                et_hardseedper.setText(sampleGerminationInfo.getQcgSgthardfugavg());
                et_deadseedper.setText(sampleGerminationInfo.getQcgSgtdeadavg());

                checkBox_rssc.setEnabled(false);
                checkBox_prm.setEnabled(false);
                checkBox_vss.setEnabled(false);

                if (sampleGerminationInfo.getQcgOprremark().contains("PRM")){
                    checkBox_prm.setChecked(true);
                }else {
                    checkBox_prm.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgOprremark().contains("VSS")){
                    checkBox_vss.setChecked(true);
                }else {
                    checkBox_vss.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgOprremark().contains("RSSC")){
                    checkBox_rssc.setChecked(true);
                }else {
                    checkBox_rssc.setChecked(false);
                }

                et_normal_rep1.setEnabled(false);
                et_abnormal_rep1.setEnabled(false);
                et_hardfug_rep1.setEnabled(false);
                et_dead_rep1.setEnabled(false);
                et_normal_rep2.setEnabled(false);
                et_abnormal_rep2.setEnabled(false);
                et_hardfug_rep2.setEnabled(false);
                et_dead_rep2.setEnabled(false);
                et_normal_rep3.setEnabled(false);
                et_abnormal_rep3.setEnabled(false);
                et_hardfug_rep3.setEnabled(false);
                et_dead_rep3.setEnabled(false);
                et_normal_rep4.setEnabled(false);
                et_abnormal_rep4.setEnabled(false);
                et_hardfug_rep4.setEnabled(false);
                et_dead_rep4.setEnabled(false);
                et_normal_rep5.setEnabled(false);
                et_abnormal_rep5.setEnabled(false);
                et_hardfug_rep5.setEnabled(false);
                et_dead_rep5.setEnabled(false);
                et_normal_rep6.setEnabled(false);
                et_abnormal_rep6.setEnabled(false);
                et_hardfug_rep6.setEnabled(false);
                et_dead_rep6.setEnabled(false);
                et_normal_rep7.setEnabled(false);
                et_abnormal_rep7.setEnabled(false);
                et_hardfug_rep7.setEnabled(false);
                et_dead_rep7.setEnabled(false);
                et_normal_rep8.setEnabled(false);
                et_abnormal_rep8.setEnabled(false);
                et_hardfug_rep8.setEnabled(false);
                et_dead_rep8.setEnabled(false);
                et_normalseedper.setEnabled(false);
                et_abnormalseedper.setEnabled(false);
                et_hardseedper.setEnabled(false);
                et_deadseedper.setEnabled(false);

                spinner_vremark.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                et_vremark.setText(sampleGerminationInfo.getQcgSgtvremark());
                et_vremark.setVisibility(View.VISIBLE);
                et_remark.setText(sampleGerminationInfo.getQcgSgtremark());
                et_remark.setEnabled(false);
            }else {
                button_photo.setVisibility(View.VISIBLE);
                ll_germpPhoto.setVisibility(View.VISIBLE);

                et_normal_rep1.setText("");
                et_abnormal_rep1.setText("");
                et_hardfug_rep1.setText("");
                et_dead_rep1.setText("");

                et_normal_rep2.setText("");
                et_abnormal_rep2.setText("");
                et_hardfug_rep2.setText("");
                et_dead_rep2.setText("");

                et_normal_rep3.setText("");
                et_abnormal_rep3.setText("");
                et_hardfug_rep3.setText("");
                et_dead_rep3.setText("");

                et_normal_rep4.setText("");
                et_abnormal_rep4.setText("");
                et_hardfug_rep4.setText("");
                et_dead_rep4.setText("");

                et_normal_rep5.setText("");
                et_abnormal_rep5.setText("");
                et_hardfug_rep5.setText("");
                et_dead_rep5.setText("");

                et_normal_rep6.setText("");
                et_abnormal_rep6.setText("");
                et_hardfug_rep6.setText("");
                et_dead_rep6.setText("");

                et_normal_rep7.setText("");
                et_abnormal_rep7.setText("");
                et_hardfug_rep7.setText("");
                et_dead_rep7.setText("");

                et_normal_rep8.setText("");
                et_abnormal_rep8.setText("");
                et_hardfug_rep8.setText("");
                et_dead_rep8.setText("");

                et_normalseedper.setText("");
                et_abnormalseedper.setText("");
                et_hardseedper.setText("");
                et_deadseedper.setText("");

                et_remark.setText("");

                et_normal_rep1.setEnabled(true);
                et_abnormal_rep1.setEnabled(true);
                et_hardfug_rep1.setEnabled(true);
                et_dead_rep1.setEnabled(true);
                et_normal_rep2.setEnabled(true);
                et_abnormal_rep2.setEnabled(true);
                et_hardfug_rep2.setEnabled(true);
                et_dead_rep2.setEnabled(true);
                et_normal_rep3.setEnabled(true);
                et_abnormal_rep3.setEnabled(true);
                et_hardfug_rep3.setEnabled(true);
                et_dead_rep3.setEnabled(true);
                et_normal_rep4.setEnabled(true);
                et_abnormal_rep4.setEnabled(true);
                et_hardfug_rep4.setEnabled(true);
                et_dead_rep4.setEnabled(true);
                et_normal_rep5.setEnabled(true);
                et_abnormal_rep5.setEnabled(true);
                et_hardfug_rep5.setEnabled(true);
                et_dead_rep5.setEnabled(true);
                et_normal_rep6.setEnabled(true);
                et_abnormal_rep6.setEnabled(true);
                et_hardfug_rep6.setEnabled(true);
                et_dead_rep6.setEnabled(true);
                et_normal_rep7.setEnabled(true);
                et_abnormal_rep7.setEnabled(true);
                et_hardfug_rep7.setEnabled(true);
                et_dead_rep7.setEnabled(true);
                et_normal_rep8.setEnabled(true);
                et_abnormal_rep8.setEnabled(true);
                et_hardfug_rep8.setEnabled(true);
                et_dead_rep8.setEnabled(true);
                spinner_vremark.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                et_vremark.setVisibility(View.GONE);
                et_remark.setEnabled(true);
                iv_photo.setImageDrawable(null);
                String[] vremark_list = {"Good","Average","Poor"};
                ArrayAdapter<String> adapter_vremark = new ArrayAdapter<>(GerminationReadingFormActivity.this, android.R.layout.simple_spinner_item, vremark_list);
                adapter_vremark.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_vremark.setAdapter(adapter_vremark);
            }
        }else if (gmmtype.equalsIgnoreCase("FGT")) {
            int noofseedsinonerep = sampleGerminationInfo.getNoofseedinonerepfgt();
            if (rep_countfgt.equalsIgnoreCase("1")) {
                ll_normalrep2.setVisibility(View.GONE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                et_totalnoofseeds.setText(String.valueOf(noofseedsinonerep));
            } else if (rep_countfgt.equalsIgnoreCase("2")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep * 2;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            } else if (rep_countfgt.equalsIgnoreCase("4")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep * 4;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            } else if (rep_countfgt.equalsIgnoreCase("8")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.VISIBLE);
                ll_normalrep78.setVisibility(View.VISIBLE);
                int totalrep = noofseedsinonerep * 8;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }

            if (sampleGerminationInfo.getQcgVigflg()==1){
                button_photo.setVisibility(View.GONE);
                if (sampleGerminationInfo.getQcgFgtimage()!=null && !sampleGerminationInfo.getQcgFgtimage().equalsIgnoreCase("")){
                    Glide.with(GerminationReadingFormActivity.this)
                            .load(Uri.parse(AppConfig.IMAGE_URL + sampleGerminationInfo.getQcgFgtimage()))
                            .into(iv_photo);
                }else {
                    ll_germpPhoto.setVisibility(View.GONE);
                }

                et_normal_rep1.setText(sampleGerminationInfo.getQcgVignormal1());
                et_abnormal_rep1.setText(sampleGerminationInfo.getQcgVigabnormal1());
                et_dead_rep1.setText(sampleGerminationInfo.getQcgVigdead1());

                et_normal_rep2.setText(sampleGerminationInfo.getQcgVignormal2());
                et_abnormal_rep2.setText(sampleGerminationInfo.getQcgVigabnormal2());
                et_dead_rep2.setText(sampleGerminationInfo.getQcgVigdead2());

                et_normal_rep3.setText(sampleGerminationInfo.getQcgVignormal3());
                et_abnormal_rep3.setText(sampleGerminationInfo.getQcgVigabnormal3());
                et_dead_rep3.setText(sampleGerminationInfo.getQcgVigdead3());

                et_normal_rep4.setText(sampleGerminationInfo.getQcgVignormal4());
                et_abnormal_rep4.setText(sampleGerminationInfo.getQcgVigabnormal4());
                et_dead_rep4.setText(sampleGerminationInfo.getQcgVigdead4());

                et_normal_rep5.setText(sampleGerminationInfo.getQcgVignormal5());
                et_abnormal_rep5.setText(sampleGerminationInfo.getQcgVigabnormal5());
                et_dead_rep5.setText(sampleGerminationInfo.getQcgVigdead5());

                et_normal_rep6.setText(sampleGerminationInfo.getQcgVignormal6());
                et_abnormal_rep6.setText(sampleGerminationInfo.getQcgVigabnormal6());
                et_dead_rep6.setText(sampleGerminationInfo.getQcgVigdead6());

                et_normal_rep7.setText(sampleGerminationInfo.getQcgVignormal7());
                et_abnormal_rep7.setText(sampleGerminationInfo.getQcgVigabnormal7());
                et_dead_rep7.setText(sampleGerminationInfo.getQcgVigdead7());

                et_normal_rep8.setText(sampleGerminationInfo.getQcgVignormal8());
                et_abnormal_rep8.setText(sampleGerminationInfo.getQcgVigabnormal8());
                et_dead_rep8.setText(sampleGerminationInfo.getQcgVigdead8());

                et_normalseedper.setText(sampleGerminationInfo.getQcgVignormalavg());
                et_abnormalseedper.setText(sampleGerminationInfo.getQcgVigabnormalavg());
                et_deadseedper.setText(sampleGerminationInfo.getQcgVigdeadavg());

                checkBox_rssc.setEnabled(false);
                checkBox_prm.setEnabled(false);
                checkBox_vss.setEnabled(false);

                if (sampleGerminationInfo.getQcgFgtoprremark().contains("PRM")){
                    checkBox_prm.setChecked(true);
                } else {
                    checkBox_prm.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgFgtoprremark().contains("VSS")){
                    checkBox_vss.setChecked(true);
                }else {
                    checkBox_vss.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgFgtoprremark().contains("RSSC")){
                    checkBox_rssc.setChecked(true);
                }else {
                    checkBox_rssc.setChecked(false);
                }

                et_normal_rep1.setEnabled(false);
                et_abnormal_rep1.setEnabled(false);
                et_hardfug_rep1.setEnabled(false);
                et_dead_rep1.setEnabled(false);
                et_normal_rep2.setEnabled(false);
                et_abnormal_rep2.setEnabled(false);
                et_hardfug_rep2.setEnabled(false);
                et_dead_rep2.setEnabled(false);
                et_normal_rep3.setEnabled(false);
                et_abnormal_rep3.setEnabled(false);
                et_hardfug_rep3.setEnabled(false);
                et_dead_rep3.setEnabled(false);
                et_normal_rep4.setEnabled(false);
                et_abnormal_rep4.setEnabled(false);
                et_hardfug_rep4.setEnabled(false);
                et_dead_rep4.setEnabled(false);
                et_normal_rep5.setEnabled(false);
                et_abnormal_rep5.setEnabled(false);
                et_hardfug_rep5.setEnabled(false);
                et_dead_rep5.setEnabled(false);
                et_normal_rep6.setEnabled(false);
                et_abnormal_rep6.setEnabled(false);
                et_hardfug_rep6.setEnabled(false);
                et_dead_rep6.setEnabled(false);
                et_normal_rep7.setEnabled(false);
                et_abnormal_rep7.setEnabled(false);
                et_hardfug_rep7.setEnabled(false);
                et_dead_rep7.setEnabled(false);
                et_normal_rep8.setEnabled(false);
                et_abnormal_rep8.setEnabled(false);
                et_hardfug_rep8.setEnabled(false);
                et_dead_rep8.setEnabled(false);
                et_normalseedper.setEnabled(false);
                et_abnormalseedper.setEnabled(false);
                et_hardseedper.setEnabled(false);
                et_deadseedper.setEnabled(false);

                spinner_vremark.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                et_vremark.setText(sampleGerminationInfo.getQcgVigvremark());
                et_vremark.setVisibility(View.VISIBLE);
                et_remark.setText(sampleGerminationInfo.getQcgFgtremark());
                et_remark.setEnabled(false);
            }else {
                button_photo.setVisibility(View.VISIBLE);
                ll_germpPhoto.setVisibility(View.VISIBLE);

                et_normal_rep1.setText("");
                et_abnormal_rep1.setText("");
                et_dead_rep1.setText("");

                et_normal_rep2.setText("");
                et_abnormal_rep2.setText("");
                et_dead_rep2.setText("");

                et_normal_rep3.setText("");
                et_abnormal_rep3.setText("");
                et_dead_rep3.setText("");

                et_normal_rep4.setText("");
                et_abnormal_rep4.setText("");
                et_dead_rep4.setText("");

                et_normal_rep5.setText("");
                et_abnormal_rep5.setText("");
                et_dead_rep5.setText("");

                et_normal_rep6.setText("");
                et_abnormal_rep6.setText("");
                et_dead_rep6.setText("");

                et_normal_rep7.setText("");
                et_abnormal_rep7.setText("");
                et_dead_rep7.setText("");

                et_normal_rep8.setText("");
                et_abnormal_rep8.setText("");
                et_dead_rep8.setText("");

                et_normalseedper.setText("");
                et_abnormalseedper.setText("");
                et_deadseedper.setText("");

                et_remark.setText("");

                et_normal_rep1.setEnabled(true);
                et_abnormal_rep1.setEnabled(true);
                et_hardfug_rep1.setEnabled(true);
                et_dead_rep1.setEnabled(true);
                et_normal_rep2.setEnabled(true);
                et_abnormal_rep2.setEnabled(true);
                et_hardfug_rep2.setEnabled(true);
                et_dead_rep2.setEnabled(true);
                et_normal_rep3.setEnabled(true);
                et_abnormal_rep3.setEnabled(true);
                et_hardfug_rep3.setEnabled(true);
                et_dead_rep3.setEnabled(true);
                et_normal_rep4.setEnabled(true);
                et_abnormal_rep4.setEnabled(true);
                et_hardfug_rep4.setEnabled(true);
                et_dead_rep4.setEnabled(true);
                et_normal_rep5.setEnabled(true);
                et_abnormal_rep5.setEnabled(true);
                et_hardfug_rep5.setEnabled(true);
                et_dead_rep5.setEnabled(true);
                et_normal_rep6.setEnabled(true);
                et_abnormal_rep6.setEnabled(true);
                et_hardfug_rep6.setEnabled(true);
                et_dead_rep6.setEnabled(true);
                et_normal_rep7.setEnabled(true);
                et_abnormal_rep7.setEnabled(true);
                et_hardfug_rep7.setEnabled(true);
                et_dead_rep7.setEnabled(true);
                et_normal_rep8.setEnabled(true);
                et_abnormal_rep8.setEnabled(true);
                et_hardfug_rep8.setEnabled(true);
                et_dead_rep8.setEnabled(true);
                spinner_vremark.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                et_vremark.setVisibility(View.GONE);
                et_remark.setEnabled(true);
                iv_photo.setImageDrawable(null);
                String[] vremark_list = {"Good","Average","Poor"};
                ArrayAdapter<String> adapter_vremark = new ArrayAdapter<>(GerminationReadingFormActivity.this, android.R.layout.simple_spinner_item, vremark_list);
                adapter_vremark.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_vremark.setAdapter(adapter_vremark);
            }
        }else if (gmmtype.equalsIgnoreCase("SGT with SGTD")) {
            int noofseedsinonerep = sampleGerminationInfo.getNoofseedinonerep();
            if (rep_countsgtd.equalsIgnoreCase("1")) {
                ll_normalrep2.setVisibility(View.GONE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                et_totalnoofseeds.setText(String.valueOf(noofseedsinonerep));
            } else if (rep_countsgtd.equalsIgnoreCase("2")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep * 2;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            } else if (rep_countsgtd.equalsIgnoreCase("4")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep * 4;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            } else if (rep_countsgtd.equalsIgnoreCase("8")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.VISIBLE);
                ll_normalrep78.setVisibility(View.VISIBLE);
                int totalrep = noofseedsinonerep * 8;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }

            if (sampleGerminationInfo.getQcgSgtdflg()==1){
                button_photo.setVisibility(View.GONE);
                if (sampleGerminationInfo.getQcgSgtdimage()!=null && !sampleGerminationInfo.getQcgSgtdimage().equalsIgnoreCase("")){
                    Glide.with(GerminationReadingFormActivity.this)
                            .load(Uri.parse(AppConfig.IMAGE_URL + sampleGerminationInfo.getQcgSgtdimage()))
                            .into(iv_photo);
                }else {
                    ll_germpPhoto.setVisibility(View.GONE);
                }

                et_normal_rep1.setText(sampleGerminationInfo.getQcgSgtdnormal1());
                et_abnormal_rep1.setText(sampleGerminationInfo.getQcgSgtdabnormal1());
                et_hardfug_rep1.setText(sampleGerminationInfo.getQcgSgtdhardfug1());
                et_dead_rep1.setText(sampleGerminationInfo.getQcgSgtddead1());

                et_normal_rep2.setText(sampleGerminationInfo.getQcgSgtdnormal2());
                et_abnormal_rep2.setText(sampleGerminationInfo.getQcgSgtdabnormal2());
                et_hardfug_rep2.setText(sampleGerminationInfo.getQcgSgtdhardfug2());
                et_dead_rep2.setText(sampleGerminationInfo.getQcgSgtddead2());

                et_normal_rep3.setText(sampleGerminationInfo.getQcgSgtdnormal3());
                et_abnormal_rep3.setText(sampleGerminationInfo.getQcgSgtdabnormal3());
                et_hardfug_rep3.setText(sampleGerminationInfo.getQcgSgtdhardfug3());
                et_dead_rep3.setText(sampleGerminationInfo.getQcgSgtddead3());

                et_normal_rep4.setText(sampleGerminationInfo.getQcgSgtdnormal4());
                et_abnormal_rep4.setText(sampleGerminationInfo.getQcgSgtdabnormal4());
                et_hardfug_rep4.setText(sampleGerminationInfo.getQcgSgtdhardfug4());
                et_dead_rep4.setText(sampleGerminationInfo.getQcgSgtddead4());

                et_normal_rep5.setText(sampleGerminationInfo.getQcgSgtdnormal5());
                et_abnormal_rep5.setText(sampleGerminationInfo.getQcgSgtdabnormal5());
                et_hardfug_rep5.setText(sampleGerminationInfo.getQcgSgtdhardfug5());
                et_dead_rep5.setText(sampleGerminationInfo.getQcgSgtddead5());

                et_normal_rep6.setText(sampleGerminationInfo.getQcgSgtdnormal6());
                et_abnormal_rep6.setText(sampleGerminationInfo.getQcgSgtdabnormal6());
                et_hardfug_rep6.setText(sampleGerminationInfo.getQcgSgtdhardfug6());
                et_dead_rep6.setText(sampleGerminationInfo.getQcgSgtddead6());

                et_normal_rep7.setText(sampleGerminationInfo.getQcgSgtdnormal7());
                et_abnormal_rep7.setText(sampleGerminationInfo.getQcgSgtdabnormal7());
                et_hardfug_rep7.setText(sampleGerminationInfo.getQcgSgtdhardfug7());
                et_dead_rep7.setText(sampleGerminationInfo.getQcgSgtddead7());

                et_normal_rep8.setText(sampleGerminationInfo.getQcgSgtdnormal8());
                et_abnormal_rep8.setText(sampleGerminationInfo.getQcgSgtdabnormal8());
                et_hardfug_rep8.setText(sampleGerminationInfo.getQcgSgtdhardfug8());
                et_dead_rep8.setText(sampleGerminationInfo.getQcgSgtddead8());

                et_normalseedper.setText(sampleGerminationInfo.getQcgSgtdnormalavg());
                et_abnormalseedper.setText(sampleGerminationInfo.getQcgSgtdabnormalavg());
                et_hardseedper.setText(sampleGerminationInfo.getQcgSgtdhardfugavg());
                et_deadseedper.setText(sampleGerminationInfo.getQcgSgtddeadavg());

                checkBox_rssc.setEnabled(false);
                checkBox_prm.setEnabled(false);
                checkBox_vss.setEnabled(false);

                if (sampleGerminationInfo.getQcgSgtdoprremark().contains("PRM")){
                    checkBox_prm.setChecked(true);
                }else {
                    checkBox_prm.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgSgtdoprremark().contains("VSS")){
                    checkBox_vss.setChecked(true);
                }else {
                    checkBox_vss.setChecked(false);
                }
                if (sampleGerminationInfo.getQcgSgtdoprremark().contains("RSSC")){
                    checkBox_rssc.setChecked(true);
                }else {
                    checkBox_rssc.setChecked(false);
                }

                et_normal_rep1.setEnabled(false);
                et_abnormal_rep1.setEnabled(false);
                et_hardfug_rep1.setEnabled(false);
                et_dead_rep1.setEnabled(false);
                et_normal_rep2.setEnabled(false);
                et_abnormal_rep2.setEnabled(false);
                et_hardfug_rep2.setEnabled(false);
                et_dead_rep2.setEnabled(false);
                et_normal_rep3.setEnabled(false);
                et_abnormal_rep3.setEnabled(false);
                et_hardfug_rep3.setEnabled(false);
                et_dead_rep3.setEnabled(false);
                et_normal_rep4.setEnabled(false);
                et_abnormal_rep4.setEnabled(false);
                et_hardfug_rep4.setEnabled(false);
                et_dead_rep4.setEnabled(false);
                et_normal_rep5.setEnabled(false);
                et_abnormal_rep5.setEnabled(false);
                et_hardfug_rep5.setEnabled(false);
                et_dead_rep5.setEnabled(false);
                et_normal_rep6.setEnabled(false);
                et_abnormal_rep6.setEnabled(false);
                et_hardfug_rep6.setEnabled(false);
                et_dead_rep6.setEnabled(false);
                et_normal_rep7.setEnabled(false);
                et_abnormal_rep7.setEnabled(false);
                et_hardfug_rep7.setEnabled(false);
                et_dead_rep7.setEnabled(false);
                et_normal_rep8.setEnabled(false);
                et_abnormal_rep8.setEnabled(false);
                et_hardfug_rep8.setEnabled(false);
                et_dead_rep8.setEnabled(false);
                et_normalseedper.setEnabled(false);
                et_abnormalseedper.setEnabled(false);
                et_hardseedper.setEnabled(false);
                et_deadseedper.setEnabled(false);

                spinner_vremark.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                et_vremark.setText(sampleGerminationInfo.getQcgSgtdvremark());
                et_vremark.setVisibility(View.VISIBLE);
                if (sampleGerminationInfo.getQcgSgtremark()!=null){
                    et_remark.setText(sampleGerminationInfo.getQcgSgtdremark());
                }
                et_remark.setEnabled(false);
            }else {
                button_photo.setVisibility(View.VISIBLE);
                ll_germpPhoto.setVisibility(View.VISIBLE);

                et_normal_rep1.setText("");
                et_abnormal_rep1.setText("");
                et_hardfug_rep1.setText("");
                et_dead_rep1.setText("");

                et_normal_rep2.setText("");
                et_abnormal_rep2.setText("");
                et_hardfug_rep2.setText("");
                et_dead_rep2.setText("");

                et_normal_rep3.setText("");
                et_abnormal_rep3.setText("");
                et_hardfug_rep3.setText("");
                et_dead_rep3.setText("");

                et_normal_rep4.setText("");
                et_abnormal_rep4.setText("");
                et_hardfug_rep4.setText("");
                et_dead_rep4.setText("");

                et_normal_rep5.setText("");
                et_abnormal_rep5.setText("");
                et_hardfug_rep5.setText("");
                et_dead_rep5.setText("");

                et_normal_rep6.setText("");
                et_abnormal_rep6.setText("");
                et_hardfug_rep6.setText("");
                et_dead_rep6.setText("");

                et_normal_rep7.setText("");
                et_abnormal_rep7.setText("");
                et_hardfug_rep7.setText("");
                et_dead_rep7.setText("");

                et_normal_rep8.setText("");
                et_abnormal_rep8.setText("");
                et_hardfug_rep8.setText("");
                et_dead_rep8.setText("");

                et_normalseedper.setText("");
                et_abnormalseedper.setText("");
                et_hardseedper.setText("");
                et_deadseedper.setText("");

                et_remark.setText("");

                et_normal_rep1.setEnabled(true);
                et_abnormal_rep1.setEnabled(true);
                et_hardfug_rep1.setEnabled(true);
                et_dead_rep1.setEnabled(true);
                et_normal_rep2.setEnabled(true);
                et_abnormal_rep2.setEnabled(true);
                et_hardfug_rep2.setEnabled(true);
                et_dead_rep2.setEnabled(true);
                et_normal_rep3.setEnabled(true);
                et_abnormal_rep3.setEnabled(true);
                et_hardfug_rep3.setEnabled(true);
                et_dead_rep3.setEnabled(true);
                et_normal_rep4.setEnabled(true);
                et_abnormal_rep4.setEnabled(true);
                et_hardfug_rep4.setEnabled(true);
                et_dead_rep4.setEnabled(true);
                et_normal_rep5.setEnabled(true);
                et_abnormal_rep5.setEnabled(true);
                et_hardfug_rep5.setEnabled(true);
                et_dead_rep5.setEnabled(true);
                et_normal_rep6.setEnabled(true);
                et_abnormal_rep6.setEnabled(true);
                et_hardfug_rep6.setEnabled(true);
                et_dead_rep6.setEnabled(true);
                et_normal_rep7.setEnabled(true);
                et_abnormal_rep7.setEnabled(true);
                et_hardfug_rep7.setEnabled(true);
                et_dead_rep7.setEnabled(true);
                et_normal_rep8.setEnabled(true);
                et_abnormal_rep8.setEnabled(true);
                et_hardfug_rep8.setEnabled(true);
                et_dead_rep8.setEnabled(true);
                spinner_vremark.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                et_vremark.setVisibility(View.GONE);
                et_remark.setEnabled(true);
                iv_photo.setImageDrawable(null);
                String[] vremark_list = {"Good","Average","Poor"};
                ArrayAdapter<String> adapter_vremark = new ArrayAdapter<>(GerminationReadingFormActivity.this, android.R.layout.simple_spinner_item, vremark_list);
                adapter_vremark.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_vremark.setAdapter(adapter_vremark);
            }

        }
    }

    public void one_observation(View v)
    {
        int germmethod = radioGrpGerm.getCheckedRadioButtonId();
        RadioButton mois = findViewById(germmethod);
        String gmmtype = mois.getText().toString();
        //ll_germpPhoto.setVisibility(View.GONE);
        if (gmmtype.equalsIgnoreCase("SGT")) {
            int noofseedsinonerep = sampleGerminationInfo.getNoofseedinonerep();
            if (rep_countsgt.equalsIgnoreCase("1")) {
                ll_normalrep2.setVisibility(View.GONE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                et_totalnoofseeds.setText(String.valueOf(noofseedsinonerep));
            } else if (rep_countsgt.equalsIgnoreCase("2")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep*2;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            } else if (rep_countsgt.equalsIgnoreCase("4")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep*4;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            } else if (rep_countsgt.equalsIgnoreCase("8")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.VISIBLE);
                ll_normalrep78.setVisibility(View.VISIBLE);
                int totalrep = noofseedsinonerep*8;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }

            if (sampleGerminationInfo.getQcgSgtoneobflg()==1){
                button_photo.setVisibility(View.GONE);
                if (sampleGerminationInfo.getQcgSgtfcimage()!=null && !sampleGerminationInfo.getQcgSgtfcimage().equalsIgnoreCase("")){
                    Glide.with(GerminationReadingFormActivity.this)
                            .load(Uri.parse(AppConfig.IMAGE_URL + sampleGerminationInfo.getQcgSgtfcimage()))
                            .into(iv_photo);
                }else {
                    ll_germpPhoto.setVisibility(View.GONE);
                }
                et_normalseed_rep1.setText(sampleGerminationInfo.getQcgSgtoobnormal1());
                et_normalseed_rep2.setText(sampleGerminationInfo.getQcgSgtoobnormal2());
                et_normalseed_rep3.setText(sampleGerminationInfo.getQcgSgtoobnormal3());
                et_normalseed_rep4.setText(sampleGerminationInfo.getQcgSgtoobnormal4());
                et_normalseed_rep5.setText(sampleGerminationInfo.getQcgSgtoobnormal5());
                et_normalseed_rep6.setText(sampleGerminationInfo.getQcgSgtoobnormal6());
                et_normalseed_rep7.setText(sampleGerminationInfo.getQcgSgtoobnormal7());
                et_normalseed_rep8.setText(sampleGerminationInfo.getQcgSgtoobnormal8());
                et_normalseed_rep1.setEnabled(false);
                et_normalseed_rep2.setEnabled(false);
                et_normalseed_rep3.setEnabled(false);
                et_normalseed_rep4.setEnabled(false);
                et_normalseed_rep5.setEnabled(false);
                et_normalseed_rep6.setEnabled(false);
                et_normalseed_rep7.setEnabled(false);
                et_normalseed_rep8.setEnabled(false);

                spinner_vremark.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                if (sampleGerminationInfo.getQcgSgtfcremark()!=null){
                    et_remark.setText(sampleGerminationInfo.getQcgSgtfcremark());
                }
                et_remark.setEnabled(false);
            }else {
                if (sampleGerminationInfo.getQcgSgtflg()==1){
                    button_photo.setVisibility(View.GONE);
                    ll_germpPhoto.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.GONE);

                    et_normalseed_rep1.setEnabled(false);
                    et_normalseed_rep2.setEnabled(false);
                    et_normalseed_rep3.setEnabled(false);
                    et_normalseed_rep4.setEnabled(false);
                    et_normalseed_rep5.setEnabled(false);
                    et_normalseed_rep6.setEnabled(false);
                    et_normalseed_rep7.setEnabled(false);
                    et_normalseed_rep8.setEnabled(false);
                    et_remark.setEnabled(false);
                }else {
                    button_photo.setVisibility(View.VISIBLE);
                    ll_germpPhoto.setVisibility(View.VISIBLE);
                    btn_submit.setVisibility(View.VISIBLE);
                    iv_photo.setImageDrawable(null);

                    et_normalseed_rep1.setEnabled(true);
                    et_normalseed_rep2.setEnabled(true);
                    et_normalseed_rep3.setEnabled(true);
                    et_normalseed_rep4.setEnabled(true);
                    et_normalseed_rep5.setEnabled(true);
                    et_normalseed_rep6.setEnabled(true);
                    et_normalseed_rep7.setEnabled(true);
                    et_normalseed_rep8.setEnabled(true);
                    et_remark.setEnabled(true);
                }
                et_normalseed_rep1.setText("");
                et_normalseed_rep2.setText("");
                et_normalseed_rep3.setText("");
                et_normalseed_rep4.setText("");
                et_normalseed_rep5.setText("");
                et_normalseed_rep6.setText("");
                et_normalseed_rep7.setText("");
                et_normalseed_rep8.setText("");
                et_remark.setText("");
            }
        }else if (gmmtype.equalsIgnoreCase("FGT")){
            int noofseedsinonerep = sampleGerminationInfo.getNoofseedinonerepfgt();
            if (rep_countfgt.equalsIgnoreCase("1")) {
                ll_normalrep2.setVisibility(View.GONE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                et_totalnoofseeds.setText(String.valueOf(noofseedsinonerep));
            } else if (rep_countfgt.equalsIgnoreCase("2")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep*2;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            } else if (rep_countfgt.equalsIgnoreCase("4")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep*4;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            } else if (rep_countfgt.equalsIgnoreCase("8")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.VISIBLE);
                ll_normalrep78.setVisibility(View.VISIBLE);
                int totalrep = noofseedsinonerep*8;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }

            if (sampleGerminationInfo.getQcgVigoneobflg()==1){
                button_photo.setVisibility(View.GONE);
                if (sampleGerminationInfo.getQcgFgtfcimage()!=null && !sampleGerminationInfo.getQcgFgtfcimage().equalsIgnoreCase("")){
                    Glide.with(GerminationReadingFormActivity.this)
                            .load(Uri.parse(AppConfig.IMAGE_URL + sampleGerminationInfo.getQcgFgtfcimage()))
                            .into(iv_photo);
                }else {
                    ll_germpPhoto.setVisibility(View.GONE);
                }

                et_normalseed_rep1.setText(sampleGerminationInfo.getQcgVigoobnormal1());
                et_normalseed_rep2.setText(sampleGerminationInfo.getQcgVigoobnormal2());
                et_normalseed_rep3.setText(sampleGerminationInfo.getQcgVigoobnormal3());
                et_normalseed_rep4.setText(sampleGerminationInfo.getQcgVigoobnormal4());
                et_normalseed_rep5.setText(sampleGerminationInfo.getQcgVigoobnormal5());
                et_normalseed_rep6.setText(sampleGerminationInfo.getQcgVigoobnormal6());
                et_normalseed_rep7.setText(sampleGerminationInfo.getQcgVigoobnormal7());
                et_normalseed_rep8.setText(sampleGerminationInfo.getQcgVigoobnormal8());

                et_normalseed_rep1.setEnabled(false);
                et_normalseed_rep2.setEnabled(false);
                et_normalseed_rep3.setEnabled(false);
                et_normalseed_rep4.setEnabled(false);
                et_normalseed_rep5.setEnabled(false);
                et_normalseed_rep6.setEnabled(false);
                et_normalseed_rep7.setEnabled(false);
                et_normalseed_rep8.setEnabled(false);

                spinner_vremark.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                if (sampleGerminationInfo.getQcgFgtfcremark()!=null){
                    et_remark.setText(sampleGerminationInfo.getQcgFgtremark());
                }
                et_remark.setEnabled(false);

            }else {
                if (sampleGerminationInfo.getQcgVigflg()==1){
                    button_photo.setVisibility(View.GONE);
                    ll_germpPhoto.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.GONE);

                    et_normalseed_rep1.setEnabled(false);
                    et_normalseed_rep2.setEnabled(false);
                    et_normalseed_rep3.setEnabled(false);
                    et_normalseed_rep4.setEnabled(false);
                    et_normalseed_rep5.setEnabled(false);
                    et_normalseed_rep6.setEnabled(false);
                    et_normalseed_rep7.setEnabled(false);
                    et_normalseed_rep8.setEnabled(false);
                    et_remark.setEnabled(false);
                }else {
                    button_photo.setVisibility(View.VISIBLE);
                    ll_germpPhoto.setVisibility(View.VISIBLE);
                    btn_submit.setVisibility(View.VISIBLE);
                    iv_photo.setImageDrawable(null);

                    et_normalseed_rep1.setEnabled(true);
                    et_normalseed_rep2.setEnabled(true);
                    et_normalseed_rep3.setEnabled(true);
                    et_normalseed_rep4.setEnabled(true);
                    et_normalseed_rep5.setEnabled(true);
                    et_normalseed_rep6.setEnabled(true);
                    et_normalseed_rep7.setEnabled(true);
                    et_normalseed_rep8.setEnabled(true);
                    et_remark.setEnabled(true);
                }

                et_normalseed_rep1.setText("");
                et_normalseed_rep2.setText("");
                et_normalseed_rep3.setText("");
                et_normalseed_rep4.setText("");
                et_normalseed_rep5.setText("");
                et_normalseed_rep6.setText("");
                et_normalseed_rep7.setText("");
                et_normalseed_rep8.setText("");
                et_remark.setText("");
            }
        }else if (gmmtype.equalsIgnoreCase("SGT with SGTD")) {
            int noofseedsinonerep = sampleGerminationInfo.getNoofseedinonerep();
            if (rep_countsgtd.equalsIgnoreCase("1")) {
                ll_normalrep2.setVisibility(View.GONE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                et_totalnoofseeds.setText(String.valueOf(noofseedsinonerep));
            } else if (rep_countsgtd.equalsIgnoreCase("2")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.GONE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep*2;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            } else if (rep_countsgtd.equalsIgnoreCase("4")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.GONE);
                ll_normalrep78.setVisibility(View.GONE);
                int totalrep = noofseedsinonerep*4;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            } else if (rep_countsgtd.equalsIgnoreCase("8")) {
                ll_normalrep2.setVisibility(View.VISIBLE);
                ll_normalrep34.setVisibility(View.VISIBLE);
                ll_normalrep56.setVisibility(View.VISIBLE);
                ll_normalrep78.setVisibility(View.VISIBLE);
                int totalrep = noofseedsinonerep*8;
                et_totalnoofseeds.setText(String.valueOf(totalrep));
            }

            if (sampleGerminationInfo.getQcgSgtdoneobflg()==1){
                button_photo.setVisibility(View.GONE);
                if (sampleGerminationInfo.getQcgSgtdfcimage()!=null && !sampleGerminationInfo.getQcgSgtdfcimage().equalsIgnoreCase("")){
                    Glide.with(GerminationReadingFormActivity.this)
                            .load(Uri.parse(AppConfig.IMAGE_URL + sampleGerminationInfo.getQcgSgtdfcimage()))
                            .into(iv_photo);
                }else {
                    ll_germpPhoto.setVisibility(View.GONE);
                }

                et_normalseed_rep1.setText(sampleGerminationInfo.getQcgSgtdoobnormal1());
                et_normalseed_rep2.setText(sampleGerminationInfo.getQcgSgtdoobnormal2());
                et_normalseed_rep3.setText(sampleGerminationInfo.getQcgSgtdoobnormal3());
                et_normalseed_rep4.setText(sampleGerminationInfo.getQcgSgtdoobnormal4());
                et_normalseed_rep5.setText(sampleGerminationInfo.getQcgSgtdoobnormal5());
                et_normalseed_rep6.setText(sampleGerminationInfo.getQcgSgdtoobnormal6());
                et_normalseed_rep7.setText(sampleGerminationInfo.getQcgSgtdoobnormal7());
                et_normalseed_rep8.setText(sampleGerminationInfo.getQcgSgdtoobnormal8());

                et_normalseed_rep1.setEnabled(false);
                et_normalseed_rep2.setEnabled(false);
                et_normalseed_rep3.setEnabled(false);
                et_normalseed_rep4.setEnabled(false);
                et_normalseed_rep5.setEnabled(false);
                et_normalseed_rep6.setEnabled(false);
                et_normalseed_rep7.setEnabled(false);
                et_normalseed_rep8.setEnabled(false);

                spinner_vremark.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                if (sampleGerminationInfo.getQcgSgtremark()!=null){
                    et_remark.setText(sampleGerminationInfo.getQcgSgtdremark());
                }
                et_remark.setEnabled(false);

            }else {
                if (sampleGerminationInfo.getQcgSgtdflg()==1){
                    button_photo.setVisibility(View.GONE);
                    ll_germpPhoto.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.GONE);

                    et_normalseed_rep1.setEnabled(false);
                    et_normalseed_rep2.setEnabled(false);
                    et_normalseed_rep3.setEnabled(false);
                    et_normalseed_rep4.setEnabled(false);
                    et_normalseed_rep5.setEnabled(false);
                    et_normalseed_rep6.setEnabled(false);
                    et_normalseed_rep7.setEnabled(false);
                    et_normalseed_rep8.setEnabled(false);
                    et_remark.setEnabled(false);
                }else {
                    button_photo.setVisibility(View.VISIBLE);
                    ll_germpPhoto.setVisibility(View.VISIBLE);
                    btn_submit.setVisibility(View.VISIBLE);
                    iv_photo.setImageDrawable(null);

                    et_normalseed_rep1.setEnabled(true);
                    et_normalseed_rep2.setEnabled(true);
                    et_normalseed_rep3.setEnabled(true);
                    et_normalseed_rep4.setEnabled(true);
                    et_normalseed_rep5.setEnabled(true);
                    et_normalseed_rep6.setEnabled(true);
                    et_normalseed_rep7.setEnabled(true);
                    et_normalseed_rep8.setEnabled(true);
                    et_remark.setEnabled(true);
                }

                et_normalseed_rep1.setText("");
                et_normalseed_rep2.setText("");
                et_normalseed_rep3.setText("");
                et_normalseed_rep4.setText("");
                et_normalseed_rep5.setText("");
                et_normalseed_rep6.setText("");
                et_normalseed_rep7.setText("");
                et_normalseed_rep8.setText("");
                et_remark.setText("");

            }
        }
        ll_both.setVisibility(View.GONE);
        ll_techremark.setVisibility(View.GONE);
        ll_oneobs.setVisibility(View.VISIBLE);
    }

    public void retest_onClick(View v1)
    {
        if(checkBox_retest.isChecked()) {
            // true,do the task
            ll_retest_reason.setVisibility(View.VISIBLE);
            ll_withsample.setVisibility(View.VISIBLE);
            retest = "Yes";
        }
        else {
            ll_retest_reason.setVisibility(View.GONE);
            ll_withsample.setVisibility(View.GONE);
            retest = "No";
        }
    }


    private void selectImage() {
        final CharSequence[] items = {"Click here to open camera", "Choose from gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(GerminationReadingFormActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(GerminationReadingFormActivity.this);

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
            MediaScannerConnection.scanFile(GerminationReadingFormActivity.this, new String[]{BILL_COPY.getPath()}, new String[]{"image/jpeg"}, null);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GerminationReadingFormActivity.this, GerminationHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}