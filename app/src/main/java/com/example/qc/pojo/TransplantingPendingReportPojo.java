package com.example.qc.pojo;

public class TransplantingPendingReportPojo {

    private int srno;
    private String doa;
    private String crop;
    private String spCode;
    private String variety;
    private String lotno;
    private String sample;
    private String dos;
    private String NL;
    private String bedNo;
    private String treyNo;
    private String noofTrey;

    public TransplantingPendingReportPojo(int srno, String doa, String crop, String spCode, String variety, String lotno, String sample, String dos, String NL, String bedNo, String treyNo, String noofTrey) {
        this.srno = srno;
        this.doa = doa;
        this.crop = crop;
        this.spCode = spCode;
        this.variety = variety;
        this.lotno = lotno;
        this.sample = sample;
        this.dos = dos;
        this.NL = NL;
        this.bedNo = bedNo;
        this.treyNo = treyNo;
        this.noofTrey = noofTrey;
    }

    public int getSrno() {
        return srno;
    }

    public void setSrno(int srno) {
        this.srno = srno;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getSpCode() {
        return spCode;
    }

    public void setSpCode(String spCode) {
        this.spCode = spCode;
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

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getDos() {
        return dos;
    }

    public void setDos(String dos) {
        this.dos = dos;
    }

    public String getNL() {
        return NL;
    }

    public void setNL(String NL) {
        this.NL = NL;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getTreyNo() {
        return treyNo;
    }

    public void setTreyNo(String treyNo) {
        this.treyNo = treyNo;
    }

    public String getNoofTrey() {
        return noofTrey;
    }

    public void setNoofTrey(String noofTrey) {
        this.noofTrey = noofTrey;
    }
}
