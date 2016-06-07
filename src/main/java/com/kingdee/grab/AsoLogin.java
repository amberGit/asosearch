package com.kingdee.grab;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kingdee.grab.entity.Geetest;
import com.kingdee.grab.entity.SearchRelatedResult;
import com.kingdee.grab.util.DateTransformUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 蝉大师登录
 */
public class AsoLogin {
    private static final String SEARCH_URL = "http://www.chandashi.com/search/index.html";
    private static final String SEARCH_RELATED_URL = "http://www.ddashi.com/search/related.html";
    private static final String GT_URL = "http://www.chandashi.com/valid/gfrist";
    private static final String LOGIN_URL = "http://www.chandashi.com/User/login";
    private static final String LOGIN_PAGE_URL = "http://www.chandashi.com/user/login.html";
    private static final String HOME_PAGE = "http://chandashi.com/";

    private Map<String, String> loginArgsMap;
    private CloseableHttpClient httpClient;

    public AsoLogin(String email, String password) {
        httpClient = HttpClients.createDefault();
        loginArgsMap = new HashMap<>();
        loginArgsMap.put("email", email);
        loginArgsMap.put("password", password);
    }

    public AsoLogin(final String cookieFilePath) {
        StringBuilder cookieStr = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(cookieFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.lines().forEach(s -> cookieStr.append(s).append('\n'));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        CookieStore cookieStore = loadLocalCookies(cookieStr.toString());
        httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    }


    private CookieStore loadLocalCookies(final String cookieStr) {
        String cookieRegex = "(\\S+)\\s\\S+\\s(\\S+)\\s\\S+\\s(?:(\\d+)\\s)?(\\S+)\\s([^\\n]+)";
        Pattern cookiePattern = Pattern.compile(cookieRegex);
        Matcher matcher = cookiePattern.matcher(cookieStr);
        CookieStore cookieStore = new BasicCookieStore();
        while (matcher.find()) {
            int matchCount = matcher.groupCount();
            BasicClientCookie cookie = new BasicClientCookie(matcher.group(matchCount - 1), matcher.group(matchCount));
            cookie.setDomain(matcher.group(1));
            cookie.setPath(matcher.group(2));
            String unixTimeStampStr = matcher.group(matchCount - 2);
            if (StringUtils.isNotBlank(unixTimeStampStr) && unixTimeStampStr.matches("\\d+")) {
                try {
                    Date expiryDate = DateTransformUtil.unixTimestamp2Date(Long.parseLong(unixTimeStampStr));
                    cookie.setExpiryDate(expiryDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            cookieStore.addCookie(cookie);
        }
        return cookieStore;
    }


    private String loginPage() {
        HttpGet httpGet = new HttpGet(LOGIN_PAGE_URL);
        try {
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
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
            return randElem.first().val();
        }
    }

    private Geetest getGeetest() {
        long currentTimestamp = System.currentTimeMillis();
        String requestParams = "?t=" + currentTimestamp;
        HttpGet httpGet = new HttpGet(GT_URL + requestParams);
        try {
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    Gson gson = new Gson();
                    return gson.fromJson(EntityUtils.toString(entity), Geetest.class);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<SearchRelatedResult> searchRelated(String text) {
        HttpGet httpGet = new HttpGet(SEARCH_RELATED_URL + "?term=" + text);
        try {
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    Gson gson = new Gson();
                    String jsonStr = EntityUtils.toString(entity);
                    return gson.fromJson(jsonStr, new TypeToken<List<SearchRelatedResult>>() {
                    }.getType());
                }
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
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println(EntityUtils.toString(entity, "UTF-8"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
