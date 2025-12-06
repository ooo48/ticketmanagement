package org.example.ticketmanagement.controller;

import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.pojo.ShowDetail;
import org.example.ticketmanagement.pojo.Shows;
import org.example.ticketmanagement.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/show")
public class AdminShowController {
    @Autowired
    private ShowService showService;

    //添加演出信息
    @PostMapping
    public Result addShow(@RequestBody Shows shows){
        showService.addShow(shows);
        return Result.success();
    }
    //修改演出信息
    @PutMapping("{showId}")
    public Result updateShow(@PathVariable Integer showId, @RequestBody Shows show) {
        show.setId(showId);
        showService.updateShow(show);
        return Result.success();
    }

    // 分页查询演出
    @GetMapping
    public Result getShows(@RequestParam String city,
                           @RequestParam String category,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int size) {
        List<Shows> shows=showService.getShows(city, category, page, size);
        return Result.success(shows);
    }

    // 获取演出详情
    @GetMapping("/{id}")
    public Result getShowDetails(@PathVariable int id) {
        List<ShowDetail> show=showService.getAdminShowDetails(id);
        return Result.success(show);
    }

    //删除演出信息
    @DeleteMapping("/{showId}")
    public Result deleteShow(@PathVariable Integer showId) {
        showService.deleteShow(showId);
        return Result.success();
    }
}
