package com.kgc.kmall.kmallorderweb.controller;

import com.kgc.kmall.annotations.LoginRequired;
import com.kgc.kmall.bean.MemberReceiveAddress;
import com.kgc.kmall.bean.OmsCartItem;
import com.kgc.kmall.bean.OmsOrder;
import com.kgc.kmall.bean.OmsOrderItem;
import com.kgc.kmall.service.CartService;
import com.kgc.kmall.service.MemberService;
import com.kgc.kmall.service.OrderService;
import com.kgc.kmall.service.SkuService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author shkstart
 * @create 2021-01-15 15:11
 */
@CrossOrigin
@Controller
public class OrderController {

    @Reference
    MemberService memberService;
    @Reference
    CartService cartService;
    @Reference
    OrderService orderService;
    @Reference
    SkuService skuService;

    @RequestMapping("/toTrade")
    @LoginRequired(value = true)
    public String toTrade(HttpServletRequest request, Model model) {
        //从拦截器中获取用户memberid和nickname
        Integer memberId = (Integer) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");

        List<MemberReceiveAddress> umsMemberReceiveAddresses = memberService.getReceiveAddressByMemberId(Long.valueOf(memberId));
        model.addAttribute("userAddressList", umsMemberReceiveAddresses);

        List<OmsCartItem> omsCartItems = cartService.cartList(memberId.toString());
        List<OmsOrderItem> omsOrderItems = new ArrayList<>();
        for (OmsCartItem omsCartItem : omsCartItems) {
            // 每循环一个购物车对象，就封装一个商品的详情到OmsOrderItem
            System.out.println(omsCartItem.getIsChecked());
            if (omsCartItem.getIsChecked() == 1) {
                OmsOrderItem omsOrderItem = new OmsOrderItem();
                omsOrderItem.setProductName(omsCartItem.getProductName());
                omsOrderItem.setProductPic(omsCartItem.getProductPic());
                omsOrderItems.add(omsOrderItem);
            }
        }


        model.addAttribute("omsOrderItems", omsOrderItems);
        model.addAttribute("totalAmount", getTotalAmount(omsCartItems));

        String tradeCode = orderService.genTradeCode(Long.valueOf(memberId));
        System.out.println(tradeCode);
        model.addAttribute("tradeCode", tradeCode);


        return "trade";
    }

    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems) {
        BigDecimal totalAmount = new BigDecimal("0");

        for (OmsCartItem omsCartItem : omsCartItems) {
            omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(new BigDecimal(omsCartItem.getQuantity())));
            BigDecimal totalPrice = omsCartItem.getTotalPrice();

            if (omsCartItem.getIsChecked() == 1) {
                totalAmount = totalAmount.add(totalPrice);
            }
        }

        return totalAmount;
    }

    @RequestMapping("submitOrder")
    @LoginRequired(value = true)
    public String submitOrder(String receiveAddressId, BigDecimal totalAmount, String tradeCode, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model){
        //从拦截器中获取用户memberid和nickname
        Integer memberId = (Integer) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");
        String outTradeNo = "kmall";
        String success = orderService.checkTradeCode(Long.valueOf(memberId), tradeCode);
        if (success.equals("success")) {
            System.out.println("提交订单");
            System.out.println(receiveAddressId);
            System.out.println(totalAmount);

            List<OmsOrderItem> omsOrderItems = new ArrayList<>();
            // 订单对象
            OmsOrder omsOrder = new OmsOrder();
            //自动确认时间（天）
            omsOrder.setAutoConfirmDay(7);
            //提交时间
            omsOrder.setCreateTime(new Date());
            //管理员后台调整订单使用的折扣金额
            omsOrder.setDiscountAmount(null);
            //omsOrder.setFreightAmount(); 运费，支付后，在生成物流信息时
            //用户编号
            omsOrder.setMemberId(Long.valueOf(memberId));
            //用户昵称
            omsOrder.setMemberUsername(nickname);
            //订单备注
            omsOrder.setNote("快点发货");
            //订单前缀

            // 将毫秒时间戳拼接到外部订单号
            outTradeNo = outTradeNo + System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHmmss");
            // 将时间字符串拼接到外部订单号
            outTradeNo = outTradeNo + sdf.format(new Date());
            //订单编号
            omsOrder.setOrderSn(outTradeNo);
            //支付金额(实际金额)
            omsOrder.setPayAmount(totalAmount);
            //订单类型：0->正常订单；1->秒杀订单
            omsOrder.setOrderType(1);
            //城市
            MemberReceiveAddress umsMemberReceiveAddress = memberService.getReceiveAddressById(Long.parseLong(receiveAddressId));
            omsOrder.setReceiverCity(umsMemberReceiveAddress.getCity());
            //详细地址
            omsOrder.setReceiverDetailAddress(umsMemberReceiveAddress.getDetailAddress());
            //收货人姓名
            omsOrder.setReceiverName(umsMemberReceiveAddress.getName());
            //收获人手机号
            omsOrder.setReceiverPhone(umsMemberReceiveAddress.getPhoneNumber());
            //收货人邮编
            omsOrder.setReceiverPostCode(umsMemberReceiveAddress.getPostCode());
            //省份/直辖市
            omsOrder.setReceiverProvince(umsMemberReceiveAddress.getProvince());
            //区
            omsOrder.setReceiverRegion(umsMemberReceiveAddress.getRegion());
            // 当前日期加一天，一天后配送
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE,1);
            Date time = c.getTime();
            //确认收货时间
            omsOrder.setReceiveTime(time);
            //订单来源：0->PC订单；1->app订单
            omsOrder.setSourceType(0);
            //订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
            omsOrder.setStatus(0);
            //订单类型：0->正常订单；1->秒杀订单
            omsOrder.setOrderType(0);
            //订单总金额
            omsOrder.setTotalAmount(totalAmount);
            // 根据用户id获得要购买的商品列表(购物车)，和总价格
            List<OmsCartItem> omsCartItems = cartService.cartList(memberId.toString());
            for (OmsCartItem omsCartItem : omsCartItems) {
                if(omsCartItem.getIsChecked() == 1){
                    // 获得订单详情列表
                    OmsOrderItem omsOrderItem = new OmsOrderItem();
                    // 检价加个
                    boolean b = skuService.checkPrice(omsCartItem.getProductSkuId(),omsCartItem.getPrice());
                    if (b == false) {
                        return "tradeFail";
                    }
                    // 验库存,远程调用库存系统
                    omsOrderItem.setProductPic(omsCartItem.getProductPic());
                    omsOrderItem.setProductName(omsCartItem.getProductName());
                    // 外部订单号，用来和其他系统进行交互，防止重复
                    omsOrderItem.setOrderSn(outTradeNo);
                    //商品分类id
                    omsOrderItem.setProductCategoryId(omsCartItem.getProductCategoryId());
                    //销售价格
                    omsOrderItem.setProductPrice(omsCartItem.getPrice());
                    //该商品经过优惠后的分解金额
                    omsOrderItem.setRealAmount(omsCartItem.getTotalPrice());
                    //购买数量
                    omsOrderItem.setProductQuantity(omsCartItem.getQuantity());
                    //商品sku条码
                    omsOrderItem.setProductSkuCode("111111111111");
                    //商品sku编号
                    omsOrderItem.setProductSkuId(omsCartItem.getProductSkuId());
                    omsOrderItem.setProductId(omsCartItem.getProductId());
                    // 在仓库中的skuId
                    omsOrderItem.setProductSn("仓库对应的商品编号");

                    omsOrderItems.add(omsOrderItem);
                }
            }

            omsOrder.setOrderItems(omsOrderItems);
            // 将订单和订单详情写入数据库
            // 删除购物车的对应商品,暂时不进行删除，因为接下来需要频繁的测试
            orderService.saveOrder(omsOrder);

        }else{
            model.addAttribute("errMsg","获取用户订单信息失败");
            return "tradeFail";
        }
        return "redirect:http://payment.kmall.com:8088/index?outTradeNo="+outTradeNo+"&totalAmount="+totalAmount;
    }

}
