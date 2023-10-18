package com.example.qc.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class SessionManager {
    android.content.SharedPreferences pref;
    SharedPreferences pref1;

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor1;
    Context _context;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidHiveLogin";
    private static final String PREF_NAME1 = "DeviceID";
    public static final String KEY_COLORMAP = "colormap";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        pref1 = _context.getSharedPreferences(PREF_NAME1, Context.MODE_PRIVATE);
        editor = pref.edit();
        editor1 = pref1.edit();
    }

    public void saveMap(HashMap<String, String> map, String key) {
        String jsonString = getJsonStringFromMap(map);
        editor.putString(key, jsonString);
        editor.commit();
    }

    public HashMap<String, String> getMap(String key) {
        String jsonString = pref.getString(key, null);
        if (jsonString != null) {
            return getMapFronJsonString(jsonString);
        } else {
            return new HashMap<>();
        }
    }

    private String getJsonStringFromMap(HashMap<String, String> map) {
        JSONObject jsonObject = new JSONObject(map);
        return jsonObject.toString();
    }

    private HashMap<String, String> getMapFronJsonString(String jsonString) {

        HashMap<String, String> Map = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> keysItr = jsonObject.keys();
            while (keysItr.hasNext()) {
                String key = keysItr.next();
                String value = (String) jsonObject.get(key);
                Map.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Map;
    }

    public void saveString(String string, String key) {

        editor.putString(key, string);
        // commit changes
        editor.commit();

    }

    public String getString(String key) {

        return pref.getString(key, null);
    }
}
