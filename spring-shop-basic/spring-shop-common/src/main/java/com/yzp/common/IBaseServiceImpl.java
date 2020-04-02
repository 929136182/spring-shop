package com.yzp.common;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

public abstract class IBaseServiceImpl<T> implements IBaseService<T>{
    public abstract IBaseDao<T> getIbaseDao();
    @Override
    public int deleteByPrimaryKey(Long id){
        return getIbaseDao().deleteByPrimaryKey(id);
    }
    @Override
    public int insert(T record){
        return getIbaseDao().insert(record);
    }
    @Override
    public int insertSelective(T record){
        return getIbaseDao().insertSelective(record);
    }
    @Override
    public T selectByPrimaryKey(Long id){
        return getIbaseDao().selectByPrimaryKey(id);
    }
    @Override
    public int updateByPrimaryKeySelective(T record){
        return getIbaseDao().updateByPrimaryKeySelective(record);
    }
    @Override
    public int updateByPrimaryKeyWithBLOBs(T record){
        return getIbaseDao().updateByPrimaryKeyWithBLOBs(record);
    }
    @Override
    public int updateByPrimaryKey(T record){
        return getIbaseDao().updateByPrimaryKey(record);
    }
    @Override
    public List<T> list(){
        return getIbaseDao().list();
    }
    @Override
    public PageInfo<T> getPageInfo(int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<T> list = list();
        PageInfo<T> pageInfo = new PageInfo<T>(list,3);
        return pageInfo;
    }
}
