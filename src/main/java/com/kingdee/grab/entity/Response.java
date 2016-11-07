package com.kingdee.grab.entity;

import org.apache.http.Header;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 *  包含input stream 尽量写在 try-with-resource代码块中
 * @author Wen Jiao [jiao_wen@kingdee.com]
 * @since 2016-11-07 14:01
 */

public class Response implements Closeable {

    private int statusCode;
    private String text;
    private byte[] content;
    private InputStream raw;
    private Charset encoding;
    private Header[] headers;
    private Header[] cookies;
    private Throwable exception;
    private Request request;


    public Response() {
    }

    public Response(int statusCode, String text, byte[] content, Charset encoding, Header[] headers, Header[] cookies, Throwable exception, Request request, InputStream raw) {
        this.statusCode = statusCode;
        this.text = text;
        this.content = content;
        this.encoding = encoding;
        this.headers = headers;
        this.cookies = cookies;
        this.exception = exception;
        this.request = request;
        this.raw = raw;
    }

    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getText() {
        return this.text;
    }

    public byte[] getContent() {
        return this.content;
    }

    public Charset getEncoding() {
        return this.encoding;
    }

    public Header[] getHeaders() {
        return this.headers;
    }

    public Header[] getCookies() {
        return this.cookies;
    }

    public Throwable getException() {
        return this.exception;
    }

    public Request getRequest() {
        return this.request;
    }

    public InputStream getRaw() {
        return raw;
    }

    public void setRaw(InputStream raw) {
        this.raw = raw;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void setEncoding(Charset encoding) {
        this.encoding = encoding;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public void setCookies(Header[] cookies) {
        this.cookies = cookies;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Response)) return false;
        final Response other = (Response) o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getStatusCode() != other.getStatusCode()) return false;
        final Object this$text = this.getText();
        final Object other$text = other.getText();
        if (this$text == null ? other$text != null : !this$text.equals(other$text)) return false;
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) return false;
        final Object this$encoding = this.getEncoding();
        final Object other$encoding = other.getEncoding();
        if (this$encoding == null ? other$encoding != null : !this$encoding.equals(other$encoding)) return false;
        if (!java.util.Arrays.deepEquals(this.getHeaders(), other.getHeaders())) return false;
        if (!java.util.Arrays.deepEquals(this.getCookies(), other.getCookies())) return false;
        final Object this$exception = this.getException();
        final Object other$exception = other.getException();
        if (this$exception == null ? other$exception != null : !this$exception.equals(other$exception)) return false;
        final Object this$request = this.getRequest();
        final Object other$request = other.getRequest();
        return this$request == null ? other$request == null : this$request.equals(other$request);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getStatusCode();
        final Object $text = this.getText();
        result = result * PRIME + ($text == null ? 43 : $text.hashCode());
        final Object $content = this.getContent();
        result = result * PRIME + ($content == null ? 43 : $content.hashCode());
        final Object $encoding = this.getEncoding();
        result = result * PRIME + ($encoding == null ? 43 : $encoding.hashCode());
        result = result * PRIME + java.util.Arrays.deepHashCode(this.getHeaders());
        result = result * PRIME + java.util.Arrays.deepHashCode(this.getCookies());
        final Object $exception = this.getException();
        result = result * PRIME + ($exception == null ? 43 : $exception.hashCode());
        final Object $request = this.getRequest();
        result = result * PRIME + ($request == null ? 43 : $request.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Response;
    }

    public String toString() {
        return "com.kingdee.grab.entity.Response(statusCode=" + this.getStatusCode() + ", text=" + this.getText() +  ", encoding=" + this.getEncoding() + ", headers=" + java.util.Arrays.deepToString(this.getHeaders()) + ", cookies=" + java.util.Arrays.deepToString(this.getCookies()) + ", exception=" + this.getException()  + ")";
    }

    @Override
    public void close() throws IOException {
        if (raw != null)
            raw.close();
    }


    public static class ResponseBuilder {
        private int statusCode;
        private String text;
        private byte[] content;
        private Charset encoding;
        private Header[] headers;
        private Header[] cookies;
        private Throwable exception;
        private Request request;
        private InputStream raw;
        ResponseBuilder() {
        }

        public Response.ResponseBuilder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Response.ResponseBuilder text(String text) {
            this.text = text;
            return this;
        }

        public Response.ResponseBuilder content(byte[] content) {
            this.content = content;
            return this;
        }

        public Response.ResponseBuilder encoding(Charset encoding) {
            this.encoding = encoding;
            return this;
        }

        public Response.ResponseBuilder headers(Header[] headers) {
            this.headers = headers;
            return this;
        }

        public Response.ResponseBuilder cookies(Header[] cookies) {
            this.cookies = cookies;
            return this;
        }

        public Response.ResponseBuilder exception(Throwable exception) {
            this.exception = exception;
            return this;
        }

        public Response.ResponseBuilder request(Request request) {
            this.request = request;
            return this;
        }

        public Response.ResponseBuilder raw(InputStream raw) {
            this.raw = raw;
            return this;
        }

        public Response build() {
            return new Response(statusCode, text, content, encoding, headers, cookies, exception, request, raw);
        }

        public String toString() {
            return "com.kingdee.grab.entity.Response.ResponseBuilder(statusCode=" + this.statusCode + ", text=" + this.text + ", content=" + Arrays.toString(this.content) + ", encoding=" + this.encoding + ", headers=" + java.util.Arrays.deepToString(this.headers) + ", cookies=" + java.util.Arrays.deepToString(this.cookies) + ", exception=" + this.exception + ", request=" + this.request + ")";
        }
    }
}
