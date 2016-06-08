package com.kingdee.grab;

import com.kingdee.grab.entity.SearchRelatedResult;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * 蝉大师登录单元测试
 */
public class AsoLoginTest {

    @Ignore
    public void searchRelatedTest() {
        AsoLogin asoLogin = new AsoLogin("C:\\Users\\Administrator\\Desktop\\cookies.txt");
        List<SearchRelatedResult> searchRelatedResults = asoLogin.searchRelated("51");
    }

    @Test
    public void searchTest() {
        AsoLogin asoLogin = new AsoLogin("C:\\Users\\Administrator\\Desktop\\cookies.txt");
        try {
            Map<String, String > searchMap = asoLogin.search("51", AsoLogin.PlatformType.IOS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Ignore
    public void matchTest() {
        String str = "123444";
        System.out.println(str.matches("\\d*"));
    }

}
