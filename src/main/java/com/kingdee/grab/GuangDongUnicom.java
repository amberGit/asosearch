package com.kingdee.grab;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wen Jiao [jiao_wen@kingdee.com]
 * @since 2016-09-27 16:24
 */
public class GuangDongUnicom {

    private static final String LOGIN_URL_PATTERN = "https://uac.10010.com/portal/Service/MallLogin?" +
            "callback={0}&req_time={1}" +
            "&redirectURL={2}" +
            "&userName={3}&password={4}&pwdType=01&productType=01" +
            "&redirectType=01&rememberMe=1&_={5}";

    private static final String CALL_DETAIL_URL_PATTERN = "http://iservice.10010.com/e3/static/query/callDetail?_={0}&menuid=000100030001"; // 详单查询
    private static final String SMS_URL_PATTERN = "http://iservice.10010.com/e3/static/query/sms?_={0}&menuid=000100030002"; // 短信/彩信查询
    private static final String CALL_FLOW_URL_PATTERN = "http://iservice.10010.com/e3/static/query/callFlow?_={0}&menuid=000100030004"; // 流量查询
    private static final String CALL_NET_PLAY_RECORD_URL_PATTERN = "http://iservice.10010.com/e3/static/query/callNetPlayRecord?_={0}&menuid=000100030009"; // 上网记录查询
    private static final String CALL_VALUE_ADDED_URL_PATTERN = "http://iservice.10010.com/e3/static/query/callValueAdded?_={0}&menuid=000100030003"; // 增值业务

    private static final String CALLBACK_PATTERN = "jQuery172{0}_{1}";



    private final CloseableHttpClient httpClient;

    private String userName;

    private String password;

    public GuangDongUnicom(String userName, String password) {

        httpClient = HttpClients.createDefault();

        this.userName = userName;
        this.password = password;
    }


    public void homepage() {
        String url = "http://www.10010.com/gd/";

        accessByGetMethod(url);

        String iServiceUrlPattern = "http://iservice.10010.com/e3/static/common/mall_info?callback=jsonp{0}";

        String iServiceUrl = MessageFormat.format(iServiceUrlPattern, System.currentTimeMillis() / 1000 + "");

        accessByGetMethod(iServiceUrl);

    }

    public String getCallFlowDetail(String date, String pageNo, String pageSize) {
       return getBusinessDetail(CALL_FLOW_URL_PATTERN, date, date, pageNo, pageSize);
    }

    public String getCallNetPlayRecordDetail(String beginDate, String endDate, String pageNo, String pageSize) {
        return getBusinessDetail(CALL_NET_PLAY_RECORD_URL_PATTERN, beginDate, endDate, pageNo, pageSize);
    }

    public String getCallDetail(String beginDate, String endDate, String pageNo, String pageSize) {
        return getBusinessDetail(CALL_DETAIL_URL_PATTERN, beginDate, endDate, pageNo, pageSize);
    }

    public String getValueAddedDetail(String beginDate, String endDate, String pageNo, String pageSize) {
        return getBusinessDetail(CALL_VALUE_ADDED_URL_PATTERN, beginDate, endDate, pageNo, pageSize);
    }

    private String getBusinessDetail(String url, String beginDate, String endDate, String pageNo, String pageSize) {
        Map<String ,String > params = new HashMap<>();
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);

        return queryDetail(url, params);
    }

    public String getSmsDetail(String beginDate, String endDate, String pageNo, String pageSize) {
        Map<String ,String > params = new HashMap<>();
        params.put("begindate", beginDate);
        params.put("enddate", endDate);
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        return queryDetail(SMS_URL_PATTERN, params);

    }




    private void accessByGetMethod(String url) {
        HttpGet get = new HttpGet(url);

        try (CloseableHttpResponse response = httpClient.execute(get)) {
            System.out.println(url + ": " + response.getStatusLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  post 查询 公共方法
     * @param urlPattern 短信或者详单的url 样式
     * @param params 提交参数
     * @return 查询结果json字符串
     */
    private String queryDetail(String urlPattern, Map<String, String> params) {
        String timestamp = System.currentTimeMillis() / 1000 + "";
        String url = MessageFormat.format(urlPattern, timestamp);
        List<NameValuePair> postParams = new ArrayList<>();
        params.entrySet().forEach(
                entry -> postParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()))
        );
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(postParams, StandardCharsets.UTF_8);

        HttpPost post = new HttpPost(url);
        post.setEntity(urlEncodedFormEntity);

        try (CloseableHttpResponse response = httpClient.execute(post)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean login() {
        boolean result = false;
        String randomNumber = (Math.random() + "").replaceAll("\\D", "");
        long timeStamp = System.currentTimeMillis() / 1000;
        
        String callback = MessageFormat.format(CALLBACK_PATTERN, randomNumber, timeStamp + "");
        String loginUrl = MessageFormat.format(LOGIN_URL_PATTERN, callback, timeStamp + "", "http://www.10010.com", userName, password, timeStamp + 4 + "" );


        HttpGet get = new HttpGet(loginUrl);

        try (CloseableHttpResponse response = httpClient.execute(get)) {

            if ("OK".equals(response.getStatusLine().getReasonPhrase()) && 200 == response.getStatusLine().getStatusCode())
                result = true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


}
