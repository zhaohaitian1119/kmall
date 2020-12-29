package com.kgc.kmall.manager.controller;

import com.kgc.kmall.bean.PmsSkuInfo;
import com.kgc.kmall.service.SkuService;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

/**
 * @author shkstart
 * @create 2020-12-24 8:41
 */

@CrossOrigin
@RestController
@Api(tags = "sku相关接口",description = "提供sku相关的Rest API")
public class SkuController {

    @Reference
    SkuService skuService;

    @ApiOperation("添加sku")
    @RequestMapping("/saveSkuInfo")
    @ApiImplicitParam(name = "skuInfo",value = "sku类",required = true)
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数问题"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"),
            @ApiResponse(code = 405, message = "请求类型不正确"),
            @ApiResponse(code = 500, message = "后端程序出错")
    })
    public String saveSkuInfo(@RequestBody PmsSkuInfo skuInfo){
        String result = skuService.saveSkuInfo(skuInfo);
        return result;
    }

}
