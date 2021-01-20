package com.kgc.kmall.service;

import com.kgc.kmall.bean.PmsBaseAttrInfo;
import com.kgc.kmall.bean.PmsBaseAttrValue;

import java.util.List;
import java.util.Set;

/**
 * @author shkstart
 * @create 2020-12-16 16:24
 */
public interface AttrService {
    public List<PmsBaseAttrInfo> select(Long catalog3Id);

    //添加属性
    public Integer add(PmsBaseAttrInfo attrInfo);

    public List<PmsBaseAttrValue> getAttrValueList(Long attrId);


    List<PmsBaseAttrInfo> selectAttrInfoValueListByValueId(Set<Long> valueIds);
}
