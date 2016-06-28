package com.kingdee.grab;

import com.kingdee.grab.entity.SearchRelatedResult;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * 蝉大师登录单元测试
 */
public class AsoLoginTest {
    private AsoLogin asoLogin = new AsoLogin("wowv58791@163.com", "johnnyven407");

    @Ignore
    public void searchRelatedTest() {
        List<SearchRelatedResult> searchRelatedResults = asoLogin.searchRelated("51");
    }
    @Test
    public void loginTest() {
        asoLogin.login();
        asoLogin.downloadAsoKeywordsRankingFile("564765093", Paths.get("C:\\Users\\Administrator\\Desktop\\test.xls"));
    }
    @Ignore
    public void searchTest() {
        try {
            Map<String, String > searchMap = asoLogin.search("51", AsoLogin.PlatformType.IOS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
