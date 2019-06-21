
package org.springframework.cloud.alibaba.dubbo.metadata;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * {@link Method} Parameter Metadata
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MethodParameterMetadata {

    private int index;

    private String name;

    private String type;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MethodParameterMetadata that = (MethodParameterMetadata) o;
        return index == that.index &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, name, type);
    }

    @Override
    public String toString() {
        return "MethodParameterMetadata{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
