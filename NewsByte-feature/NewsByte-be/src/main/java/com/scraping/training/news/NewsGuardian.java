package com.scraping.training.news;

public class NewsGuardian {
    private String webTitle;
    private String webUrl;
    private String  webPublicationDate;

    public NewsGuardian() {
    }
    public NewsGuardian(String webPublicationDate,String webTitle, String webUrl) {
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;

    }
    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public void setWebPublicationDate(String webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

}
