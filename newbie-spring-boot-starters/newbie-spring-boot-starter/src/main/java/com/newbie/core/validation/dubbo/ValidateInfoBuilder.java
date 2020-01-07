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
package com.newbie.core.validation.dubbo;

import lombok.extern.log4j.Log4j2;
import lombok.var;

import javax.validation.ConstraintViolationException;

@Log4j2
public class ValidateInfoBuilder {
    public static StringBuilder info(ConstraintViolationException e) {
        StringBuilder details = new StringBuilder();
        log.error("远程调用参数绑定异常",  e);
        var violations = e.getConstraintViolations().iterator();
        while (violations.hasNext()) {
            var violation =  violations.next();
            var property = violation.getPropertyPath().toString();
            var message =  violation.getMessage();
            details.append(String.format("%s : %s",property, message));
        }
        return details;
    }
}
