package com.scraping.training.news;

import java.util.List;

public class NewsGuardianResponse {
    private NewsResponse response;

    public NewsResponse getResponse() {
        return response;
    }

    public void setResponse(NewsResponse response) {
        this.response = response;
    }

    public static class NewsResponse {
        private List<NewsGuardian> results;

        public List<NewsGuardian> getResults() {
            return results;
        }

        public void setResults(List<NewsGuardian> results) {
            this.results = results;
        }
    }
}
