package com.example.qc.retrofit;

import com.example.qc.parser.PendingReportResponse;
import com.example.qc.parser.SubmitSuccessResponse;
import com.example.qc.parser.germpsampleinfo.GermpSampleInfoResponse;
import com.example.qc.parser.gotofflinedatafgt.FGTOfflineDataResponse;
import com.example.qc.parser.gotpendinglist.GOTPendingList;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("gotreportdetailed.php")
    Call<PendingReportResponse> getReportDetails(
            @Query("mobile1") String mobile1, @Query("reptype") String reptype,
            @Query("crop") String crop, @Query("variety") String variety);

    @GET("samplegempinfo.php")
    Call<GermpSampleInfoResponse> getGermpSampleInfo(
            @Query("mobile1") String mobile1, @Query("sampleno") String reptype);

    @Multipart
    @POST("sampleppupdate.php")
    Call<SubmitSuccessResponse> submitPPData(
            @Part("mobile1") RequestBody mobile1, @Part("scode") RequestBody scode,
            @Part("sampleno") RequestBody sampleno, @Part("samplewt") RequestBody samplewt,
            @Part("pureseed") RequestBody pureseed, @Part("pureseedper") RequestBody pureseedper,
            @Part("imseed") RequestBody imseed, @Part("imseedper") RequestBody imseedper,
            @Part("lightseed") RequestBody lightseed, @Part("lightseedper") RequestBody lightseedper,
            @Part("ocseedno") RequestBody ocseedno, @Part("ocseedinkg") RequestBody ocseedinkg,
            @Part("odvseedno") RequestBody odvseedno, @Part("odvseedinkg") RequestBody odvseedinkg,
            @Part("dcseed") RequestBody dcseed, @Part("dcseedper") RequestBody dcseedper,
            @Part("phseedno") RequestBody phseedno, @Part("phseedinkg") RequestBody phseedinkg,
            @Part("pinhole_remarks") RequestBody pinhole_remarks, @Part("pinhole_wt") RequestBody pinhole_wt,
            @Part("pinhole_wt_per") RequestBody pinhole_wt_per, @Part("germp_remarks") RequestBody germp_remarks,
            @Part("germp_wt") RequestBody germp_wt, @Part("germp_wt_per") RequestBody germp_wt_per,
            @Part("totalnoinsample_germp") RequestBody totalnoinsample_germp, @Part("tobeexnoperkg_germp") RequestBody tobeexnoperkg_germp,
            @Part("ocs_remarks") RequestBody ocs_remarks, @Part("ocs_wt") RequestBody ocs_wt,
            @Part("ocs_wt_per") RequestBody ocs_wt_per, @Part("odv_wt") RequestBody odv_wt,
            @Part("odv_wt_per") RequestBody odv_wt_per, @Part("odv_remarks") RequestBody odv_remarks,
            @Part("odv_1010") RequestBody odv_1010, @Part("odv_fineGrain") RequestBody odv_fineGrain,
            @Part("odv_boldGrain") RequestBody odv_boldGrain, @Part("odv_longGrain") RequestBody odv_longGrain,
            @Part("odv_otherTyp") RequestBody odv_otherTyp, @Part("odv_total") RequestBody odv_total,
            @Part("odv_total_per") RequestBody odv_total_per, @Part List<MultipartBody.Part> list);

    @Multipart
    @POST("samplemoistfinsub.php")
    Call<SubmitSuccessResponse> submitMoistureData(
            @Part("mobile1") RequestBody mobile1, @Part("scode") RequestBody scode,
            @Part("sampleno") RequestBody sampleno, @Part("type") RequestBody type,
            @Part("remarks") RequestBody remarks, @Part List<MultipartBody.Part> list);


    @Multipart
    @POST("sampleppupdate.php")
    Call<SubmitSuccessResponse> submitMoistureData1(
            @Part("mobile1") RequestBody mobile1, @Part("scode") RequestBody scode,
            @Part("sampleno") RequestBody sampleno, @Part("hmtype") RequestBody hmtype,
            @Part("m1rep1") RequestBody m1rep1, @Part("m1rep2") RequestBody m1rep2,
            @Part("m1rep3") RequestBody m1rep3, @Part("m1rep4") RequestBody m1rep4,
            @Part("m2rep1") RequestBody m2rep1, @Part("m2rep2") RequestBody m2rep2,
            @Part("m2rep3") RequestBody m2rep3, @Part("m2rep4") RequestBody m2rep4,
            @Part("m3rep1") RequestBody m3rep1, @Part("m3rep2") RequestBody m3rep2,
            @Part("m3rep3") RequestBody m3rep3, @Part("m3rep4") RequestBody m3rep4,
            @Part("rep1moistper") RequestBody rep1moistper, @Part("rep2moistper") RequestBody rep2moistper,
            @Part("rep3moistper") RequestBody rep3moistper, @Part("rep4moistper") RequestBody rep4moistper,
            @Part("haommoistper") RequestBody haommoistper, @Part("mmrep1") RequestBody mmrep1,
            @Part("mmrep2") RequestBody mmrep2, @Part("mmrep3") RequestBody mmrep3,
            @Part("mmrmoistper") RequestBody mmrmoistper, @Part List<MultipartBody.Part> list);

    @Multipart
    @POST("gempsubmit.php")
    Call<SubmitSuccessResponse> submitFGTFinalCount(
            @Part("mobile1") RequestBody mobile1, @Part("scode") RequestBody scode,
            @Part("sampleno") RequestBody sampleno, @Part("noofrepsgt") RequestBody noofrepsgt,
            @Part("testtype") RequestBody testtype, @Part("fgtfcfltype") RequestBody fgtfcfltype,
            @Part("fgtmtype") RequestBody fgtmtype, @Part("fgtnormal1") RequestBody fgtnormal1,
            @Part("fgtnormal2") RequestBody fgtnormal2, @Part("fgtnormal3") RequestBody fgtnormal3,
            @Part("fgtnormal4") RequestBody fgtnormal4, @Part("fgtnormal5") RequestBody fgtnormal5,
            @Part("fgtnormal6") RequestBody fgtnormal6, @Part("fgtnormal7") RequestBody fgtnormal7,
            @Part("fgtnormal8") RequestBody fgtnormal8, @Part("fgtnormalavg") RequestBody fgtnormalavg,
            @Part("fgtabnormal1") RequestBody fgtabnormal1, @Part("fgtabnormal2") RequestBody fgtabnormal2,
            @Part("fgtabnormal3") RequestBody fgtabnormal3, @Part("fgtabnormal4") RequestBody fgtabnormal4,
            @Part("fgtabnormal5") RequestBody fgtabnormal5, @Part("fgtabnormal6") RequestBody fgtabnormal6,
            @Part("fgtabnormal7") RequestBody fgtabnormal7, @Part("fgtabnormal8") RequestBody fgtabnormal8,
            @Part("fgtabnormalavg") RequestBody fgtabnormalavg, @Part("fgtdead1") RequestBody fgtdead1,
            @Part("fgtdead2") RequestBody fgtdead2, @Part("fgtdead3") RequestBody fgtdead3,
            @Part("fgtdead4") RequestBody fgtdead4, @Part("fgtdead5") RequestBody fgtdead5,
            @Part("fgtdead6") RequestBody fgtdead6, @Part("fgtdead7") RequestBody fgtdead7,
            @Part("fgtdead8") RequestBody fgtdead8, @Part("fgtdeadavg") RequestBody fgtdeadavg,
            @Part("fgtdt") RequestBody fgtdt, @Part("fgtvremark") RequestBody fgtvremark,
            @Part("oprremark") RequestBody oprremark, @Part("retest_reason") RequestBody retest_reason,
            @Part("retest") RequestBody retest, @Part("retesttype") RequestBody retesttype,
            @Part("remarks") RequestBody remarks, @Part List<MultipartBody.Part> list);

    @Multipart
    @POST("gempsubmit.php")
    Call<SubmitSuccessResponse> submitFGTFC(
            @Part("mobile1") RequestBody mobile1, @Part("scode") RequestBody scode,
            @Part("sampleno") RequestBody sampleno, @Part("noofrepsgt") RequestBody noofrepsgt,
            @Part("testtype") RequestBody testtype, @Part("fgtfcfltype") RequestBody fgtfcfltype,
            @Part("fgtmtype") RequestBody fgtmtype, @Part("fgtoobnormal1") RequestBody fgtnormal1,
            @Part("fgtoobnormal2") RequestBody fgtnormal2, @Part("fgtoobnormal3") RequestBody fgtnormal3,
            @Part("fgtoobnormal4") RequestBody fgtnormal4, @Part("fgtoobnormal5") RequestBody fgtnormal5,
            @Part("fgtoobnormal6") RequestBody fgtnormal6, @Part("fgtoobnormal7") RequestBody fgtnormal7,
            @Part("fgtoobnormal8") RequestBody fgtnormal8, @Part("fgtoobnormalavg") RequestBody fgtnormalavg,
            @Part("fgtoobnormaldt") RequestBody fgtabnormal1, @Part("retest_reason") RequestBody fgtabnormal2,
            @Part("retest") RequestBody fgtabnormal3, @Part("retesttype") RequestBody fgtabnormal4,
            @Part("remarks") RequestBody fgtabnormal5, @Part List<MultipartBody.Part> list);

    @Multipart
    @POST("gempsubmit.php")
    Call<SubmitSuccessResponse> submitSGTFC(
            @Part("mobile1") RequestBody mobile1, @Part("scode") RequestBody scode,
            @Part("sampleno") RequestBody sampleno, @Part("noofrepsgt") RequestBody noofrepsgt,
            @Part("testtype") RequestBody testtype, @Part("sgtfcfltype") RequestBody fgtfcfltype,
            @Part("sgtoobnormal1") RequestBody fgtmtype, @Part("sgtoobnormal2") RequestBody fgtnormal1,
            @Part("sgtoobnormal3") RequestBody fgtnormal2, @Part("sgtoobnormal4") RequestBody fgtnormal3,
            @Part("sgtoobnormal5") RequestBody fgtnormal4, @Part("sgtoobnormal6") RequestBody fgtnormal5,
            @Part("sgtoobnormal7") RequestBody fgtnormal6, @Part("sgtoobnormal8") RequestBody fgtnormal7,
            @Part("sgtoobnormalavg") RequestBody fgtnormal8, @Part("sgtoobnormaldt") RequestBody fgtnormalavg,
            @Part("retest_reason") RequestBody fgtabnormal1, @Part("retest") RequestBody fgtabnormal2,
            @Part("retesttype") RequestBody fgtabnormal3, @Part("remarks") RequestBody fgtabnormal4,
            @Part List<MultipartBody.Part> list);

    @Multipart
    @POST("gempimgupd.php")
    Call<SubmitSuccessResponse> submitImage(
            @Part("mobile1") RequestBody mobile1, @Part("scode") RequestBody scode,
            @Part("sampleno") RequestBody sampleno, @Part("testtype") RequestBody noofrepsgt,
            @Part("observation") RequestBody testtype, @Part List<MultipartBody.Part> list);


    @Multipart
    @POST("gempsubmit.php")
    Call<SubmitSuccessResponse> submitSGTFinalCount(
            @Part("mobile1") RequestBody mobile1, @Part("scode") RequestBody scode,
            @Part("sampleno") RequestBody sampleno, @Part("noofrepsgt") RequestBody noofrepsgt,
            @Part("testtype") RequestBody testtype, @Part("sgtfcfltype") RequestBody sgtfcfltype,
            @Part("sgtnormal1") RequestBody sgtnormal1, @Part("sgtnormal2") RequestBody sgtnormal2,
            @Part("sgtnormal3") RequestBody sgtnormal3, @Part("sgtnormal4") RequestBody sgtnormal4,
            @Part("sgtnormal5") RequestBody sgtnormal5, @Part("sgtnormal6") RequestBody sgtnormal6,
            @Part("sgtnormal7") RequestBody sgtnormal7, @Part("sgtnormal8") RequestBody sgtnormal8,
            @Part("sgtnormalavg") RequestBody sgtnormalavg, @Part("sgtabnormal1") RequestBody sgtabnormal1,
            @Part("sgtabnormal2") RequestBody sgtabnormal2, @Part("sgtabnormal3") RequestBody sgtabnormal3,
            @Part("sgtabnormal4") RequestBody sgtabnormal4, @Part("sgtabnormal5") RequestBody sgtabnormal5,
            @Part("sgtabnormal6") RequestBody sgtabnormal6, @Part("sgtabnormal7") RequestBody sgtabnormal7,
            @Part("sgtabnormal8") RequestBody sgtabnormal8, @Part("sgtabnormalavg") RequestBody sgtabnormalavg,
            @Part("sgthardfug1") RequestBody sgthardfug1, @Part("sgthardfug2") RequestBody sgthardfug2,
            @Part("sgthardfug3") RequestBody sgthardfug3, @Part("sgthardfug4") RequestBody sgthardfug4,
            @Part("sgthardfug5") RequestBody sgthardfug5, @Part("sgthardfug6") RequestBody sgthardfug6,
            @Part("sgthardfug7") RequestBody sgthardfug7, @Part("sgthardfug8") RequestBody sgthardfug8,
            @Part("sgthardfugavg") RequestBody sgthardfugavg, @Part("sgtdead1") RequestBody sgtdead1,
            @Part("sgtdead2") RequestBody sgtdead2, @Part("sgtdead3") RequestBody sgtdead3,
            @Part("sgtdead4") RequestBody sgtdead4, @Part("sgtdead5") RequestBody sgtdead5,
            @Part("sgtdead6") RequestBody sgtdead6, @Part("sgtdead7") RequestBody sgtdead7,
            @Part("sgtdead8") RequestBody sgtdead8, @Part("sgtdeadavg") RequestBody sgtdeadavg,
            @Part("sgtdt") RequestBody sgtdt, @Part("sgtvremark") RequestBody sgtvremark,
            @Part("oprremark") RequestBody oprremark, @Part("retest_reason") RequestBody retest_reason,
            @Part("retest") RequestBody retest, @Part("retesttype") RequestBody retesttype,
            @Part("remarks") RequestBody remarks, @Part List<MultipartBody.Part> list);

    @GET("gempsetupsync.php")
    Call<FGTOfflineDataResponse> getFGTOfflineData(
            @Query("mobile1") String mobile1, @Query("scode") String reptype);


    @Multipart
    @POST("gempdatasync.php")
    Call<SubmitSuccessResponse> syncFGTDataToServer(
            @Part("mobile1") String mobile1, @Part("scode") String scode,
            @Part("germpdata") String fgtJsonData);

    @POST("gotfinsubmit.php")
    Call<SubmitSuccessResponse> updateGOTReviewData(
            @Query("mobile1") String mobile1, @Query("sampleno") String sampleno);

    @GET("gotsamplelist.php")
    Call<GOTPendingList> getGOTPendingList(
            @Query("mobile1") String mobile1, @Query("statename") String statename,
            @Query("location") String location);


}
