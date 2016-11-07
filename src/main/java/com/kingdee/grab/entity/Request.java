package com.kingdee.grab.entity;

import java.util.Map;

/**
 * @author Wen Jiao [jiao_wen@kingdee.com]
 * @since 2016-11-07 14:03
 */

public class Request {
    private String method;
    private String url;
    private Map<String, String> params;
    private Map<String, String> cookies;
    private Map<String, String> headers;

    @java.beans.ConstructorProperties({"method", "url", "params", "cookies", "headers"})
    Request(String method, String url, Map<String, String> params, Map<String, String> cookies, Map<String, String> headers) {
        this.method = method;
        this.url = url;
        this.params = params;
        this.cookies = cookies;
        this.headers = headers;
    }

    public static RequestBuilder builder() {
        return new RequestBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (!method.equals(request.method)) return false;
        return url.equals(request.url) && (params != null ? params.equals(request.params) : request.params == null);

    }

    @Override
    public int hashCode() {
        int result = method.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + (params != null ? params.hashCode() : 0);
        return result;
    }

    public String getMethod() {
        return this.method;
    }

    public String getUrl() {
        return this.url;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public Map<String, String> getCookies() {
        return this.cookies;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public static class RequestBuilder {
        private String method;
        private String url;
        private Map<String, String> params;
        private Map<String, String> cookies;
        private Map<String, String> headers;

        RequestBuilder() {
        }

        public Request.RequestBuilder method(String method) {
            this.method = method;
            return this;
        }

        public Request.RequestBuilder url(String url) {
            this.url = url;
            return this;
        }

        public Request.RequestBuilder params(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public Request.RequestBuilder cookies(Map<String, String> cookies) {
            this.cookies = cookies;
            return this;
        }

        public Request.RequestBuilder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Request build() {
            return new Request(method, url, params, cookies, headers);
        }

        public String toString() {
            return "com.kingdee.grab.entity.Request.RequestBuilder(method=" + this.method + ", url=" + this.url + ", params=" + this.params + ", cookies=" + this.cookies + ", headers=" + this.headers + ")";
        }
    }
}
