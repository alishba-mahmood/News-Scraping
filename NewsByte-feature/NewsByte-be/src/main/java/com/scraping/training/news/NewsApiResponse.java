package com.scraping.training.news;

import java.util.List;

public class NewsApiResponse {

    private List<NewsArticle> articles;

    public List<NewsArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsArticle> articles) {
        this.articles = articles;
    }


    public static class NewsResponse {
        private List<NewsArticle> results;

        public List<NewsArticle> getResults() {
            return results;
        }

        public void setResults(List<NewsArticle> results) {
            this.results = results;
        }
    }
}

