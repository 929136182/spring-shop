package com.yzp.cart.api;

import com.yzp.common.pojo.ResultBean;

public interface ICartService {
    //添加商品到购物车
    /**
     *
     * @param key  保存到redis中的key
     * @param productId  添加的商品id
     * @param count      添加的商品对应的数量
     * @return
     */
    ResultBean addToCart(String key,Long productId,Integer count);
    //获取购物车

    /**
     *
     * @param key redis中保存购物车的key
     * @return
     */
    ResultBean getCart(String key);

    /**
     * 返回给前端购物车详情功能
     * @param key
     * @return
     */
    ResultBean getCartVO(String key);

    //更改购物车商品数量

    /**
     *
     * @param key  redis中保存购物车key
     * @param productId 要更改的商品id
     * @param count      要更改的商品数量
     * @return
     */
    ResultBean resetCount(String key,Long productId,Integer count);

    //删除购物车中的商品

    /**
     *
     * @param key redis中保存购物车key
     * @param productId  要删除的商品id
     * @return
     */
    ResultBean remove(String key,Long productId);

    /**
     * 合并购物车接口
     * @param noLoginCartKey 未登录key
     * @param loginCartKey   登录key
     * @return
     */
    ResultBean merge(String noLoginCartKey,String loginCartKey);
}
