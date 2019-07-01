/*
 * 版权所有 (c) 2019-2029, halower (halower@foxmail.com).
 *
 * Apache 2.0 License 同时该协议为补充协议，不允许 996 工作制度企业使用该开源软件
 *
 * 反996许可证版本1.0
 *
 * 在符合下列条件的情况下，特此免费向任何得到本授权作品的副本（包括源代码、文件和/或相关内容，以下
 * 统称为“授权作品”）的个人和法人实体授权：被授权个人或法人实体有权以任何目的处置授权作品，包括但
 * 不限于使用、复制，修改，衍生利用、散布，发布和再许可：
 *
 * 1. 个人或法人实体必须在许可作品的每个再散布或衍生副本上包含以上版权声明和本许可证，不得自行修
 * 改。
 * 2. 个人或法人实体必须严格遵守与个人实际所在地或个人出生地或归化地、或法人实体注册地或经营地
 * （以较严格者为准）的司法管辖区所有适用的与劳动和就业相关法律、法规、规则和标准。如果该司法管辖
 * 区没有此类法律、法规、规章和标准或其法律、法规、规章和标准不可执行，则个人或法人实体必须遵守国
 * 际劳工标准的核心公约。
 * 3. 个人或法人不得以任何方式诱导、暗示或强迫其全职或兼职员工或其独立承包人以口头或书面形式同意直接或
 * 间接限制、削弱或放弃其所拥有的，受相关与劳动和就业有关的法律、法规、规则和标准保护的权利或补救
 * 措施，无论该等书面或口头协议是否被该司法管辖区的法律所承认，该等个人或法人实体也不得以任何方法
 * 限制其雇员或独立承包人向版权持有人或监督许可证合规情况的有关当局报告或投诉上述违反许可证的行为
 * 的权利。
 *
 * 该授权作品是"按原样"提供，不做任何明示或暗示的保证，包括但不限于对适销性、特定用途适用性和非侵
 * 权性的保证。在任何情况下，无论是在合同诉讼、侵权诉讼或其他诉讼中，版权持有人均不承担因本软件或
 * 本软件的使用或其他交易而产生、引起或与之相关的任何索赔、损害或其他责任。
 */
package org.springframework.cloud.alibaba.dubbo.env;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.apache.dubbo.common.Constants.DEFAULT_PROTOCOL;
import static org.apache.dubbo.config.spring.util.PropertySourcesUtils.getSubProperties;

/**
 * Dubbo {@link WebApplicationType#NONE Non-Web Application} {@link EnvironmentPostProcessor}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class DubboNonWebApplicationEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final String DOT = ".";

    /**
     * The name of default {@link PropertySource} defined in SpringApplication#configurePropertySources method.
     */
    private static final String PROPERTY_SOURCE_NAME = "defaultProperties";

    private static final String SERVER_PORT_PROPERTY_NAME = "server.port";

    private static final String PORT_PROPERTY_NAME = "port";

    private static final String PROTOCOL_PROPERTY_NAME_PREFIX = "dubbo.protocol";

    private static final String PROTOCOL_NAME_PROPERTY_NAME_SUFFIX = DOT + "name";

    private static final String PROTOCOL_PORT_PROPERTY_NAME_SUFFIX = DOT + PORT_PROPERTY_NAME;

    private static final String PROTOCOL_PORT_PROPERTY_NAME = PROTOCOL_PROPERTY_NAME_PREFIX + PROTOCOL_PORT_PROPERTY_NAME_SUFFIX;

    private static final String PROTOCOL_NAME_PROPERTY_NAME = PROTOCOL_PROPERTY_NAME_PREFIX + PROTOCOL_NAME_PROPERTY_NAME_SUFFIX;

    private static final String PROTOCOLS_PROPERTY_NAME_PREFIX = "dubbo.protocols";

    private static final String REST_PROTOCOL = "rest";

    private final Logger logger = LoggerFactory.getLogger(DubboNonWebApplicationEnvironmentPostProcessor.class);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        WebApplicationType webApplicationType = application.getWebApplicationType();

        if (!WebApplicationType.NONE.equals(webApplicationType)) { // Just works in Non-Web Application
            if (logger.isDebugEnabled()) {
                logger.debug("Current application is a Web Application, the process will be ignored.");
            }
            return;
        }

        MutablePropertySources propertySources = environment.getPropertySources();
        Map<String, Object> defaultProperties = createDefaultProperties(environment);
        if (!CollectionUtils.isEmpty(defaultProperties)) {
            addOrReplace(propertySources, defaultProperties);
        }
    }

    private Map<String, Object> createDefaultProperties(ConfigurableEnvironment environment) {
        Map<String, Object> defaultProperties = new HashMap<String, Object>();
        resetServerPort(environment, defaultProperties);
        return defaultProperties;
    }

    /**
     * Reset server port property if it's absent, whose value is configured by "dubbbo.protocol.port"
     * or "dubbo.protcols.rest.port"
     *
     * @param environment
     * @param defaultProperties
     */
    private void resetServerPort(ConfigurableEnvironment environment, Map<String, Object> defaultProperties) {

        String serverPort = environment.getProperty(SERVER_PORT_PROPERTY_NAME, environment.getProperty(PORT_PROPERTY_NAME));

        if (serverPort != null) {
            return;
        }

        serverPort = getRestPortFromProtocolProperty(environment);

        if (serverPort == null) {
            serverPort = getRestPortFromProtocolsProperties(environment);
        }

        setServerPort(environment, serverPort, defaultProperties);
    }

    private String getRestPortFromProtocolProperty(ConfigurableEnvironment environment) {

        String protocol = environment.getProperty(PROTOCOL_NAME_PROPERTY_NAME, DEFAULT_PROTOCOL);

        return isRestProtocol(protocol) ?
                environment.getProperty(PROTOCOL_PORT_PROPERTY_NAME) :
                null;
    }

    private String getRestPortFromProtocolsProperties(ConfigurableEnvironment environment) {

        String restPort = null;

        Map<String, Object> subProperties = getSubProperties(environment, PROTOCOLS_PROPERTY_NAME_PREFIX);

        Properties properties = new Properties();

        properties.putAll(subProperties);

        for (String propertyName : properties.stringPropertyNames()) {
            if (propertyName.endsWith(PROTOCOL_NAME_PROPERTY_NAME_SUFFIX)) { // protocol name property
                String protocol = properties.getProperty(propertyName);
                if (isRestProtocol(protocol)) {
                    String beanName = resolveBeanName(propertyName);
                    if (StringUtils.hasText(beanName)) {
                        restPort = properties.getProperty(beanName + PROTOCOL_PORT_PROPERTY_NAME_SUFFIX);
                        break;
                    }
                }
            }
        }

        return restPort;
    }

    private String resolveBeanName(String propertyName) {
        int index = propertyName.indexOf(DOT);
        return index > -1 ? propertyName.substring(0, index) : null;
    }

    private void setServerPort(ConfigurableEnvironment environment, String serverPort,
                               Map<String, Object> defaultProperties) {
        if (serverPort == null) {
            return;
        }

        defaultProperties.put(SERVER_PORT_PROPERTY_NAME, serverPort);

    }

    /**
     * Copy from BusEnvironmentPostProcessor#addOrReplace(MutablePropertySources, Map)
     *
     * @param propertySources {@link MutablePropertySources}
     * @param map             Default Dubbo Properties
     */
    private void addOrReplace(MutablePropertySources propertySources,
                              Map<String, Object> map) {
        MapPropertySource target = null;
        if (propertySources.contains(PROPERTY_SOURCE_NAME)) {
            PropertySource<?> source = propertySources.get(PROPERTY_SOURCE_NAME);
            if (source instanceof MapPropertySource) {
                target = (MapPropertySource) source;
                for (String key : map.keySet()) {
                    if (!target.containsProperty(key)) {
                        target.getSource().put(key, map.get(key));
                    }
                }
            }
        }
        if (target == null) {
            target = new MapPropertySource(PROPERTY_SOURCE_NAME, map);
        }
        if (!propertySources.contains(PROPERTY_SOURCE_NAME)) {
            propertySources.addLast(target);
        }
    }

    @Override
    public int getOrder() { // Keep LOWEST_PRECEDENCE
        return LOWEST_PRECEDENCE;
    }

    private static boolean isRestProtocol(String protocol) {
        return REST_PROTOCOL.equalsIgnoreCase(protocol);
    }
}
