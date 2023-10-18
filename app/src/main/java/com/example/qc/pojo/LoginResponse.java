package com.example.qc.pojo;

public class LoginResponse {
    private String name;
    private String role;
    private String mobile1;
    private String sessionid;
    private String scode;
    private String pcode;
    private String massage;
    private String oprtype;

    public LoginResponse() {
    }

    public LoginResponse(String name, String role, String mobile1, String sessionid, String scode, String pcode, String massage, String oprtype) {
        this.name = name;
        this.role = role;
        this.mobile1 = mobile1;
        this.sessionid = sessionid;
        this.scode = scode;
        this.pcode = pcode;
        this.massage = massage;
        this.oprtype = oprtype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
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

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getOprtype() {
        return oprtype;
    }

    public void setOprtype(String oprtype) {
        this.oprtype = oprtype;
    }
}
