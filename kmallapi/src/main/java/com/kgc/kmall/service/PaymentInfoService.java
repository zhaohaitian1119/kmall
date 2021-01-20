package com.kgc.kmall.service;

import com.kgc.kmall.bean.PaymentInfo;

import java.util.Map;

/**
 * @author shkstart
 * @create 2021-01-18 17:04
 */
public interface PaymentInfoService {
    void savePaymentInfo(PaymentInfo paymentInfo);

    void updatePayment(PaymentInfo paymentInfo);

    void sendDelayPaymentResultCheckQueue(String outTradeNo, int i);

    Map<String,Object> checkAlipayPayment(String out_trade_no);
}
