package com.kingdee.grab.entity;

/**
 * @author Wen Jiao [jiao_wen@kingdee.com]
 * @since 2016-11-05 16:07
 */
public class PortalInfo {

    private String provinceName;
    private int provinceCode;
    private String cityName;
    private int cityCode;

    @java.beans.ConstructorProperties({"provinceName", "provinceCode", "cityName", "cityCode"})
    PortalInfo(String provinceName, int provinceCode, String cityName, int cityCode) {
        this.provinceName = provinceName;
        this.provinceCode = provinceCode;
        this.cityName = cityName;
        this.cityCode = cityCode;
    }

    public static PortalInfoBuilder builder() {
        return new PortalInfoBuilder();
    }

    protected boolean canEqual(Object other) {
        return other instanceof PortalInfo;
    }

    public String getProvinceName() {
        return this.provinceName;
    }

    public int getProvinceCode() {
        return this.provinceCode;
    }

    public String getCityName() {
        return this.cityName;
    }

    public int getCityCode() {
        return this.cityCode;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof PortalInfo)) return false;
        final PortalInfo other = (PortalInfo) o;
        if (!other.canEqual(this)) return false;
        final Object this$provinceName = this.getProvinceName();
        final Object other$provinceName = other.getProvinceName();
        if (this$provinceName == null ? other$provinceName != null : !this$provinceName.equals(other$provinceName))
            return false;
        if (this.getProvinceCode() != other.getProvinceCode()) return false;
        final Object this$cityName = this.getCityName();
        final Object other$cityName = other.getCityName();
        if (this$cityName == null ? other$cityName != null : !this$cityName.equals(other$cityName)) return false;
        return this.getCityCode() == other.getCityCode();
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $provinceName = this.getProvinceName();
        result = result * PRIME + ($provinceName == null ? 43 : $provinceName.hashCode());
        result = result * PRIME + this.getProvinceCode();
        final Object $cityName = this.getCityName();
        result = result * PRIME + ($cityName == null ? 43 : $cityName.hashCode());
        result = result * PRIME + this.getCityCode();
        return result;
    }

    public String toString() {
        return "com.kingdee.grab.entity.PortalInfo(provinceName=" + this.getProvinceName() + ", provinceCode=" + this.getProvinceCode() + ", cityName=" + this.getCityName() + ", cityCode=" + this.getCityCode() + ")";
    }

    public static class PortalInfoBuilder {
        private String provinceName;
        private int provinceCode;
        private String cityName;
        private int cityCode;

        PortalInfoBuilder() {
        }

        public PortalInfo.PortalInfoBuilder provinceName(String provinceName) {
            this.provinceName = provinceName;
            return this;
        }

        public PortalInfo.PortalInfoBuilder provinceCode(int provinceCode) {
            this.provinceCode = provinceCode;
            return this;
        }

        public PortalInfo.PortalInfoBuilder cityName(String cityName) {
            this.cityName = cityName;
            return this;
        }

        public PortalInfo.PortalInfoBuilder cityCode(int cityCode) {
            this.cityCode = cityCode;
            return this;
        }

        public PortalInfo build() {
            return new PortalInfo(provinceName, provinceCode, cityName, cityCode);
        }

        public String toString() {
            return "com.kingdee.grab.entity.PortalInfo.PortalInfoBuilder(provinceName=" + this.provinceName + ", provinceCode=" + this.provinceCode + ", cityName=" + this.cityName + ", cityCode=" + this.cityCode + ")";
        }
    }
}
