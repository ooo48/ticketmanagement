package org.example.ticketmanagement.controller;

import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.pojo.Shows;
import org.example.ticketmanagement.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shows")
public class ShowController {
    @Autowired
    private ShowService showService;

    // 首页获取地区与分类演出列表
    @GetMapping("/home")
    public Result getShowsForHomepage(@RequestParam(required = false) String city) {
        List<Shows> shows=showService.getShowsForHomepage(city);
        return Result.success(shows);
    }

    // 搜索演出
    @GetMapping("/search")
    public Result searchShows(@RequestParam String keyword) {
        List<Shows> show=showService.searchShows(keyword);
        return Result.success(show);
    }

    // 条件查询演出
    @GetMapping
    public Result getShows(@RequestParam(required = false) String city,
                                      @RequestParam(required = false) String category,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        List<Shows> shows=showService.getShows(city, category, page, size);
        return Result.success(shows);
    }

    // 获取演出详情
    @GetMapping("/{id}")
    public Result getShowDetails(@PathVariable int id) {
        Shows show=showService.getShowDetails(id);
        return Result.success(show);
    }
}
