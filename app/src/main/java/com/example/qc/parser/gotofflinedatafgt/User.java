package com.example.qc.parser.gotofflinedatafgt;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("crop")
    @Expose
    private String crop;
    @SerializedName("variety")
    @Expose
    private String variety;
    @SerializedName("lotno")
    @Expose
    private String lotno;
    @SerializedName("trstage")
    @Expose
    private String trstage;
    @SerializedName("sampleno")
    @Expose
    private String sampleno;
    @SerializedName("qcg_testtype")
    @Expose
    private String qcgTesttype;
    @SerializedName("qcg_setupdt")
    @Expose
    private String qcgSetupdt;
    @SerializedName("seedsize")
    @Expose
    private String seedsize;
    @SerializedName("qcg_vigtesttype")
    @Expose
    private String qcgVigtesttype;
    @SerializedName("qcg_noofseedinonerepfgt")
    @Expose
    private Integer qcgNoofseedinonerepfgt;
    @SerializedName("qcg_vignoofrep")
    @Expose
    private Integer qcgVignoofrep;

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getLotno() {
        return lotno;
    }

    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    public String getTrstage() {
        return trstage;
    }

    public void setTrstage(String trstage) {
        this.trstage = trstage;
    }

    public String getSampleno() {
        return sampleno;
    }

    public void setSampleno(String sampleno) {
        this.sampleno = sampleno;
    }

    public String getQcgTesttype() {
        return qcgTesttype;
    }

    public void setQcgTesttype(String qcgTesttype) {
        this.qcgTesttype = qcgTesttype;
    }

    public String getQcgSetupdt() {
        return qcgSetupdt;
    }

    public void setQcgSetupdt(String qcgSetupdt) {
        this.qcgSetupdt = qcgSetupdt;
    }

    public String getSeedsize() {
        return seedsize;
    }

    public void setSeedsize(String seedsize) {
        this.seedsize = seedsize;
    }

    public String getQcgVigtesttype() {
        return qcgVigtesttype;
    }

    public void setQcgVigtesttype(String qcgVigtesttype) {
        this.qcgVigtesttype = qcgVigtesttype;
    }

    public Integer getQcgNoofseedinonerepfgt() {
        return qcgNoofseedinonerepfgt;
    }

    public void setQcgNoofseedinonerepfgt(Integer qcgNoofseedinonerepfgt) {
        this.qcgNoofseedinonerepfgt = qcgNoofseedinonerepfgt;
    }

    public Integer getQcgVignoofrep() {
        return qcgVignoofrep;
    }

    public void setQcgVignoofrep(Integer qcgVignoofrep) {
        this.qcgVignoofrep = qcgVignoofrep;
    }
}
