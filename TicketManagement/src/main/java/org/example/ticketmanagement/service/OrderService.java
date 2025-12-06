package org.example.ticketmanagement.service;

import org.example.ticketmanagement.pojo.OrderDetail;
import org.example.ticketmanagement.pojo.Orders;
import org.example.ticketmanagement.pojo.Result;

import java.util.List;

public interface OrderService {
    Result createOrder(Integer userId, Integer showTicketId, Integer quantity);
    List<Orders> getOrderList(Integer userId, String status, int page, int size);
    OrderDetail getOrderDetail(Integer orderId, Integer userId);
    Result cancelOrder(Integer orderId, Integer userId);
    List<Orders> getAdminOrderList( String status, int page, int size);
    OrderDetail getAdminOrderDetail(Integer orderId);
    void updateOrderStatus(Integer orderId,String status);
}
