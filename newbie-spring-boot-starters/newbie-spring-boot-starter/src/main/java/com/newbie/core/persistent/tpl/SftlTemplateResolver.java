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

package com.newbie.core.persistent.tpl;

import lombok.var;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

import java.util.Iterator;
import java.util.List;

/**
 * @Author: halower
 * @Date: 2019/4/17 13:35
 *
 */
public class SftlTemplateResolver implements TemplateResolver {

    private String encoding = "UTF-8";

    @Override
    public Iterator<Void> doInTemplateResource(Resource resource, final TemplateCallback callback)
            throws Exception {
        var inputStream = resource.getInputStream();
        final List<String> lines = IOUtils.readLines(inputStream, encoding);
        return new Iterator<Void>() {
            String name;

            StringBuilder content = new StringBuilder();

            int index = 0;

            int total = lines.size();

            @Override
            public boolean hasNext() {
                return index < total;
            }

            @Override
            public Void next() {
                do {
                    String line = lines.get(index);
                    if(!isCommentLine(line)) {
                        if (isNameLine(line)) {
                            name = StringUtils.trim(StringUtils.remove(line, "--"));
                        }
                        else {
                            line = StringUtils.trimToNull(line);
                            if (line != null) {
                                content.append(line).append(" ");
                            }
                        }
                    }
                    index++;
                } while (!isLastLine() && !isNextNameLine());

                callback.process(name, content.toString());
                name = null;
                content = new StringBuilder();
                return null;
            }

            private boolean isNameLine(String line) {
                return StringUtils.contains(line, "--");
            }

            private boolean isCommentLine(String line) {
                return StringUtils.startsWith(line, "#");
            }

            private boolean isNextNameLine() {
                String line = lines.get(index);
                return isNameLine(line);
            }

            private boolean isLastLine() {
                return index == total;
            }
        };
    }
}

