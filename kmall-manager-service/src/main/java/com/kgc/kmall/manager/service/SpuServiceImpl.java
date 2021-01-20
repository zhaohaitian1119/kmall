package com.kgc.kmall.manager.service;

import com.kgc.kmall.bean.*;
import com.kgc.kmall.manager.mapper.*;
import com.kgc.kmall.service.SpuService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-12-17 16:04
 */
@Component
@Service
public class SpuServiceImpl implements SpuService {

    @Resource
    PmsProductInfoMapper pmsProductInfoMapper;
    @Resource
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;
    @Resource
    PmsProductImageMapper pmsProductImageMapper;
    @Resource
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
    @Resource
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Override
    public List<PmsProductInfo> spuList(Long catalog3Id) {
        PmsProductInfoExample example = new PmsProductInfoExample();
        PmsProductInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCatalog3IdEqualTo(catalog3Id);
        List<PmsProductInfo> pmsProductInfos = pmsProductInfoMapper.selectByExample(example);
        return pmsProductInfos;
    }

    @Override
    public List<PmsBaseSaleAttr> selSaleAttrAll() {
        List<PmsBaseSaleAttr> pmsBaseSaleAttrs = pmsBaseSaleAttrMapper.selectByExample(null);
        return pmsBaseSaleAttrs;
    }

    @Override
    public Integer saveSpuInfo(PmsProductInfo pmsProductInfo) {
        try {
            //添加spu
            pmsProductInfoMapper.insertSelective(pmsProductInfo);
            //添加图片
            List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();
            for (PmsProductImage pmsProductImage : spuImageList) {
                pmsProductImage.setProductId(pmsProductInfo.getId());
                pmsProductImageMapper.insertSelective(pmsProductImage);
            }
            //添加销售属性
            List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
            if (spuSaleAttrList.size()>0&&spuSaleAttrList!=null){
                for (PmsProductSaleAttr pmsProductSaleAttr : spuSaleAttrList) {
                    pmsProductSaleAttr.setProductId(pmsProductInfo.getId());
                    List<PmsProductSaleAttrValue> spuSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();
                    //添加销售属性值
                    for (PmsProductSaleAttrValue pmsProductSaleAttrValue : spuSaleAttrValueList) {
                        pmsProductSaleAttrValue.setProductId(pmsProductInfo.getId());
                        pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);
                    }
                    pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);
                }
            }
            return 1;
        }catch (Exception e){
            return 0;
        }

    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(Long spuId) {
        PmsProductSaleAttrExample example=new PmsProductSaleAttrExample();
        PmsProductSaleAttrExample.Criteria criteria = example.createCriteria();
        criteria.andProductIdEqualTo(spuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductSaleAttrMapper.selectByExample(example);
        for (PmsProductSaleAttr pmsProductSaleAttr : pmsProductSaleAttrList) {
            PmsProductSaleAttrValueExample example1=new PmsProductSaleAttrValueExample();
            PmsProductSaleAttrValueExample.Criteria criteria1 = example1.createCriteria();
            criteria1.andSaleAttrIdEqualTo(pmsProductSaleAttr.getSaleAttrId());
            criteria1.andProductIdEqualTo(spuId);

            List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList = pmsProductSaleAttrValueMapper.selectByExample(example1);
            pmsProductSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValueList);
        }
        return pmsProductSaleAttrList;
    }

    @Override
    public List<PmsProductImage> spuImageList(Long spuId) {
        PmsProductImageExample example = new PmsProductImageExample();
        PmsProductImageExample.Criteria criteria = example.createCriteria();
        criteria.andProductIdEqualTo(spuId);
        List<PmsProductImage> pmsProductImages = pmsProductImageMapper.selectByExample(example);
        return pmsProductImages;
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListIsCheck(Long spuId, Long skuId) {
        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductSaleAttrMapper.spuSaleAttrListIsCheck(spuId, skuId);
        return pmsProductSaleAttrList;
    }


}
