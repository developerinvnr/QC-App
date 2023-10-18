package com.example.qc.germination;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qc.R;
import com.example.qc.moisture.MoistureHomeActivity;
import com.example.qc.moisture.MoistureReadingFormActivity;
import com.example.qc.parser.JsonParser;
import com.example.qc.parser.germpsampleinfo.Samplegemparray;
import com.example.qc.pojo.SampleGerminationInfo;
import com.example.qc.samplecollection.SampleCollectionHomeActivity;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.Utils;
import com.example.qc.volley.SendVolleyCall;
import com.example.qc.volley.volleyCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GerminationSetupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private LinearLayout ll_dos;
    private LinearLayout ll_sgtnoofrep;
    private LinearLayout ll_fgtnoofrep;
    private LinearLayout ll_spinnermediatype;
    private EditText et_dos;
    private Spinner spinner_gmm;
    private Spinner spinner_sgtnoofrep;
    private Spinner spinner_fgtnoofrep;
    private Spinner spinner_mediatype;
    private Button btn_submit;
    private ImageButton back_nav;

    private TextView tv_sampleno;
    private EditText et_docsrefno;
    private LinearLayout ll_dbtnoofrep;
    private Spinner spinner_dbtnoofrep;
    private Samplegemparray sampleGerminationInfo;
    private LinearLayout ll_docRefNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_germination_setup);
        getSupportActionBar().hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        back_nav = findViewById(R.id.back_nav);
        ll_dos = findViewById(R.id.ll_dos);
        btn_submit = findViewById(R.id.btn_submit);
        et_dos = findViewById(R.id.et_dos);
        ll_sgtnoofrep = findViewById(R.id.ll_sgtnoofrep);
        ll_fgtnoofrep = findViewById(R.id.ll_fgtnoofrep);
        ll_dbtnoofrep = findViewById(R.id.ll_dbtnoofrep);
        ll_spinnermediatype = findViewById(R.id.ll_spinnermediatype);
        ll_docRefNo = findViewById(R.id.ll_docRefNo);
        spinner_gmm = findViewById(R.id.spinner_gmm);
        spinner_sgtnoofrep = findViewById(R.id.spinner_sgtnoofrep);
        spinner_fgtnoofrep = findViewById(R.id.spinner_fgtnoofrep);
        spinner_dbtnoofrep = findViewById(R.id.spinner_dbtnoofrep);
        spinner_mediatype = findViewById(R.id.spinner_mediatype);

        et_docsrefno = findViewById(R.id.et_docsrefno);

        TextView tv_crop = findViewById(R.id.tv_crop);
        TextView tv_variety = findViewById(R.id.tv_variety);
        TextView tv_lono = findViewById(R.id.tv_lono);
        TextView tv_qtykg = findViewById(R.id.tv_qtykg);
        TextView tv_stage = findViewById(R.id.tv_stage);
        TextView tv_srfNo = findViewById(R.id.tv_srfNo);
        tv_sampleno = findViewById(R.id.tv_sampleno);

        sampleGerminationInfo = (Samplegemparray) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_SAMPLEGERMINATIONINFO_OBJ, Samplegemparray.class);
        tv_crop.setText(sampleGerminationInfo.getCrop());
        tv_variety.setText(sampleGerminationInfo.getVariety());
        tv_lono.setText(sampleGerminationInfo.getLotno());
        tv_qtykg.setText(String.valueOf(sampleGerminationInfo.getQty()));
        tv_stage.setText(sampleGerminationInfo.getTrstage());
        tv_srfNo.setText(sampleGerminationInfo.getSrfno());
        tv_sampleno.setText(sampleGerminationInfo.getSampleno());

        if (sampleGerminationInfo.getSrfno().equalsIgnoreCase("")){
            ll_docRefNo.setVisibility(View.VISIBLE);
        }else {
            ll_docRefNo.setVisibility(View.GONE);
        }

        String currentdate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        et_dos.setText(currentdate);

        String[] DayOfWeek = {"SGT", "FGT", "SGT and FGT", "SGT with SGTD", "ALL"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(GerminationSetupActivity.this, android.R.layout.simple_spinner_item, DayOfWeek);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gmm.setAdapter(adapter);

        String[] noofrep_sgt = {"1", "2", "4", "8"};
        ArrayAdapter<String> adapter_sgt = new ArrayAdapter<>(GerminationSetupActivity.this, android.R.layout.simple_spinner_item, noofrep_sgt);
        adapter_sgt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sgtnoofrep.setAdapter(adapter_sgt);

        String[] noofrep_fgt = {"1", "2", "4", "8"};
        ArrayAdapter<String> adapter_fgt = new ArrayAdapter<>(GerminationSetupActivity.this, android.R.layout.simple_spinner_item, noofrep_fgt);
        adapter_fgt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_fgtnoofrep.setAdapter(adapter_fgt);

        String[] noofrep_dbt = {"1", "2", "4", "8"};
        ArrayAdapter<String> adapter_dbt = new ArrayAdapter<>(GerminationSetupActivity.this, android.R.layout.simple_spinner_item, noofrep_dbt);
        adapter_dbt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_dbtnoofrep.setAdapter(adapter_dbt);

        String[] mediatype_list = {"Sand", "Soil", "Cocopeat"};
        ArrayAdapter<String> adapter_mediatype = new ArrayAdapter<>(GerminationSetupActivity.this, android.R.layout.simple_spinner_item, mediatype_list);
        adapter_mediatype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mediatype.setAdapter(adapter_mediatype);
    }

    private void init() {
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GerminationSetupActivity.this, GerminationHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gmm = spinner_gmm.getSelectedItem().toString().trim();
                String sgtnoofrep = spinner_sgtnoofrep.getSelectedItem().toString().trim();
                String fgtnoofrep = spinner_fgtnoofrep.getSelectedItem().toString().trim();
                String dbtnoofrep = spinner_dbtnoofrep.getSelectedItem().toString().trim();
                String mediatype = spinner_mediatype.getSelectedItem().toString().trim();
                String docsrefno = "";
                if (sampleGerminationInfo.getSrfno().equalsIgnoreCase("")){
                    docsrefno = et_docsrefno.getText().toString().trim();
                }else {
                    docsrefno = sampleGerminationInfo.getSrfno();
                }

                if (gmm.equalsIgnoreCase("FGT")){
                    if (!fgtnoofrep.equalsIgnoreCase("") && !mediatype.equalsIgnoreCase("")){
                        uploadSetupData(gmm,sgtnoofrep,fgtnoofrep,dbtnoofrep,mediatype,docsrefno);
                    }else {
                        Toast.makeText(getApplicationContext(), "Select Media and No. of Replications", Toast.LENGTH_LONG).show();
                    }
                }else if (gmm.equalsIgnoreCase("SGT and FGT")){
                    if (!sgtnoofrep.equalsIgnoreCase("") && !fgtnoofrep.equalsIgnoreCase("") && !mediatype.equalsIgnoreCase("")){
                        uploadSetupData(gmm,sgtnoofrep,fgtnoofrep,dbtnoofrep,mediatype,docsrefno);
                    }else {
                        Toast.makeText(getApplicationContext(), "Select Media and No. of Replications", Toast.LENGTH_LONG).show();
                    }
                }else if (gmm.equalsIgnoreCase("SGT")){
                    if (!sgtnoofrep.equalsIgnoreCase("")){
                        uploadSetupData(gmm,sgtnoofrep,fgtnoofrep,dbtnoofrep,mediatype,docsrefno);
                    }else {
                        Toast.makeText(getApplicationContext(), "Select No. of Replications", Toast.LENGTH_LONG).show();
                    }
                }else if (gmm.equalsIgnoreCase("SGT with SGTD")){
                    if (!sgtnoofrep.equalsIgnoreCase("") && !dbtnoofrep.equalsIgnoreCase("")){
                        uploadSetupData(gmm,sgtnoofrep,fgtnoofrep,dbtnoofrep,mediatype,docsrefno);
                    }else {
                        Toast.makeText(getApplicationContext(), "Select No. of Replications", Toast.LENGTH_LONG).show();
                    }
                }else if (gmm.equalsIgnoreCase("ALL")){
                    if (!sgtnoofrep.equalsIgnoreCase("") && !dbtnoofrep.equalsIgnoreCase("") && !fgtnoofrep.equalsIgnoreCase("") && !mediatype.equalsIgnoreCase("")){
                        uploadSetupData(gmm,sgtnoofrep,fgtnoofrep,dbtnoofrep,mediatype,docsrefno);
                    }else {
                        Toast.makeText(getApplicationContext(), "Select Media and No. of Replications", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        spinner_gmm.setOnItemSelectedListener(GerminationSetupActivity.this);
    }

    private void uploadSetupData(final String gmm, final String sgtnoofrep, final String fgtnoofrep, final String dbtnoofrep, final String mediatype, String docsrefno) {
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
        params.put("testtype", gmm);
        params.put("sgtnorep", sgtnoofrep);
        params.put("fgtnorep", fgtnoofrep);
        params.put("dbtnorep", dbtnoofrep);
        params.put("fgtmtype", mediatype);
        params.put("docsrefno", docsrefno);

        new SendVolleyCall().executeProgram(GerminationSetupActivity.this, AppConfig.SAMPLEGERMINATIONSETUP_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                String message = JsonParser.getInstance().parseSubmitData(response);
                if (message.equalsIgnoreCase("Success")){
                    Intent intent = new Intent(GerminationSetupActivity.this, GerminationHomeActivity.class);
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


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spinner_gmm) {
            String gmm = spinner_gmm.getSelectedItem().toString().trim();
            if (gmm.equalsIgnoreCase("SGT")) {
                ll_sgtnoofrep.setVisibility(View.VISIBLE);
                ll_fgtnoofrep.setVisibility(View.GONE);
                ll_dbtnoofrep.setVisibility(View.GONE);
                ll_spinnermediatype.setVisibility(View.GONE);
            } else if (gmm.equalsIgnoreCase("FGT")) {
                ll_sgtnoofrep.setVisibility(View.GONE);
                ll_dbtnoofrep.setVisibility(View.GONE);
                ll_fgtnoofrep.setVisibility(View.VISIBLE);
                ll_spinnermediatype.setVisibility(View.VISIBLE);
            } else if (gmm.equalsIgnoreCase("SGT and FGT")) {
                ll_sgtnoofrep.setVisibility(View.VISIBLE);
                ll_fgtnoofrep.setVisibility(View.VISIBLE);
                ll_spinnermediatype.setVisibility(View.VISIBLE);
                ll_dbtnoofrep.setVisibility(View.GONE);
            } else if (gmm.equalsIgnoreCase("SGT with SGTD")) {
                ll_sgtnoofrep.setVisibility(View.VISIBLE);
                ll_fgtnoofrep.setVisibility(View.GONE);
                ll_dbtnoofrep.setVisibility(View.VISIBLE);
                ll_spinnermediatype.setVisibility(View.GONE);
            }else if (gmm.equalsIgnoreCase("ALL")) {
                ll_sgtnoofrep.setVisibility(View.VISIBLE);
                ll_fgtnoofrep.setVisibility(View.VISIBLE);
                ll_dbtnoofrep.setVisibility(View.VISIBLE);
                ll_spinnermediatype.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GerminationSetupActivity.this, GerminationHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}