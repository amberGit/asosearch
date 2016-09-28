package com.kingdee.grab;

import org.junit.Test;

/**
 * @author Wen Jiao [jiao_wen@kingdee.com]
 * @since 2016-09-27 17:16
 */
public class GuangDongUnicomTest {
    @Test
    public void testRandom() {
        String randomNumber = (Math.random() + "").replaceAll("\\D", "");
        System.out.println(randomNumber);
    }

    @Test
    public void testTimeStamp() {
        System.out.println(System.currentTimeMillis() / 1000);
    }


    @Test
    public void testLogin() {
        GuangDongUnicom guangDongUnicom = new GuangDongUnicom("18588200807", "930811");
        if (guangDongUnicom.login()) {
            guangDongUnicom.homepage();
            String callDetail = guangDongUnicom.getCallDetail("2016-09-01", "2016-09-27", "1", "50");
            String sms = guangDongUnicom.getSmsDetail("20160901", "20160928", "1", "20");
            String flow = guangDongUnicom.getCallFlowDetail("2016-09-28", "1", "20");
            String netPlayRecord = guangDongUnicom.getCallNetPlayRecordDetail("2016-09-01", "2016-09-28", "1", "20");
            String valueAdded = guangDongUnicom.getValueAddedDetail("2016-09-01", "2016-09-28", "1", "20");
            System.out.println(sms);
        }

    }
}