package com.example.exception;

public class CustomerEeception extends RuntimeException {
    private String code;
    private String msg;

    public CustomerEeception(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
    public CustomerEeception(String msg) {
        super(msg);
        this.code = "500";
        this.msg = msg;
    }

    public CustomerEeception(){}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
