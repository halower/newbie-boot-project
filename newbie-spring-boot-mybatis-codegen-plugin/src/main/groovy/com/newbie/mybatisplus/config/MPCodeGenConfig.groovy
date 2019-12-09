package com.newbie.mybatisplus.config

import com.baomidou.mybatisplus.generator.config.DataSourceConfig
import com.baomidou.mybatisplus.generator.config.GlobalConfig
import com.baomidou.mybatisplus.generator.config.PackageConfig
import com.baomidou.mybatisplus.generator.config.StrategyConfig
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

/**
 * @Author: halower
 * @Date: 2019/6/26 14:09
 *
 */
class MPCodeGenConfig {
    GlobalConfig globalConfig
    DataSourceConfig dataSourceConfig
    PackageConfig packageConfig
    XmlMapperConfig xmlMapperConfig
    StrategyConfig strategyConfig

    @Inject
    MPCodeGenConfig(ObjectFactory objectFactory) {
       //创建配置对象
        globalConfig = objectFactory.newInstance(GlobalConfig)
        dataSourceConfig = objectFactory.newInstance(DataSourceConfig)
        packageConfig = objectFactory.newInstance(PackageConfig)
        xmlMapperConfig = objectFactory.newInstance(XmlMapperConfig)
        strategyConfig = objectFactory.newInstance(StrategyConfig)
    }

    void globalConfig(Action<? super GlobalConfig> action) {
        action.execute(globalConfig)
    }

    void dataSourceConfig(Action<? super DataSourceConfig> action) {
        action.execute(dataSourceConfig)
    }

    void packageConfig(Action<? super PackageConfig> action) {
        action.execute(packageConfig)
    }

    void xmlMapperConfig(Action<? super XmlMapperConfig> action) {
        action.execute(xmlMapperConfig)
    }

    void strategyConfig(Action<? super StrategyConfig> action) {
        action.execute(strategyConfig)
    }

    @Override
    String toString() {
        return "MPCodeGenConfig{ " +
                "globalConfig=" + globalConfig +
                ", dataSourceConfig=" + dataSourceConfig +
                ", packageConfig=" + packageConfig +
                ", xmlMapperConfig=" + xmlMapperConfig +
                ", strategyConfig=" + strategyConfig +
           "}"
    }
}
