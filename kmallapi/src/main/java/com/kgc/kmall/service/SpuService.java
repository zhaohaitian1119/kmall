package com.kgc.kmall.service;

import com.kgc.kmall.bean.PmsProductInfo;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-12-17 16:04
 */
public interface SpuService {
    public List<PmsProductInfo> spuList(Long catalog3Id);
}
