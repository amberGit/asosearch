package com.kingdee.grab.entity;

/**
 * Created by Administrator on 2016/6/2.
 */
public enum HttpRequestHeaders {
    ACCEPT("Accept", "*/*"),
    ACCEPT_ENCODING("Accept-Encoding", "gzip,deflate"),
    ACCEPT_LANGUAGE("Accept-language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3"),
    CONTENT_TYPE("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"),
    USER_AGENT("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0");


    HttpRequestHeaders(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private final String key;
    private final String value;

    public String key() {
        return key;
    }
    public String value() {
        return value;
    }
}
