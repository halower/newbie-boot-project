package com.newbie.core.persistent.tpl;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
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
 * @Author: 谢海龙
 * @Date: 2019/4/17 9:18
 * @Description
 */
@Log4j2
public class JPQLResolver {
    private static Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
    private static StringTemplateLoader sqlTemplateLoader = new StringTemplateLoader();
    private String sqlFileName;
    public JPQLResolver(String sqlFileName) {
        this.sqlFileName = sqlFileName;
        loadIfPossible(sqlFileName);
    }
    static {
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
        List<Resource> resourceList = new ArrayList<>();
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
