package com.yzp.springshopuserservice.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.yzp.common.IBaseDao;
import com.yzp.common.IBaseServiceImpl;
import com.yzp.common.constant.RedisConstant;
import com.yzp.common.pojo.ResultBean;
import com.yzp.entity.TUser;
import com.yzp.mapper.TUserMapper;
import com.yzp.user.api.IUserService;
import org.apache.zookeeper.data.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Service
public class UserServiceImpl extends IBaseServiceImpl<TUser> implements IUserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public IBaseDao<TUser> getIbaseDao() {
        return userMapper;
    }

    @Override
    public TUser checkIsExists(TUser user) {
        TUser currentUser = userMapper.selectByUserName(user.getUsername());
        if (currentUser!=null){
            if (bCryptPasswordEncoder.matches(user.getPassword(),currentUser.getPassword())){
                return currentUser;

            }
        }
        return null;
    }

    @Override
    public ResultBean checkIsLogin(String uuid) {
        if (uuid == null){
            return  new ResultBean(1,null,"当前用户未登录");

        }
        //根据cookie的uuid获取redis凭证
        String key = new StringBuilder(RedisConstant.USER_TOKEN_PRE).append(uuid).toString();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        TUser user = (TUser) redisTemplate.opsForValue().get(key);
        if (user != null){
            //刷新redis的有效期
            redisTemplate.expire(key,30, TimeUnit.MINUTES);
            return new ResultBean(0,user,"");

        }
        return new ResultBean(1,null,"当前用户未登录");
    }
}