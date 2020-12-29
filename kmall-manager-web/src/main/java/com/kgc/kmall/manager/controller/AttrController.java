package com.kgc.kmall.manager.controller;

import com.kgc.kmall.bean.PmsBaseAttrInfo;
import com.kgc.kmall.bean.PmsBaseAttrValue;
import com.kgc.kmall.service.AttrService;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-12-16 16:48
 */
@CrossOrigin
@RestController
@Api(tags = "销售属性相关接口",description = "提供销售属性相关的Rest API")
public class AttrController {
    @Reference
    AttrService attrService;



    @ApiOperation("根据三级分类查询平台属性")
    @RequestMapping("/attrInfoList")
    @ApiImplicitParam(name = "catalog3Id",value = "三级分类id",required = true)
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数问题"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"),
            @ApiResponse(code = 405, message = "请求类型不正确"),
            @ApiResponse(code = 500, message = "后端程序出错")
    })
    public List<PmsBaseAttrInfo> attrInfoList(Long catalog3Id){
        List<PmsBaseAttrInfo> select = attrService.select(catalog3Id);
        return select;
    }


    @ApiOperation("添加平台属性")
    @RequestMapping("/saveAttrInfo")
    @ApiImplicitParam(name = "attrInfo",value = "平台属性类",required = true)
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数问题"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"),
            @ApiResponse(code = 405, message = "请求类型不正确"),
            @ApiResponse(code = 500, message = "后端程序出错")
    })
    public Integer saveAttrInfo(@RequestBody PmsBaseAttrInfo attrInfo){
        Integer i = attrService.add(attrInfo);
        return i;
    }

    @ApiOperation("根据平台属性id查询查询属性值")
    @RequestMapping("/getAttrValueList")
    @ApiImplicitParam(name = "attrId",value = "属性id",required = true)
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数问题"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"),
            @ApiResponse(code = 405, message = "请求类型不正确"),
            @ApiResponse(code = 500, message = "后端程序出错")
    })
    public List<PmsBaseAttrValue> getAttrValueList(Long attrId){
        List<PmsBaseAttrValue> attrValueList = attrService.getAttrValueList(attrId);
        return attrValueList;
    }
}
