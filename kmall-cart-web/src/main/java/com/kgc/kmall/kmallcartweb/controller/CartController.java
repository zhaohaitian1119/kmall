package com.kgc.kmall.kmallcartweb.controller;

import ch.qos.logback.classic.turbo.TurboFilter;
import com.alibaba.fastjson.JSON;
import com.kgc.kmall.annotations.LoginRequired;
import com.kgc.kmall.bean.OmsCartItem;
import com.kgc.kmall.bean.PmsSkuInfo;
import com.kgc.kmall.service.CartService;
import com.kgc.kmall.service.SkuService;
import com.kgc.kmall.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author shkstart
 * @create 2021-01-08 14:19
 */
@CrossOrigin
@Controller
public class CartController {

    @Reference
    SkuService skuService;
    @Reference
    CartService cartService;

    @LoginRequired(false)
    @RequestMapping("/addToCart")
    public String addToCart(Long skuId, int num, HttpServletRequest request, HttpServletResponse response) {
        List<OmsCartItem> omsCartItemList = new ArrayList<>();
        PmsSkuInfo pmsSkuInfo = skuService.seltBySkuIds(skuId);
        //将商品信息封装成购物车信息
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(new BigDecimal(pmsSkuInfo.getPrice()));
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(pmsSkuInfo.getCatalog3Id());
        omsCartItem.setProductId(pmsSkuInfo.getSpuId());
        omsCartItem.setProductName(pmsSkuInfo.getSkuName());
        omsCartItem.setProductPic(pmsSkuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuCode("11111111111");
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setQuantity(num);

        //判断用户是否登录
        String memberId = "";
        if(request.getAttribute("memberId")!=null){
            memberId=request.getAttribute("memberId").toString();
        }

        if (StringUtils.isBlank(memberId)) {
            // cookie里原有的购物车数据
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isBlank(cartListCookie)) {
                //cookie为空
                omsCartItemList.add(omsCartItem);
            } else {
                //cookie不为空
                omsCartItemList = JSON.parseArray(cartListCookie, OmsCartItem.class);
                // 判断添加的购物车数据在cookie中是否存在
                boolean b = if_cart_exist(omsCartItemList, omsCartItem);
                if (b) {
                    // 之前添加过，更新购物车添加数量
                    for (OmsCartItem cartItem : omsCartItemList) {
                        if (cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())) {
                            cartItem.setQuantity(cartItem.getQuantity() + omsCartItem.getQuantity());
                            break;
                        }
                    }
                } else {
                    omsCartItemList.add(omsCartItem);
                }
                CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItemList), 60 * 60 * 72, true);

            }
        } else {
            OmsCartItem omsCartItem1 = cartService.ifCartExistByUser(memberId, skuId);
            if (omsCartItem1 == null) {
                omsCartItem.setMemberId((Long.parseLong(memberId)));
                omsCartItem.setMemberNickname("test小名");
                cartService.addCart(omsCartItem);
            } else {
                // 该用户添加过当前商品
                Integer quantity = omsCartItem1.getQuantity();
                quantity = quantity + num;
                omsCartItem1.setQuantity(quantity);
                cartService.updateCart(omsCartItem1);
            }

            // 同步缓存
            cartService.flushCartCache(memberId);
        }


        return "redirect:/success.html";
    }
    @LoginRequired(false)
    @RequestMapping("/cartList")
    public String cartList(ModelMap modelMap, HttpServletRequest request) {
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        String memberId = "";
        if(request.getAttribute("memberId")!=null){
            memberId=request.getAttribute("memberId").toString();
        }
        System.out.println("memberId:"+memberId);
        if (StringUtils.isNotBlank(memberId)) {
            // 已经登录查询db
            omsCartItems = cartService.cartList(memberId);
        } else {
            // 没有登录查询cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isNotBlank(cartListCookie)) {
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
            }
        }

//        //计算小计
//        for (OmsCartItem omsCartItem : omsCartItems) {
//            BigDecimal multiply = omsCartItem.getPrice().multiply(new BigDecimal(omsCartItem.getQuantity()));
//            omsCartItem.setTotalPrice(multiply);
//        }

        modelMap.put("cartList", omsCartItems);

        //计算总价
        BigDecimal totalAmount = getTotalAmount(omsCartItems);
        modelMap.put("totalAmount", totalAmount);
        System.out.println("总价"+totalAmount);
        return "cartList";
    }


    private boolean if_cart_exist(List<OmsCartItem> omsCartItems, OmsCartItem omsCartItem) {

        boolean b = false;

        for (OmsCartItem cartItem : omsCartItems) {
            Long productSkuId = cartItem.getProductSkuId();
            if (productSkuId.equals(omsCartItem.getProductSkuId())) {
                b = true;
                break;
            }
        }

        return b;
    }

    @LoginRequired(false)
    @RequestMapping("/checkCart")
    @ResponseBody
    public Map<String, Object> checkCart(Integer isChecked, Long skuId, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        String memberId = "";
        if(request.getAttribute("memberId").toString()!=null){

            memberId= request.getAttribute("memberId").toString();
        }
        System.out.println("是否选中"+isChecked);
        if (StringUtils.isNotBlank(memberId)) {
            //用户以登录
            OmsCartItem omsCartItem = new OmsCartItem();
            omsCartItem.setProductSkuId(skuId);
            omsCartItem.setMemberId(new Long(memberId));
            omsCartItem.setIsChecked(isChecked);
            cartService.checkCart(omsCartItem);
            //计算总价
            List<OmsCartItem> omsCartItems = cartService.cartList(memberId);
            BigDecimal totalAmount = getTotalAmount(omsCartItems);
            map.put("totalAmount", totalAmount);
            System.out.println("总价"+totalAmount);
        } else {
            //没有登录 查询cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            List<OmsCartItem> omsCartItemList = JSON.parseArray(cartListCookie, OmsCartItem.class);
            for (OmsCartItem omsCartItem : omsCartItemList) {
                if (omsCartItem.getProductSkuId() == skuId) {
                    omsCartItem.setIsChecked(isChecked);
                    break;
                }
            }

            CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItemList), 60 * 60 * 72, true);
            //计算总价
            List<OmsCartItem> omsCartItems = cartService.cartList(memberId);
            BigDecimal totalAmount = getTotalAmount(omsCartItems);
            map.put("totalAmount", totalAmount);
        }

        return map;
    }

    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems) {
        if (omsCartItems == null || omsCartItems.size() == 0) {
            return new BigDecimal(0);
        }
        BigDecimal total = new BigDecimal(0);
        for (OmsCartItem omsCartItem : omsCartItems) {
            BigDecimal multiply = omsCartItem.getPrice().multiply(new BigDecimal(omsCartItem.getQuantity()));
            omsCartItem.setTotalPrice(multiply);
            if (omsCartItem.getIsChecked() != null && omsCartItem.getIsChecked() == 1) {
                total = total.add(omsCartItem.getTotalPrice());
            }

        }
        return total;
    }


}
