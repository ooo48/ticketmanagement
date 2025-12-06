package org.example.ticketmanagement.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import org.example.ticketmanagement.config.AlipayConfig;
import org.example.ticketmanagement.mapper.OrderMapper;
import org.example.ticketmanagement.mapper.UserMapper;
import org.example.ticketmanagement.pojo.*;
import org.example.ticketmanagement.service.PaymentService;
import org.example.ticketmanagement.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private OrderMapper orderMapper;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    @Override
    public Result createPayment(Integer orderId, Integer userId) throws AlipayApiException {
        Orders order = orderMapper.getOrderById(orderId);
        // 验证订单
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            return Result.error("无权操作此订单");
        }
        if (!"待支付".equals(order.getStatus())) {
            return Result.error("订单状态异常");
        }
        // 创建支付宝预下单请求
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();

        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setOutTradeNo(orderId.toString());
        model.setTotalAmount(order.getTotalPrice().toString());
        model.setSubject("演出门票 - 订单号: " + orderId);
        model.setBody("演出门票购买");

        request.setBizModel(model);
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        request.setReturnUrl(alipayConfig.getReturnUrl());
        // 调用支付宝接口
        AlipayTradePrecreateResponse response = alipayClient.execute(request);

        if (response.isSuccess()) {
            // 启动超时关单任务（30分钟）
            scheduleCloseOrder(orderId, 30);
            // 返回二维码链接
            Map<String, String> result = new HashMap<>();
            result.put("qrCode", response.getQrCode());
            result.put("outTradeNo", response.getOutTradeNo());
            return Result.success(result);
        }
        else {
            return Result.error("创建支付失败: " + response.getSubMsg());
        }
    }

    @Override
    public void handleAlipayCallback(Map<String, String> params) {
        // 验证签名
        String tradeStatus = params.get("trade_status");
        String outTradeNo = params.get("out_trade_no");
        String tradeNo = params.get("trade_no");

        if ("成功".equals(tradeStatus)) {
            // 支付成功
            Integer orderId = Integer.parseInt(outTradeNo);
            handlePaymentSuccess(orderId, tradeNo);
        }
        else if ("失败".equals(tradeStatus)) {
            // 交易关闭
            Integer orderId = Integer.parseInt(outTradeNo);
            orderMapper.updateOrderStatus(orderId, "已取消");
        }
    }

    @Override
    public Result compensateMissingOrders() {
        // 查找创建时间超过5分钟但状态仍是待支付的订单
        LocalDateTime checkTime = LocalDateTime.now().minusMinutes(5);
        List<Integer> pendingOrders = orderMapper.findPendingOrders(checkTime);

        for (Integer orderId : pendingOrders) {
            try {
                // 查询支付宝订单状态
                String alipayStatus = queryAlipayOrderStatus(orderId.toString());
                if ("成功".equals(alipayStatus)) {
                    // 支付宝已支付，但系统未收到回调，补偿处理
                    handlePaymentSuccess(orderId, "补偿订单" + orderId);
                    return Result.success();
                }
            } catch (Exception e) {
                return Result.error("补偿订单失败: " + orderId + ", error: " + e.getMessage());
            }
        }
        return Result.success();
    }

    @Override
    @Transactional
    public void handlePaymentSuccess(Integer orderId, String tradeNo) {
        // 更新订单状态
        orderMapper.updateOrderStatus(orderId, "已支付");
        orderMapper.updatePaymentStatus(orderId, "成功");
        // 发送邮件通知
        Orders order = orderMapper.getOrderById(orderId);
        Users user = userMapper.getCurrentUser(order.getUserId());
        // 获取演出详情
        OrderDetail detail = orderMapper.getOrderDetail(orderId, order.getUserId());
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            emailUtil.sendTicketSuccessEmail(
                    user.getEmail(),
                    detail.getShowName(),
                    detail.getSessionTime().toString(),
                    "共" + order.getQuantity() + "张",
                    orderId.toString()
            );
        }
    }

    private String queryAlipayOrderStatus(String outTradeNo) throws AlipayApiException {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(outTradeNo);
        request.setBizModel(model);

        AlipayTradeQueryResponse response = alipayClient.execute(request);

        if (response.isSuccess()) {
            return response.getTradeStatus();
        }
        return null;
    }

    private void scheduleCloseOrder(Integer orderId, int minutes) {
        scheduler.schedule(() -> {
            try {
                Orders order = orderMapper.getOrderById(orderId);
                if ("待支付".equals(order.getStatus())) {
                    // 查询支付宝订单状态
                    String alipayStatus = queryAlipayOrderStatus(orderId.toString());
                    if ("成功".equals(alipayStatus) || "失败".equals(alipayStatus)) {
                        // 支付宝已支付，处理支付成功
                        handlePaymentSuccess(orderId, "超时关单补偿");
                    }
                    else {
                        // 关闭订单
                        orderMapper.updateOrderStatus(orderId, "已取消");
                        // 恢复库存
                        ShowTickets ticket = orderMapper.getShowTicketById(order.getShowTicketId());
                        orderMapper.restoreStock(order.getShowTicketId(), order.getQuantity());
                    }
                }
            } catch (Exception e) {
                System.err.println("超时关单失败: " + e.getMessage());
            }
        }, minutes, TimeUnit.MINUTES);
    }
}
