package com.kgc.kmall.kmallorderservice.service;

import com.kgc.kmall.bean.OmsOrder;
import com.kgc.kmall.bean.OmsOrderExample;
import com.kgc.kmall.bean.OmsOrderItem;
import com.kgc.kmall.kmallorderservice.mapper.OmsOrderItemMapper;
import com.kgc.kmall.kmallorderservice.mapper.OmsOrderMapper;
import com.kgc.kmall.service.OrderService;
import com.kgc.kmall.utils.RedisUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @author shkstart
 * @create 2021-01-17 12:20
 */
@Component
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    RedisUtils redisUtil;
    @Resource
    OmsOrderMapper omsOrderMapper;
    @Resource
    OmsOrderItemMapper omsOrderItemMapper;

    @Override
    public String genTradeCode(Long MemberId) {
        Jedis jedis = redisUtil.getJedis();

        String tradeKey  =  "user:"+MemberId+":tradeCode";

        String tradeCode = UUID.randomUUID().toString();

        jedis.setex(tradeKey, 60 * 60 * 2, tradeCode);

        jedis.close();

        return tradeCode;
    }

    @Override
    public String checkTradeCode(Long MemberId, String tradeCode) {
        Jedis jedis = redisUtil.getJedis();

        String tradeKey  =  "user:"+MemberId+":tradeCode";

        String s = jedis.get(tradeKey);

        jedis.close();

        if(s!=null&&s.equals(tradeCode)){
            return "success";
        }else{
            return "fail";
        }

    }

    @Override
    public void saveOrder(OmsOrder omsOrder) {
        // 保存订单表
        omsOrderMapper.insertSelective(omsOrder);
        Long orderId = omsOrder.getId();
        // 保存订单详情
        List<OmsOrderItem> omsOrderItems = omsOrder.getOrderItems();
        for (OmsOrderItem orderItem : omsOrderItems) {
            orderItem.setOrderId(orderId);
            omsOrderItemMapper.insertSelective(orderItem);
            // 删除购物车数据,暂时不进行购物车删除，因为需要频繁的测试
            // cartService.delCart();
        }
    }

    @Override
    public OmsOrder getOrderByOutTradeNo(String outTradeNo) {
        OmsOrderExample example = new OmsOrderExample();
        OmsOrderExample.Criteria criteria = example.createCriteria();
        criteria.andOrderSnEqualTo(outTradeNo);
        List<OmsOrder> omsOrders = omsOrderMapper.selectByExample(example);
        if (omsOrders!=null&&omsOrders.size()>0) {
            return omsOrders.get(0);
        }else {
            return null;
        }
    }

    @Override
    public void updateOrder(OmsOrder omsOrder) {
        OmsOrderExample example = new OmsOrderExample();
        OmsOrderExample.Criteria criteria = example.createCriteria();
        criteria.andOrderSnEqualTo(omsOrder.getOrderSn());

        omsOrderMapper.updateByExampleSelective(omsOrder,example);
    }
}
