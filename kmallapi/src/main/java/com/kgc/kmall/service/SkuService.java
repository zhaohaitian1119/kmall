package com.kgc.kmall.service;

import com.kgc.kmall.bean.PmsSkuInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-12-24 8:41
 */
public interface SkuService {
    public String saveSkuInfo(PmsSkuInfo skuInfo);

    PmsSkuInfo seltBySkuIds(Long skuId);

    List<PmsSkuInfo> selectBySpuId(Long spuId);

    List<PmsSkuInfo> getAllSku();

    boolean checkPrice(Long productSkuId, BigDecimal price);
}
