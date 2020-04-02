package com.yzp.springshopproductweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.yzp.common.constant.RabbitConstant;
import com.yzp.common.pojo.ResultBean;
import com.yzp.entity.TProduct;
import com.yzp.entity.TProductDesc;
import com.yzp.item.api.IitemService;
import com.yzp.product.api.IProductDescService;
import com.yzp.product.api.IProductService;
import com.yzp.product.api.dto.ProductDto;
import com.yzp.product.api.dto.UpProductDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Reference
    private IitemService itemService;
    @Reference
    private IProductService iProductService;
    @Reference
    private IProductDescService iProductDescService;
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Value("${image.path}")
    private String IMAGE_PATH;
    @RequestMapping("getOne/{id}")
    @ResponseBody
    public String getProduct(@PathVariable("id")Long id){
        TProduct tProduct = iProductService.selectByPrimaryKey(id);
        return tProduct.toString();
    }
     @RequestMapping("product/page/{pageNum}/{pageSize}")
    public String listByPage(Model model ,@PathVariable("pageNum")int pageNum,@PathVariable("pageSize")int pageSize){
         PageInfo<TProduct> pageInfo = iProductService.getPageInfo(pageNum, pageSize);
         model.addAttribute("pageInfo",pageInfo);
         return "product";

     }
     @RequestMapping("testList")
     @ResponseBody
     public String testList(){
         List<TProduct> products = iProductService.list();
         ResultBean bean = itemService.batchCreateItemPages(products);
         return (String) bean.getData();
     }

     @RequestMapping("product/add")
     @ResponseBody
    public String productAdd(ProductDto dto,MultipartFile file){
         System.out.println(dto);
         System.out.println("file is " + file);
         String message = "发送成功";
         // return "redirect:/product/page/1/5";
         //用fastdfs进行图片上传 得到图片的地址
         String originalFilename = file.getOriginalFilename();
         String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
         String fullPath = null;
         try {
             StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(),file.getSize(),ext,null);
             fullPath = storePath.getFullPath();
         } catch (IOException e) {
             e.printStackTrace();
         }
         dto.gettProduct().setImage(IMAGE_PATH + "/" + fullPath);
         Long typeId = dto.gettProduct().getTypeId();
         if (typeId.longValue() == 1) {
             dto.gettProduct().setTypeName("手机数码");
         } else {
             dto.gettProduct().setTypeName("家用电器");
         }
         //保存数据库
         Long productId = iProductService.save(dto);
         System.out.println(productId);

         TProduct product = dto.gettProduct();
         product.setId(productId);
         itemService.createItemPages(product);
         rabbitTemplate.convertAndSend(RabbitConstant.PRODUCT_EXCHANGE,"product.add",product);
         rabbitTemplate.convertAndSend(RabbitConstant.PRODUCT_MAIL_EXCHANGE,"product.add",product);
         return "redirect:/product/page/1/5";

     }

     @RequestMapping("toupdate")
     @ResponseBody
    public UpProductDto toUpdate(Long id){

         UpProductDto one = iProductService.selectById(id);
         return one;

     }

     @RequestMapping("update")
     @ResponseBody
    public Integer upDate(UpProductDto dto,MultipartFile file){
         String originalFilename = file.getOriginalFilename();
         String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
         String fullPath = null;
         try {
             StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(),file.getSize(),ext,null);
             fullPath = storePath.getFullPath();
         } catch (IOException e) {
             e.printStackTrace();
         }
         dto.gettProduct().setImage(IMAGE_PATH + "/" + fullPath);
        if (dto.gettProduct().getTypeId()==1){
            dto.gettProduct().setTypeName("手机数码");
        }
        if (dto.gettProduct().getTypeId()==2){
            dto.gettProduct().setTypeName("家用电器");
        }
         Integer i = iProductService.upDateById(dto);
         return i;
     }
     @RequestMapping("del")
     @ResponseBody
    public Integer del(Long id){
         Long productId = id;
        int i = iProductService.deleteByPrimaryKey(id);
         int i1 = iProductDescService.deleteByPrimaryKey(id);
         if (i>0&&i1>0){
            return i=0;
        }
        return i=1;
     }

}
