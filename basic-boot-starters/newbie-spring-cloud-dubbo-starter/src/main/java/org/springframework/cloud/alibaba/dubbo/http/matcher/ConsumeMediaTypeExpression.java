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

package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.springframework.http.MediaType;

/**
 * Parses and matches a single media type expression to a request's 'Content-Type' header.
 * <p>
 * The source code is scratched from
 * org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition.ConsumeMediaTypeExpression
 *
 * @Author Rossen Stoyanchev
 * @Author Arjen Poutsma
 */
class ConsumeMediaTypeExpression extends AbstractMediaTypeExpression {

    ConsumeMediaTypeExpression(String expression) {
        super(expression);
    }

    ConsumeMediaTypeExpression(MediaType mediaType, boolean negated) {
        super(mediaType, negated);
    }

    public final boolean match(MediaType contentType) {
        boolean match = getMediaType().includes(contentType);
        return (!isNegated() ? match : !match);
    }
}
