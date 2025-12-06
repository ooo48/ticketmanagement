package org.example.ticketmanagement.service;

import com.alipay.api.AlipayApiException;
import org.example.ticketmanagement.pojo.Result;

import java.util.Map;

public interface PaymentService {
    Result createPayment(Integer orderId, Integer userId) throws AlipayApiException;
    void handleAlipayCallback(Map<String, String> params);
    Result compensateMissingOrders();
    void handlePaymentSuccess(Integer orderId, String tradeNo);
}
