package com.example.qc.germination;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qc.MainActivity;
import com.example.qc.R;
import com.example.qc.database.DatabaseHelper;
import com.example.qc.parser.germpsampleinfo.Samplegemparray;
import com.example.qc.pojo.OfflineSampleInfo;
import com.example.qc.pp.Utility;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.Utils;
import com.jsibbold.zoomage.ZoomageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class OfflineFGTDataUpdateActivity extends AppCompatActivity {
    private ImageButton back_nav;

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
    private EditText et_dead_rep1;

    private EditText et_normal_rep2;
    private EditText et_abnormal_rep2;
    private EditText et_dead_rep2;

    private EditText et_normal_rep3;
    private EditText et_abnormal_rep3;
    private EditText et_dead_rep3;

    private EditText et_normal_rep4;
    private EditText et_abnormal_rep4;
    private EditText et_dead_rep4;

    private EditText et_normal_rep5;
    private EditText et_abnormal_rep5;
    private EditText et_dead_rep5;

    private EditText et_normal_rep6;
    private EditText et_abnormal_rep6;
    private EditText et_dead_rep6;

    private EditText et_normal_rep7;
    private EditText et_abnormal_rep7;
    private EditText et_dead_rep7;

    private EditText et_normal_rep8;
    private EditText et_abnormal_rep8;
    private EditText et_dead_rep8;

    private EditText et_normalseedper;
    private EditText et_abnormalseedper;
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

    private RadioGroup radioGrpObrs;
    private DatabaseHelper dbobj;

    //Add Photo
    private ZoomageView iv_photo;
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
    private String sampleno;
    private OfflineSampleInfo row;
    private Spinner spinner_vremark;
    private Button btn_submit;
    private String rep_countfgt;
    private LinearLayout ll_both;
    private LinearLayout ll_oneobs;
    private LinearLayout ll_normalrep2;
    private LinearLayout ll_normalrep34;
    private LinearLayout ll_normalrep56;
    private LinearLayout ll_normalrep78;
    private LinearLayout ll_block34;
    private LinearLayout ll_block5678;
    private TextView tv_doe;
    private byte[] byteArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_fgtdata_update);
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

        radioGrpObrs = findViewById(R.id.radioGrpObrs);
        RadioButton radioOneObs = findViewById(R.id.radioOneObs);
        RadioButton radioFinalObs = findViewById(R.id.radioFinalObs);

        ll_techremark = findViewById(R.id.ll_techremark);

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
        et_dead_rep1 = findViewById(R.id.et_dead_rep1);

        //Rep2
        et_normal_rep2 = findViewById(R.id.et_normal_rep2);
        et_abnormal_rep2 = findViewById(R.id.et_abnormal_rep2);
        et_dead_rep2 = findViewById(R.id.et_dead_rep2);

        //Rep3
        et_normal_rep3 = findViewById(R.id.et_normal_rep3);
        et_abnormal_rep3 = findViewById(R.id.et_abnormal_rep3);
        et_dead_rep3 = findViewById(R.id.et_dead_rep3);

        //Rep4
        et_normal_rep4 = findViewById(R.id.et_normal_rep4);
        et_abnormal_rep4 = findViewById(R.id.et_abnormal_rep4);
        et_dead_rep4 = findViewById(R.id.et_dead_rep4);

        //Rep5
        et_normal_rep5 = findViewById(R.id.et_normal_rep5);
        et_abnormal_rep5 = findViewById(R.id.et_abnormal_rep5);
        et_dead_rep5 = findViewById(R.id.et_dead_rep5);

        //Rep6
        et_normal_rep6 = findViewById(R.id.et_normal_rep6);
        et_abnormal_rep6 = findViewById(R.id.et_abnormal_rep6);
        et_dead_rep6 = findViewById(R.id.et_dead_rep6);

        //Rep7
        et_normal_rep7 = findViewById(R.id.et_normal_rep7);
        et_abnormal_rep7 = findViewById(R.id.et_abnormal_rep7);
        et_dead_rep7 = findViewById(R.id.et_dead_rep7);

        //Rep8
        et_normal_rep8 = findViewById(R.id.et_normal_rep8);
        et_abnormal_rep8 = findViewById(R.id.et_abnormal_rep8);
        et_dead_rep8 = findViewById(R.id.et_dead_rep8);

        //Mean
        et_normalseedper = findViewById(R.id.et_normalseedper);
        et_abnormalseedper = findViewById(R.id.et_abnormalseedper);
        et_deadseedper = findViewById(R.id.et_deadseedper);

        spinner_vremark = findViewById(R.id.spinner_vremark);
        checkBox_rssc = findViewById(R.id.checkBox_rssc);
        checkBox_prm = findViewById(R.id.checkBox_prm);
        checkBox_vss = findViewById(R.id.checkBox_vss);
        btn_submit = findViewById(R.id.btn_submit);
        ll_both = findViewById(R.id.ll_both);
        ll_oneobs = findViewById(R.id.ll_oneobs);
        ll_normalrep2 = findViewById(R.id.ll_normalrep2);
        ll_normalrep34 = findViewById(R.id.ll_normalrep34);
        ll_normalrep56 = findViewById(R.id.ll_normalrep56);
        ll_normalrep78 = findViewById(R.id.ll_normalrep78);
        ll_block34 = findViewById(R.id.ll_block34);
        ll_block5678 = findViewById(R.id.ll_block5678);

        String[] vremark_list = {"Good","Average","Poor"};
        ArrayAdapter<String> adapter_vremark = new ArrayAdapter<>(OfflineFGTDataUpdateActivity.this, android.R.layout.simple_spinner_item, vremark_list);
        adapter_vremark.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_vremark.setAdapter(adapter_vremark);


        TextView tv_crop = findViewById(R.id.tv_crop);
        TextView tv_variety = findViewById(R.id.tv_variety);
        TextView tv_lono = findViewById(R.id.tv_lono);
        TextView tv_stage = findViewById(R.id.tv_stage);
        tv_sampleno = findViewById(R.id.tv_sampleno);
        TextView tv_noofseedsinonerep = findViewById(R.id.tv_noofseedsinonerep);
        TextView tv_sizeofseeds = findViewById(R.id.tv_sizeofseeds);
        TextView tv_noofreps = findViewById(R.id.tv_noofreps);
        TextView tv_testType = findViewById(R.id.tv_testType);
        tv_doe = findViewById(R.id.tv_doe);

        ll_both.setVisibility(View.VISIBLE);
        //ll_germpPhoto.setVisibility(View.VISIBLE);
        ll_oneobs.setVisibility(View.GONE);
        ll_techremark.setVisibility(View.VISIBLE);

        //sampleno = getIntent().getStringExtra("sampleno");
        String sampleno = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SAMPLENO);
        row = dbobj.getSampleDetails(sampleno);
        String currentdate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tv_doe.setText(currentdate);

        if (row!=null) {
            tv_crop.setText(row.getCrop());
            tv_variety.setText(row.getVariety());
            tv_lono.setText(row.getLotno());
            tv_stage.setText(row.getTrstage());
            tv_sampleno.setText(row.getSampleno());
            tv_noofseedsinonerep.setText(String.valueOf(row.getSeedinrep()));
            tv_sizeofseeds.setText(String.valueOf(row.getSeedsize()));
            tv_noofreps.setText(String.valueOf(row.getNoofrep()));
            tv_testType.setText(String.valueOf(row.getFgttesttype()));
        }

        assert row != null;
        rep_countfgt = row.getNoofrep();
        finalNoofseedonerep = Float.parseFloat(row.getSeedinrep());

        if (rep_countfgt.equalsIgnoreCase("1")) {
            ll_normalrep2.setVisibility(View.GONE);
            ll_normalrep34.setVisibility(View.GONE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);
            ll_block34.setVisibility(View.GONE);
            ll_block5678.setVisibility(View.GONE);
        } else if (rep_countfgt.equalsIgnoreCase("2")) {
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.GONE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);
            ll_block34.setVisibility(View.GONE);
            ll_block5678.setVisibility(View.GONE);
        } else if (rep_countfgt.equalsIgnoreCase("4")) {
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.VISIBLE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);
            ll_block34.setVisibility(View.VISIBLE);
            ll_block5678.setVisibility(View.GONE);
        } else if (rep_countfgt.equalsIgnoreCase("8")) {
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.VISIBLE);
            ll_normalrep56.setVisibility(View.VISIBLE);
            ll_normalrep78.setVisibility(View.VISIBLE);
            ll_block34.setVisibility(View.GONE);
            ll_block5678.setVisibility(View.GONE);
        }
    }

    private void init(){
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OfflineFGTDataUpdateActivity.this, GerminationHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
                String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
                String sampleno = tv_sampleno.getText().toString().trim();

                int germsobrstype = radioGrpObrs.getCheckedRadioButtonId();
                RadioButton obrstype = findViewById(germsobrstype);
                String obrstype1 = obrstype.getText().toString();

                int totalnoofseeds1 = Integer.parseInt(row.getNoofrep())*Integer.parseInt(row.getSeedinrep());
                String totalnoofseeds = String.valueOf(totalnoofseeds1);
                String doefgt = tv_doe.getText().toString().trim();

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
                String dead_rep1 = et_dead_rep1.getText().toString().trim();

                String normal_rep2 = et_normal_rep2.getText().toString().trim();
                String abnormal_rep2 = et_abnormal_rep2.getText().toString().trim();
                String dead_rep2 = et_dead_rep2.getText().toString().trim();

                String normal_rep3 = et_normal_rep3.getText().toString().trim();
                String abnormal_rep3 = et_abnormal_rep3.getText().toString().trim();
                String dead_rep3 = et_dead_rep3.getText().toString().trim();

                String normal_rep4 = et_normal_rep4.getText().toString().trim();
                String abnormal_rep4 = et_abnormal_rep4.getText().toString().trim();
                String dead_rep4 = et_dead_rep4.getText().toString().trim();

                String normal_rep5 = et_normal_rep5.getText().toString().trim();
                String abnormal_rep5 = et_abnormal_rep5.getText().toString().trim();
                String dead_rep5 = et_dead_rep5.getText().toString().trim();

                String normal_rep6 = et_normal_rep6.getText().toString().trim();
                String abnormal_rep6 = et_abnormal_rep6.getText().toString().trim();
                String dead_rep6 = et_dead_rep6.getText().toString().trim();

                String normal_rep7 = et_normal_rep7.getText().toString().trim();
                String abnormal_rep7 = et_abnormal_rep7.getText().toString().trim();
                String dead_rep7 = et_dead_rep7.getText().toString().trim();

                String normal_rep8 = et_normal_rep8.getText().toString().trim();
                String abnormal_rep8 = et_abnormal_rep8.getText().toString().trim();
                String dead_rep8 = et_dead_rep8.getText().toString().trim();

                String normalseedper = et_normalseedper.getText().toString().trim();
                String abnormalseedper = et_abnormalseedper.getText().toString().trim();
                String deadseedper = et_deadseedper.getText().toString().trim();
                String remark = et_remark.getText().toString().trim();

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

                if (row.getNoofrep().equals("1")) {
                    if (obrstype1.equalsIgnoreCase("First Count")) {
                        if (normalseed_rep1.equals("")) {
                            Toast.makeText(getApplicationContext(), "Enter all data", Toast.LENGTH_LONG).show();
                        } else {
                            confirmAlertFGT(userid, scode, sampleno, obrstype1, normalseed_rep1, normalseed_rep2, normalseed_rep3, normalseed_rep4, normalseed_rep5, normalseed_rep6, normalseed_rep7, normalseed_rep8, remark, doefgt);
                            //submitFGTFC(userid,scode,sampleno,noofrepfgt,gmmtype,obrstype1,normalseed_rep1,normalseed_rep2,normalseed_rep3,normalseed_rep4,normalseed_rep5,normalseed_rep6,normalseed_rep7,normalseed_rep8,doefgt,fgtmedia);
                        }
                    } else {
                        double reptotalseeds = Double.parseDouble(normal_rep1) + Double.parseDouble(abnormal_rep1) + Double.parseDouble(dead_rep1);
                        if (normal_rep1.equals("") || abnormal_rep1.equals("") || dead_rep1.equals("")) {
                            Toast.makeText(getApplicationContext(), "Replication #1 : Enter all data", Toast.LENGTH_LONG).show();
                        } else if (reptotalseeds != Integer.parseInt(row.getSeedinrep())) {
                            Toast.makeText(getApplicationContext(), "Replication #1 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                        } else {
                            confirmAlertFGTFC(userid, scode, sampleno, obrstype1, vegremark, techremark, normal_rep1, abnormal_rep1, dead_rep1, normal_rep2, abnormal_rep2, dead_rep2, normal_rep3, abnormal_rep3, dead_rep3, normal_rep4, abnormal_rep4, dead_rep4, normal_rep5, abnormal_rep5, dead_rep5, normal_rep6, abnormal_rep6, dead_rep6, normal_rep7, abnormal_rep7, dead_rep7, normal_rep8, abnormal_rep8, dead_rep8, normalseedper, abnormalseedper, deadseedper, remark, doefgt);
                            //submitFGTFinalCount(userid,scode,sampleno,noofrepfgt,gmmtype,obrstype1,vegremark,techremark,normal_rep1,abnormal_rep1,dead_rep1,normal_rep2,abnormal_rep2,dead_rep2,normal_rep3,abnormal_rep3,dead_rep3,normal_rep4,abnormal_rep4,dead_rep4,normal_rep5,abnormal_rep5,dead_rep5,normal_rep6,abnormal_rep6,dead_rep6,normal_rep7,abnormal_rep7,dead_rep7,normal_rep8,abnormal_rep8,dead_rep8,normalseedper,abnormalseedper,deadseedper,doefgt,fgtmedia);
                        }
                    }
                } else if (row.getNoofrep().equals("2")) {
                    if (obrstype1.equalsIgnoreCase("First Count")) {
                        if (normalseed_rep1.equals("") || normalseed_rep2.equals("")) {
                            Toast.makeText(getApplicationContext(), "Enter all data", Toast.LENGTH_LONG).show();
                        } else {
                            confirmAlertFGT(userid, scode, sampleno, obrstype1, normalseed_rep1, normalseed_rep2, normalseed_rep3, normalseed_rep4, normalseed_rep5, normalseed_rep6, normalseed_rep7, normalseed_rep8, remark, doefgt);
                            //submitFGTFC(userid,scode,sampleno,noofrepfgt,gmmtype,obrstype1,normalseed_rep1,normalseed_rep2,normalseed_rep3,normalseed_rep4,normalseed_rep5,normalseed_rep6,normalseed_rep7,normalseed_rep8,doefgt,fgtmedia);
                        }
                    } else {
                        if (!normal_rep1.equalsIgnoreCase("") && !abnormal_rep1.equalsIgnoreCase("") && !dead_rep1.equalsIgnoreCase("")) {
                            if (!normal_rep2.equalsIgnoreCase("") && !abnormal_rep2.equalsIgnoreCase("") && !dead_rep2.equalsIgnoreCase("")) {
                                double reptotalseeds = Double.parseDouble(normal_rep1) + Double.parseDouble(abnormal_rep1) + Double.parseDouble(dead_rep1);
                                double reptotalseeds2 = Double.parseDouble(normal_rep2) + Double.parseDouble(abnormal_rep2) + Double.parseDouble(dead_rep2);
                                if (normal_rep1.equals("") || abnormal_rep1.equals("") || dead_rep1.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds != Integer.parseInt(row.getSeedinrep())) {
                                    Toast.makeText(getApplicationContext(), "Replication #1 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else if (normal_rep2.equals("") || abnormal_rep2.equals("") || dead_rep2.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Replication #2 : Enter all data", Toast.LENGTH_LONG).show();
                                } else if (reptotalseeds2 != Integer.parseInt(row.getSeedinrep())) {
                                    Toast.makeText(getApplicationContext(), "Replication #2 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                } else {
                                    confirmAlertFGTFC(userid, scode, sampleno, obrstype1, vegremark, techremark, normal_rep1, abnormal_rep1, dead_rep1, normal_rep2, abnormal_rep2, dead_rep2, normal_rep3, abnormal_rep3, dead_rep3, normal_rep4, abnormal_rep4, dead_rep4, normal_rep5, abnormal_rep5, dead_rep5, normal_rep6, abnormal_rep6, dead_rep6, normal_rep7, abnormal_rep7, dead_rep7, normal_rep8, abnormal_rep8, dead_rep8, normalseedper, abnormalseedper, deadseedper, remark, doefgt);
                                    //submitFGTFinalCount(userid,scode,sampleno,noofrepfgt,gmmtype,obrstype1,vegremark,techremark,normal_rep1,abnormal_rep1,dead_rep1,normal_rep2,abnormal_rep2,dead_rep2,normal_rep3,abnormal_rep3,dead_rep3,normal_rep4,abnormal_rep4,dead_rep4,normal_rep5,abnormal_rep5,dead_rep5,normal_rep6,abnormal_rep6,dead_rep6,normal_rep7,abnormal_rep7,dead_rep7,normal_rep8,abnormal_rep8,dead_rep8,normalseedper,abnormalseedper,deadseedper,doefgt,fgtmedia);
                                }
                            }
                        }
                    }
                } else if (row.getNoofrep().equals("4")) {
                    if (obrstype1.equalsIgnoreCase("First Count")) {
                        if (normalseed_rep1.equals("") || normalseed_rep2.equals("") || normalseed_rep3.equals("") || normalseed_rep4.equals("")) {
                            Toast.makeText(getApplicationContext(), "Enter all data", Toast.LENGTH_LONG).show();
                        } else {
                            confirmAlertFGT(userid, scode, sampleno, obrstype1, normalseed_rep1, normalseed_rep2, normalseed_rep3, normalseed_rep4, normalseed_rep5, normalseed_rep6, normalseed_rep7, normalseed_rep8, remark, doefgt);
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
                                        } else if (reptotalseeds != Integer.parseInt(row.getSeedinrep())) {
                                            Toast.makeText(getApplicationContext(), "Replication #1 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                        } else if (normal_rep2.equals("") || abnormal_rep2.equals("") || dead_rep2.equals("")) {
                                            Toast.makeText(getApplicationContext(), "Replication #2 : Enter all data", Toast.LENGTH_LONG).show();
                                        } else if (reptotalseeds2 != Integer.parseInt(row.getSeedinrep())) {
                                            Toast.makeText(getApplicationContext(), "Replication #2 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                        } else if (normal_rep3.equals("") || abnormal_rep3.equals("") || dead_rep3.equals("")) {
                                            Toast.makeText(getApplicationContext(), "Replication #3 : Enter all data", Toast.LENGTH_LONG).show();
                                        } else if (reptotalseeds3 != Integer.parseInt(row.getSeedinrep())) {
                                            Toast.makeText(getApplicationContext(), "Replication #3 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                        } else if (normal_rep4.equals("") || abnormal_rep4.equals("") || dead_rep4.equals("")) {
                                            Toast.makeText(getApplicationContext(), "Replication #4 : Enter all data", Toast.LENGTH_LONG).show();
                                        } else if (reptotalseeds4 != Integer.parseInt(row.getSeedinrep())) {
                                            Toast.makeText(getApplicationContext(), "Replication #4 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                        } else {
                                            confirmAlertFGTFC(userid, scode, sampleno, obrstype1, vegremark, techremark, normal_rep1, abnormal_rep1, dead_rep1, normal_rep2, abnormal_rep2, dead_rep2, normal_rep3, abnormal_rep3, dead_rep3, normal_rep4, abnormal_rep4, dead_rep4, normal_rep5, abnormal_rep5, dead_rep5, normal_rep6, abnormal_rep6, dead_rep6, normal_rep7, abnormal_rep7, dead_rep7, normal_rep8, abnormal_rep8, dead_rep8, normalseedper, abnormalseedper, deadseedper, remark, doefgt);
                                            //submitFGTFinalCount(userid,scode,sampleno,noofrepfgt,gmmtype,obrstype1,vegremark,techremark,normal_rep1,abnormal_rep1,dead_rep1,normal_rep2,abnormal_rep2,dead_rep2,normal_rep3,abnormal_rep3,dead_rep3,normal_rep4,abnormal_rep4,dead_rep4,normal_rep5,abnormal_rep5,dead_rep5,normal_rep6,abnormal_rep6,dead_rep6,normal_rep7,abnormal_rep7,dead_rep7,normal_rep8,abnormal_rep8,dead_rep8,normalseedper,abnormalseedper,deadseedper,doefgt,fgtmedia);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (row.getNoofrep().equals("8")) {
                    if (obrstype1.equalsIgnoreCase("First Count")) {
                        if (normalseed_rep1.equals("") || normalseed_rep2.equals("") || normalseed_rep3.equals("") || normalseed_rep4.equals("") || normalseed_rep5.equals("") || normalseed_rep6.equals("") || normalseed_rep7.equals("") || normalseed_rep8.equals("")) {
                            Toast.makeText(getApplicationContext(), "Enter all data", Toast.LENGTH_LONG).show();
                        } else {
                            confirmAlertFGT(userid, scode, sampleno, obrstype1, normalseed_rep1, normalseed_rep2, normalseed_rep3, normalseed_rep4, normalseed_rep5, normalseed_rep6, normalseed_rep7, normalseed_rep8, remark, doefgt);
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
                                                        } else if (reptotalseeds != Integer.parseInt(row.getSeedinrep())) {
                                                            Toast.makeText(getApplicationContext(), "Replication #1 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                        } else if (normal_rep2.equals("") || abnormal_rep2.equals("") || dead_rep2.equals("")) {
                                                            Toast.makeText(getApplicationContext(), "Replication #2 : Enter all data", Toast.LENGTH_LONG).show();
                                                        } else if (reptotalseeds2 != Integer.parseInt(row.getSeedinrep())) {
                                                            Toast.makeText(getApplicationContext(), "Replication #2 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                        } else if (normal_rep3.equals("") || abnormal_rep3.equals("") || dead_rep3.equals("")) {
                                                            Toast.makeText(getApplicationContext(), "Replication #3 : Enter all data", Toast.LENGTH_LONG).show();
                                                        } else if (reptotalseeds3 != Integer.parseInt(row.getSeedinrep())) {
                                                            Toast.makeText(getApplicationContext(), "Replication #3 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                        } else if (normal_rep4.equals("") || abnormal_rep4.equals("") || dead_rep4.equals("")) {
                                                            Toast.makeText(getApplicationContext(), "Replication #4 : Enter all data", Toast.LENGTH_LONG).show();
                                                        } else if (reptotalseeds4 != Integer.parseInt(row.getSeedinrep())) {
                                                            Toast.makeText(getApplicationContext(), "Replication #4 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                        } else if (normal_rep5.equals("") || abnormal_rep5.equals("") || dead_rep5.equals("")) {
                                                            Toast.makeText(getApplicationContext(), "Replication #5 : Enter all data", Toast.LENGTH_LONG).show();
                                                        } else if (reptotalseeds5 != Integer.parseInt(row.getSeedinrep())) {
                                                            Toast.makeText(getApplicationContext(), "Replication #5 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                        } else if (normal_rep6.equals("") || abnormal_rep6.equals("") || dead_rep6.equals("")) {
                                                            Toast.makeText(getApplicationContext(), "Replication #6 : Enter all data", Toast.LENGTH_LONG).show();
                                                        } else if (reptotalseeds6 != Integer.parseInt(row.getSeedinrep())) {
                                                            Toast.makeText(getApplicationContext(), "Replication #6 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                        } else if (normal_rep7.equals("") || abnormal_rep7.equals("") || dead_rep7.equals("")) {
                                                            Toast.makeText(getApplicationContext(), "Replication #7 : Enter all data", Toast.LENGTH_LONG).show();
                                                        } else if (reptotalseeds7 != Integer.parseInt(row.getSeedinrep())) {
                                                            Toast.makeText(getApplicationContext(), "Replication #7 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                        } else if (normal_rep8.equals("") || abnormal_rep8.equals("") || dead_rep8.equals("")) {
                                                            Toast.makeText(getApplicationContext(), "Replication #8 : Enter all data", Toast.LENGTH_LONG).show();
                                                        } else if (reptotalseeds8 != Integer.parseInt(row.getSeedinrep())) {
                                                            Toast.makeText(getApplicationContext(), "Replication #8 : Please check, No. of seeds does not match with No. of Seeds in one Repplication", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            confirmAlertFGTFC(userid, scode, sampleno, obrstype1, vegremark, techremark, normal_rep1, abnormal_rep1, dead_rep1, normal_rep2, abnormal_rep2, dead_rep2, normal_rep3, abnormal_rep3, dead_rep3, normal_rep4, abnormal_rep4, dead_rep4, normal_rep5, abnormal_rep5, dead_rep5, normal_rep6, abnormal_rep6, dead_rep6, normal_rep7, abnormal_rep7, dead_rep7, normal_rep8, abnormal_rep8, dead_rep8, normalseedper, abnormalseedper, deadseedper, remark, doefgt);
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
        });

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
    }

    @SuppressLint("SetTextI18n")
    private void confirmAlertFGTFC(String userid, String scode, String sampleno, String obrstype1, String vegremark, String techremark, String normalRep1, String abnormalRep1, String deadRep1, String normalRep2, String abnormalRep2, String deadRep2, String normalRep3, String abnormalRep3, String deadRep3, String normalRep4, String abnormalRep4, String deadRep4, String normalRep5, String abnormalRep5, String deadRep5, String normalRep6, String abnormalRep6, String deadRep6, String normalRep7, String abnormalRep7, String deadRep7, String normalRep8, String abnormalRep8, String deadRep8, String normalseedper, String abnormalseedper, String deadseedper, String remark, String doefgt) {
        final Dialog dialog = new Dialog(OfflineFGTDataUpdateActivity.this);
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
                dbobj.updateFCSampleDetails(sampleno,obrstype1,vegremark,techremark, normalRep1, abnormalRep1, deadRep1, normalRep2, abnormalRep2, deadRep2, normalRep3, abnormalRep3, deadRep3, normalRep4, abnormalRep4, deadRep4, normalRep5, abnormalRep5, deadRep5, normalRep6, abnormalRep6, deadRep6, normalRep7, abnormalRep7, deadRep7, normalRep8, abnormalRep8, deadRep8, normalseedper, abnormalseedper, deadseedper, doefgt, remark, byteArray);
                Intent intent = new Intent(OfflineFGTDataUpdateActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void confirmAlertFGT(String userid, String scode, String sampleno, String obrstype1, String normalseedRep1, String normalseedRep2, String normalseedRep3, String normalseedRep4, String normalseedRep5, String normalseedRep6, String normalseedRep7, String normalseedRep8, String remark, String doefgt) {
        final Dialog dialog = new Dialog(OfflineFGTDataUpdateActivity.this);
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


        float NORREP1 = Float.parseFloat(normalseedRep1);
        float NORREP2 = Float.parseFloat(normalseedRep2);
        float NORREP3 = Float.parseFloat(normalseedRep3);
        float NORREP4 = Float.parseFloat(normalseedRep4);
        float NORREP5 = Float.parseFloat(normalseedRep5);
        float NORREP6 = Float.parseFloat(normalseedRep6);
        float NORREP7 = Float.parseFloat(normalseedRep7);
        float NORREP8 = Float.parseFloat(normalseedRep8);
        float NORPER = (NORREP1+NORREP2+NORREP3+NORREP4+NORREP5+NORREP6+NORREP7+NORREP8) / 8;
        float meanper = (NORPER*100)/finalNoofseedonerep;
        int perround = Math.round(meanper);
        String normalseedper = String.valueOf(perround);

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
                dbobj.updateSampleDetails(sampleno,obrstype1, normalseedRep1, normalseedRep2, normalseedRep3, normalseedRep4, normalseedRep5, normalseedRep6, normalseedRep7, normalseedRep8, normalseedper, doefgt, remark, byteArray);
                Intent intent = new Intent(OfflineFGTDataUpdateActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
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

    public void final_observation(View v)
    {
        ll_both.setVisibility(View.VISIBLE);
        //ll_germpPhoto.setVisibility(View.VISIBLE);
        ll_oneobs.setVisibility(View.GONE);
        ll_techremark.setVisibility(View.VISIBLE);
        if (rep_countfgt.equalsIgnoreCase("1")) {
            ll_normalrep2.setVisibility(View.GONE);
            ll_normalrep34.setVisibility(View.GONE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);
            ll_block34.setVisibility(View.GONE);
            ll_block5678.setVisibility(View.GONE);
        } else if (rep_countfgt.equalsIgnoreCase("2")) {
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.GONE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);
            ll_block34.setVisibility(View.GONE);
            ll_block5678.setVisibility(View.GONE);
        } else if (rep_countfgt.equalsIgnoreCase("4")) {
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.VISIBLE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);
            ll_block34.setVisibility(View.VISIBLE);
            ll_block5678.setVisibility(View.GONE);
        } else if (rep_countfgt.equalsIgnoreCase("8")) {
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.VISIBLE);
            ll_normalrep56.setVisibility(View.VISIBLE);
            ll_normalrep78.setVisibility(View.VISIBLE);
            ll_block34.setVisibility(View.GONE);
            ll_block5678.setVisibility(View.GONE);
        }
    }

    public void one_observation(View v)
    {
        ll_both.setVisibility(View.GONE);
        ll_techremark.setVisibility(View.GONE);
        ll_oneobs.setVisibility(View.VISIBLE);
        if (rep_countfgt.equalsIgnoreCase("1")) {
            ll_normalrep2.setVisibility(View.GONE);
            ll_normalrep34.setVisibility(View.GONE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);
        } else if (rep_countfgt.equalsIgnoreCase("2")) {
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.GONE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);
        } else if (rep_countfgt.equalsIgnoreCase("4")) {
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.VISIBLE);
            ll_normalrep56.setVisibility(View.GONE);
            ll_normalrep78.setVisibility(View.GONE);

        } else if (rep_countfgt.equalsIgnoreCase("8")) {
            ll_normalrep2.setVisibility(View.VISIBLE);
            ll_normalrep34.setVisibility(View.VISIBLE);
            ll_normalrep56.setVisibility(View.VISIBLE);
            ll_normalrep78.setVisibility(View.VISIBLE);
        }
    }

    public void retest_onClick(View v1)
    {
        if(checkBox_retest.isChecked()) {
            //true,do the task
            ll_retest_reason.setVisibility(View.VISIBLE);
            ll_withsample.setVisibility(View.VISIBLE);
            retest = "Yes";
        } else {
            ll_retest_reason.setVisibility(View.GONE);
            ll_withsample.setVisibility(View.GONE);
            retest = "No";
        }
    }


    private void selectImage() {
        final CharSequence[] items = {"Click here to open camera", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OfflineFGTDataUpdateActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(OfflineFGTDataUpdateActivity.this);

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

        InputStream iStream = null;
        try {
            iStream = getContentResolver().openInputStream(image_uri);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            assert iStream != null;
            byteArray = getBytes(iStream);
            Log.e("ByteArray", Arrays.toString(byteArray));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
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
            MediaScannerConnection.scanFile(OfflineFGTDataUpdateActivity.this, new String[]{BILL_COPY.getPath()}, new String[]{"image/jpeg"}, null);
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
        Intent intent = new Intent(OfflineFGTDataUpdateActivity.this, GerminationHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}