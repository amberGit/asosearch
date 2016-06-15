package com.kingdee.grab.entity;

/**
 * gt 返回的JSON 实体类
 */
public class Geetest {
    private int success;
    private String gt;
    private String challenge;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getGt() {
        return gt;
    }

    public void setGt(String gt) {
        this.gt = gt;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }
}
