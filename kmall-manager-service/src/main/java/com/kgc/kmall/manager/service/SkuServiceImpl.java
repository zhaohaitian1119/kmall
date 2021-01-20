package com.kgc.kmall.manager.service;

import com.alibaba.fastjson.JSON;
import com.kgc.kmall.bean.*;
import com.kgc.kmall.manager.mapper.PmsSkuAttrValueMapper;
import com.kgc.kmall.manager.mapper.PmsSkuImageMapper;
import com.kgc.kmall.manager.mapper.PmsSkuInfoMapper;
import com.kgc.kmall.manager.mapper.PmsSkuSaleAttrValueMapper;
import com.kgc.kmall.service.SkuService;
import com.kgc.kmall.utils.RedisUtils;
import org.apache.dubbo.config.annotation.Service;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import com.kgc.kmall.config.RedisConfing;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

/**
 * @author shkstart
 * @create 2020-12-24 8:42
 */
@Component
@Service
public class SkuServiceImpl implements SkuService {

    @Resource
    PmsSkuInfoMapper pmsSkuInfoMapper;
    @Resource
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;
    @Resource
    PmsSkuImageMapper pmsSkuImageMapper;
    @Resource
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Resource
    RedisUtils redisUtils;
    @Resource
    RedissonClient redissonClient;


    @Override
    public String saveSkuInfo(PmsSkuInfo skuInfo) {

        pmsSkuInfoMapper.insert(skuInfo);

        List<PmsSkuImage> skuImageList = skuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(skuInfo.getId());
            pmsSkuImageMapper.insert(pmsSkuImage);
        }

        List<PmsSkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(skuInfo.getId());
            pmsSkuAttrValueMapper.insert(pmsSkuAttrValue);
        }

        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(skuInfo.getId());
            pmsSkuSaleAttrValueMapper.insert(pmsSkuSaleAttrValue);
        }


        return "success";
    }

//    @Override
//    public PmsSkuInfo seltBySkuIds(Long skuId) {
//        PmsSkuInfo pmsSkuInfo = null;
//        Jedis jedis = redisUtils.getJedis();
//        //命名一个key
//        String key = "sku:"+skuId+":info";
//        String skuInfoJson  = jedis.get(key);
//        if(skuInfoJson !=null){
//            //缓存中有数据
//            pmsSkuInfo = JSON.parseObject(skuInfoJson , PmsSkuInfo.class);
//            jedis.close();
//            return pmsSkuInfo;
//        }else{
//            //使用nx分布式锁,避免缓存击穿
//            String keyLock =  "sku:"+skuId+":lock";
//            String keyLockValue= UUID.randomUUID().toString();
//            //设置一个分布式锁的key
//            String ok = jedis.set(keyLock, keyLockValue, "NX", "PX", 60 * 1000);
//            if(ok.equals("OK")) {
//                //缓存中没有数据
//                pmsSkuInfo = pmsSkuInfoMapper.selectByPrimaryKey(skuId);
//                //防止缓存穿透
//                if (pmsSkuInfo != null) {
//                    //保存到redis
//                    String skuInfoJsonStr = JSON.toJSONString(pmsSkuInfo);
//                    //有效期随机 防止缓存雪崩
//                    Random random = new Random();
//                    int i = random.nextInt(10);
//                    jedis.setex(key, i * 60 * 1000, skuInfoJsonStr);
//                } else {
//                    jedis.setex(key, 60 * 1000, "empty");
//                }
//                //在数据库拿到数据后删除分布式锁
//                String keyLockValue2 = jedis.get(keyLockValue);
//                if(keyLockValue2!=null&&keyLockValue2.equals(keyLockValue)){
//                    jedis.del(keyLock);
//                }
//
//
//            }else{
//                //没有拿到分布式锁 睡眠3S
//                try {
//                    Thread.sleep(3000);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                return seltBySkuIds(skuId);
//            }
//            jedis.close();
//            return pmsSkuInfo;
//        }
//
//    }

    @Override
    public PmsSkuInfo seltBySkuIds(Long skuId) {
        PmsSkuInfo pmsSkuInfo = null;
        Jedis jedis = redisUtils.getJedis();
        //命名一个key
        String key = "sku:" + skuId + ":info";
        String skuInfoJson = jedis.get(key);
        if (skuInfoJson != null) {
            //缓存中有数据
            pmsSkuInfo = JSON.parseObject(skuInfoJson, PmsSkuInfo.class);
            jedis.close();
            return pmsSkuInfo;
        } else {
            Lock lock = redissonClient.getLock("lock");// 声明锁
            lock.lock();//上锁
            //缓存中没有数据
            pmsSkuInfo = pmsSkuInfoMapper.selectByPrimaryKey(skuId);
            //防止缓存穿透
            if (pmsSkuInfo != null) {
                //保存到redis
                String skuInfoJsonStr = JSON.toJSONString(pmsSkuInfo);
                //有效期随机 防止缓存雪崩
                Random random = new Random();
                int i = random.nextInt(10);
                jedis.setex(key, i * 60 * 1000, skuInfoJsonStr);
            } else {
                jedis.setex(key, 60 * 1000, "empty");
            }


            jedis.close();
            lock.unlock();
            return pmsSkuInfo;
        }

    }


    @Override
    public List<PmsSkuInfo> selectBySpuId(Long spuId) {


        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectBySpuId(spuId);
        return pmsSkuInfos;
    }

    @Override
    public List<PmsSkuInfo> getAllSku() {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectByExample(null);
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            PmsSkuAttrValueExample example = new PmsSkuAttrValueExample();
            PmsSkuAttrValueExample.Criteria criteria = example.createCriteria();
            criteria.andSkuIdEqualTo(pmsSkuInfo.getId());
            List<PmsSkuAttrValue> pmsSkuAttrValues = pmsSkuAttrValueMapper.selectByExample(example);
            pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValues);
        }
        return pmsSkuInfos;
    }

    @Override
    public boolean checkPrice(Long productSkuId, BigDecimal price) {
        boolean b = false;


        PmsSkuInfo pmsSkuInfo1 = pmsSkuInfoMapper.selectByPrimaryKey(productSkuId);

        BigDecimal price1 = new BigDecimal(pmsSkuInfo1.getPrice());

        if(price.compareTo(price1)==0){
            b = true;
        }

        return b;
    }
}
