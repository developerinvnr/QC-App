package com.example.qc.pojo;

public class BioTechTestInitiativeReportPojo {

    int srno;
    String crop;
    String spCode;
    String variety;
    String lotno;
    String sampleNo;
    String ad;
    String dos;
    String trd;

    public BioTechTestInitiativeReportPojo(int srno, String crop, String spCode, String variety, String lotno, String sampleNo, String ad, String dos, String trd) {
        this.srno = srno;
        this.crop = crop;
        this.spCode = spCode;
        this.variety = variety;
        this.lotno = lotno;
        this.sampleNo = sampleNo;
        this.ad = ad;
        this.dos = dos;
        this.trd = trd;
    }

    public int getSrno() {
        return srno;
    }

    public void setSrno(int srno) {
        this.srno = srno;
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

    public String getSampleNo() {
        return sampleNo;
    }

    public void setSampleNo(String sampleNo) {
        this.sampleNo = sampleNo;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getDos() {
        return dos;
    }

    public void setDos(String dos) {
        this.dos = dos;
    }

    public String getTrd() {
        return trd;
    }

    public void setTrd(String trd) {
        this.trd = trd;
    }
}
