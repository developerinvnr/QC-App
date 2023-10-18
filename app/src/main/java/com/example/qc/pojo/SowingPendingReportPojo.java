package com.example.qc.pojo;

public class SowingPendingReportPojo {
    int srno;
    String doa;
    String crop;
    String spCode;
    String variety;
    String lotno;
    String sample;
    String NoB;
    String quantity;
    String gotStatus;
    String PDLoc;
    String organiser;
    String farmer;
    String PP;
    String SLOC;

    public SowingPendingReportPojo(int srno, String doa, String crop, String spCode, String variety, String lotno, String sample, String noB, String quantity, String gotStatus, String PDLoc, String organiser, String farmer, String PP, String SLOC) {
        this.srno = srno;
        this.doa = doa;
        this.crop = crop;
        this.spCode = spCode;
        this.variety = variety;
        this.lotno = lotno;
        this.sample = sample;
        NoB = noB;
        this.quantity = quantity;
        this.gotStatus = gotStatus;
        this.PDLoc = PDLoc;
        this.organiser = organiser;
        this.farmer = farmer;
        this.PP = PP;
        this.SLOC = SLOC;
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

    public String getNoB() {
        return NoB;
    }

    public void setNoB(String noB) {
        NoB = noB;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getGotStatus() {
        return gotStatus;
    }

    public void setGotStatus(String gotStatus) {
        this.gotStatus = gotStatus;
    }

    public String getPDLoc() {
        return PDLoc;
    }

    public void setPDLoc(String PDLoc) {
        this.PDLoc = PDLoc;
    }

    public String getOrganiser() {
        return organiser;
    }

    public void setOrganiser(String organiser) {
        this.organiser = organiser;
    }

    public String getFarmer() {
        return farmer;
    }

    public void setFarmer(String farmer) {
        this.farmer = farmer;
    }

    public String getPP() {
        return PP;
    }

    public void setPP(String PP) {
        this.PP = PP;
    }

    public String getSLOC() {
        return SLOC;
    }

    public void setSLOC(String SLOC) {
        this.SLOC = SLOC;
    }

}
