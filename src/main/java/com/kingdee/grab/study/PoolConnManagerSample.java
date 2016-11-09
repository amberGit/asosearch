package com.kingdee.grab.study;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.stream.Stream;

/**
 * @author Wen Jiao [jiao_wen@kingdee.com]
 * @since 2016-11-09 12:22
 */
public class PoolConnManagerSample {

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        HttpClientContext context = HttpClientContext.create();

        PoolingHttpClientConnectionManager connMnanager = new PoolingHttpClientConnectionManager();
        // 设置最大连接数
        connMnanager.setMaxTotal(200);
        // 将每个路由基础连接增加到20
        connMnanager.setDefaultMaxPerRoute(20);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connMnanager)
                .build();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ExecutorCompletionService completionService = new ExecutorCompletionService(executorService);
        String[] uris = {
                "http://www.baidu.com",
                "http://cn.bing.com",
                "http://www.sina.com.cn/",
                "http://www.firefoxchina.cn/?ntab"
        };

        Stream.of(uris)
                .map(u -> new uriTask(httpClient, u))
                .forEach(completionService::submit);

        for (int i = 0; i < uris.length; i++) {
            try (CloseableHttpResponse response = (CloseableHttpResponse) completionService.take().get()) {
                System.out.println("Status code: " + response.getStatusLine().getStatusCode());
                System.out.println("content length: " + response.getEntity().getContentLength());
//                System.out.println("content: " + EntityUtils.toString(response.getEntity()));
            }


        }

        executorService.shutdown();

    }

}

class uriTask implements Callable<CloseableHttpResponse> {
    private CloseableHttpClient httpClient;
    private String requestUrl;

    public uriTask(CloseableHttpClient httpClient, String url) {
        this.httpClient = httpClient;
        requestUrl = url;
    }

    @Override
    public CloseableHttpResponse call() throws Exception {
        return httpClient.execute(
                RequestBuilder.get(requestUrl)
                        .build()
        );

    }
}
