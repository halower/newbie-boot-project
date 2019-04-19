package com.newbie.core.persistent.sql;

import org.springframework.core.io.Resource;

import java.util.Iterator;

/**
 * @Author: 谢海龙
 * @Date: 2019/4/17 13:35
 * @Description
 */
public interface TemplateResolver {
    Iterator<Void> doInTemplateResource(Resource resource, final TemplateCallback callback) throws Exception;
}
