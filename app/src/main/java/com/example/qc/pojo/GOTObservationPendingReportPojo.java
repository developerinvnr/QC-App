package com.example.qc.pojo;

public class GOTObservationPendingReportPojo {

    int srno;
    String crop;
    String spCode;
    String variety;
    String lotNo;
    String sampleNo;
    String TL;
    String DOT;
    String bedNo;
    String direction;

    public GOTObservationPendingReportPojo(int srno, String crop, String spCode, String variety, String lotNo, String sampleNo, String TL, String DOT, String bedNo, String direction) {
        this.srno = srno;
        this.crop = crop;
        this.spCode = spCode;
        this.variety = variety;
        this.lotNo = lotNo;
        this.sampleNo = sampleNo;
        this.TL = TL;
        this.DOT = DOT;
        this.bedNo = bedNo;
        this.direction = direction;
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

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getSampleNo() {
        return sampleNo;
    }

    public void setSampleNo(String sampleNo) {
        this.sampleNo = sampleNo;
    }

    public String getTL() {
        return TL;
    }

    public void setTL(String TL) {
        this.TL = TL;
    }

    public String getDOT() {
        return DOT;
    }

    public void setDOT(String DOT) {
        this.DOT = DOT;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
