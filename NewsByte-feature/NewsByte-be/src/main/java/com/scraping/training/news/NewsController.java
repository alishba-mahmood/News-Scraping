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
        System.out.println(url);
        return newsService.getNews92(url);
    }

    @GetMapping("/92/{category}")
    public List<NewsArticle> getNewsByCategory92(@PathVariable String category) throws IOException {
        String baseUrl = "https://92newshd.tv/category/";
        String url = baseUrl + category + "/";
        System.out.println(url);
        return newsService.getNewsByCategory92(url, 4);
    }
    /***************************************************************************************************************************************/

    @GetMapping("/guardian")
    public List<NewsArticle> getNewsGuardian() {
        return newsService.getNewsGuardian();
    }

    @GetMapping("/guardian/{category}")
    public List<NewsArticle> getNewsByCategoryGuardian(@PathVariable String category) throws IOException{
        return newsService.getNewsByCategoryGuardian(category);
    }


    /***************************************************************************************************************************************/

    @GetMapping("/ary")
    public List<NewsArticle> getNewsAry() {
        String url = "https://arynews.tv/category/latest-blogs";
        return newsService.getNewsAry(url,4);
    }

    @GetMapping("/ary/{category}")
    public List<NewsArticle> getNewsByCategoryAry(@PathVariable String category) throws IOException {
        return newsService.getNewsByCategoryAry(category);
    }

    /***************************************************************************************************************************************/

    @GetMapping("/nyTimes")
    public List<NewsArticle> getNewsNY() {
        return newsService.getNewsNy();
    }
    @GetMapping("/nyTimes/{category}")
    public List<NewsArticle> getNewsByCategoryNY(@PathVariable String category) throws IOException { //@PathVariable int page
        return newsService.getNewsByCategoryNY(category);
    }
//
//   @GetMapping("/nation")
//   public List<NewsArticle> getNewsNation() {
//       return newsService.getNewsNation();
//   }
//    @GetMapping("/nation/{category}")
//    @Async
//    public List<NewsArticle> getNewsByCategoryNation(@PathVariable String category) throws IOException, InterruptedException {
//        return newsService.getNewsByCategoryNation(category);
//    }
}

