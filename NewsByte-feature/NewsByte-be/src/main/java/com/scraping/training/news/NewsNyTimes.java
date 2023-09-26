package com.scraping.training.news;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NewsNyTimes {

    private String title;
    @JsonProperty("published_date")
    private String published_date;
    @JsonProperty("url")
    private String pageUrl;
    @JsonProperty("media")
    private List<Media> media;
    private String image;

    public NewsNyTimes(String title, String pageUrl, String img, String published_date) {
        this.title = title;
        this.pageUrl = pageUrl;
        this.image = img;
        this.published_date = published_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
