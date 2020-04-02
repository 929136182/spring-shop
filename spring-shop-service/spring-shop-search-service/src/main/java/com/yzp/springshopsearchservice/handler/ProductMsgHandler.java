package com.yzp.springshopsearchservice.handler;

import com.yzp.common.constant.RabbitConstant;
import com.yzp.entity.TProduct;
import com.yzp.springshopsearchservice.serviceimpl.SearchServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMsgHandler {
    @Autowired
    private SearchServiceImpl searchService;

    @RabbitListener(queues = RabbitConstant.PRODUCT_ADD_QUEUE)
    public void processSaveOrUpdate(TProduct product){
        System.out.println("接收到信息成功" + product.toString());
        searchService.saveOrUpdate(product);
        System.out.println("更新成功");
    }
}
