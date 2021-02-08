package com.newbie.core.persistent.mybaits;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

public class CommonServiceImpl<M extends CommonMapper<T>, T> extends ServiceImpl<M, T> {

    public <E> IPage<E> selectByPage(IPage<E> page, Wrapper<T> wrapper) {
        return baseMapper.selectByPage(page, wrapper);
    }

}
