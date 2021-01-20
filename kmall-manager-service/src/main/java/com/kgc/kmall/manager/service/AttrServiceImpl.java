package com.kgc.kmall.manager.service;

import com.kgc.kmall.bean.PmsBaseAttrInfo;
import com.kgc.kmall.bean.PmsBaseAttrInfoExample;
import com.kgc.kmall.bean.PmsBaseAttrValue;
import com.kgc.kmall.bean.PmsBaseAttrValueExample;
import com.kgc.kmall.manager.mapper.PmsBaseAttrInfoMapper;
import com.kgc.kmall.manager.mapper.PmsBaseAttrValueMapper;
import com.kgc.kmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author shkstart
 * @create 2020-12-16 16:25
 */
@Component
@Service
public class AttrServiceImpl implements AttrService {

    @Resource
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;
    @Resource
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;



    @Override
    public List<PmsBaseAttrInfo> select(Long catalog3Id) {
        PmsBaseAttrInfoExample example = new PmsBaseAttrInfoExample();
        PmsBaseAttrInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCatalog3IdEqualTo(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.selectByExample(example);
//        //为每个平台属性添加平台属性值
        for (PmsBaseAttrInfo pmsBaseAttrInfo : pmsBaseAttrInfos) {
            PmsBaseAttrValueExample example1 = new PmsBaseAttrValueExample();
            PmsBaseAttrValueExample.Criteria criteria1 = example1.createCriteria();
            criteria1.andAttrIdEqualTo(pmsBaseAttrInfo.getId());
            List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.selectByExample(example1);
            pmsBaseAttrInfo.setAttrValueList(pmsBaseAttrValues);
        }
//            pmsBaseAttrInfoMapper.sel();

        return pmsBaseAttrInfos;
    }

    @Override
    public Integer add(PmsBaseAttrInfo attrInfo) {
//        int i =

        try {
            if(attrInfo.getId() == null){
                pmsBaseAttrInfoMapper.insertSelective(attrInfo);
            }else{
                pmsBaseAttrInfoMapper.updateByPrimaryKeySelective(attrInfo);
                PmsBaseAttrValueExample example = new PmsBaseAttrValueExample();
                PmsBaseAttrValueExample.Criteria criteria = example.createCriteria();
                criteria.andAttrIdEqualTo(attrInfo.getId());
                pmsBaseAttrValueMapper.deleteByExample(example);
            }

            pmsBaseAttrValueMapper.insertBatch(attrInfo.getId(),attrInfo.getAttrValueList());

            return 1;
        }catch (Exception ex){
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(Long attrId) {
        PmsBaseAttrValueExample example = new PmsBaseAttrValueExample();
        PmsBaseAttrValueExample.Criteria criteria = example.createCriteria();
        criteria.andAttrIdEqualTo(attrId);
        List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.selectByExample(example);
        return pmsBaseAttrValues;
    }

    @Override
    public List<PmsBaseAttrInfo> selectAttrInfoValueListByValueId(Set<Long> valueIds) {
        String join = StringUtils.join(valueIds);
        join = join.substring(1, join.length() - 1);
        System.out.println(join);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.selectAttrInfoValueListByValueId(join);
        return pmsBaseAttrInfos;
    }
}
