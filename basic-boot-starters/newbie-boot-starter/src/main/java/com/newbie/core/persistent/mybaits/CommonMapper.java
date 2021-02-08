package com.newbie.core.persistent.mybaits;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

public interface CommonMapper<T> extends BaseMapper<T> {

    <E> IPage<E> selectByPage(IPage<E> page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

}