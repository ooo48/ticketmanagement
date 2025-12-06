package org.example.ticketmanagement.controller;

import com.alipay.api.AlipayApiException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    // 创建支付订单
    @PostMapping("/{orderId}/create")
    public Result createPayment(HttpServletRequest request,@PathVariable Integer orderId) throws AlipayApiException {
        Integer userId = (Integer) request.getAttribute("userId");
        return paymentService.createPayment(orderId, userId);
    }

    // 支付宝回调接口
    @PostMapping("/alipay/callback")
    public Result alipayCallback(@RequestParam Map<String, String> params) {
        paymentService.handleAlipayCallback(params);
        return Result.success();
    }

    // 掉单补偿
    @PostMapping("/compensate")
    public Result compensateMissingOrders() {
        return paymentService.compensateMissingOrders();
    }
}
