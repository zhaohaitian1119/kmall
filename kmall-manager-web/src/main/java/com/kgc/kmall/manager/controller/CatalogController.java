package com.kgc.kmall.manager.controller;

import com.kgc.kmall.bean.PmsBaseCatalog1;
import com.kgc.kmall.service.CatalogService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-12-16 15:31
 */
@RestController
public class CatalogController {

    @Reference
    CatalogService catalogService;

    @RequestMapping("getCatalog1")
    public List<PmsBaseCatalog1> getCatalog1(){
        List<PmsBaseCatalog1> catalog1List = catalogService.getCatalog1();
        return catalog1List;
    }
}
