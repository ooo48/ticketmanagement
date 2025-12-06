package org.example.ticketmanagement.mapper;

import org.apache.ibatis.annotations.*;
import org.example.ticketmanagement.pojo.OrderDetail;
import org.example.ticketmanagement.pojo.Orders;
import org.example.ticketmanagement.pojo.ShowTickets;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO orders(user_id, show_ticket_id, quantity, total_price, status) " +
            "VALUES(#{userId}, #{showTicketId}, #{quantity}, #{totalPrice}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createOrder(Orders order);

    @Select("SELECT * FROM show_tickets WHERE id = #{ticketId}")
    ShowTickets getShowTicketById(Integer ticketId);

    @Update("UPDATE show_tickets SET stock = stock - #{quantity} WHERE id = #{ticketId}")
    void deductStock(Integer ticketId,Integer quantity);

    // 获取订单列表，条件查询
    @Select("select * from orders where user_id=#{userId} AND status=#{status} ORDER BY id DESC LIMIT #{index}, #{size}")
    List<Orders> getOrderList(Integer userId, String status,int index,int size);

    // 获取订单详情
    @Select("SELECT o.*, " +
            "st.price as ticket_price, " +
            "sh.name as show_name, " +
            "ss.date as date, " +
            "ss.time as session_time, " +
            "p.payment_date, " +
            "p.callback_data " +
            "FROM orders o " +
            "LEFT JOIN show_tickets st ON o.show_ticket_id = st.id " +
            "LEFT JOIN show_sessions ss ON st.session_id = ss.id " +
            "LEFT JOIN shows sh ON ss.show_id = sh.id " +
            "LEFT JOIN payments p ON o.id = p.order_id " +
            "WHERE o.id = #{orderId} AND o.user_id = #{userId}")
    OrderDetail getOrderDetail(Integer orderId,Integer userId);


    @Select("SELECT * FROM orders WHERE id = #{orderId}")
    Orders getOrderById(Integer orderId);

    @Update("UPDATE orders SET status = #{status} WHERE id = #{orderId}")
    void updateOrderStatus(Integer orderId,String status);

    @Update("UPDATE show_tickets SET stock = stock + #{quantity} WHERE id = #{ticketId}")
    void restoreStock(Integer ticketId,Integer quantity);

    @Update("UPDATE payments SET status = #{status} WHERE id = #{orderId}")
    void updatePaymentStatus(Integer orderId,String status);

    // 查找待处理的订单（用于掉单补偿）
    @Select("SELECT id FROM orders WHERE status = '待支付'")
    List<Integer> findPendingOrders(@Param("checkTime") LocalDateTime checkTime);

    // 获取订单列表，条件查询
    @Select("select * from orders where status=#{status} ORDER BY id DESC LIMIT #{index}, #{size}")
    List<Orders> getAdminOrderList(String status,int index,int size);

    // 获取订单详情
    @Select("SELECT o.*, " +
            "st.price as ticket_price, " +
            "sh.name as show_name, " +
            "ss.date as date, " +
            "ss.time as session_time, " +
            "p.payment_date, " +
            "p.callback_data " +
            "FROM orders o " +
            "LEFT JOIN show_tickets st ON o.show_ticket_id = st.id " +
            "LEFT JOIN show_sessions ss ON st.session_id = ss.id " +
            "LEFT JOIN shows sh ON ss.show_id = sh.id " +
            "LEFT JOIN payments p ON o.id = p.order_id " +
            "WHERE o.id = #{orderId}")
    OrderDetail getAdminOrderDetail(Integer orderId);
}
