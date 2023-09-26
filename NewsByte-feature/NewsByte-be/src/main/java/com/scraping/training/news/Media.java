package com.scraping.training.news;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Media {
    @JsonProperty("url")
    private String url;

    public String getUrl(){return url;}
    public void setUrl(String url){this.url = url;}
    @JsonProperty("media-metadata")
    private List<MediaMetadata> metadata;

    public void setMetadata(List<MediaMetadata> metadata) {
        this.metadata = metadata;
    }

    public List<MediaMetadata> getMetadata() {
        return metadata;
    }
}
