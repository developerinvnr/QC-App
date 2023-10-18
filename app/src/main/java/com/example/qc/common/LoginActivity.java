package com.example.qc.common;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qc.MainActivity;
import com.example.qc.R;
import com.example.qc.database.DatabaseHelper;
import com.example.qc.parser.JsonParser;
import com.example.qc.pojo.LoginResponse;
import com.example.qc.pojo.OfflineLoginDetails;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.AppConfig;
import com.example.qc.utils.ConnectionDetector;
import com.example.qc.utils.Constant;
import com.example.qc.utils.Utils;
import com.example.qc.volley.SendVolleyCall;
import com.example.qc.volley.volleyCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView textView;
    private EditText inputEmail;
    private EditText inputPassword;
    private Button subBtn;

    private ProgressDialog pDialog;

    private DatabaseHelper dbobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTheme();
        init();
    }

    private void setTheme() {
        getSupportActionBar().hide();
        SharedPreferences.getInstance(getApplicationContext());
        Utils.getInstance(getApplicationContext());
        //for internet detection
        Utils.getInstance().initConnectionDetector();
        dbobj = new DatabaseHelper(getApplicationContext());
        textView = findViewById(R.id.forgotpassward);
        inputEmail = findViewById(R.id.loginId);
        inputPassword = findViewById(R.id.passward);
        subBtn = findViewById(R.id.submitButton);

    }

    private void init() {
        LoginResponse loginRes = (LoginResponse) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_LOGIN_OBJ, LoginResponse.class);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(Html.fromHtml("Forgot Password?"));
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        inputPassword.setFilters(new InputFilter[]{filter});
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userid = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (new ConnectionDetector(LoginActivity.this).isConnectingToInternet()) {
                    if (!userid.isEmpty() && !password.isEmpty()) {

                        /*Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);

                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        String formattedDate = df.format(c);*/
                        checkLogin(userid, password);

                    } else {
                        // Prompt user to enter credentials
                        //Toast.makeText(getApplicationContext(), "Please enter the Mobile No/Passward!", Toast.LENGTH_LONG).show();
                        if (userid.isEmpty()) {
                            Utils.getInstance().showAlert(LoginActivity.this, "Please enter Login ID");
                        } else if (password.isEmpty()) {
                            Utils.getInstance().showAlert(LoginActivity.this, "Please enter Password");
                        } else {
                            Utils.getInstance().showAlert(LoginActivity.this, "Please enter Login ID and Password");
                        }
                    }
                } else {
                    OfflineLoginDetails userobj = dbobj.getLoginDetails(userid, password, Constant.FLAG_CHECK_LOGIN);
                    if (userobj == null) {
                        Utils.getInstance().showAlert(LoginActivity.this, "Invalid Login ID and Password");
                        return;
                    } else {
                        SharedPreferences.getInstance().createString("offlinesession", SharedPreferences.KEY_SESSIONID);
                        LoginResponse loginResponse = new LoginResponse(userobj.getOprname(), userobj.getRole(), userid, "offlinesession", userobj.getScode(), userobj.getPcode(), "Success", "");
                        SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_LOGIN_OBJ, loginResponse);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void checkLogin(final String userid, String password) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Logging in ...");
        pDialog.show();
        pDialog.setCancelable(false);
        Map<String, String> params = new HashMap<>();
        params.put("mobile1", userid);
        params.put("password", password);

        new SendVolleyCall().executeProgram(LoginActivity.this, AppConfig.LOGIN_PROGRAM, params, new volleyCallback() {
            @Override
            public void onSuccess(String response) {
                pDialog.dismiss();
                JsonParser.getInstance().loginParser(response);
                LoginResponse loginRes = (LoginResponse) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_LOGIN_OBJ, LoginResponse.class);
                if (loginRes.getMassage().equalsIgnoreCase("Success")) {
                    OfflineLoginDetails userobj = dbobj.getLoginDetails(userid, password, Constant.FLAG_CHECK_USERNAME);
                    if (userobj == null) {
                        dbobj.saveUserDetails(loginRes.getName(), loginRes.getScode(), loginRes.getPcode(), "AR", loginRes.getRole(), userid, password);
                    }
                    SharedPreferences.getInstance().createString(userid, SharedPreferences.KEY_MOBILE1);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    //error
                    //CommonDesign.getInstance().showCustomToast(LoginActivity.this, loginRes.getMassage());
                    Toast.makeText(getApplicationContext(), loginRes.getMassage(), Toast.LENGTH_SHORT).show();
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