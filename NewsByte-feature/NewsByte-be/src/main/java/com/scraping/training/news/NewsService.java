package com.scraping.training.news;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import org.springframework.scheduling.annotation.Async;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Service
public class NewsService {
    /*****************************************************************  API KEYS *************************************************************************************/
    @Value("${news.api.keyGuardian}")
    private String apikeyGuardian;

    @Value("${news.api.keyNyTimes}")
    private String apikeyNyTimes;

    @Value("${news.api.keyNation}")
    private String apikeyNation;

    /*****************************************************************************************************************************************************************/
    private final RestTemplate restTemplate;

    private static final int CONNECTION_TIMEOUT_MS = 5000; // 5 seconds
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.0.0 Safari/537.36";

    public NewsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /*****************************************************************  92 NEWS *************************************************************************************/
    public List<NewsArticle> getNews92(String url){
        List<NewsArticle> articles = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url)
                    .timeout(CONNECTION_TIMEOUT_MS)
                    .userAgent(USER_AGENT)
                    .get();
            Elements newsList = document.select(".sub-posts.post-item");

            for (Element news : newsList) {
                Element titleElement = news.select(".title").first();
                String title = titleElement != null ? titleElement.text() : "";

                Element linkElement = news.select("a.post_link").first();
                String postUrl = linkElement != null ? "https://92newshd.tv" + linkElement.attr("href") : "";

                String imageUrl = scrapeImageFromArticle(postUrl);

                Element dateElement = news.select(".published_time").first();
                String date = dateElement != null ? dateElement.text() : "";

                NewsArticle newsArticle = new NewsArticle();
                newsArticle.setTitle(title);
                newsArticle.setUrlToImage(imageUrl);
                newsArticle.setUrl(postUrl);
                newsArticle.setPublishedAt(date);
                articles.add(newsArticle);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return articles;
    }
    public List<NewsArticle> getNewsByCategory92(String baseUrl, int totalPages) {
        List<NewsArticle> scrapedData = new ArrayList<>();

        for (int page = 1; page <= totalPages; page++) {
            String pageUrl = baseUrl + "?pno=" + page;
            List<NewsArticle> pageData = getNews92(pageUrl);
            scrapedData.addAll(pageData);
        }

        return scrapedData;
    }
    @Async
    private String scrapeImageFromArticle(String articleUrl) {
        try {
            Document articleDocument = Jsoup.connect(articleUrl)
                    .timeout(CONNECTION_TIMEOUT_MS)
                    .userAgent(USER_AGENT)
                    .get();
            Element imageElement = articleDocument.select(".feature-image").first();
            if (imageElement != null) {

                return imageElement.attr("src");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "default-image-url.jpg";
    }
 /***************************************************************** GUARDIAN NEWS *************************************************************************************/
    public List<NewsArticle> getNewsGuardian() {
        int num=10;
        List<NewsArticle> headlines = new ArrayList<>();
        String apiUrl = "https://content.guardianapis.com/search?q=uk-news&section=uk-news&page-size="+ num + "&order-by=newest" +  "&api-key=" + apikeyGuardian ;
        try {
            NewsGuardianResponse response = restTemplate.getForObject(apiUrl,NewsGuardianResponse.class);
            System.out.println(apiUrl);
            if (response != null && response.getResponse() != null &&
                    response.getResponse().getResults() != null) {
                List<NewsGuardian> news = response.getResponse().getResults();

                for (NewsGuardian newsGuardian : news) {
                    NewsArticle newsArticle = new NewsArticle();
                    newsArticle.setTitle(newsGuardian.getWebTitle());
                    newsArticle.setUrl(newsGuardian.getWebUrl());
                    newsArticle.setPublishedAt(newsGuardian.getWebPublicationDate());
                    headlines.add(newsArticle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception for debugging
        }

        return headlines;
    }

    public List<NewsArticle> getNewsByCategoryGuardian(String category) {
        int num=30;
        List<NewsArticle> headlines = new ArrayList<>();
        String apiUrl = "https://content.guardianapis.com/search?q=" + category +
                "&section=" + category + "&page-size="+ num + "&order-by=newest" +  "&api-key=" + apikeyGuardian ;

        try {
            NewsGuardianResponse response = restTemplate.getForObject(apiUrl,NewsGuardianResponse.class);
            System.out.println(apiUrl);
            if (response != null && response.getResponse() != null &&
                    response.getResponse().getResults() != null) {
                List<NewsGuardian> news = response.getResponse().getResults();
                for (NewsGuardian newsGuardian : news) {
                    NewsArticle newsArticle = new NewsArticle();
                    newsArticle.setTitle(newsGuardian.getWebTitle());
                    newsArticle.setUrl(newsGuardian.getWebUrl());
                    newsArticle.setPublishedAt(newsGuardian.getWebPublicationDate());

                    headlines.add(newsArticle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception for debugging
        }

        return headlines;
    }

/***************************************************************** ARY NEWS *************************************************************************************/
    public List<NewsArticle> getNewsAry(String url, int clicks) {
        System.out.println(url);
    List<NewsArticle> articles = new ArrayList<>();
    System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

    try {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, 10);

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        driver.get(url);

        int loadMoreClicks = 0;

        while (loadMoreClicks < clicks) {
            jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(2000);

            WebElement loadMoreButton = driver.findElement(By.cssSelector(".td-load-more-wrap > a"));
            if (loadMoreButton != null && loadMoreButton.isDisplayed()) {
                loadMoreButton.click();
                wait.until(ExpectedConditions.invisibilityOf(loadMoreButton));
                loadMoreClicks++;
            } else {
                break;
            }
        }
        String pageSource = driver.getPageSource();
        Document document = Jsoup.parse(pageSource);
        Elements newsList = document.select(".td-module-container.td-category-pos-above");
        driver.quit();
        if (newsList != null) {
            for (Element news : newsList) {
                Element titleElement = news.select("h3 > a").first();
                if (titleElement == null || titleElement.text().isEmpty()) {
                    titleElement = news.select(".td-image-wrap ").first();
                }
                String title = (titleElement != null) ? titleElement.attr("title") : "No Title Found";
                String description = news.select(".td-excerpt").text();
                Element link = news.select("a").first();
                String hrefValue = link.attr("href");
                Element span = news.select("span.entry-thumb").first();
                String imageUrl = span.attr("data-img-url");
                if (imageUrl.isEmpty()) {
                    String styleAttribute = span.attr("style");
                    int startIndex = styleAttribute.indexOf("url(");
                    int endIndex = styleAttribute.indexOf(")");
                    if (startIndex != -1 && endIndex != -1) {
                        imageUrl = styleAttribute.substring(startIndex + 4, endIndex);
                    }
                }
                NewsArticle newsArticle = new NewsArticle();
                newsArticle.setUrl(hrefValue);
                newsArticle.setTitle(title);
                newsArticle.setUrlToImage(imageUrl);
                articles.add(newsArticle);
            }
        } else {
            System.out.println("Response is empty!!");
        }
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }

    return articles;
}
    public List<NewsArticle> getNewsByCategoryAry(String category) {
        String baseUrl = "https://arynews.tv/category/";
        String url = baseUrl + category + "/";
        return getNewsAry(url,4);
    }

    /***************************************************************** NEWYORK TIMES NEWS *************************************************************************************/
    public List<NewsArticle> getNewsNy() {
        String apiUrl = "https://api.nytimes.com/svc/mostpopular/v2/viewed/1.json" + "?api-key=" + apikeyNyTimes;
        System.out.println("service method called");
        NewsNYResponse response = restTemplate.getForObject(apiUrl, NewsNYResponse.class);
        System.out.println("gotten response");
        List<NewsArticle> headlines = new ArrayList<>();
        if (response != null && response.getArticles() != null) {
            for (NewsNyTimes article : response.getArticles()) {
                NewsNyTimes newsNy = getNyTimesNewsArticle(article);

                NewsArticle newsArticle = new NewsArticle();
                newsArticle.setUrl(newsNy.getPageUrl());
                newsArticle.setTitle(newsNy.getTitle());
                newsArticle.setUrlToImage(newsNy.getImage());
                newsArticle.setPublishedAt(newsNy.getPublished_date());

                headlines.add(newsArticle);
            }
        }

        return headlines;
    }

    public List<NewsArticle> getNewsByCategoryNY(String category) throws IOException {
        int page1 = 1;
        int page2=2;
        String apiUrl1 = "https://api.nytimes.com/svc/search/v2/articlesearch.json?fq=news_desk:(" +category +")&page="+page1+"&api-key="+ apikeyNyTimes;
        String apiUrl2 = "https://api.nytimes.com/svc/search/v2/articlesearch.json?fq=news_desk:(" +category +")&page="+page2+"&api-key="+ apikeyNyTimes;
        ObjectMapper objectMapper1 = new ObjectMapper();

        // Read data from the API URL
        JsonNode rootNode1 = null;
        try {
            rootNode1 = objectMapper1.readTree(new URL(apiUrl1));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

//        // Extract data from the "docs" array
        List<NYCategory> docs1 = new ArrayList<>();
        JsonNode docsArray1 = rootNode1.path("response").path("docs");
        docs1 = getNyTimesDocs(docsArray1, docs1);

        ObjectMapper objectMapper2 = new ObjectMapper();

        // Read data from the API URL
        JsonNode rootNode2 = null;
        try {
            rootNode2 = objectMapper2.readTree(new URL(apiUrl2));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        List<NYCategory> docs2 = new ArrayList<>();
        JsonNode docsArray = rootNode2.path("response").path("docs");
        docs2 = getNyTimesDocs(docsArray, docs2);

        List<NewsArticle> articles = new ArrayList<>();
        for (NYCategory docs: docs1) {

            NewsArticle newsArticle = new NewsArticle();
            newsArticle.setUrl(docs.getPageUrl());
            newsArticle.setTitle(docs.getTitle());
            newsArticle.setUrlToImage(docs.getImageUrl());
            newsArticle.setPublishedAt(docs.getPub_date());
            articles.add(newsArticle);
        }

        for (NYCategory docs: docs2) {

            NewsArticle newsArticle = new NewsArticle();
            newsArticle.setUrl(docs.getPageUrl());
            newsArticle.setTitle(docs.getTitle());
            newsArticle.setUrlToImage(docs.getImageUrl());
            newsArticle.setPublishedAt(docs.getPub_date());
            articles.add(newsArticle);

        }
        return articles;
    }
    private static NewsNyTimes getNyTimesNewsArticle(NewsNyTimes article) {
        List<String> images = new ArrayList<>();
        List<Media> media = article.getMedia();
        String img = "";
        for (Media mediaItem : media) {
            List<MediaMetadata> mediaMetadata = mediaItem.getMetadata();
            img = mediaMetadata.get(1).getUrl();
        }
        NewsNyTimes newsArticle = new NewsNyTimes(article.getTitle() , article.getPageUrl(), img, article.getPublished_date());
        return newsArticle;
    }
    public List<NYCategory> getNyTimesDocs(JsonNode docsArray, List<NYCategory> docs){
        for (JsonNode doc : docsArray) {
            // Check if "multimedia" is not empty
            if (doc.has("multimedia") && doc.get("multimedia").isArray()) {
                // Extract desired fields
                String abstractText = doc.path("abstract").asText();
                String webUrl = doc.path("web_url").asText();
                String headline = doc.path("headline").path("main").asText();
                JsonNode multimediaArray = doc.path("multimedia");
                String pub_date = doc.path("pub_date").asText();
                String multimediaUrl = "";
                // Iterate over multimedia items
                Iterator<JsonNode> multimediaItems = multimediaArray.elements();
                while (multimediaItems.hasNext()) {
                    JsonNode multimediaItem = multimediaItems.next();
                    multimediaUrl = "https://www.nytimes.com/"+multimediaItem.path("url").asText();

                }
                docs.add(new NYCategory(webUrl, headline, multimediaUrl, pub_date));
            }
        }
        return docs;
    }

    /***************************************************************** NATION NEWS *************************************************************************************/



//
//
//    public List<NewsArticle> getNationNewsHeadlines(String country, String language) throws InterruptedException {
////        &category=sports
////        country=pk&
//        String apiUrl = "https://newsdata.io/api/1/news?domainurl=nation.com.pk&category=top&apikey=" + nation_apiKey;
//        // Make an HTTP GET request to the News API
//        NewsApiResponse response = restTemplate.getForObject(apiUrl, NewsApiResponse.class);
//        String nextPage = response.getNextPage();
//        List<NewsArticle> headlines = new ArrayList<>();
//        if (response != null && response.getArticles() != null) {
//            for (NewsArticle article : response.getArticles()) {
//                NewsArticle newsArticle = new NewsArticle(article.getTitle(), article.getLink(), article.getImage_url(), article.getPublishedDate());
//                headlines.add(newsArticle);
//            }
//        }
//        if(nextPage==null){
//            return headlines;
//        }
//        else {
//            apiUrl = "https://newsdata.io/api/1/news?domainurl=nation.com.pk&apikey=" + nation_apiKey + "&page=" + nextPage;
//            try {
//                response = restTemplate.getForObject(apiUrl, NewsApiResponse.class);
//                System.out.println("in try");
//                if (response != null && response.getArticles() != null) {
//                    for (NewsArticle article : response.getArticles()) {
//                        NewsArticle newsArticle = new NewsArticle(article.getTitle(), article.getLink(), article.getImage_url(), article.getPublishedDate());
//                        headlines.add(newsArticle);
//                    }
//                }
//            } catch (HttpClientErrorException.TooManyRequests e) {
//                // Handle rate limit exceeded error
//                // Extract rate limit headers from e.getResponseHeaders() and wait accordingly
//                int retryAfterSeconds = 10;
//                Thread.sleep(retryAfterSeconds * 1000);
//                System.out.println("in catch");
//
//                response = restTemplate.getForObject(apiUrl, NewsApiResponse.class);
//                if (response != null && response.getArticles() != null) {
//                    for (NewsArticle article : response.getArticles()) {
//                        NewsArticle newsArticle = new NewsArticle(article.getTitle(), article.getLink(), article.getImage_url(), article.getPublishedDate());
//                        headlines.add(newsArticle);
//                    }
//                }
//            }
//        }
//
//        return headlines;
//    }

}
