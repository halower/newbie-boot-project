
package org.springframework.cloud.alibaba.dubbo.metadata;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;
import java.util.Set;

/**
 * Service Rest Metadata
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see RestMethodMetadata
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceRestMetadata {

    private String url;

    private Set<RestMethodMetadata> meta;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<RestMethodMetadata> getMeta() {
        return meta;
    }

    public void setMeta(Set<RestMethodMetadata> meta) {
        this.meta = meta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceRestMetadata)) {
            return false;
        }
        ServiceRestMetadata that = (ServiceRestMetadata) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(meta, that.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, meta);
    }
}
