package com.kgc.kmall.itemweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shkstart
 * @create 2020-12-29 14:13
 */
@Controller
public class ItemController {

    @RequestMapping("{skuId}.html")
    public String item(@PathVariable(value = "skuId") String skuId){
        return "item";
    }

}
