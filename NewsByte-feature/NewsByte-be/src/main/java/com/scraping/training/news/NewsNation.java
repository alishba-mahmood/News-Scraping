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

}
