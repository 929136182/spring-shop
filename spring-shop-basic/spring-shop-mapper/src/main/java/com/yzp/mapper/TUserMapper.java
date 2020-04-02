package com.yzp.mapper;

import com.yzp.common.IBaseDao;
import com.yzp.entity.TUser;

public interface TUserMapper extends IBaseDao<TUser> {


    TUser selectByUserName(String username);
}