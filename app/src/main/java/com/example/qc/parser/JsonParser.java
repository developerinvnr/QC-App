package com.example.qc.parser;

import android.content.Context;

import com.example.qc.adapter.FMV_Reasons;
import com.example.qc.pojo.LoginResponse;
import com.example.qc.pojo.SampleGOTInfo;
import com.example.qc.pojo.SampleGerminationInfo;
import com.example.qc.pojo.SampleInfo;
import com.example.qc.pojo.SampleMoistureInfo;
import com.example.qc.pojo.SamplePPInfo;
import com.example.qc.sharedpreference.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    private static final JsonParser ourInstance = new JsonParser();


    public static JsonParser getInstance() {
        return ourInstance;
    }

    private JsonParser() {

    }

    public void loginParser(String response) {
        String massage;
        try {
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");
            // Check for error node in json

            if (!error) {
                //success
                JSONObject user = jObj.getJSONObject("user");
                String name = user.getString("name");
                String role = user.getString("role");
                String mobile1 = user.getString("deviceid");
                String sessionid = user.getString("sessionid");
                String scode = user.getString("scode");
                String pcode = user.getString("pcode");
                String oprtype = user.getString("oprtype");
                massage = "Success";
                SharedPreferences.getInstance().createString(sessionid, SharedPreferences.KEY_SESSIONID);
                SharedPreferences.getInstance().createString(scode, SharedPreferences.KEY_SCODE);
                LoginResponse loginResponse = new LoginResponse(name, role, mobile1, sessionid, scode, pcode, massage,oprtype);
                SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_LOGIN_OBJ, loginResponse);

            } else {
                String returnString = jObj.getString("error_msg");
                LoginResponse loginResponse = new LoginResponse(null, null, null, null,null,null, returnString,null);
                SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_LOGIN_OBJ, loginResponse);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sampleInfoParser(String response) {
        String massage;
        try {
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");
            // Check for error node in json

            if (!error) {
                //success
                JSONObject user = jObj.getJSONObject("user");
                JSONArray samplearray = user.getJSONArray("samplearray");
                for (int i = 0; i < samplearray.length(); i++) {
                    JSONObject details = samplearray.getJSONObject(i);
                    String srdate = details.getString("srdate");
                    String crop = details.getString("crop");
                    String variety = details.getString("variety");
                    String lotno = details.getString("lotno");
                    String trstage = details.getString("trstage");
                    String nob = details.getString("nob");
                    String qty = details.getString("qty");
                    String sloc = details.getString("sloc");
                    String qctest = details.getString("qctest");
                    String sampleno = details.getString("sampleno");
                    String spdate = details.getString("spdate");
                    String sampflg = details.getString("sampflg");
                    String srfseries = details.getString("srfseries");
                    String srfno = details.getString("srfno");
                    massage="Success";

                    SampleInfo sampleInfo = new SampleInfo(srdate, crop, variety, lotno, trstage, nob, qty, sloc, qctest, sampleno, spdate, sampflg,massage, srfseries, srfno);
                    SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_SAMPLEINFO_OBJ, sampleInfo);
                }

            } else {
                String returnString = jObj.getString("error_msg");
                SampleInfo sampleInfo = new SampleInfo(null, null, null, null, null, null, null, null, null, null, null, null, returnString, "", "");
                SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_SAMPLEINFO_OBJ, sampleInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sampleBTSInfoParser(String response) {
        String massage;
        try {
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");
            // Check for error node in json

            if (!error) {
                //success
                JSONObject user = jObj.getJSONObject("user");
                JSONArray samplearray = user.getJSONArray("samplearray");
                for (int i = 0; i < samplearray.length(); i++) {
                    JSONObject details = samplearray.getJSONObject(i);
                    String crop = details.getString("crop");
                    String variety = details.getString("variety");
                    String lotno = details.getString("lotno");
                    String trstage = details.getString("trstage");
                    String nob = details.getString("nob");
                    String qty = details.getString("qty");
                    String sampleno = details.getString("sampleno");
                    /*String srfseries = details.getString("srfseries");
                    String srfno = details.getString("srfno");*/
                    massage="Success";

                    SampleInfo sampleInfo = new SampleInfo("srdate", crop, variety, lotno, trstage, nob, qty, "sloc", "qctest", sampleno, "spdate", "sampflg",massage, "", "");
                    SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_SAMPLEINFO_OBJ, sampleInfo);
                }

            } else {
                String returnString = jObj.getString("error_msg");
                SampleInfo sampleInfo = new SampleInfo(null, null, null, null, null, null, null, null, null, null, null, null, returnString, "", "");
                SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_SAMPLEINFO_OBJ, sampleInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sampleGOTInfoParser(String response) {
        String massage;
        try {
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");
            // Check for error node in json

            if (!error) {
                //success
                JSONObject user = jObj.getJSONObject("user");
                JSONArray samplearray = user.getJSONArray("infodataarray");
                for (int i = 0; i < samplearray.length(); i++) {
                    JSONObject details = samplearray.getJSONObject(i);
                    String srdate = details.getString("srdate");
                    String crop = details.getString("crop");
                    String variety = details.getString("variety");
                    String lotno = details.getString("lotno");
                    String sampleno = details.getString("sampleno");
                    String trstage = details.getString("trstage");
                    String scdate = details.getString("scdate");
                    String sddate = details.getString("sddate");
                    String ackdate = details.getString("ackdate");
                    String ackflg = details.getString("ackflg");
                    String sowingflg = details.getString("sowingflg");
                    String transplantflg = details.getString("transplantflg");
                    String fieldmflg = details.getString("fieldmflg");
                    String finobrflg = details.getString("finobrflg");
                    String resamplingflg = details.getString("resamplingflg");
                    String retestflg = details.getString("retestflg");
                    String dosow = details.getString("dosow");
                    String sow_nurplotno = details.getString("sow_nurplotno");
                    String sow_noofseeds = details.getString("sow_noofseeds");
                    String sow_sptype = details.getString("sow_sptype");
                    String sow_state = details.getString("sow_state");
                    String sow_loc = details.getString("sow_loc");
                    String sow_noofcellstray = details.getString("sow_noofcellstray");
                    String sow_nooftraylot = details.getString("sow_nooftraylot");
                    String sow_bedno = details.getString("sow_bedno");
                    String sow_direction = details.getString("sow_direction");
                    String sow_noofrows = details.getString("sow_noofrows");
                    String sow_noofplants = details.getString("sow_noofplants");
                    String transp_date = details.getString("transp_date");
                    String transp_state = details.getString("transp_state");
                    String transp_loc = details.getString("transp_loc");
                    String transp_plotno = details.getString("transp_plotno");
                    String transp_bedno = details.getString("transp_bedno");
                    String transp_direction = details.getString("transp_direction");
                    String transp_noofrows = details.getString("transp_noofrows");
                    String transp_range = details.getString("transp_range");
                    massage="Success";

                    SampleGOTInfo sampleGOTInfo = new SampleGOTInfo(srdate, crop, variety, sampleno, lotno, trstage, scdate, sddate, ackflg, sowingflg, transplantflg, fieldmflg, finobrflg, resamplingflg, retestflg, dosow, sow_nurplotno, sow_noofseeds, sow_sptype, sow_state, sow_loc, sow_noofcellstray, sow_nooftraylot, sow_bedno, sow_direction, sow_noofrows, sow_noofplants, transp_date, transp_state, transp_loc, transp_plotno, transp_bedno, transp_direction, transp_noofrows, transp_range, massage, ackdate);
                    SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_SAMPLEINFO_OBJ, sampleGOTInfo);
                }

            } else {
                String returnString = jObj.getString("error_msg");
                SampleGOTInfo sampleGOTInfo = new SampleGOTInfo(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, returnString, null);
                SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_SAMPLEINFO_OBJ, sampleGOTInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sampleAckInfoParser(String response) {
        String massage;
        try {
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");
            // Check for error node in json

            if (!error) {
                //success
                JSONObject user = jObj.getJSONObject("user");
                JSONArray samplearray = user.getJSONArray("samplearray");
                for (int i = 0; i < samplearray.length(); i++) {
                    JSONObject details = samplearray.getJSONObject(i);
                    String dosdate = details.getString("dosdate");
                    String crop = details.getString("crop");
                    String variety = details.getString("variety");
                    String lotno = details.getString("lotno");
                    String sampleno = details.getString("sampleno");
                    String ackflg = details.getString("ackflg");
                    /*String srfseries = details.getString("srfseries");
                    String srfno = details.getString("srfno");*/
                    massage="Success";

                    SampleInfo sampleInfo = new SampleInfo(dosdate, crop, variety, lotno, "", "", "", "", "", sampleno, "", ackflg, massage, "", "");
                    SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_SAMPLEINFO_OBJ, sampleInfo);
                }

            } else {
                String returnString = jObj.getString("error_msg");
                SampleInfo sampleInfo = new SampleInfo(null, null, null, null, null, null, null, null, null, null, null, null, returnString, "", "");
                SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_SAMPLEINFO_OBJ, sampleInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void samplePPInfoParser(String response) {
        String massage;
        try {
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");
            // Check for error node in json

            if (!error) {
                //success
                JSONObject user = jObj.getJSONObject("user");
                JSONArray samplearray = user.getJSONArray("samplearray");
                for (int i = 0; i < samplearray.length(); i++) {
                    JSONObject details = samplearray.getJSONObject(i);
                    String srdate = details.getString("srdate");
                    String crop = details.getString("crop");
                    String variety = details.getString("variety");
                    String lotno = details.getString("lotno");
                    String trstage = details.getString("trstage");
                    String nob = details.getString("nob");
                    String qty = details.getString("qty");
                    String sloc = details.getString("sloc");
                    String qctest = details.getString("qctest");
                    String sampleno = details.getString("sampleno");
                    String spdate = details.getString("spdate");
                    String sampflg = details.getString("sampflg");
                    String sampcldate = details.getString("sampcldate");
                    String sampclrole = details.getString("sampclrole");
                    String qcp_samplewt = details.getString("qcp_samplewt");
                    String qcp_pureseed = details.getString("qcp_pureseed");
                    String qcp_pureseedper = details.getString("qcp_pureseedper");
                    String qcp_imseed = details.getString("qcp_imseed");
                    String qcp_imseedper = details.getString("qcp_imseedper");
                    String qcp_lightseed = details.getString("qcp_lightseed");
                    String qcp_lightseedper = details.getString("qcp_lightseedper");
                    String qcp_ocseedno = details.getString("qcp_ocseedno");
                    String qcp_ocseedinkg = details.getString("qcp_ocseedinkg");
                    String qcp_odvseedno = details.getString("qcp_odvseedno");
                    String qcp_odvseedinkg = details.getString("qcp_odvseedinkg");
                    String qcp_dcseed = details.getString("qcp_dcseed");
                    String qcp_dcseedper = details.getString("qcp_dcseedper");
                    String qcp_phseedno = details.getString("qcp_phseedno");
                    String qcp_phseedinkg = details.getString("qcp_phseedinkg");
                    String qcp_ppresult = details.getString("qcp_ppresult");
                    String qcp_ppdatadt = details.getString("qcp_ppdatadt");
                    String qcp_ppphoto = details.getString("qcp_ppphoto");
                    String qcp_ppdataflg = details.getString("qcp_ppdataflg");
                    String qcp_gstotnosmp = details.getString("qcp_gstotnosmp");
                    String qcp_gsnoperkg = details.getString("qcp_gsnoperkg");
                    String qcp_gswtgms = details.getString("qcp_gswtgms");
                    String qcp_gsper = details.getString("qcp_gsper");
                    String qcp_gsremark = details.getString("qcp_gsremark");
                    String qcp_ocwt = details.getString("qcp_ocwt");
                    String qcp_ocper = details.getString("qcp_ocper");
                    String qcp_ocremark = details.getString("qcp_ocremark");
                    String qcp_odvwt = details.getString("qcp_odvwt");
                    String qcp_odvper = details.getString("qcp_odvper");
                    String qcp_odv1010 = details.getString("qcp_odv1010");
                    String qcp_odvfgrain = details.getString("qcp_odvfgrain");
                    String qcp_odvbgrain = details.getString("qcp_odvbgrain");
                    String qcp_odvlonggrain = details.getString("qcp_odvlonggrain");
                    String qcp_odvothertype = details.getString("qcp_odvothertype");
                    String qcp_odvtotal = details.getString("qcp_odvtotal");
                    String qcp_odvtotalper = details.getString("qcp_odvtotalper");
                    String qcp_odvremark = details.getString("qcp_odvremark");
                    String qcp_phwt = details.getString("qcp_phwt");
                    String qcp_phper = details.getString("qcp_phper");
                    String qcp_phremark = details.getString("qcp_phremark");
                    String srfno = details.getString("srfno");
                    massage="Success";

                    SamplePPInfo samplePPInfo = new SamplePPInfo(srdate,crop,variety,lotno,trstage,nob,qty,sloc,qctest,sampleno,spdate,
                            sampflg,sampcldate,sampclrole,qcp_samplewt,qcp_pureseed,qcp_pureseedper,qcp_imseed,qcp_imseedper,
                            qcp_lightseed,qcp_lightseedper,qcp_ocseedno,qcp_ocseedinkg,qcp_odvseedno,qcp_odvseedinkg,
                            qcp_dcseed,qcp_dcseedper,qcp_phseedno,qcp_phseedinkg,qcp_ppresult,qcp_ppdatadt,qcp_ppphoto,
                            qcp_ppdataflg,massage, qcp_gstotnosmp,qcp_gsnoperkg,qcp_gswtgms,qcp_gsper,qcp_gsremark,qcp_ocwt,qcp_ocper,
                            qcp_ocremark,qcp_odvwt,qcp_odvper,qcp_odv1010,qcp_odvfgrain,qcp_odvbgrain,qcp_odvlonggrain,qcp_odvothertype,
                            qcp_odvtotal,qcp_odvtotalper,qcp_odvremark,qcp_phwt,qcp_phper,qcp_phremark,srfno);
                    SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_SAMPLEPPINFO_OBJ, samplePPInfo);
                }

            } else {
                String returnString = jObj.getString("error_msg");
                SamplePPInfo samplePPInfo = new SamplePPInfo(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,returnString,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
                SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_SAMPLEPPINFO_OBJ, samplePPInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sampleMoistureInfoParser(String response) {
        String massage;
        try {
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");
            // Check for error node in json

            if (!error) {
                //success
                JSONObject user = jObj.getJSONObject("user");
                JSONArray samplearray = user.getJSONArray("samplemoistarray");
                for (int i = 0; i < samplearray.length(); i++) {
                    JSONObject details = samplearray.getJSONObject(i);
                    String crop = details.getString("crop");
                    String variety = details.getString("variety");
                    String lotno = details.getString("lotno");
                    String trstage = details.getString("trstage");
                    String nob = details.getString("nob");
                    String qty = details.getString("qty");
                    String sampleno = details.getString("sampleno");
                    String qcm_m1rep1 = details.getString("qcm_m1rep1");
                    String qcm_m1rep2 = details.getString("qcm_m1rep2");
                    String qcm_m1rep3 = details.getString("qcm_m1rep3");
                    String qcm_m1rep4 = details.getString("qcm_m1rep4");
                    String qcm_m2rep1 = details.getString("qcm_m2rep1");
                    String qcm_m2rep2 = details.getString("qcm_m2rep2");
                    String qcm_m2rep3 = details.getString("qcm_m2rep3");
                    String qcm_m2rep4 = details.getString("qcm_m2rep4");
                    String qcm_m3rep1 = details.getString("qcm_m3rep1");
                    String qcm_m3rep2 = details.getString("qcm_m3rep2");
                    String qcm_m3rep3 = details.getString("qcm_m3rep3");
                    String qcm_m3rep4 = details.getString("qcm_m3rep4");
                    String qcm_rep1moistper = details.getString("qcm_rep1moistper");
                    String qcm_rep2moistper = details.getString("qcm_rep2moistper");
                    String qcm_rep3moistper = details.getString("qcm_rep3moistper");
                    String qcm_rep4moistper = details.getString("qcm_rep4moistper");
                    String qcm_haomflg = details.getString("qcm_haomflg");
                    String qcm_mmrep1 = details.getString("qcm_mmrep1");
                    String qcm_mmrep2 = details.getString("qcm_mmrep2");
                    String qcm_mmrep3 = details.getString("qcm_mmrep3");
                    String qcm_mmrmoistper = details.getString("qcm_mmrmoistper");
                    String qcm_mmrflg = details.getString("qcm_mmrflg");
                    String qcm_moistflg = details.getString("qcm_moistflg");
                    String srfno = details.getString("srfno");
                    massage="Success";

                    SampleMoistureInfo sampleMoistureInfo = new SampleMoistureInfo(crop, variety, lotno, trstage, nob, qty, sampleno, qcm_m1rep1, qcm_m1rep2, qcm_m1rep3, qcm_m1rep4, qcm_m2rep1, qcm_m2rep2, qcm_m2rep3, qcm_m2rep4, qcm_m3rep1, qcm_m3rep2, qcm_m3rep3, qcm_m3rep4, qcm_rep1moistper, qcm_rep2moistper, qcm_rep3moistper, qcm_rep4moistper, qcm_haomflg, qcm_mmrep1, qcm_mmrep2, qcm_mmrep3, qcm_mmrmoistper, qcm_mmrflg, qcm_moistflg, massage, srfno);
                    SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_SAMPLEMOISTUREINFO_OBJ, sampleMoistureInfo);
                }

            } else {
                String returnString = jObj.getString("error_msg");
                SampleMoistureInfo sampleMoistureInfo = new SampleMoistureInfo(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, returnString,"");
                SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_SAMPLEMOISTUREINFO_OBJ, sampleMoistureInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sampleGerminationInfoParser(String response) {
        String massage;
        try {
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");
            // Check for error node in json

            if (!error) {
                //success
                JSONObject user = jObj.getJSONObject("user");
                JSONArray samplearray = user.getJSONArray("samplegemparray");
                for (int i = 0; i < samplearray.length(); i++) {
                    JSONObject details = samplearray.getJSONObject(i);
                    String crop = details.getString("crop");
                    String variety = details.getString("variety");
                    String lotno = details.getString("lotno");
                    String trstage = details.getString("trstage");
                    String nob = details.getString("nob");
                    String qty = details.getString("qty");
                    String sampleno = details.getString("sampleno");
                    String seedsize = details.getString("seedsize");
                    String noofseedinonerep = details.getString("noofseedinonerep");
                    String noofseedinonerepfgt = details.getString("noofseedinonerepfgt");
                    String qcg_testtype = details.getString("qcg_testtype");
                    String qcg_setupflg = details.getString("qcg_setupflg");
                    String qcg_sgtoneobflg = details.getString("qcg_sgtoneobflg");
                    String qcg_sgtnoofrep = details.getString("qcg_sgtnoofrep");
                    String qcg_sgtoobnormal1 = details.getString("qcg_sgtoobnormal1");
                    String qcg_sgtoobnormal2 = details.getString("qcg_sgtoobnormal2");
                    String qcg_sgtoobnormal3 = details.getString("qcg_sgtoobnormal3");
                    String qcg_sgtoobnormal4 = details.getString("qcg_sgtoobnormal4");
                    String qcg_sgtoobnormal5 = details.getString("qcg_sgtoobnormal5");
                    String qcg_sgtoobnormal6 = details.getString("qcg_sgtoobnormal6");
                    String qcg_sgtoobnormal7 = details.getString("qcg_sgtoobnormal7");
                    String qcg_sgtoobnormal8 = details.getString("qcg_sgtoobnormal8");
                    String qcg_sgtoobnormalavg = details.getString("qcg_sgtoobnormalavg");
                    String qcg_sgtoobnormaldt = details.getString("qcg_sgtoobnormaldt");
                    String qcg_sgtnormal1 = details.getString("qcg_sgtnormal1");
                    String qcg_sgtnormal2 = details.getString("qcg_sgtnormal2");
                    String qcg_sgtnormal3 = details.getString("qcg_sgtnormal3");
                    String qcg_sgtnormal4 = details.getString("qcg_sgtnormal4");
                    String qcg_sgtnormal5 = details.getString("qcg_sgtnormal5");
                    String qcg_sgtnormal6 = details.getString("qcg_sgtnormal6");
                    String qcg_sgtnormal7 = details.getString("qcg_sgtnormal7");
                    String qcg_sgtnormal8 = details.getString("qcg_sgtnormal8");
                    String qcg_sgtnormalavg = details.getString("qcg_sgtnormalavg");
                    String qcg_sgtabnormal1 = details.getString("qcg_sgtabnormal1");
                    String qcg_sgtabnormal2 = details.getString("qcg_sgtabnormal2");
                    String qcg_sgtabnormal3 = details.getString("qcg_sgtabnormal3");
                    String qcg_sgtabnormal4 = details.getString("qcg_sgtabnormal4");
                    String qcg_sgtabnormal5 = details.getString("qcg_sgtabnormal5");
                    String qcg_sgtabnormal6 = details.getString("qcg_sgtabnormal6");
                    String qcg_sgtabnormal7 = details.getString("qcg_sgtabnormal7");
                    String qcg_sgtabnormal8 = details.getString("qcg_sgtabnormal8");
                    String qcg_sgtabnormalavg = details.getString("qcg_sgtabnormalavg");
                    String qcg_sgthardfug1 = details.getString("qcg_sgthardfug1");
                    String qcg_sgthardfug2 = details.getString("qcg_sgthardfug2");
                    String qcg_sgthardfug3 = details.getString("qcg_sgthardfug3");
                    String qcg_sgthardfug4 = details.getString("qcg_sgthardfug4");
                    String qcg_sgthardfug5 = details.getString("qcg_sgthardfug5");
                    String qcg_sgthardfug6 = details.getString("qcg_sgthardfug6");
                    String qcg_sgthardfug7 = details.getString("qcg_sgthardfug7");
                    String qcg_sgthardfug8 = details.getString("qcg_sgthardfug8");
                    String qcg_sgthardfugavg = details.getString("qcg_sgthardfugavg");
                    String qcg_sgtdead1 = details.getString("qcg_sgtdead1");
                    String qcg_sgtdead2 = details.getString("qcg_sgtdead2");
                    String qcg_sgtdead3 = details.getString("qcg_sgtdead3");
                    String qcg_sgtdead4 = details.getString("qcg_sgtdead4");
                    String qcg_sgtdead5 = details.getString("qcg_sgtdead5");
                    String qcg_sgtdead6 = details.getString("qcg_sgtdead6");
                    String qcg_sgtdead7 = details.getString("qcg_sgtdead7");
                    String qcg_sgtdead8 = details.getString("qcg_sgtdead8");
                    String qcg_sgtdeadavg = details.getString("qcg_sgtdeadavg");
                    String qcg_sgtdt = details.getString("qcg_sgtdt");
                    String qcg_sgtvremark = details.getString("qcg_sgtvremark");
                    String qcg_vigtesttype = details.getString("qcg_vigtesttype");
                    String qcg_vigoneobflg = details.getString("qcg_vigoneobflg");
                    String qcg_vignoofrep = details.getString("qcg_vignoofrep");
                    String qcg_vigoobnormal1 = details.getString("qcg_vigoobnormal1");
                    String qcg_vigoobnormal2 = details.getString("qcg_vigoobnormal2");
                    String qcg_vigoobnormal3 = details.getString("qcg_vigoobnormal3");
                    String qcg_vigoobnormal4 = details.getString("qcg_vigoobnormal4");
                    String qcg_vigoobnormal5 = details.getString("qcg_vigoobnormal5");
                    String qcg_vigoobnormal6 = details.getString("qcg_vigoobnormal6");
                    String qcg_vigoobnormal7 = details.getString("qcg_vigoobnormal7");
                    String qcg_vigoobnormal8 = details.getString("qcg_vigoobnormal8");
                    String qcg_vigoobnormalavg = details.getString("qcg_vigoobnormalavg");
                    String qcg_vigoobnormaldt = details.getString("qcg_vigoobnormaldt");
                    String qcg_vignormal1 = details.getString("qcg_vignormal1");
                    String qcg_vignormal2 = details.getString("qcg_vignormal2");
                    String qcg_vignormal3 = details.getString("qcg_vignormal3");
                    String qcg_vignormal4 = details.getString("qcg_vignormal4");
                    String qcg_vignormal5 = details.getString("qcg_vignormal5");
                    String qcg_vignormal6 = details.getString("qcg_vignormal6");
                    String qcg_vignormal7 = details.getString("qcg_vignormal7");
                    String qcg_vignormal8 = details.getString("qcg_vignormal8");
                    String qcg_vignormalavg = details.getString("qcg_vignormalavg");
                    String qcg_vigabnormal1 = details.getString("qcg_vigabnormal1");
                    String qcg_vigabnormal2 = details.getString("qcg_vigabnormal2");
                    String qcg_vigabnormal3 = details.getString("qcg_vigabnormal3");
                    String qcg_vigabnormal4 = details.getString("qcg_vigabnormal4");
                    String qcg_vigabnormal5 = details.getString("qcg_vigabnormal5");
                    String qcg_vigabnormal6 = details.getString("qcg_vigabnormal6");
                    String qcg_vigabnormal7 = details.getString("qcg_vigabnormal7");
                    String qcg_vigabnormal8 = details.getString("qcg_vigabnormal8");
                    String qcg_vigabnormalavg = details.getString("qcg_vigabnormalavg");
                    String qcg_vigdead1 = details.getString("qcg_vigdead1");
                    String qcg_vigdead2 = details.getString("qcg_vigdead2");
                    String qcg_vigdead3 = details.getString("qcg_vigdead3");
                    String qcg_vigdead4 = details.getString("qcg_vigdead4");
                    String qcg_vigdead5 = details.getString("qcg_vigdead5");
                    String qcg_vigdead6 = details.getString("qcg_vigdead6");
                    String qcg_vigdead7 = details.getString("qcg_vigdead7");
                    String qcg_vigdead8 = details.getString("qcg_vigdead8");
                    String qcg_vigdeadavg = details.getString("qcg_vigdeadavg");
                    String qcg_viglogid = details.getString("qcg_viglogid");
                    String qcg_vigdt = details.getString("qcg_vigdt");
                    String qcg_viggermp = details.getString("qcg_viggermp");
                    String qcg_vigflg = details.getString("qcg_vigflg");
                    String qcg_vigvremark = details.getString("qcg_vigvremark");
                    String qcg_oprremark = details.getString("qcg_oprremark");
                    String qcg_sgtflg = details.getString("qcg_sgtflg");

                    massage="Success";

                    SampleGerminationInfo sampleGerminationInfo = new SampleGerminationInfo(crop,variety,lotno,trstage,nob,qty,sampleno,seedsize,noofseedinonerep,qcg_testtype,qcg_setupflg,qcg_sgtoneobflg,qcg_sgtnoofrep,qcg_sgtoobnormal1,qcg_sgtoobnormal2,qcg_sgtoobnormal3,qcg_sgtoobnormal4,qcg_sgtoobnormal5,
                            qcg_sgtoobnormal6,qcg_sgtoobnormal7,qcg_sgtoobnormal8,qcg_sgtoobnormalavg,qcg_sgtoobnormaldt,qcg_sgtnormal1,qcg_sgtnormal2,qcg_sgtnormal3,qcg_sgtnormal4,qcg_sgtnormal5,qcg_sgtnormal6,qcg_sgtnormal7,qcg_sgtnormal8,qcg_sgtnormalavg,qcg_sgtabnormal1,qcg_sgtabnormal2,qcg_sgtabnormal3,
                            qcg_sgtabnormal4,qcg_sgtabnormal5,qcg_sgtabnormal6,qcg_sgtabnormal7,qcg_sgtabnormal8,qcg_sgtabnormalavg,qcg_sgthardfug1,qcg_sgthardfug2,qcg_sgthardfug3,qcg_sgthardfug4,qcg_sgthardfug5,qcg_sgthardfug6,qcg_sgthardfug7,qcg_sgthardfug8,qcg_sgthardfugavg,qcg_sgtdead1,qcg_sgtdead2,qcg_sgtdead3,
                            qcg_sgtdead4,qcg_sgtdead5,qcg_sgtdead6,qcg_sgtdead7,qcg_sgtdead8,qcg_sgtdeadavg,qcg_sgtdt,qcg_vigtesttype,qcg_vigoneobflg,qcg_vignoofrep,qcg_vigoobnormal1,qcg_vigoobnormal2,qcg_vigoobnormal3,qcg_vigoobnormal4,qcg_vigoobnormal5,qcg_vigoobnormal6,qcg_vigoobnormal7,qcg_vigoobnormal8,
                            qcg_vigoobnormalavg,qcg_vigoobnormaldt,qcg_vignormal1,qcg_vignormal2,qcg_vignormal3,qcg_vignormal4,qcg_vignormal5,qcg_vignormal6,qcg_vignormal7,qcg_vignormal8,qcg_vignormalavg,qcg_vigabnormal1,qcg_vigabnormal2,qcg_vigabnormal3,qcg_vigabnormal4,qcg_vigabnormal5,qcg_vigabnormal6,
                            qcg_vigabnormal7,qcg_vigabnormal8,qcg_vigabnormalavg,qcg_vigdead1,qcg_vigdead2,qcg_vigdead3,qcg_vigdead4,qcg_vigdead5,qcg_vigdead6,qcg_vigdead7,qcg_vigdead8,qcg_vigdeadavg,qcg_viglogid,qcg_vigdt,qcg_viggermp,qcg_vigflg,qcg_vigvremark,qcg_oprremark, massage, qcg_sgtflg, noofseedinonerepfgt, qcg_sgtvremark);
                    SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_SAMPLEGERMINATIONINFO_OBJ, sampleGerminationInfo);
                }

            } else {
                String returnString = jObj.getString("error_msg");
                SampleGerminationInfo sampleGerminationInfo = new SampleGerminationInfo(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
                        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
                        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
                        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
                        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
                        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null, returnString, null, null, null);
                SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_SAMPLEGERMINATIONINFO_OBJ, sampleGerminationInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> parseStateList(Context context, String response) {
        ArrayList<String> stateArrayList = new ArrayList<>();
        String message="";
        //Log.v("JSON", response);
        try {
            //Log.v("JSON", response);
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");
            // Check for error node in json
            if (!error) {
                //success
                JSONObject user = jObj.getJSONObject("user");
                JSONArray statearray = user.getJSONArray("statearray");
                message = jObj.getString("msg");
                stateArrayList.add(0, "Select");
                for (int i = 0; i < statearray.length(); i++) {
                    stateArrayList.add(statearray.getString(i));
                }

            } else {
                // Error in login. Get the error message
                String errorMsg = jObj.getString("error_msg");
                message = errorMsg;
                //Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
        }

        SharedPreferences.getInstance().createString(message, SharedPreferences.KEY_MESSAGE);
        return stateArrayList;
    }

    public ArrayList<String> parseLocList(Context context, String response) {
        ArrayList<String> stateArrayList = new ArrayList<>();
        String message="";
        //Log.v("JSON", response);
        try {
            //Log.v("JSON", response);
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");
            // Check for error node in json
            if (!error) {
                //success
                JSONObject user = jObj.getJSONObject("user");
                JSONArray statearray = user.getJSONArray("locationarray");
                message = jObj.getString("msg");
                stateArrayList.add(0, "Select");
                for (int i = 0; i < statearray.length(); i++) {
                    stateArrayList.add(statearray.getString(i));
                }

            } else {
                // Error in login. Get the error message
                String errorMsg = jObj.getString("error_msg");
                message = errorMsg;
                //Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
        }

        SharedPreferences.getInstance().createString(message, SharedPreferences.KEY_MESSAGE);
        return stateArrayList;
    }

    public String parseSubmitData(String response) {
        String message="";
        try {
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");
            // Check for error node in json
            if (!error) {
                message = jObj.getString("msg");
            }else {
                message = jObj.getString("error_msg");
            }
        } catch (JSONException e) {
            message="Json error: " + e.getMessage();
            e.printStackTrace();
        }
        return message;
    }

    public String parseSubmitGermpData(String response) {
        String message="";
        try {
            JSONObject jObj = new JSONObject(response);
            boolean status = jObj.getBoolean("status");
            // Check for error node in json
            if (status) {
                message = jObj.getString("msg");
            }else {
                message = jObj.getString("msg");
            }
        } catch (JSONException e) {
            message="Json error: " + e.getMessage();
            e.printStackTrace();
        }
        return message;
    }

    public String parseNewSRF(String response) {
        String message="";
        try {
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");
            // Check for error node in json

            if (!error) {
                //success
                JSONObject user = jObj.getJSONObject("user");
                //JSONArray samplearray = user.getJSONArray("samplearray");
                JSONObject samplearray = user.getJSONObject("samplearray");
                message = samplearray.getString("srfno");
                /*for (int i = 0; i < samplearray.length(); i++) {
                    JSONObject details = samplearray.getJSONObject(i);
                    message = details.getString("srfno");
                }*/
            } else {
                String returnString = jObj.getString("error_msg");
                message=returnString;
                SampleInfo sampleInfo = new SampleInfo(null, null, null, null, null, null, null, null, null, null, null, null, returnString, "", "");
                SharedPreferences.getInstance().storeObject(SharedPreferences.KEY_SAMPLEINFO_OBJ, sampleInfo);
            }
        } catch (JSONException e) {
            message="Json error: " + e.getMessage();
            e.printStackTrace();
        }

        return message;
    }

    public String parseSubmitAckData(String response) {
        String message="";
        String msg="";
        try {
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");
            // Check for error node in json
            if (!error) {
                message="Success";
                msg = jObj.getString("msg");
            }else {
                msg = jObj.getString("error_msg");
                message=msg;
            }
        } catch (JSONException e) {
            msg="Json error: " + e.getMessage();
            message=msg;
            e.printStackTrace();
        }
        SharedPreferences.getInstance().createString(message, SharedPreferences.KEY_MESSAGE);
        return msg;
    }
}
