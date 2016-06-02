package com.kingdee.grab;

import com.kingdee.grab.entity.Geetest;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Administrator on 2016/6/1.
 */
public class AsoLoginTest {

    @Test
    public void loginTest() {
        AsoLogin asoLogin = new AsoLogin("wowv58791@163.com", "cuowudemima");
        asoLogin.login();
    }


}
