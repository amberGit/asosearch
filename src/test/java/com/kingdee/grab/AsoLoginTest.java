package com.kingdee.grab;

import com.kingdee.grab.entity.SearchRelatedResult;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * 蝉大师登录单元测试
 */
public class AsoLoginTest {

    @Test
    public void loginTest() {
        AsoLogin asoLogin = new AsoLogin("C:\\Users\\Administrator\\Desktop\\cookies.txt");
        List<SearchRelatedResult> searchRelatedResults = asoLogin.searchRelated("51");
    }
    @Ignore
    public void matchTest() {
        String str = "123444";
        System.out.println(str.matches("\\d*"));
    }

}
