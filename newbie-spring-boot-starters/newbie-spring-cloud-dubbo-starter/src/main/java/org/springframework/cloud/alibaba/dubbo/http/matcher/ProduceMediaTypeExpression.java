
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.springframework.http.MediaType;

import java.util.List;

/**
 * Parses and matches a single media type expression to a request's 'Accept' header.
 * <p>
 * The source code is scratched from
 * org.springframework.web.servlet.mvc.condition.ProducesRequestCondition.ProduceMediaTypeExpression
 *
 * @author Rossen Stoyanchev
 * @author Arjen Poutsma
 */
class ProduceMediaTypeExpression extends AbstractMediaTypeExpression {

    ProduceMediaTypeExpression(String expression) {
        super(expression);
    }

    ProduceMediaTypeExpression(MediaType mediaType, boolean negated) {
        super(mediaType, negated);
    }

    public final boolean match(List<MediaType> acceptedMediaTypes) {
        boolean match = matchMediaType(acceptedMediaTypes);
        return (!isNegated() ? match : !match);
    }

    private boolean matchMediaType(List<MediaType> acceptedMediaTypes) {
        for (MediaType acceptedMediaType : acceptedMediaTypes) {
            if (getMediaType().isCompatibleWith(acceptedMediaType)) {
                return true;
            }
        }
        return false;
    }
}
