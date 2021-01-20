package com.kgc.kmall.service;

import com.kgc.kmall.bean.PmsBaseSaleAttr;
import com.kgc.kmall.bean.PmsProductImage;
import com.kgc.kmall.bean.PmsProductInfo;
import com.kgc.kmall.bean.PmsProductSaleAttr;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-12-17 16:04
 */
public interface SpuService {
    public List<PmsProductInfo> spuList(Long catalog3Id);

    public List<PmsBaseSaleAttr> selSaleAttrAll();

    Integer saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductSaleAttr> spuSaleAttrList(Long spuId);

    List<PmsProductImage> spuImageList(Long spuId);


    //回显销售属性和销售属性值
    List<PmsProductSaleAttr> spuSaleAttrListIsCheck(Long spuId,Long skuId);
}
