package com.example.qc.germination;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qc.MainActivity;
import com.example.qc.R;
import com.example.qc.parser.SubmitSuccessResponse;
import com.example.qc.parser.germpsampleinfo.GermpSampleInfoResponse;
import com.example.qc.parser.germpsampleinfo.Samplegemparray;
import com.example.qc.pp.Utility;
import com.example.qc.retrofit.ApiInterface;
import com.example.qc.retrofit.RetrofitClient;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.Utils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jsibbold.zoomage.ZoomageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kotlin.io.TextStreamsKt;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageUploadActivity extends AppCompatActivity {
    private static final String TAG = "ImageUploadActivity";
    private TextView tv_crop,tv_variety,tv_lono,tv_qtykg,tv_stage,tv_sampleno,tv_srfNo;
    private ImageButton back_nav;
    private ImageButton btn_samplescan;
    private ImageButton btn_samplemanual;
    private LinearLayout ll_form;
    private Button btn_submit;

    private RadioGroup radioGrpGerm;
    private RadioGroup radioGrpObrs;

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
    private String gmmtype="FGT";
    private String obrType="Final Count";
    private String cameraString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        getSupportActionBar().hide();
        setTheme();
        init();
    }

    private void setTheme() {
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        Utils.getInstance().initConnectionDetector();
        back_nav = findViewById(R.id.back_nav);
        btn_samplescan = findViewById(R.id.btn_samplescan);
        btn_samplemanual = findViewById(R.id.btn_samplemanual);
        btn_submit = findViewById(R.id.btn_submit);

        radioGrpGerm = findViewById(R.id.radioGrpGerm);
        RadioButton radiosgt = findViewById(R.id.radiosgt);
        RadioButton radiofgt = findViewById(R.id.radiofgt);
        RadioButton radiodbt = findViewById(R.id.radiodbt);
        radioGrpObrs = findViewById(R.id.radioGrpObrs);
        RadioButton radioOneObs = findViewById(R.id.radioOneObs);
        RadioButton radioFinalObs = findViewById(R.id.radioFinalObs);

        tv_crop = findViewById(R.id.tv_crop);
        tv_variety = findViewById(R.id.tv_variety);
        tv_lono = findViewById(R.id.tv_lono);
        tv_qtykg = findViewById(R.id.tv_qtykg);
        tv_stage = findViewById(R.id.tv_stage);
        tv_sampleno = findViewById(R.id.tv_sampleno);
        tv_srfNo = findViewById(R.id.tv_srfNo);
        ll_form = findViewById(R.id.ll_form);

        //Photo Upload
        iv_photo = findViewById(R.id.iv_photo);
        button_photo = findViewById(R.id.button_photo);
        ll_germpPhoto = findViewById(R.id.ll_germpPhoto);
    }

    private void init(){
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageUploadActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        btn_samplescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraString="QR Code";
                IntentIntegrator integrator = new IntentIntegrator(ImageUploadActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(false);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        btn_samplemanual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ImageUploadActivity.this);
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
                        dialog.dismiss();
                        getSampleInfo(sampleno);
                    }
                });
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int germmethod = radioGrpGerm.getCheckedRadioButtonId();
                RadioButton mois = findViewById(germmethod);
                gmmtype = mois.getText().toString();

                int rdGrpObrs = radioGrpObrs.getCheckedRadioButtonId();
                RadioButton germpObr = findViewById(rdGrpObrs);
                obrType = germpObr.getText().toString();
                String sampleno = tv_sampleno.getText().toString().trim();
                if (list.isEmpty()){
                    Toast.makeText(ImageUploadActivity.this, "Upload Image", Toast.LENGTH_SHORT).show();
                } else if (sampleno.isEmpty()) {
                    Toast.makeText(ImageUploadActivity.this, "Scan or enter sample number", Toast.LENGTH_SHORT).show();
                } else {
                    submitImage();
                }
            }
        });

        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sampleno = tv_sampleno.getText().toString().trim();
                if (sampleno.isEmpty()){
                    Toast.makeText(ImageUploadActivity.this, "Scan or enter sample number", Toast.LENGTH_SHORT).show();
                }else {
                    cameraString="Photo Upload";
                    selectImage();
                }

            }
        });

    }

    private void submitImage() {
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        String scode = SharedPreferences.getInstance().getString(SharedPreferences.KEY_SCODE);
        String sampleno = tv_sampleno.getText().toString().trim();
        RequestBody userid1 = RequestBody.create(MediaType.parse("text/plain"), userid);
        RequestBody scode1 = RequestBody.create(MediaType.parse("text/plain"), scode);
        RequestBody sampleno1 = RequestBody.create(MediaType.parse("text/plain"), sampleno);
        RequestBody obrType1 = RequestBody.create(MediaType.parse("text/plain"), obrType);
        RequestBody gmmtype1 = RequestBody.create(MediaType.parse("text/plain"), gmmtype);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Call<SubmitSuccessResponse> call =apiInterface.submitImage(userid1,scode1,sampleno1,gmmtype1,obrType1, list);
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
                        Intent intent = new Intent(ImageUploadActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(ImageUploadActivity.this, submitSuccessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.cancel();
                    if (response.errorBody() != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            String msg = jsonObj.getString("message");
                            Toast.makeText(ImageUploadActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ImageUploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void getSampleInfo(String sampleno) {
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Log.e("Params:", sampleno);
        String userid = SharedPreferences.getInstance().getString(SharedPreferences.KEY_MOBILE1);
        Call<GermpSampleInfoResponse> call =apiInterface.getGermpSampleInfo(userid,sampleno);

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
                                list.clear();
                                ll_form.setVisibility(View.GONE);
                                tv_crop.setText("");
                                tv_variety.setText("");
                                tv_lono.setText("");
                                tv_qtykg.setText("");
                                tv_stage.setText("");
                                tv_sampleno.setText("");
                                tv_srfNo.setText("");
                            }else {
                                Samplegemparray sampleInfo = germpSampleInfoResponse.getUser().getSamplegemparray();
                                tv_crop.setText(sampleInfo.getCrop());
                                tv_variety.setText(sampleInfo.getVariety());
                                tv_lono.setText(sampleInfo.getLotno());
                                tv_qtykg.setText(String.valueOf(sampleInfo.getQty()));
                                tv_stage.setText(sampleInfo.getTrstage());
                                tv_sampleno.setText(sampleInfo.getSampleno());
                                tv_srfNo.setText(sampleInfo.getSrfno());
                                ll_form.setVisibility(View.VISIBLE);
                                list.clear();
                            }
                        }else {
                            list.clear();
                            ll_form.setVisibility(View.GONE);
                            /*assert response.body() != null;
                            JSONObject jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            String msg = jsonObj.getString("error_msg");*/
                            Toast.makeText(ImageUploadActivity.this, germpSampleInfoResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    progressDialog.cancel();
                    if (response.errorBody() != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            String msg = jsonObj.getString("error_msg");
                            Toast.makeText(ImageUploadActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ImageUploadActivity.this, "RetrofitError : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = {"Click here to open camera", "Choose from gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ImageUploadActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ImageUploadActivity.this);

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
        if (cameraString.equalsIgnoreCase("Photo Upload")){
            if (resultCode == RESULT_OK) {
                if (requestCode == SELECT_FILE) {
                    onSelectFromGalleryResult(data);
                } else if (requestCode == REQUEST_CAMERA) {
                    onCaptureImageResult(data);
                }
            }
        }else {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanningResult != null) {
                String scanContent = "";
                String scanFormat = "";
                if (scanningResult.getContents() != null) {
                    scanContent = scanningResult.getContents().toString();
                    scanFormat = scanningResult.getFormatName().toString();
                }
                getSampleInfo(scanContent);
                //Toast.makeText(this,scanContent+"   type:"+scanFormat,Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Nothing scanned",Toast.LENGTH_SHORT).show();
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
            MediaScannerConnection.scanFile(ImageUploadActivity.this, new String[]{BILL_COPY.getPath()}, new String[]{"image/jpeg"}, null);
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
        Intent intent = new Intent(ImageUploadActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}