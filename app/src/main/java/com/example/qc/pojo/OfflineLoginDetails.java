package com.example.qc.pojo;

public class OfflineLoginDetails {
    String oprname = "";
    String scode = "";
    String pcode = "";
    String logid = "";
    String loginid = "";
    String password = "";
    String role = "";

    public OfflineLoginDetails() {

    }

    public OfflineLoginDetails(String oprname, String scode, String pcode, String logid, String loginid, String password, String role) {
        this.oprname = oprname;
        this.scode = scode;
        this.pcode = pcode;
        this.logid = logid;
        this.loginid = loginid;
        this.password = password;
        this.role = role;
    }

    public String getOprname() {
        return oprname;
    }

    public void setOprname(String oprname) {
        this.oprname = oprname;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getLogid() {
        return logid;
    }

    public void setLogid(String logid) {
        this.logid = logid;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
