package com.newbie.core.persistent.sql;

import lombok.var;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

import java.util.Iterator;
import java.util.List;

/**
 * @Author: 谢海龙
 * @Date: 2019/4/17 13:35
 * @Description
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
                    if (isNameLine(line)) {
                        name = StringUtils.trim(StringUtils.remove(line, "--"));
                    }
                    else {
                        line = StringUtils.trimToNull(line);
                        if (line != null) {
                            content.append(line).append(" ");
                        }
                    }
                    index++;
                } while (!isLastLine() && !isNextNameLine());

                //next template
                callback.process(name, content.toString());
                name = null;
                content = new StringBuilder();
                return null;
            }

            @Override
            public void remove() {
                //ignore
            }

            private boolean isNameLine(String line) {
                return StringUtils.contains(line, "--");
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

