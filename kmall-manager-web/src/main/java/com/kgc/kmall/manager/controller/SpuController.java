package com.kgc.kmall.manager.controller;

import com.kgc.kmall.bean.PmsBaseSaleAttr;
import com.kgc.kmall.bean.PmsProductImage;
import com.kgc.kmall.bean.PmsProductInfo;
import com.kgc.kmall.bean.PmsProductSaleAttr;
import com.kgc.kmall.service.SpuService;
import io.swagger.annotations.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-12-17 16:07
 */
@CrossOrigin
@RestController
@Api(tags = "spu相关接口",description = "提供spu相关的Rest API")
public class SpuController {

    @Reference
    SpuService spuService;
    @Value("${fileServer.url}")
    String fileUrl;

    @ApiOperation("根据三级查询spu")
    @RequestMapping("/spuList")
    @ApiImplicitParam(name = "catalog3Id",value = "3级分类id",required = true)
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数问题"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"),
            @ApiResponse(code = 405, message = "请求类型不正确"),
            @ApiResponse(code = 500, message = "后端程序出错")
    })
    public List<PmsProductInfo> pms(Long catalog3Id){
        List<PmsProductInfo> pmsProductInfos = spuService.spuList(catalog3Id);
        return pmsProductInfos;
    }

    @ApiOperation("查询全部销售属性")
    @RequestMapping("/baseSaleAttrList")
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数问题"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"),
            @ApiResponse(code = 405, message = "请求类型不正确"),
            @ApiResponse(code = 500, message = "后端程序出错")
    })
    public List<PmsBaseSaleAttr> baseSaleAttrList(){
        List<PmsBaseSaleAttr> saleAttrList = spuService.selSaleAttrAll();
        return saleAttrList;
    }

    @ApiOperation("上传文件")
    @RequestMapping("/fileUpload")
    @ApiImplicitParam(name = "file",value = "文件",required = true)
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数问题"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"),
            @ApiResponse(code = 405, message = "请求类型不正确"),
            @ApiResponse(code = 500, message = "后端程序出错")
    })
    public String fileUpload(@RequestParam("file")MultipartFile file){
        try {
            //文件上传
            //返回文件上传后的路径
            String confFile = this.getClass().getResource("/tracker.conf").getFile();
            ClientGlobal.init(confFile);
            TrackerClient trackerClient=new TrackerClient();
            TrackerServer trackerServer=trackerClient.getTrackerServer();
            StorageClient storageClient=new StorageClient(trackerServer,null);

            String originalFilename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);
            String[] upload_file = storageClient.upload_file(file.getBytes(), extension, null);
            String path=fileUrl;
            for (int i = 0; i < upload_file.length; i++) {
                String s = upload_file[i];
                System.out.println("s = " + s);
                path+="/"+s;
            }
            System.out.println(path);
            return path;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    @ApiOperation("添加spu")
    @RequestMapping("/saveSpuInfo")
    @ApiImplicitParam(name = "pmsProductInfo",value = "spu类",required = true)
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数问题"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"),
            @ApiResponse(code = 405, message = "请求类型不正确"),
            @ApiResponse(code = 500, message = "后端程序出错")
    })
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){
        //添加spu
        Integer integer = spuService.saveSpuInfo(pmsProductInfo);
        return "success";
    }

    @ApiOperation("查询全部销售属性值")
    @RequestMapping("/spuSaleAttrList")
    @ApiImplicitParam(name = "pmsProductInfo",value = "spu类",required = true)
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数问题"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"),
            @ApiResponse(code = 405, message = "请求类型不正确"),
            @ApiResponse(code = 500, message = "后端程序出错")
    })
    public List<PmsProductSaleAttr> spuSaleAttrList(Long spuId){
        List<PmsProductSaleAttr> pmsProductSaleAttrList=spuService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrList;
    }

    @ApiOperation("查询全部销售属性值")
    @RequestMapping("/spuImageList")
    @ApiImplicitParam(name = "pmsProductInfo",value = "spu类",required = true)
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数问题"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"),
            @ApiResponse(code = 405, message = "请求类型不正确"),
            @ApiResponse(code = 500, message = "后端程序出错")
    })
    public List<PmsProductImage> spuImageList(Long spuId){
        List<PmsProductImage> pmsProductImageList = spuService.spuImageList(spuId);
        return pmsProductImageList;
    }

}
