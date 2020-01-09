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
package com.newbie.swagger;

import java.util.List;

public interface SwaggerReader {
    /**
     * 是否列入swagger清单
     * @param path 请求路径
     * @param description 描述
     * @param requestMethod 请求方式
     * @return 列入swagger清单结果 （true为显示在清单中,反之亦然)
     */
     boolean listed(String path, String description, String requestMethod);

    /**
     * 获取所有展示Swagger测试按钮的路径
     * @return
     */
    List<String> listTryItOutPaths();
}
