package com.scraping.training.news;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewsNation {
    private String title;
    @JsonProperty("link")
    private String link;
    @JsonProperty("image_url")
    private String image_url;
    @JsonProperty("pubDate")
    private String publishedDate;

    public NewsNation(String title, String link, String image_url, String publishedDate) {
        this.title = title;
        this.link = link;
        this.image_url = image_url;
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }
}
