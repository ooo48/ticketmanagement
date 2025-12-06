package org.example.ticketmanagement.controller;

import org.example.ticketmanagement.pojo.OrderDetail;
import org.example.ticketmanagement.pojo.Orders;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {
    @Autowired
    private OrderService orderService;

    // 分页查询订单列表
    @GetMapping
    public Result getOrderList(@RequestParam String status,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size) {
        List<Orders> orders = orderService.getAdminOrderList(status, page, size);
        return Result.success(orders);
    }

    // 获取订单详情
    @GetMapping("/{orderId}")
    public Result getOrderDetail(@PathVariable Integer orderId) {
        OrderDetail order = orderService.getAdminOrderDetail(orderId);
        return Result.success(order);
    }

    //修改订单信息，更新订单状态
    @PutMapping("/{orderId}/status")
    public Result updateOrderStatus(@PathVariable Integer orderId, @RequestParam String status) {
        if (!"待支付".equals(status) && !"已支付".equals(status) && !"已取消".equals(status)){
            return Result.error("订单状态不正确");
        }
        orderService.updateOrderStatus(orderId, status);
        return Result.success();
    }
}
