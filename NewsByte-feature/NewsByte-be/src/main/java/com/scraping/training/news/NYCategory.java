package com.scraping.training.news;

import com.fasterxml.jackson.annotation.JsonProperty;
public class NYCategory {
    @JsonProperty("url")
    private String pageUrl;
    @JsonProperty("title")
    private String title;
    @JsonProperty("image")
    private String imageUrl;

    @JsonProperty("pub_date")
    private String pub_date;
    public NYCategory(String pageUrl, String title, String imageUrl, String date) {
        this.pageUrl = pageUrl;
        this.title = title;
        this.imageUrl = imageUrl;
        this.pub_date = date;
    }


    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String url) {
        this.pageUrl = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPub_date() {
        return pub_date;
    }

    public void setPub_date(String pub_date) {
        this.pub_date = pub_date;
    }
}
