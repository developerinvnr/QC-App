package com.example.qc.sharedpreference;

import android.content.Context;
import com.google.gson.Gson;

public class SharedPreferences {
    private android.content.SharedPreferences pref;
    // Editor reference for Shared preferences
    private android.content.SharedPreferences.Editor editor;
    private Context _context;
    private static final String PREFER_NAME = "ACapPref";
    public static final String KEY_LOGIN_OBJ = "loginobj";
    public static final String KEY_SAMPLEINFO_OBJ = "sampleinfoobj";
    public static final String KEY_SAMPLEPPINFO_OBJ = "sampleppinfoobj";
    public static final String KEY_SAMPLEMOISTUREINFO_OBJ = "samplemoistinfoobj";
    public static final String KEY_SAMPLEGERMINATIONINFO_OBJ = "samplegerminfoobj";
    public static final String KEY_SESSIONID = "key_sessionid";
    public static final String KEY_MOBILE1 = "mobile1";
    public static final String KEY_SCODE = "scode";
    public static final String KEY_SAMPLENO = "sampleno";
    public static final String KEY_MESSAGE = "mwssage";

    private static SharedPreferences sSharedPrefs;

    public SharedPreferences(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }


    public static SharedPreferences getInstance(Context context) {
        if (sSharedPrefs == null) {
            sSharedPrefs = new SharedPreferences(context.getApplicationContext());
        }
        return sSharedPrefs;
    }

    public static SharedPreferences getInstance() {
        if (sSharedPrefs != null) {
            return sSharedPrefs;
        }

        //Option 1:
        throw new IllegalArgumentException("Should use getInstance(Context) at least once before using this method.");

        //Option 2:
        // Alternatively, you can create a new instance here
        // with something like this:
        // getInstance(MyCustomApplication.getAppContext());
    }

    public void storeObject(String key, Object object) {
        Gson gson = new Gson();
        String json = null;
        if (object != null)
            json = gson.toJson(object);
        editor.putString(key, json);
        editor.commit();

    }

    public Object getObject(String key, Class cls) {
        Gson gson = new Gson();
        String json = pref.getString(key, "");
        if (!json.equals("")) {
            return gson.fromJson(json, cls);
        } else {
            return null;
        }

    }

    public String getJsonFromObject(Object object) {
        Gson gson = new Gson();
        String json = null;
        if (object != null)
            json = gson.toJson(object);
        return json;
    }

    public void createString(String string, String key) {
        editor.putString(key, string);
        editor.commit();
    }

    public String getString(String key) {
        return pref.getString(key, null);
    }
}
