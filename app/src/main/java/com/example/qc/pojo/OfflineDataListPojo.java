package com.example.qc.pojo;

public class OfflineDataListPojo {
    private String crop;
    private String variety;
    private String lotno;
    private String trstage;
    private String sampleno;
    private String qcgTesttype;
    private String qcgSetupdt;
    private String seedsize;
    private String qcgVigtesttype;
    private Integer qcgNoofseedinonerepfgt;
    private Integer qcgVignoofrep;

    public OfflineDataListPojo() {
    }

    public OfflineDataListPojo(String crop, String variety, String lotno, String trstage, String sampleno, String qcgTesttype, String qcgSetupdt, String seedsize, String qcgVigtesttype, Integer qcgNoofseedinonerepfgt, Integer qcgVignoofrep) {
        this.crop = crop;
        this.variety = variety;
        this.lotno = lotno;
        this.trstage = trstage;
        this.sampleno = sampleno;
        this.qcgTesttype = qcgTesttype;
        this.qcgSetupdt = qcgSetupdt;
        this.seedsize = seedsize;
        this.qcgVigtesttype = qcgVigtesttype;
        this.qcgNoofseedinonerepfgt = qcgNoofseedinonerepfgt;
        this.qcgVignoofrep = qcgVignoofrep;
    }

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
