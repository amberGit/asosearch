package com.kingdee.grab.entity;

/**
 * 蝉大师搜索返回的json
 */
public class SearchRelatedResult {
    private int id;
    private String label;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
