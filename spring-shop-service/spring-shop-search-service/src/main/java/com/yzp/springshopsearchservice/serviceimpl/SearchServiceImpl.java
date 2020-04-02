package com.yzp.springshopsearchservice.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yzp.common.pojo.PageResultBean;
import com.yzp.common.pojo.ResultBean;
import com.yzp.entity.TProduct;
import com.yzp.mapper.TProductMapper;
import com.yzp.search.api.ISearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Service
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private TProductMapper tProductMapper;
    @Autowired
    private  SolrClient solrClient;
    @Override
    public ResultBean synAllData() {
        List<TProduct> list = tProductMapper.list();

        for (TProduct product : list) {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id",product.getId());
            document.addField("name",product.getName());
            document.addField("image",product.getImage());
            document.addField("price",product.getPrice());
            document.addField("sale_price",product.getSalePrice());
            document.addField("sale_point",product.getSalePoint());
            try {
                solrClient.add(document);
            } catch (Exception e) {
                e.printStackTrace();
                return ResultBean.errResult("添加数据到索引库失败");
            }
        }
        try {
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.errResult("添加数据到索引库失败");
        }
        return ResultBean.successResult("同步索引库成功");
    }

    @Override
    public PageResultBean<TProduct> getProductByKeywordsByPage(String productKeywords, Integer pageIndex, Integer pageSize) {
        PageResultBean<TProduct> pageResultBean =new PageResultBean<>();
        //查询搜索索引库
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("product_keywords:"+productKeywords);
        //开启添加高亮信息
        solrQuery.setHighlight(true);
        //设置高亮信息字段
        solrQuery.addHighlightField("name");
        //设置分页信息
        solrQuery.setStart((pageIndex-1)*pageSize);
        solrQuery.setRows(pageSize);
        //设置高亮样式
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        //执行查询
        try {
            QueryResponse response = solrClient.query(solrQuery);
            long total = response.getResults().getNumFound();
            List<TProduct> products = getSearch(response);
            pageResultBean.setList(products);
            pageResultBean.setPageNum(pageIndex);
            pageResultBean.setPageSize(pageSize);
            pageResultBean.setTotal(total);
            pageResultBean.setPages((int)(total%pageSize==0?total/pageSize:(total/pageSize)+1));

            return pageResultBean;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResultBean();
        }

    }


    @Override
    public List<TProduct> getProductByKeywords(String productKeywords) {
        List<TProduct> products = null;
        //查询搜索索引库
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("product_keywords:"+productKeywords);
        //开启添加高亮信息
        solrQuery.setHighlight(true);
        //设置高亮信息字段
        solrQuery.addHighlightField("name");
        //设置高亮样式
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        //执行查询
        try {
            QueryResponse response = solrClient.query(solrQuery);
            products = getSearch(response);
            return products;
        } catch (Exception e) {
            e.printStackTrace();
            return  new ArrayList<>();
        }

    }

    private List<TProduct> getSearch(QueryResponse response) {
        List<TProduct> products;
        SolrDocumentList documentList = response.getResults();
        products = new ArrayList<>(documentList.size());
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        for (SolrDocument entries : documentList) {
            TProduct product = new TProduct();
            product.setId(Long.parseLong(entries.getFieldValue("id").toString()));
            product.setImage(entries.getFieldValue("image").toString());
            product.setPrice(Long.parseLong(entries.getFieldValue("price").toString()));
            product.setSalePrice(Long.parseLong(entries.getFieldValue("sale_price").toString()));
            product.setSalePoint(entries.getFieldValue("sale_point").toString());
            //处理高亮字段
            Map<String, List<String>> highlightText = highlighting.get(entries.getFieldValue("id").toString());
            List<String> highList = highlightText.get("name");
            //System.out.println();
            if (highList != null) {
                product.setName(highList.get(0));
            } else {
                product.setName(entries.getFieldValue("name").toString());
            }
            products.add(product);
        }
        return products;
    }

    public void saveOrUpdate(TProduct product) {
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id",product.getId());
        document.addField("name",product.getName());
        document.addField("image",product.getImage());
        document.addField("price",product.getPrice());
        document.addField("sale_price",product.getSalePrice());
        document.addField("sale_point",product.getSalePoint());
        try {
            solrClient.add(document);
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
