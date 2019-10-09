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

package com.newbie.core.utils;

public class Utils {

    /**
     * Json与Java对象互转
     */
    public static JsonUtil json = JsonUtil.pick("");

    /**
     * Json与Java对象互转
     * <p>
     * 使用自定义实例ID（用于支持不同Json配置）
     */
    public static JsonUtil json(String instanceId) {
        assert instanceId != null && !instanceId.trim().equals("");
        return JsonUtil.pick(instanceId);
    }

    /**
     * Java Bean操作
     */
    public static BeanUtil bean = new BeanUtil();

    /**
     * Java Bean操作
     *
     * @param useCache 是否启用缓存，启用后会缓存获取过的字段和方法列表，默认启用
     */
    public static BeanUtil bean(boolean useCache) {
        return new BeanUtil(useCache);
    }

    /**
     * 常用字段操作
     */
    public static FieldUtil field = new FieldUtil();

    /**
     * 常用文件操作
     */
    public static FileUtil file = new FileUtil();

    /**
     * MIME信息操作
     */
    public static MimeUtil mime = new MimeUtil();

    /**
     * 时间操作
     */
    public static DateTimeUtil datetime = new DateTimeUtil();

    /**
     * 金额操作
     */
    public static AmountUtil amount = new AmountUtil();

    /**
     * 网络操作
     */
    public static NetworkUtil network = new NetworkUtil();

    /**
     * 随机数
     */
    public static RandomUtil random = new RandomUtil();

    /**
     * Unicode字符转换
     */
    public static UnicodeConvertUtil unicode = new UnicodeConvertUtil();
}
