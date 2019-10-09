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

package com.newbie.core.datasource;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: halower
 * @Date: 2019/8/1 15:35
 * @Description
 */
public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<DatabaseSourceKey> contextHolder = new ThreadLocal<>();

    /**
     * 管理所有的数据源id
     * 主要是为了判断数据源是否存在
     */
    public static List<DatabaseSourceKey> databaseSourceKeys = new ArrayList<>();

    /**
     * 设置数据源
     * @param dataSourceType
     */
    public static void setDataSourceType(DatabaseSourceKey dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    /**
     * 获取数据源
     * @return
     */
    public static DatabaseSourceKey getDataSourceType() {
        return contextHolder.get();
    }

    /**
     * 清除数据源
     */
    public static void clearDataSourceType() {
        contextHolder.remove();
    }

    /**
     *  判断指定的DataSource是否存在
     */
    public static boolean containsDataSource(DatabaseSourceKey dataSourceId) {
        return databaseSourceKeys.contains(dataSourceId);
    }
}
