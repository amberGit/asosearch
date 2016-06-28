package com.kingdee.grab;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kingdee.grab.entity.SearchRelatedResult;
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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.*;

/**
 *
 * 蝉大师登录
 */
public class AsoLogin {
    private static final String HOME_PAGE = "http://www.chandashi.com/";
    private static final String SEARCH_URL = "http://www.chandashi.com/search/index.html";
    private static final String SEARCH_RELATED_URL = "http://www.ddashi.com/search/related.html";
    private static final String LOGIN_PAGE_URL = "http://www.chandashi.com/user/login.html";
    private static final String SEARCH_RESULT_URL = HOME_PAGE + "apps/keywordcontent/appId/{0}.html";
    private static final String ASO_KEYWORDS_RANKING_EXPORT_URL = HOME_PAGE + "/export/index/appid/{0}.html";

    public enum PlatformType {
        IOS("store"), ANDROID("android");

        private final String value;

        PlatformType(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }


    private Map<String, String> loginArgsMap;
    private final CloseableHttpClient httpClient;

    public AsoLogin(String email, String password) {
        httpClient = HttpClients.createDefault();
        loginArgsMap = new HashMap<>();
        loginArgsMap.put("email", email);
        loginArgsMap.put("password", password);
    }


    public Map<String, String> search(String keyword, PlatformType platformType) throws Exception {
        HttpGet httpGet = new HttpGet(SEARCH_URL + "?view=web&keyword=" + keyword + "&type=" + platformType.value());
        try {
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    Document document = Jsoup.parse(EntityUtils.toString(entity));
                    Elements searchResults = document.select("#searchlist");
                    if (searchResults == null) {
                        throw new Exception("不能找到搜索列表");
                    }
                    Map<String, String> searchResultMap = new HashMap<>();
                    Element searchResult = searchResults.first();
                    searchResult.children().stream().filter(element -> element.child(0).child(1).tagName().equals("a")).forEach(element1 -> {
                        Element oneOfSearchResult = element1.child(0).child(1);
                        searchResultMap.put(oneOfSearchResult.attr("title"), oneOfSearchResult.attr("href"));
                    });
                    return searchResultMap;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Deprecated
    public void viewSearchDetail(String appId) {
        String url = MessageFormat.format(SEARCH_RESULT_URL, appId);
        HttpGet httpGet = new HttpGet(url);
        try {
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println(EntityUtils.toString(entity));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadAsoKeywordsRankingFile(String appId, Path destPath) {
        Objects.requireNonNull(destPath, "文件路径不能为空");
        String url = MessageFormat.format(ASO_KEYWORDS_RANKING_EXPORT_URL, appId);
        HttpGet httpGet = new HttpGet(url);
        try {
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                Files.copy(entity.getContent(), destPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        HttpPost httpPost = new HttpPost(LOGIN_PAGE_URL);
        List<NameValuePair> params = new ArrayList<>();
        String content = loginPage();
        try {
            String rand = getRand(content);
            if (StringUtils.isNotBlank(rand)) {
                params.add(new BasicNameValuePair("rand", rand));
            } else {
                throw new Exception("获取 rand 出错");
            }


            loginArgsMap.entrySet().stream().forEach(entry -> params.add(new BasicNameValuePair(entry.getKey(), entry.getValue())));
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(urlEncodedFormEntity);
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println(response.getStatusLine());
                    System.out.println(EntityUtils.toString(entity, "UTF-8"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


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

}
