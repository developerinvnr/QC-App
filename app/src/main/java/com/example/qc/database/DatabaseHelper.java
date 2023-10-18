package com.example.qc.database;

import static com.example.qc.utils.Constant.FLAG_CHECK_LOGIN;
import static com.example.qc.utils.Constant.FLAG_CHECK_USERNAME;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.qc.pojo.OfflineDataListPojo;
import com.example.qc.pojo.OfflineDataSyncToServer;
import com.example.qc.pojo.OfflineLoginDetails;
import com.example.qc.pojo.OfflineSampleInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 6;
    // Database Name
    private static final String DATABASE_NAME = "local_database";

    private static final String TABLE_FGT = "tbl_fgt";//table name
    private static final String TABLE_USER = "table_user";

    private static final String COLUMN_ID = "fgtid";
    private static final String COLUMN_CROP = "qcg_crop";
    private static final String COLUMN_VARIETY = "qcg_variety";
    private static final String COLUMN_LOTNO = "qcg_lotno";
    private static final String COLUMN_SAMPLENO = "qcg_sampleno";
    private static final String COLUMN_STAGE = "qcg_stage";
    private static final String COLUMN_TESTTYPE = "qcg_testtype";
    private static final String COLUMN_SETUPFLG = "qcg_setupflg";
    private static final String COLUMN_SETUPDT = "qcg_setupdt";
    private static final String COLUMN_SEEDSIZE = "qcg_seedsize";
    private static final String COLUMN_SEEDSINONEREP = "qcg_noofseedinonerepfgt";
    private static final String COLUMN_FGTTESTTYPE = "qcg_vigtesttype";
    private static final String COLUMN_FGTONEOBFLG = "qcg_vigoneobflg";
    private static final String COLUMN_NOOFREP = "qcg_vignoofrep";
    private static final String COLUMN_OOBNORMAL1 = "qcg_vigoobnormal1";
    private static final String COLUMN_OOBNORMAL2 = "qcg_vigoobnormal2";
    private static final String COLUMN_OOBNORMAL3 = "qcg_vigoobnormal3";
    private static final String COLUMN_OOBNORMAL4 = "qcg_vigoobnormal4";
    private static final String COLUMN_OOBNORMAL5 = "qcg_vigoobnormal5";
    private static final String COLUMN_OOBNORMAL6 = "qcg_vigoobnormal6";
    private static final String COLUMN_OOBNORMAL7 = "qcg_vigoobnormal7";
    private static final String COLUMN_OOBNORMAL8 = "qcg_vigoobnormal8";
    private static final String COLUMN_OOBNORMALAVG = "qcg_vigoobnormalavg";
    private static final String COLUMN_OOBNORMALDT = "qcg_vigoobnormaldt";
    private static final String COLUMN_OOBNORMALLOGID = "qcg_vigoobnormallogid";
    private static final String COLUMN_NORMAL1 = "qcg_vignormal1";
    private static final String COLUMN_NORMAL2 = "qcg_vignormal2";
    private static final String COLUMN_NORMAL3 = "qcg_vignormal3";
    private static final String COLUMN_NORMAL4 = "qcg_vignormal4";
    private static final String COLUMN_NORMAL5 = "qcg_vignormal5";
    private static final String COLUMN_NORMAL6 = "qcg_vignormal6";
    private static final String COLUMN_NORMAL7 = "qcg_vignormal7";
    private static final String COLUMN_NORMAL8 = "qcg_vignormal8";
    private static final String COLUMN_NORMALAVG = "qcg_vignormalavg";
    private static final String COLUMN_ABNORMAL1 = "qcg_vigabnormal1";
    private static final String COLUMN_ABNORMAL2 = "qcg_vigabnormal2";
    private static final String COLUMN_ABNORMAL3 = "qcg_vigabnormal3";
    private static final String COLUMN_ABNORMAL4 = "qcg_vigabnormal4";
    private static final String COLUMN_ABNORMAL5 = "qcg_vigabnormal5";
    private static final String COLUMN_ABNORMAL6 = "qcg_vigabnormal6";
    private static final String COLUMN_ABNORMAL7 = "qcg_vigabnormal7";
    private static final String COLUMN_ABNORMAL8 = "qcg_vigabnormal8";
    private static final String COLUMN_ABNORMALAVG = "qcg_vigabnormalavg";
    private static final String COLUMN_DEADSEED1 = "qcg_vigdead1";
    private static final String COLUMN_DEADSEED2 = "qcg_vigdead2";
    private static final String COLUMN_DEADSEED3 = "qcg_vigdead3";
    private static final String COLUMN_DEADSEED4 = "qcg_vigdead4";
    private static final String COLUMN_DEADSEED5 = "qcg_vigdead5";
    private static final String COLUMN_DEADSEED6 = "qcg_vigdead6";
    private static final String COLUMN_DEADSEED7 = "qcg_vigdead7";
    private static final String COLUMN_DEADSEED8 = "qcg_vigdead8";
    private static final String COLUMN_DEADSEEDAVG = "qcg_vigdeadavg";
    private static final String COLUMN_VIGLOGID = "qcg_viglogid";
    private static final String COLUMN_VIGDT = "qcg_vigdt";
    private static final String COLUMN_VIGGERMP = "qcg_viggermp";
    private static final String COLUMN_VIGFLG = "qcg_vigflg";
    private static final String COLUMN_FIRSTFLG = "qcg_firstflg";
    private static final String COLUMN_DOE = "qcg_fgtdoe";
    private static final String COLUMN_TOTALSEEDS = "qcg_totalseeds";
    private static final String COLUMN_VIGVREMARK = "qcg_vigvremark";
    private static final String COLUMN_OPRREMARK = "qcg_oprremark";
    private static final String COLUMN_REMARK = "qcg_remark";
    private static final String COLUMN_FGTIMAGE = "qcg_fgtimage";
    private static final String COLUMN_FGTFCIMAGE = "qcg_fgtfcimage";
    private static final String COLUMN_FGTSYNCFLG = "qcg_fgtsyncflg";

    private static final String CREATE_TABLE_FGTDATA =
            "CREATE TABLE " + TABLE_FGT + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CROP + " TEXT,"
                    + COLUMN_VARIETY + " TEXT,"
                    + COLUMN_LOTNO + " TEXT,"
                    + COLUMN_SAMPLENO + " TEXT,"
                    + COLUMN_STAGE + " TEXT,"
                    + COLUMN_TESTTYPE + " TEXT,"
                    + COLUMN_SEEDSIZE + " TEXT,"
                    + COLUMN_SEEDSINONEREP + " INTEGER,"
                    + COLUMN_FGTTESTTYPE + " TEXT,"
                    + COLUMN_FGTONEOBFLG + " TEXT,"
                    + COLUMN_NOOFREP + " INTEGER,"
                    + COLUMN_OOBNORMAL1 + " TEXT,"
                    + COLUMN_OOBNORMAL2 + " TEXT,"
                    + COLUMN_OOBNORMAL3 + " TEXT,"
                    + COLUMN_OOBNORMAL4 + " TEXT,"
                    + COLUMN_OOBNORMAL5 + " TEXT,"
                    + COLUMN_OOBNORMAL6 + " TEXT,"
                    + COLUMN_OOBNORMAL7 + " TEXT,"
                    + COLUMN_OOBNORMAL8 + " TEXT,"
                    + COLUMN_OOBNORMALAVG + " TEXT,"
                    + COLUMN_NORMAL1 + " TEXT,"
                    + COLUMN_NORMAL2 + " TEXT,"
                    + COLUMN_NORMAL3 + " TEXT,"
                    + COLUMN_NORMAL4 + " TEXT,"
                    + COLUMN_NORMAL5 + " TEXT,"
                    + COLUMN_NORMAL6 + " TEXT,"
                    + COLUMN_NORMAL7 + " TEXT,"
                    + COLUMN_NORMAL8 + " TEXT,"
                    + COLUMN_NORMALAVG + " TEXT,"
                    + COLUMN_ABNORMAL1 + " TEXT,"
                    + COLUMN_ABNORMAL2 + " TEXT,"
                    + COLUMN_ABNORMAL3 + " TEXT,"
                    + COLUMN_ABNORMAL4 + " TEXT,"
                    + COLUMN_ABNORMAL5 + " TEXT,"
                    + COLUMN_ABNORMAL6 + " TEXT,"
                    + COLUMN_ABNORMAL7 + " TEXT,"
                    + COLUMN_ABNORMAL8 + " TEXT,"
                    + COLUMN_ABNORMALAVG + " TEXT,"
                    + COLUMN_DEADSEED1 + " TEXT,"
                    + COLUMN_DEADSEED2 + " TEXT,"
                    + COLUMN_DEADSEED3 + " TEXT,"
                    + COLUMN_DEADSEED4 + " TEXT,"
                    + COLUMN_DEADSEED5 + " TEXT,"
                    + COLUMN_DEADSEED6 + " TEXT,"
                    + COLUMN_DEADSEED7 + " TEXT,"
                    + COLUMN_DEADSEED8 + " TEXT,"
                    + COLUMN_DEADSEEDAVG + " TEXT,"
                    + COLUMN_VIGLOGID + " TEXT,"
                    + COLUMN_VIGDT + " TEXT,"
                    + COLUMN_VIGGERMP + " TEXT,"
                    + COLUMN_VIGFLG + " TEXT,"
                    + COLUMN_FIRSTFLG + " TEXT,"
                    + COLUMN_DOE + " TEXT,"
                    + COLUMN_TOTALSEEDS + " TEXT,"
                    + COLUMN_VIGVREMARK + " TEXT,"
                    + COLUMN_OPRREMARK + " TEXT,"
                    + COLUMN_REMARK + " TEXT,"
                    + COLUMN_FGTSYNCFLG + " TEXT,"
                    + COLUMN_FGTIMAGE + " BLOB,"
                    + COLUMN_FGTFCIMAGE + " BLOB"
                    + ")";


    //User
    private static final String COLUMN_OPRID = "oprid";
    private static final String COLUMN_OPRNAME = "oprname";
    private static final String COLUMN_SCODE = "scode";
    private static final String COLUMN_PCODE = "pcode";
    private static final String COLUMN_LOGID = "logid";
    private static final String COLUMN_LOGINID = "loginid";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";


    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + "("
                    + COLUMN_OPRID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_OPRNAME + " TEXT,"
                    + COLUMN_SCODE + " TEXT,"
                    + COLUMN_PCODE + " TEXT,"
                    + COLUMN_LOGID + " TEXT,"
                    + COLUMN_ROLE + " TEXT,"
                    + COLUMN_LOGINID + " TEXT,"
                    + COLUMN_PASSWORD + " TEXT"
                    + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(CREATE_TABLE_FGTDATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FGT);
        onCreate(sqLiteDatabase);
    }

    public void saveUserDetails(String oprname, String scode, String pcode, String logid, String role, String loginid, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(COLUMN_OPRNAME, oprname);
        values.put(COLUMN_SCODE, scode);
        values.put(COLUMN_PCODE, pcode);
        values.put(COLUMN_LOGID, logid);
        values.put(COLUMN_ROLE, role);
        values.put(COLUMN_LOGINID, loginid);
        values.put(COLUMN_PASSWORD, password);
        // insert row
        long id = db.insert(TABLE_USER, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public OfflineLoginDetails getLoginDetails(String loginid, String password, Integer flag) {
        OfflineLoginDetails loginObj = null;
        //Select All Query
        String selectQuery = "";
        if (flag== FLAG_CHECK_LOGIN) {
            selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_LOGINID + "=" + "'"+loginid+"'" + " AND " + COLUMN_PASSWORD + "=" + "'"+password+"'";
        }else if (flag==FLAG_CHECK_USERNAME){
            selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_LOGINID + "=" + "'"+loginid+"'";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            if (cursor.getCount() != 0) {
                do {
                    loginObj = new OfflineLoginDetails();
                    loginObj.setOprname(cursor.getString(cursor.getColumnIndex(COLUMN_OPRNAME)));
                    loginObj.setScode(cursor.getString(cursor.getColumnIndex(COLUMN_SCODE)));
                    loginObj.setPcode(cursor.getString(cursor.getColumnIndex(COLUMN_PCODE)));
                    loginObj.setLogid(cursor.getString(cursor.getColumnIndex(COLUMN_LOGID)));
                    loginObj.setRole(cursor.getString(cursor.getColumnIndex(COLUMN_ROLE)));
                    loginObj.setLoginid(cursor.getString(cursor.getColumnIndex(COLUMN_LOGINID)));
                    loginObj.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        // close db connection
        db.close();
        if (flag==FLAG_CHECK_USERNAME){
            if (loginObj!=null){
                if (!password.equals(loginObj.getPassword())){
                    updateUserPassword(loginid,password);
                }
            }
        }
        return loginObj;
    }

    private void updateUserPassword(String loginid, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, password);
        //updating row
        db.update(TABLE_USER, values, COLUMN_LOGINID + " = ?", new String[]{loginid});
    }

    public int getRowCount(String sampleno) {
        int totRows = 0;
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FGT + " WHERE " + COLUMN_SAMPLENO + "='" + sampleno + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        totRows=cursor.getCount();
        cursor.close();
        // close db connection
        db.close();
        return totRows;
    }

    public int getSampleStatus(String sampleno) {
        int totRows = 0;
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FGT + " WHERE " + COLUMN_SAMPLENO + "='" + sampleno + "' AND " + COLUMN_VIGFLG + "=" + 1;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        totRows=cursor.getCount();
        cursor.close();
        // close db connection
        db.close();
        return totRows;
    }

    @SuppressLint("Range")
    public OfflineSampleInfo getSampleDetails(String sampleno) {
        OfflineSampleInfo loginObj = null;
        //Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FGT + " WHERE " + COLUMN_SAMPLENO + "='" + sampleno + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            if (cursor.getCount() != 0) {
                do {
                    loginObj = new OfflineSampleInfo();
                    loginObj.setCrop(cursor.getString(cursor.getColumnIndex(COLUMN_CROP)));
                    loginObj.setVariety(cursor.getString(cursor.getColumnIndex(COLUMN_VARIETY)));
                    loginObj.setLotno(cursor.getString(cursor.getColumnIndex(COLUMN_LOTNO)));
                    loginObj.setSampleno(cursor.getString(cursor.getColumnIndex(COLUMN_SAMPLENO)));
                    loginObj.setTrstage(cursor.getString(cursor.getColumnIndex(COLUMN_STAGE)));
                    loginObj.setSeedsize(cursor.getString(cursor.getColumnIndex(COLUMN_SEEDSIZE)));
                    loginObj.setSeedinrep(cursor.getString(cursor.getColumnIndex(COLUMN_SEEDSINONEREP)));
                    loginObj.setNoofrep(cursor.getString(cursor.getColumnIndex(COLUMN_NOOFREP)));
                    loginObj.setFgttesttype(cursor.getString(cursor.getColumnIndex(COLUMN_FGTTESTTYPE)));
                    loginObj.setTesttype(cursor.getString(cursor.getColumnIndex(COLUMN_TESTTYPE)));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        // close db connection
        db.close();
        return loginObj;
    }

    public void saveSampleDetails(String crop, String variety, String lotno, String sampleno, String trstage, String testtype, String seedsize, Integer seedinrep, String fgttesttype, Integer noofrep) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(COLUMN_CROP, crop);
        values.put(COLUMN_VARIETY, variety);
        values.put(COLUMN_LOTNO, lotno);
        values.put(COLUMN_SAMPLENO, sampleno);
        values.put(COLUMN_STAGE, trstage);
        values.put(COLUMN_TESTTYPE, testtype);
        values.put(COLUMN_SEEDSIZE, seedsize);
        values.put(COLUMN_SEEDSINONEREP, seedinrep);
        values.put(COLUMN_FGTTESTTYPE, fgttesttype);
        values.put(COLUMN_NOOFREP, noofrep);
        values.put(COLUMN_FGTSYNCFLG, "0");

        // insert row
        long id = db.insert(TABLE_FGT, null, values);
        Log.e("InsertID", String.valueOf(id));
        db.close();
    }

    @SuppressLint("Range")
    public List<OfflineDataListPojo> getTransactioList() {
        List<OfflineDataListPojo> transactionList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FGT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            if (cursor.getCount() != 0) {
                do {
                    OfflineDataListPojo transactionObj = new OfflineDataListPojo();
                    transactionObj.setCrop(cursor.getString(cursor.getColumnIndex(COLUMN_CROP)));
                    transactionObj.setVariety(cursor.getString(cursor.getColumnIndex(COLUMN_VARIETY)));
                    transactionObj.setLotno(cursor.getString(cursor.getColumnIndex(COLUMN_LOTNO)));
                    transactionObj.setSampleno(cursor.getString(cursor.getColumnIndex(COLUMN_SAMPLENO)));
                    transactionObj.setTrstage(cursor.getString(cursor.getColumnIndex(COLUMN_STAGE)));
                    transactionObj.setQcgTesttype(cursor.getString(cursor.getColumnIndex(COLUMN_TESTTYPE)));
                    transactionObj.setSeedsize(cursor.getString(cursor.getColumnIndex(COLUMN_SEEDSIZE)));
                    transactionObj.setQcgNoofseedinonerepfgt(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SEEDSINONEREP))));
                    transactionObj.setQcgVigtesttype(cursor.getString(cursor.getColumnIndex(COLUMN_FGTTESTTYPE)));
                    transactionObj.setQcgVignoofrep(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_NOOFREP))));
                    transactionList.add(transactionObj);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        //close db connection
        db.close();
        return transactionList;
    }

    public void updateFCSampleDetails(String sampleno, String obrstype1, String vegremark, String techremark, String normalRep1, String abnormalRep1, String deadRep1, String normalRep2, String abnormalRep2, String deadRep2, String normalRep3, String abnormalRep3, String deadRep3, String normalRep4, String abnormalRep4, String deadRep4, String normalRep5, String abnormalRep5, String deadRep5, String normalRep6, String abnormalRep6, String deadRep6, String normalRep7, String abnormalRep7, String deadRep7, String normalRep8, String abnormalRep8, String deadRep8, String normalseedper, String abnormalseedper, String deadseedper, String doefgt, String remark, byte[] list) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VIGVREMARK, vegremark);
        values.put(COLUMN_OPRREMARK, techremark);
        values.put(COLUMN_NORMAL1, normalRep1);
        values.put(COLUMN_NORMAL2, normalRep2);
        values.put(COLUMN_NORMAL3, normalRep3);
        values.put(COLUMN_NORMAL4, normalRep4);
        values.put(COLUMN_NORMAL5, normalRep5);
        values.put(COLUMN_NORMAL6, normalRep6);
        values.put(COLUMN_NORMAL7, normalRep7);
        values.put(COLUMN_NORMAL8, normalRep8);
        values.put(COLUMN_ABNORMAL1, abnormalRep1);
        values.put(COLUMN_ABNORMAL2, abnormalRep2);
        values.put(COLUMN_ABNORMAL3, abnormalRep3);
        values.put(COLUMN_ABNORMAL4, abnormalRep4);
        values.put(COLUMN_ABNORMAL5, abnormalRep5);
        values.put(COLUMN_ABNORMAL6, abnormalRep6);
        values.put(COLUMN_ABNORMAL7, abnormalRep7);
        values.put(COLUMN_ABNORMAL8, abnormalRep8);
        values.put(COLUMN_DEADSEED1, deadRep1);
        values.put(COLUMN_DEADSEED2, deadRep2);
        values.put(COLUMN_DEADSEED3, deadRep3);
        values.put(COLUMN_DEADSEED4, deadRep4);
        values.put(COLUMN_DEADSEED5, deadRep5);
        values.put(COLUMN_DEADSEED6, deadRep6);
        values.put(COLUMN_DEADSEED7, deadRep7);
        values.put(COLUMN_DEADSEED8, deadRep8);
        values.put(COLUMN_NORMALAVG, normalseedper);
        values.put(COLUMN_ABNORMALAVG, abnormalseedper);
        values.put(COLUMN_DEADSEEDAVG, deadseedper);
        values.put(COLUMN_DOE, doefgt);
        values.put(COLUMN_REMARK, remark);
        values.put(COLUMN_FGTFCIMAGE, list);
        values.put(COLUMN_VIGFLG, "1");
        //updating row
        db.update(TABLE_FGT, values, COLUMN_SAMPLENO + " = ?", new String[]{sampleno});
    }

    public void updateSampleDetails(String sampleno, String obrstype1, String normalseedRep1, String normalseedRep2, String normalseedRep3, String normalseedRep4, String normalseedRep5, String normalseedRep6, String normalseedRep7, String normalseedRep8, String normalseedper, String doefgt, String remark, byte[] list) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OOBNORMAL1, normalseedRep1);
        values.put(COLUMN_OOBNORMAL2, normalseedRep2);
        values.put(COLUMN_OOBNORMAL3, normalseedRep3);
        values.put(COLUMN_OOBNORMAL4, normalseedRep4);
        values.put(COLUMN_OOBNORMAL5, normalseedRep5);
        values.put(COLUMN_OOBNORMAL6, normalseedRep6);
        values.put(COLUMN_OOBNORMAL7, normalseedRep7);
        values.put(COLUMN_OOBNORMAL8, normalseedRep8);
        values.put(COLUMN_OOBNORMALAVG, normalseedper);
        values.put(COLUMN_DOE, doefgt);
        values.put(COLUMN_REMARK, remark);
        values.put(COLUMN_FGTIMAGE, list);
        values.put(COLUMN_FGTONEOBFLG, "1");
        //updating row
        db.update(TABLE_FGT, values, COLUMN_SAMPLENO + " = ?", new String[]{sampleno});
        
    }

    @SuppressLint("Range")
    public List<OfflineDataSyncToServer> getFGTDataForServer() {
        List<OfflineDataSyncToServer> transactionList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FGT + " WHERE " + COLUMN_VIGFLG + "=" + 1 + " AND " + COLUMN_FGTSYNCFLG + "=" + 0;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            if (cursor.getCount() != 0) {
                do {
                    OfflineDataSyncToServer transactionObj = new OfflineDataSyncToServer();
                    transactionObj.setCrop(cursor.getString(cursor.getColumnIndex(COLUMN_CROP)));
                    transactionObj.setVariety(cursor.getString(cursor.getColumnIndex(COLUMN_VARIETY)));
                    transactionObj.setLotno(cursor.getString(cursor.getColumnIndex(COLUMN_LOTNO)));
                    transactionObj.setSampleno(cursor.getString(cursor.getColumnIndex(COLUMN_SAMPLENO)));
                    transactionObj.setQcg_vigoobnormal1(cursor.getString(cursor.getColumnIndex(COLUMN_OOBNORMAL1)));
                    transactionObj.setQcg_vigoobnormal2(cursor.getString(cursor.getColumnIndex(COLUMN_OOBNORMAL2)));
                    transactionObj.setQcg_vigoobnormal3(cursor.getString(cursor.getColumnIndex(COLUMN_OOBNORMAL3)));
                    transactionObj.setQcg_vigoobnormal4(cursor.getString(cursor.getColumnIndex(COLUMN_OOBNORMAL4)));
                    transactionObj.setQcg_vigoobnormal5(cursor.getString(cursor.getColumnIndex(COLUMN_OOBNORMAL5)));
                    transactionObj.setQcg_vigoobnormal6(cursor.getString(cursor.getColumnIndex(COLUMN_OOBNORMAL6)));
                    transactionObj.setQcg_vigoobnormal7(cursor.getString(cursor.getColumnIndex(COLUMN_OOBNORMAL7)));
                    transactionObj.setQcg_vigoobnormal8(cursor.getString(cursor.getColumnIndex(COLUMN_OOBNORMAL8)));
                    transactionObj.setQcg_vigoobnormalavg(cursor.getString(cursor.getColumnIndex(COLUMN_OOBNORMALAVG)));
                    transactionObj.setQcg_vignormal1(cursor.getString(cursor.getColumnIndex(COLUMN_NORMAL1)));
                    transactionObj.setQcg_vignormal2(cursor.getString(cursor.getColumnIndex(COLUMN_NORMAL2)));
                    transactionObj.setQcg_vignormal3(cursor.getString(cursor.getColumnIndex(COLUMN_NORMAL3)));
                    transactionObj.setQcg_vignormal4(cursor.getString(cursor.getColumnIndex(COLUMN_NORMAL4)));
                    transactionObj.setQcg_vignormal5(cursor.getString(cursor.getColumnIndex(COLUMN_NORMAL5)));
                    transactionObj.setQcg_vignormal6(cursor.getString(cursor.getColumnIndex(COLUMN_NORMAL6)));
                    transactionObj.setQcg_vignormal7(cursor.getString(cursor.getColumnIndex(COLUMN_NORMAL7)));
                    transactionObj.setQcg_vignormal8(cursor.getString(cursor.getColumnIndex(COLUMN_NORMAL8)));
                    transactionObj.setQcg_vignormalavg(cursor.getString(cursor.getColumnIndex(COLUMN_NORMALAVG)));
                    transactionObj.setQcg_vigabnormal1(cursor.getString(cursor.getColumnIndex(COLUMN_ABNORMAL1)));
                    transactionObj.setQcg_vigabnormal2(cursor.getString(cursor.getColumnIndex(COLUMN_ABNORMAL2)));
                    transactionObj.setQcg_vigabnormal3(cursor.getString(cursor.getColumnIndex(COLUMN_ABNORMAL3)));
                    transactionObj.setQcg_vigabnormal4(cursor.getString(cursor.getColumnIndex(COLUMN_ABNORMAL4)));
                    transactionObj.setQcg_vigabnormal5(cursor.getString(cursor.getColumnIndex(COLUMN_ABNORMAL5)));
                    transactionObj.setQcg_vigabnormal6(cursor.getString(cursor.getColumnIndex(COLUMN_ABNORMAL6)));
                    transactionObj.setQcg_vigabnormal7(cursor.getString(cursor.getColumnIndex(COLUMN_ABNORMAL7)));
                    transactionObj.setQcg_vigabnormal8(cursor.getString(cursor.getColumnIndex(COLUMN_ABNORMAL8)));
                    transactionObj.setQcg_vigabnormalavg(cursor.getString(cursor.getColumnIndex(COLUMN_ABNORMALAVG)));
                    transactionObj.setQcg_vigdead1(cursor.getString(cursor.getColumnIndex(COLUMN_DEADSEED1)));
                    transactionObj.setQcg_vigdead2(cursor.getString(cursor.getColumnIndex(COLUMN_DEADSEED2)));
                    transactionObj.setQcg_vigdead3(cursor.getString(cursor.getColumnIndex(COLUMN_DEADSEED3)));
                    transactionObj.setQcg_vigdead4(cursor.getString(cursor.getColumnIndex(COLUMN_DEADSEED4)));
                    transactionObj.setQcg_vigdead5(cursor.getString(cursor.getColumnIndex(COLUMN_DEADSEED5)));
                    transactionObj.setQcg_vigdead6(cursor.getString(cursor.getColumnIndex(COLUMN_DEADSEED6)));
                    transactionObj.setQcg_vigdead7(cursor.getString(cursor.getColumnIndex(COLUMN_DEADSEED7)));
                    transactionObj.setQcg_vigdead8(cursor.getString(cursor.getColumnIndex(COLUMN_DEADSEED8)));
                    transactionObj.setQcg_vigdeadavg(cursor.getString(cursor.getColumnIndex(COLUMN_DEADSEEDAVG)));
                    transactionObj.setQcg_fgtdoe(cursor.getString(cursor.getColumnIndex(COLUMN_DOE)));
                    transactionObj.setQcg_totalseeds(cursor.getString(cursor.getColumnIndex(COLUMN_TOTALSEEDS)));
                    transactionObj.setQcg_vigvremark(cursor.getString(cursor.getColumnIndex(COLUMN_VIGVREMARK)));
                    transactionObj.setQcg_oprremark(cursor.getString(cursor.getColumnIndex(COLUMN_OPRREMARK)));
                    transactionObj.setQcg_fgtimage(cursor.getString(cursor.getColumnIndex(COLUMN_FGTIMAGE)));
                    transactionObj.setQcg_fgtfcimage(cursor.getString(cursor.getColumnIndex(COLUMN_FGTIMAGE)));
                    transactionList.add(transactionObj);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        //close db connection
        db.close();
        return transactionList;
    }

    public void updateArrSyncFlag(String flg, String syncflag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FGTSYNCFLG, syncflag);
        //updating row
        db.update(TABLE_FGT, values, COLUMN_VIGFLG + " = ?", new String[]{flg});
    }
}
