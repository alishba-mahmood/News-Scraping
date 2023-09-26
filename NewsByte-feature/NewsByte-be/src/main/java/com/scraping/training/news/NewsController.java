package com.scraping.training.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RequestMapping("/news")
@RestController
@EnableAsync
@CrossOrigin
public class NewsController {

    @Autowired
    private NewsService newsService;

    /***************************************************************************************************************************************/
    @GetMapping("/92")
    public List<NewsArticle> getNews92() {
        String url = "https://92newshd.tv/latest-news";
        return newsService.getNews92(url);
    }

    @GetMapping("/92/{category}")
    public List<NewsArticle> getNewsByCategory92(@PathVariable String category) throws IOException {
        String baseUrl = "https://92newshd.tv/category/";
        String url = baseUrl + category + "/";
        return newsService.getNewsByCategory92(url, 5);
    }
    /***************************************************************************************************************************************/

    @GetMapping("/guardian")
    public List<NewsArticle> getNewsGuardian() {
        return newsService.getNewsGuardian();
    }

    @GetMapping("/guardian/{category}")
    public List<NewsArticle> getNewsGuardianByCategory(@PathVariable String category) throws IOException{
        return newsService.getNewsGuardianByCategory(category);
    }


    /***************************************************************************************************************************************/

    @GetMapping("/nyTimes")
    public List<NewsArticle> getNewsNY() {
        return newsService.getNewsNyTimes();
    }
    @GetMapping("/nyTimes/{category}")
    @Async
    public List<Docs> getNYNews(@PathVariable String category) throws IOException { //@PathVariable int page
        return newsService.getNyTimesNews(category);
    }
}

