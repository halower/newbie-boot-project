
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.springframework.http.MediaType;

/**
 * Parses and matches a single media type expression to a request's 'Content-Type' header.
 * <p>
 * The source code is scratched from
 * org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition.ConsumeMediaTypeExpression
 *
 * @author Rossen Stoyanchev
 * @author Arjen Poutsma
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
