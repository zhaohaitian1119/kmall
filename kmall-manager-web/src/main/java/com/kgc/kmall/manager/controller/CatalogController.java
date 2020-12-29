package com.kgc.kmall.manager.controller;

import com.kgc.kmall.bean.PmsBaseCatalog1;
import com.kgc.kmall.bean.PmsBaseCatalog2;
import com.kgc.kmall.bean.PmsBaseCatalog3;
import com.kgc.kmall.service.CatalogService;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-12-16 15:31
 */
@CrossOrigin
@RestController
@Api(tags = "后台分类相关接口",description = "提供分类相关的Rest API")
public class CatalogController {

    @Reference
    CatalogService catalogService;


    @ApiOperation("查询一级分类")
    @PostMapping("getCatalog1")
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数问题"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"),
            @ApiResponse(code = 405, message = "请求类型不正确"),
            @ApiResponse(code = 500, message = "后端程序出错")
    })
    public List<PmsBaseCatalog1> getCatalog1(){
        List<PmsBaseCatalog1> catalog1List = catalogService.getCatalog1();
        return catalog1List;
    }

    @ApiOperation("查询二级分类")
    @PostMapping("/getCatalog2")
    @ApiImplicitParam(name = "catalog1Id",value = "1级分类id",required = true)
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数问题"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"),
            @ApiResponse(code = 405, message = "请求类型不正确"),
            @ApiResponse(code = 500, message = "后端程序出错")
    })
    public List<PmsBaseCatalog2> getCatalog2(Integer catalog1Id){
        List<PmsBaseCatalog2> catalog2List = catalogService.getCatalog2(catalog1Id);
        return catalog2List;
    }

    @ApiOperation("查询三级分类")
    @PostMapping("/getCatalog3")
    @ApiImplicitParam(name = "catalog2Id",value = "2级分类id",required = true)
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数问题"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"),
            @ApiResponse(code = 405, message = "请求类型不正确"),
            @ApiResponse(code = 500, message = "后端程序出错")
    })
    public List<PmsBaseCatalog3> getCatalog3(Long catalog2Id){
        List<PmsBaseCatalog3> catalog3List = catalogService.getCatalog3(catalog2Id);
        return catalog3List;
    }
}
