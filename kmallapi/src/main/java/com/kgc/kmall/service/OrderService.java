package com.kgc.kmall.service;

import com.kgc.kmall.bean.OmsOrder;

/**
 * @author shkstart
 * @create 2021-01-17 12:19
 */
public interface OrderService {

    String genTradeCode(Long MemberId);

    String checkTradeCode(Long MemberId, String tradeCode);

    void saveOrder(OmsOrder omsOrder);

    OmsOrder getOrderByOutTradeNo(String outTradeNo);
    public void updateOrder(OmsOrder omsOrder);
}
