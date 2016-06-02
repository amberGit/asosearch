package com.kingdee.grab;

import com.google.gson.Gson;
import com.kingdee.grab.entity.Geetest;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 蝉大师登录
 */
public class AsoLogin {
    private static final String D_DASHI_URL = "http://www.ddashi.com/search/related.html";
    private static final String GT_URL = "http://www.chandashi.com/valid/gfrist";
    private static final String LOGIN_URL = "http://www.chandashi.com/User/login";
    private static final String LOGIN_PAGE_URL = "http://www.chandashi.com/user/login.html";

    private Map<String, String> loginArgsMap;
    private CloseableHttpClient httpClient;

    public AsoLogin(String email, String password) {
        httpClient = HttpClients.createDefault();
        loginArgsMap = new HashMap<>();
        loginArgsMap.put("email", email);
        loginArgsMap.put("password", password);
    }
    private String loginPage() {
        HttpGet httpGet = new HttpGet(LOGIN_PAGE_URL);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getRand(String content) throws Exception {
        if (StringUtils.isBlank(content)) {
            throw new Exception("没有抓取到主页内容");
        } else {
            Document document = Jsoup.parse(content);
            Elements randElem = document.select("input[name=rand]");
            String rand = randElem.first().val();
            return rand;
        }
    }

    private Geetest getGeetest() {
        long currentTimestamp = System.currentTimeMillis();
        String requestParams = "?t=" + currentTimestamp;
        HttpGet httpGet = new HttpGet(GT_URL + requestParams);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    Gson gson = new Gson();
                    return gson.fromJson(EntityUtils.toString(entity), Geetest.class);
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void login() {
        HttpPost httpPost = new HttpPost(LOGIN_URL);
        List<NameValuePair> params = new ArrayList<>();
        String content = loginPage();
        try {
            String rand = getRand(content);
            if (StringUtils.isNotBlank(rand)) {
                params.add(new BasicNameValuePair("rand", rand));
            } else {
                throw new Exception("获取 rand 出错");
            }
            Geetest geetest = getGeetest();
            if (geetest != null) {
                params.add(new BasicNameValuePair("geetest_challenge", geetest.getChallenge()));
                params.add(new BasicNameValuePair("geetest_seccode", geetest.getGt() + "|jordan"));
                params.add(new BasicNameValuePair("geetest_validate", geetest.getGt()));
            } else {
                throw new Exception("获取geetest 信息出错");
            }

            loginArgsMap.entrySet().stream().forEach(entry -> params.add(new BasicNameValuePair(entry.getKey(), entry.getValue())));
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(urlEncodedFormEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println(EntityUtils.toString(entity, "UTF-8"));
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}