package com.scraping.training.news;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NewsNYResponse {

    @JsonProperty("results")
    private List<NewsNyTimes> articles;
    @JsonProperty("nextPage")
    private String nextPage;

    public List<NewsNyTimes> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsNyTimes> articles) {
        this.articles = articles;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }
}
