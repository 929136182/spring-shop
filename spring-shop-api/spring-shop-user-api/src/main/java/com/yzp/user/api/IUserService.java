package com.yzp.user.api;

import com.yzp.common.pojo.ResultBean;
import com.yzp.entity.TUser;

public interface IUserService {
    //检查用户是否存在
    TUser checkIsExists(TUser user);
    //登录
    ResultBean checkIsLogin(String uuid);
}
