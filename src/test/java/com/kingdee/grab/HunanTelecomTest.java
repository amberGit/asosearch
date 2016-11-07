package com.kingdee.grab;

import com.kingdee.grab.entity.PortalInfo;
import com.kingdee.grab.entity.VerifyInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Wen Jiao [jiao_wen@kingdee.com]
 * @since 2016-11-05 16:54
 */
public class HunanTelecomTest {
    private HunanTelecom hunanTelecom;

    @Before
    public void setUp() throws Exception {
        hunanTelecom = new HunanTelecom("17769314905", "930811");

    }

    @Test
    public void getPortal() throws Exception {
        PortalInfo portalInfo = hunanTelecom.getPortal();
        Assert.assertNotNull(portalInfo);
        System.out.println(portalInfo.toString());
    }

    @Test
    public void getVerifyInfo() {
        PortalInfo portalInfo = hunanTelecom.getPortal();
        VerifyInfo verifyInfo = hunanTelecom.getCheckVerifyInfo(portalInfo.getProvinceCode());
        System.out.println(verifyInfo.getCaptchaFlag());
    }



}