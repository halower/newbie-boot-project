package com.newbie.mybatisplus

import com.baomidou.mybatisplus.core.toolkit.StringUtils
import com.baomidou.mybatisplus.generator.AutoGenerator
import com.baomidou.mybatisplus.generator.InjectionConfig
import com.baomidou.mybatisplus.generator.config.*
import com.baomidou.mybatisplus.generator.config.po.TableInfo
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine
import com.newbie.mybatisplus.config.MPCodeGenConfig
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @Author: 谢海龙
 * @Date: 2019/6/26 14:21
 * @Description
 */
class MPCodeGenPlugin implements Plugin<Project> {
    def mockTestUrl = "jdbc:mysql://192.168.2.169:3306/newbie?serverTimezone=GMT%2B8&useSSL=false"
    @Override
    void apply(Project project) {
        MPCodeGenConfig mpCodeGenConfig =  project.extensions.create('mybatis', MPCodeGenConfig, project.objects)

        if (mpCodeGenConfig != null) {
            autoCodeGenTask(project, mpCodeGenConfig)
        }
    }

    /**
     * 代码生成任务
     * @param project
     * @param codeGenConfig
     */
    private void autoCodeGenTask(Project project, MPCodeGenConfig config) {
        def task = project.tasks.create("代码生成")
        task.group = 'toolkit'
        task.doLast {
            AutoGenerator mpg = new AutoGenerator()
            // 全局配置
            GlobalConfig gc = config.globalConfig
            String projectPath = System.getProperty("user.dir")
            gc.setOutputDir(projectPath + "/src/main/java")
            gc.setOpen(false)
            mpg.setGlobalConfig(gc)
            // 数据源配置
            DataSourceConfig dsc = config.dataSourceConfig
            if(dsc.getDriverName()==null)dsc.setDriverName("com.mysql.cj.jdbc.Driver")
            if(dsc.getUrl()==null)dsc.setUrl(mockTestUrl)
            if(dsc.getUsername()==null)dsc.setUsername("root")
            if(dsc.getPassword()==null)dsc.setPassword("123456")
            mpg.setDataSource(dsc);
            // 包配置
            PackageConfig pc = config.packageConfig
            pc.setEntity("domain")
            pc.setController("portal")
            pc.setMapper("infrastructure")
            pc.setService("application")
            pc.setServiceImpl("application.impl")
            mpg.setPackageInfo(pc)
            // 自定义配置
            InjectionConfig cfg = new InjectionConfig() {
                @Override
                void initMap() {
                    // 初始化
                }
            }

            // 自定义输出配置
            List<FileOutConfig> focList = new ArrayList<>()
            // 自定义配置会被优先输出
            focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {

                @Override
                String outputFile(TableInfo tableInfo) {
                    def xmlMapperConfig = config.xmlMapperConfig
                    if (xmlMapperConfig == null
                            || StringUtils.isEmpty(xmlMapperConfig.path)
                            || StringUtils.isEmpty(xmlMapperConfig.name)) {
                        def path = project.projectDir.path + "/src/main/resources/mapper/"
                        return path + tableInfo.getEntityName() + "Mapper.xml"
                    }
                    // 自定义输出文件名
                    return xmlMapperConfig.path + xmlMapperConfig.name
                }
            })

            cfg.setFileOutConfigList(focList)
            mpg.setCfg(cfg)

            // 配置模板
            TemplateConfig templateConfig = new TemplateConfig()
            templateConfig.setXml(null)
            mpg.setTemplate(templateConfig)
            // 策略配置
            StrategyConfig strategy = config.strategyConfig
            strategy.setNaming(NamingStrategy.underline_to_camel);
            strategy.setColumnNaming(NamingStrategy.underline_to_camel);
            strategy.setEntityLombokModel(true);
            strategy.setRestControllerStyle(true);
            mpg.setStrategy(strategy)
            mpg.setTemplateEngine(new FreemarkerTemplateEngine())
            mpg.execute()
        }
    }
}
