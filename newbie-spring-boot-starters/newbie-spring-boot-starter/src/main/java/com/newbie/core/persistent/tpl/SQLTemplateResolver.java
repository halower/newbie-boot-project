/*
 * Apache License
 *
 * Copyright (c) 2019  halower (halower@foxmail.com).
 *
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.newbie.core.persistent.tpl;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author: halower
 * @date: 2019/4/17 9:18
 *
 */
@Log4j2
@Data
public class SQLTemplateResolver {
    private  Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
    private  StringTemplateLoader sqlTemplateLoader = new StringTemplateLoader();
    private  String sqlFileName;
    private List<Resource> resourceList = new ArrayList<>();
    public SQLTemplateResolver(String sqlFileName) {
        this.sqlFileName = sqlFileName;
        loadIfPossible(sqlFileName);
        cfg.setTemplateLoader(sqlTemplateLoader);
    }

    /**
     * 获取模板SQL字符串
     * @param methodName 方法名
     * @param model 填充值
     * @return
     */
    public String getSql( String methodName, Map<String, Object> model) {
        try {
            StringWriter writer = new StringWriter();
            cfg.getTemplate(getTemplateKey(methodName), "UTF-8").process(model, writer);
            return writer.toString();
        } catch (Exception e) {
            log.error("处理模板 " + sqlFileName + " 方法:" + methodName, e);
            return StringUtils.EMPTY;
        }
    }

    public void loadIfPossible(final String sqlFileName) {
    try {
        ResourceLoader loader = new DefaultResourceLoader();
        Resource loaderResource = loader.getResource("classpath:sqls/" + sqlFileName + ".sftl");
        resourceList.add(loaderResource);
        for (Resource resource : resourceList) {
            Iterator<Void> iterator = new SftlTemplateResolver()
                    .doInTemplateResource(resource, (templateName, content) -> {
                        String key = getTemplateKey(templateName);
                        Object src = sqlTemplateLoader.findTemplateSource(key);
                        if (src != null) {
                            log.warn("发现重复的方法名, 请替换, 方法名:" + key);
                        }
                        sqlTemplateLoader.putTemplate(getTemplateKey(templateName), content);
                    });
            while (iterator.hasNext()) {
                iterator.next();
            }
        }
        } catch (Exception e) {
        log.error(e);
       }
    }

    private String getTemplateKey(String methodName) {
        return this.sqlFileName + ":" + methodName;
    }

}
