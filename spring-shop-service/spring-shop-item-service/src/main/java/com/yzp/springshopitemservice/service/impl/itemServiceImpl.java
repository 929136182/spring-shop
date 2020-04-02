package com.yzp.springshopitemservice.service.impl;


import com.yzp.common.pojo.ResultBean;
import com.yzp.entity.TProduct;
import com.yzp.item.api.IitemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class itemServiceImpl implements IitemService {
    @Autowired
    private Configuration configuration;
    @Value("${template.path}")
    private String TEMPLATE_PATH;
    private static final int CORE_THREAD_COUNT=Runtime.getRuntime().availableProcessors();
    private ExecutorService pool = new ThreadPoolExecutor(CORE_THREAD_COUNT,CORE_THREAD_COUNT*2,60L,
            TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(100));
    @Override
    public ResultBean createItemPages(TProduct product) {
        try {
        Template template = configuration.getTemplate("product_detail.ftl");
        Map<String,Object> data = new HashMap<>();
        data.put("product",product);
        FileWriter out = new FileWriter(TEMPLATE_PATH + product.getId()+".html");
        template.process(data,out);
    } catch (Exception e) {
        e.printStackTrace();
        return ResultBean.errResult("生成模板失败");
    }
        return ResultBean.successResult("生成模板成功");
}

    @Override
    public ResultBean batchCreateItemPages(List<TProduct> products) {
        List<Future> futures = new ArrayList<>();
        for (TProduct product : products) {
            Future<Long> future = pool.submit(new MyTask(product));
            futures.add(future);
        }
        for (Future future : futures) {
            try {
                System.out.println(future.get() + "生成成功");
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    System.out.println(future.get() + "生成失败");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return ResultBean.successResult("批量生成结束");
    }

    class MyTask implements Callable<Long>{
        public MyTask(TProduct product) {
            this.product = product;
        }

        private TProduct product;
        @Override
        public Long call() throws Exception {
            try {
                Template template = configuration.getTemplate("product_detail.ftl");
                Map<String,Object> data = new HashMap<>();
                data.put("product",product);
                FileWriter out = new FileWriter(TEMPLATE_PATH + product.getId()+".html");
                template.process(data,out);
            } catch (Exception e) {
                e.printStackTrace();
                return product.getId();
            }
            return product.getId();
        }
    }
}
