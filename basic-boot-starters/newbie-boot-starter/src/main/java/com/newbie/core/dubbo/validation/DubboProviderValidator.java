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
package com.newbie.core.dubbo.validation;

import com.newbie.core.exception.BusinessException;
import com.newbie.dto.ResponseTypes;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.validation.Validator;
import org.apache.dubbo.validation.support.jvalidation.JValidator;

import javax.validation.ConstraintViolationException;

public class DubboProviderValidator extends JValidator implements Validator {
    private final Class<?> clazz;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public DubboProviderValidator(URL url) {
        super(url);
        this.clazz = ReflectUtils.forName(url.getServiceInterface());
    }

    @Override
    public void validate(String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Exception {
        try{
            super.validate(methodName,parameterTypes,arguments);
        }catch (ConstraintViolationException e){
            StringBuilder details = ValidateInfoBuilder.info(e);
            throw new BusinessException(ResponseTypes.REMOTE_CALL_FAIL, details.toString());
        }
    }
}