package com.kingdee.grab.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author Wen Jiao [jiao_wen@kingdee.com]
 * @since 2016-11-07 10:46
 */
public class VerifyInfo {
    @SerializedName("CaptchaFlag")
    private int captchaFlag; // 验证码标志
    @SerializedName("ErrMsg")
    private String errMsg;
    @SerializedName("FailTimes")
    private int failTimes;
    @SerializedName("LockTimes")
    private int lockTimes;
    @SerializedName("ResultCode")
    private int resultCode;

    public VerifyInfo() {
    }

    public int getCaptchaFlag() {
        return this.captchaFlag;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public int getFailTimes() {
        return this.failTimes;
    }

    public int getLockTimes() {
        return this.lockTimes;
    }

    public int getResultCode() {
        return this.resultCode;
    }

    public void setCaptchaFlag(int captchaFlag) {
        this.captchaFlag = captchaFlag;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public void setFailTimes(int failTimes) {
        this.failTimes = failTimes;
    }

    public void setLockTimes(int lockTimes) {
        this.lockTimes = lockTimes;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof VerifyInfo)) return false;
        final VerifyInfo other = (VerifyInfo) o;
        if (!other.canEqual(this)) return false;
        if (this.getCaptchaFlag() != other.getCaptchaFlag()) return false;
        final Object this$errMsg = this.getErrMsg();
        final Object other$errMsg = other.getErrMsg();
        if (this$errMsg == null ? other$errMsg != null : !this$errMsg.equals(other$errMsg)) return false;
        if (this.getFailTimes() != other.getFailTimes()) return false;
        if (this.getLockTimes() != other.getLockTimes()) return false;
        return this.getResultCode() == other.getResultCode();
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getCaptchaFlag();
        final Object $errMsg = this.getErrMsg();
        result = result * PRIME + ($errMsg == null ? 43 : $errMsg.hashCode());
        result = result * PRIME + this.getFailTimes();
        result = result * PRIME + this.getLockTimes();
        result = result * PRIME + this.getResultCode();
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof VerifyInfo;
    }

    public String toString() {
        return "com.kingdee.grab.entity.VerifyInfo(captchaFlag=" + this.getCaptchaFlag() + ", errMsg=" + this.getErrMsg() + ", failTimes=" + this.getFailTimes() + ", lockTimes=" + this.getLockTimes() + ", resultCode=" + this.getResultCode() + ")";
    }
}
