package com.newbie.core.persistent;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.java.Log;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * @Author: 谢海龙
 * @Date: 2019/7/4 10:45
 * @Description
 */
@Component
@Log
public class MyMetaObjectHandler implements MetaObjectHandler {
    final static String createDateField = "cjsj";
    final static String updateDateField = "zhxgsj";
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("新增自动填充属性值");
        Object createDate = metaObject.getValue(createDateField);
        Object updateDate = metaObject.getValue(updateDateField);

        if (null == createDate) {
            metaObject.setValue(createDateField, new Date());
        }
        if (null == updateDate) {
            metaObject.setValue(updateDateField, new Date());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("更新时自动填充属性值");
        metaObject.setValue(updateDateField, new Date());
    }
}
