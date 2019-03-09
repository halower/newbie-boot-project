package com.newbie.core.persistent.tpls;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;

@org.springframework.context.annotation.Configuration
public class SQLParser {
    /***
     * 解析SQL模板
     * @param templateName 模板名称 例如: Blog.sftl
     * @param methodName 方法名
     * @param map 键值对
     * @return  还原后的字符串
     */

    @SneakyThrows
    @Cacheable(value = "#templateName",key="#methodName")
    public String parse(String templateName, String methodName, Map<String,Object> map) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setDirectoryForTemplateLoading(ResourceUtils.getFile("classpath:sqls"));
        cfg.setDefaultEncoding("UTF-8");
        StringWriter result = new StringWriter();
        Template temp = new Template(methodName,getTemplateContent(templateName,methodName), cfg);;
        temp.process(map, result);
        return result.toString();
    }

    @SneakyThrows
    private static String getTemplateContent(String templateName, String methodName) {
        try(
                InputStream inputStream = SQLParser.class.getResourceAsStream("/sqls/" + templateName + ".sftl");
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader  bufferedReader = new BufferedReader(reader);
           ){
            String text;
            StringBuilder sb = new StringBuilder();
            while ((text = bufferedReader.readLine()) != null) {
                sb.append(text);
            }
            String content =  sb.toString();
            String[] templates = content.split("##METHOD");
            String resTpl = Arrays.stream(templates).filter(tpl ->tpl.contains(methodName)).findFirst().get();
            resTpl = resTpl.replaceAll(methodName,"").trim();
            return resTpl;
        }
    }
}
