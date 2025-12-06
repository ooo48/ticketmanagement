package org.example.ticketmanagement.controller;

import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.example.ticketmanagement.pojo.OrderDetail;
import org.example.ticketmanagement.pojo.Orders;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    //抢票创建订单
    @PostMapping
    public Result createOrder(HttpServletRequest request,
                              @RequestParam Integer showTicketId,
                              @RequestParam Integer quantity) {
        Integer userId = (Integer) request.getAttribute("userId");
        return orderService.createOrder(userId, showTicketId, quantity);
    }

    // 分页查询订单列表
    @GetMapping
    public Result getOrderList(HttpServletRequest request,
                               @RequestParam String status,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size) {
        Integer userId = (Integer) request.getAttribute("userId");
        List<Orders> orders = orderService.getOrderList(userId, status, page, size);
        return Result.success(orders);
    }

    // 获取订单详情
    @GetMapping("/{orderId}")
    public Result getOrderDetail(HttpServletRequest request,@PathVariable Integer orderId) {
        Integer userId = (Integer) request.getAttribute("userId");
        OrderDetail order = orderService.getOrderDetail(orderId, userId);
        if (order == null) {
            return Result.error("订单不存在或无权访问");
        }
        return Result.success(order);
    }

    // 取消订单
    @PutMapping("/{orderId}/cancel")
    public Result cancelOrder(HttpServletRequest request,@PathVariable Integer orderId) {
        Integer userId = (Integer) request.getAttribute("userId");
        return orderService.cancelOrder(orderId, userId);
    }
}
