package com.kingdee.grab.study;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author Wen Jiao [jiao_wen@kingdee.com]
 * @since 2016-11-08 12:42
 */
public class RequestBuilderSample {

    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpUriRequest request = RequestBuilder.get()
                .setUri("http://cn.bing.com/search")
                .addParameter("q", "Java+Autocloseable")
                .addParameter("FORM", "QSRE1")
                .build();

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            System.out.println(EntityUtils.toString(entity));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
