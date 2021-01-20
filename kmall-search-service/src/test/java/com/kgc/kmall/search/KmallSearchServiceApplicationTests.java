package com.kgc.kmall.search;

import com.kgc.kmall.bean.PmsSearchSkuInfo;
import com.kgc.kmall.bean.PmsSearchSkuParam;
import com.kgc.kmall.bean.PmsSkuInfo;
import com.kgc.kmall.service.SearchService;
import com.kgc.kmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.dubbo.config.annotation.Reference;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class KmallSearchServiceApplicationTests {

	@Reference
    SkuService skuService;
    @Resource
    JestClient jestClient;
    @Resource
    SearchService searchService;
	
	@Test
	void contextLoads() {
        List<PmsSkuInfo> allSku = skuService.getAllSku();
        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();
        for (PmsSkuInfo pmsSkuInfo : allSku) {
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);
            pmsSearchSkuInfo.setProductId(pmsSearchSkuInfo.getId());
            pmsSearchSkuInfoList.add(pmsSearchSkuInfo);
        }

        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfoList) {
            Index index=new Index.Builder(pmsSearchSkuInfo).index("kmall").type("PmsSkuInfo").id(pmsSearchSkuInfo.getId()+"").build();
            try {
                jestClient.execute(index);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void contextLoads2() {
	    List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();

        //查询条件
        String json="{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"filter\": [\n" +
                "          {\"terms\":{\"skuAttrValueList.valueId\":[\"39\",\"40\",\"41\",\"42\"]}},\n" +
                "          {\"term\":{\"skuAttrValueList.valueId\":\"43\"}}\n" +
                "        ], \n" +
                "      \"must\": \n" +
                "        {\n" +
                "          \"match\": {\n" +
                "            \"skuName\": \"iphone\"\n" +
                "          }\n" +
                "        }\n" +
                "      \n" +
                "    }\n" +
                "  }\n" +
                "}";

        Search search = new Search.Builder(json).addIndex("kmall").addType("PmsSkuInfo").build();

        try {
            SearchResult execute = jestClient.execute(search);
            List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = execute.getHits(PmsSearchSkuInfo.class);
            for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
                PmsSearchSkuInfo source = hit.source;
                pmsSearchSkuInfoList.add(source);
                System.out.println(source);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void contextLoads3() {
        PmsSearchSkuParam pmsSearchSkuParam = new PmsSearchSkuParam();
        pmsSearchSkuParam.setKeyword("IPhone");
        List<PmsSearchSkuInfo> sel = searchService.sel(pmsSearchSkuParam);
        for (PmsSearchSkuInfo pmsSearchSkuInfo : sel) {
            System.out.println(pmsSearchSkuInfo.toString());
        }
        System.out.println(1);


    }

}
