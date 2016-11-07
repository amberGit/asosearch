package com.kingdee.grab;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kingdee.grab.entity.PortalInfo;
import com.kingdee.grab.entity.Request;
import com.kingdee.grab.entity.Response;
import com.kingdee.grab.entity.VerifyInfo;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Wen Jiao [jiao_wen@kingdee.com]
 * @since 2016-11-05 15:58
 */
public class HunanTelecom {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";
    private CloseableHttpClient httpClient;

    private static final String VERIFY_URL = "http://login.189.cn/login/ajax";
    private String userName;
    private String password;

    private void initHttpClient() {

        httpClient = HttpClients
                .custom()
                .setUserAgent(USER_AGENT)
                .build();


    }

    public HunanTelecom(String userName, String password) {
        initHttpClient();
        this.userName = userName;
        this.password = password;
    }

    public Response request(Request request) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return get(request);
        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            return post(request);
        }
        return null;
    }

    private Response post(Request request) {
        HttpPost post = new HttpPost(request.getUrl());
        Optional.ofNullable(request.getParams())
                .ifPresent(params -> {

                    List<NameValuePair> paramList = params.entrySet()
                            .stream()
                            .map(p -> new BasicNameValuePair(p.getKey(), p.getValue()))
                            .collect(Collectors.toList());
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(paramList, StandardCharsets.UTF_8);
                    post.setEntity(urlEncodedFormEntity);
                });

        Optional.ofNullable(request.getHeaders())
                .ifPresent(notNullHeaders -> post.setHeaders(
                        notNullHeaders.entrySet()
                                .stream()
                                .map(h -> new BasicHeader(h.getKey(), h.getValue()))
                                .toArray(BasicHeader[]::new)
                ));

        Response responseEntity = new Response();
        try (CloseableHttpResponse response = httpClient.execute(post)) {
            String text = EntityUtils.toString(response.getEntity());
            Charset charset = null;
            Header contentEncoding = response.getEntity().getContentEncoding();
            if ( contentEncoding != null && contentEncoding.getValue() != null) {
                charset = Charset.forName(contentEncoding.getValue());
            }

            responseEntity = Response.builder()
                    .statusCode(response.getStatusLine().getStatusCode())
                    .content(EntityUtils.toByteArray(response.getEntity()))
                    .text(text)
                    .headers(response.getAllHeaders())
                    .encoding(charset)
                    .cookies(response.getHeaders("Set-Cookie"))
                    .request(request)
                    .raw(response.getEntity().getContent())
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
            responseEntity.setRequest(request);
            responseEntity.setException(e);
        }
        return responseEntity;
    }

    private Response get(Request request) {
        HttpGet get = new HttpGet(request.getUrl());

        Optional.ofNullable(request.getHeaders())
                .ifPresent(notNullHeaders -> get.setHeaders(
                        notNullHeaders.entrySet()
                                .stream()
                                .map(h -> new BasicHeader(h.getKey(), h.getValue()))
                                .toArray(BasicHeader[]::new)
                ));
        Response responseEntity;

        try (CloseableHttpResponse response = httpClient.execute(get)) {
            String text = EntityUtils.toString(response.getEntity());

            responseEntity = Response.builder()
                    .encoding(Charset.forName(response.getEntity().getContentEncoding().getValue()))
                    .text(text)
                    .cookies(response.getHeaders("Set-Cookie"))
                    .content(EntityUtils.toByteArray(response.getEntity()))
                    .headers(response.getAllHeaders())
                    .request(request)
                    .statusCode(response.getStatusLine().getStatusCode())
                    .raw(response.getEntity().getContent())
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
            responseEntity = Response.builder()
                    .exception(e)
                    .request(request)
                    .build();
        }
        return responseEntity;
    }

    public PortalInfo getPortal() {


        Map<String, String> params = new HashMap<>();
        params.put("m", "checkphone");
        params.put("phone", userName);
        Request request = Request.builder()
                .url(VERIFY_URL)
                .cookies(null)
                .headers(null)
                .method("post")
                .params(params)
                .build();

        Response response = request(request);

        if (response != null && response.getException() == null) {

            JSONObject portalJson = JSON.parseObject(response.getText());
            if (portalJson != null) {
                String cityName = portalJson.getString("CityName");
                int cityCode = portalJson.getIntValue("CityNo");
                String provinceName = portalJson.getString("ProvinceName");
                int provinceId = portalJson.getIntValue("ProvinceID");
                return PortalInfo.builder()
                        .cityCode(cityCode)
                        .cityName(cityName)
                        .provinceCode(provinceId)
                        .provinceName(provinceName)
                        .build();
            }
        }
        return null;
    }

    public VerifyInfo getCheckVerifyInfo(int provinceId) {
        Map<String, String> params = new HashMap<>();
        params.put("m", "loadlogincaptcha");
        params.put("Account", userName);
        params.put("UType", "201");
        params.put("ProvinceID", provinceId + "");
        params.put("AreaCode", "");
        params.put("CityNo", "");
        Request request = Request.builder()
                .method("post")
                .params(params)
                .url(VERIFY_URL)
                .build();
        Response response = request(request);

        if (response != null && response.getException() == null) {

            Gson gson = new Gson();
            return gson.fromJson(response.getText(), VerifyInfo.class);
        }
        return null;
    }

    public static void main(String[] args) {

    }

}
