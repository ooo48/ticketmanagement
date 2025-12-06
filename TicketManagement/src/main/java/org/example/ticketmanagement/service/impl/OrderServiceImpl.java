package org.example.ticketmanagement.service.impl;

import org.example.ticketmanagement.mapper.OrderMapper;
import org.example.ticketmanagement.pojo.*;
import org.example.ticketmanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Transactional
    public Result createOrder(Integer userId, Integer showTicketId, Integer quantity) {
        //检查库存
        ShowTickets ticket = orderMapper.getShowTicketById(showTicketId);
        if (ticket == null) {
            return Result.error("票档不存在");
        }
        if (ticket.getStock() < quantity) {
            return Result.error("库存不足");
        }
        if (ticket.getIsOpen() == 0) {
            return Result.error("票档未开放销售");
        }
        //计算总价
        Integer totalPrice = ticket.getPrice() * quantity;
        //创建订单
        Orders order = new Orders();
        order.setUserId(userId);
        order.setShowTicketId(showTicketId);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);
        order.setStatus("待支付");

        orderMapper.createOrder(order);
        orderMapper.deductStock(showTicketId, quantity);
        return Result.success();
    }

    @Override
    public List<Orders> getOrderList(Integer userId, String status, int page, int size) {
        int index=(page-1)*size;
        List<Orders> orders = orderMapper.getOrderList(userId, status,index,size);
        return orders;
    }

    @Override
    public OrderDetail getOrderDetail(Integer orderId, Integer userId) {
        return orderMapper.getOrderDetail(orderId, userId);
    }

    @Override
    @Transactional
    public Result cancelOrder(Integer orderId, Integer userId) {
        Orders order = orderMapper.getOrderById(orderId);
        // 验证订单所有者
        if (!order.getUserId().equals(userId)) {
            return Result.error("无权操作此订单");
        }
        // 更新订单状态
        orderMapper.updateOrderStatus(orderId, "已取消");
        // 恢复库存
        orderMapper.restoreStock(order.getShowTicketId(), order.getQuantity());
        return Result.success();
    }

    @Override
    public List<Orders> getAdminOrderList( String status, int page, int size){
        int index=(page-1)*size;
        List<Orders> orders = orderMapper.getAdminOrderList(status,index,size);
        return orders;
    }

    @Override
    public OrderDetail getAdminOrderDetail(Integer orderId){
        return orderMapper.getAdminOrderDetail(orderId);
    }

    @Override
    public void updateOrderStatus(Integer orderId,String status){
        orderMapper.updateOrderStatus(orderId, status);
    }

}
