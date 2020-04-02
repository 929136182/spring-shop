package com.yzp.springshopcartservice.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.yzp.cart.api.ICartService;
import com.yzp.cart.api.pojo.CartItem;
import com.yzp.cart.api.vo.CartItemVo;
import com.yzp.common.constant.RedisConstant;
import com.yzp.common.pojo.ResultBean;
import com.yzp.entity.TProduct;
import com.yzp.product.api.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Service
public class CartServiceImpl implements ICartService {

    @Reference
    private IProductService productService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ResultBean addToCart(String key, Long productId, Integer count) {
        //拼接redis中的key
       //String unKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(key).toString();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        List<CartItem> cart = (List<CartItem>) redisTemplate.opsForValue().get(key);
        if (cart == null){
            //购物车空的时候添加购物车并且添加新的数据
            cart = new ArrayList<>();
            return getResultBean(productId, count, key, cart);

        }
        //更新已经拥有商品的数量
        for (CartItem cartItem : cart) {
            if (cartItem.getProductId().longValue()==productId){
                cartItem.setCount(cartItem.getCount() + count);
                //放到redis中
                redisTemplate.setKeySerializer(new StringRedisSerializer());
                redisTemplate.opsForValue().set(key,cart);
                //设置有效期
                redisTemplate.expire(key,30,TimeUnit.DAYS);
                return new ResultBean(0,cart.size(),"添加商品成功");
            }
        }
        //添加一件新的商品
        return getResultBean(productId, count, key, cart);
    }

    private ResultBean getResultBean(Long productId, Integer count, String unKey, List<CartItem> cart) {
        CartItem cartItem = new CartItem(productId, count, new Date());
        cart.add(cartItem);
        //放到redis中
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(unKey, cart);
        //设置有效期
        redisTemplate.expire(unKey, 30, TimeUnit.DAYS);
        return new ResultBean(0, cart.size(), "添加商品成功");
    }

    @Override
    public ResultBean getCart(String key) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        List<CartItem> cart = (List<CartItem>) redisTemplate.opsForValue().get(key);
        if (cart == null || cart.size()==0){
            return new ResultBean(1,"","用户不存在购物车");


        }
        redisTemplate.expire(key,30,TimeUnit.DAYS);
        return new ResultBean(0,cart,"");
    }

    @Override
    public ResultBean getCartVO(String key) {
        ResultBean resultBean = getCart(key);
        //转换
        if (resultBean.getCode()==0){
            List<CartItem> cart = (List<CartItem>) resultBean.getData();
            List<CartItemVo> cartItemVos = new ArrayList<>();
            for (CartItem cartItem : cart) {
                CartItemVo cartItemVo = new CartItemVo();
                //完成cartItem和Product的转换,先去缓存中获取,缓存没有再去数据库中获取
                String rKey = new StringBuilder(RedisConstant.CART_PRODUCT_PRE).append(cartItem.getProductId()).toString();
                redisTemplate.setKeySerializer(new StringRedisSerializer());
                TProduct product = (TProduct) redisTemplate.opsForValue().get(rKey);
                if (product == null){
                    //说明缓存中没有商品
                    System.out.println(productService.selectByPrimaryKey(cartItem.getProductId()));
                    product = productService.selectByPrimaryKey(cartItem.getProductId());
                    redisTemplate.setKeySerializer(new StringRedisSerializer());
                    redisTemplate.opsForValue().set(rKey,product);
                    redisTemplate.expire(rKey,30,TimeUnit.DAYS);

                }
                cartItemVo.setProduct(product);
                cartItemVo.setCount(cartItem.getCount());
                cartItemVo.setUpdateTime(cartItem.getUpdateTime());
                cartItemVos.add(cartItemVo);
            }
            return new ResultBean(0,cartItemVos,"");
        }
        return resultBean;
    }

    @Override
    public ResultBean resetCount(String key, Long productId, Integer count) {
        ResultBean resultBean = getCart(key);
        if (resultBean.getCode()==0){
            List<CartItem> cart = (List<CartItem>) resultBean.getData();
            for (CartItem cartItem : cart) {
                if (cartItem.getProductId().longValue()==productId){
                    cartItem.setCount(count);
                   // String rKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(key).toString();
                    redisTemplate.setKeySerializer(new StringRedisSerializer());
                    redisTemplate.opsForValue().set(key,cart);
                    redisTemplate.expire(key,30,TimeUnit.DAYS);
                    return new ResultBean(0,cart,"更新购物车数量成功");
                }
            }
        }
        return resultBean;
    }

    @Override
    public ResultBean remove(String key, Long productId) {
        ResultBean resultBean = getCart(key);
        if (resultBean.getCode()==0){
            List<CartItem> cart = (List<CartItem>) resultBean.getData();
            for (CartItem cartItem : cart) {
                if (cartItem.getProductId().longValue()==productId){
                    cart.remove(cartItem);
                   // String rKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(key).toString();
                    redisTemplate.setKeySerializer(new StringRedisSerializer());
                    redisTemplate.opsForValue().set(key,cart);
                    redisTemplate.expire(key,30,TimeUnit.DAYS);
                    return new ResultBean(0,cart,"移除商品成功");
                }
            }

        }
        return resultBean;
    }

    @Override
    public ResultBean merge(String noLoginCartKey, String loginCartKey) {
        //获取未登录购物车商品
        ResultBean noLoginCart = getCart(noLoginCartKey);
        //获取已登录购物车商品
        ResultBean loginCart = getCart(loginCartKey);
        //将未登录购物车商品放到已登录购物车中
        if (loginCart.getCode()==1){
            //这种情况是登录购物车中没有商品需要将未登录购物车内容合并到已登录购物车
            List<CartItem> noLoginCartItems = (List<CartItem>) noLoginCart.getData();
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.opsForValue().set(loginCartKey,noLoginCartItems);
            redisTemplate.expire(loginCartKey,30,TimeUnit.DAYS);
            redisTemplate.delete(noLoginCartKey);
            return new ResultBean(0,"合并成功","合并成功");
        }
        //彼此存在商品在这时候需要合并了
        Map<Long,CartItem> map = new HashMap<>();
        //先剑未登录购物车内容放到里面
        List<CartItem> noLoginCrtItems = (List<CartItem>) noLoginCart.getData();
        for (CartItem noLoginCrtItem : noLoginCrtItems) {
            map.put(noLoginCrtItem.getProductId(),noLoginCrtItem);
        }
        //将已登录购物车商品放到map中
        List<CartItem> loginCartItems = (List<CartItem>) loginCart.getData();
        for (CartItem loginCartItem : loginCartItems) {
            CartItem now = map.get(loginCartItem.getProductId());
            if (now == null){
                map.put(loginCartItem.getProductId(),loginCartItem);
            }else {
                //存在相同的商品，进行数量合并
                now.setCount(loginCartItem.getCount() + now.getCount());
                map.put(loginCartItem.getProductId(),now);

            }
        }
        //得到合并后的购物车
        Collection<CartItem> cartItems = map.values();
        List<CartItem> mergedCart = new ArrayList<>(cartItems);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(loginCartKey,mergedCart);
        redisTemplate.expire(loginCartKey,30,TimeUnit.DAYS);
        //删除未登录的购物车
        redisTemplate.delete(noLoginCartKey);

        return new ResultBean(0,"购物车合并成功","购物车合并成功");
    }
}
