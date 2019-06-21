
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.springframework.http.MediaType;

/**
 * The some source code is scratched from org.springframework.web.servlet.mvc.condition.AbstractMediaTypeExpression
 *
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class AbstractMediaTypeExpression implements MediaTypeExpression, Comparable<AbstractMediaTypeExpression> {

    private final MediaType mediaType;

    private final boolean negated;

    AbstractMediaTypeExpression(String expression) {
        if (expression.startsWith("!")) {
            this.negated = true;
            expression = expression.substring(1);
        } else {
            this.negated = false;
        }
        this.mediaType = MediaType.parseMediaType(expression);
    }

    AbstractMediaTypeExpression(MediaType mediaType, boolean negated) {
        this.mediaType = mediaType;
        this.negated = negated;
    }

    @Override
    public MediaType getMediaType() {
        return this.mediaType;
    }

    @Override
    public boolean isNegated() {
        return this.negated;
    }


    @Override
    public int compareTo(AbstractMediaTypeExpression other) {
        return MediaType.SPECIFICITY_COMPARATOR.compare(this.getMediaType(), other.getMediaType());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        AbstractMediaTypeExpression otherExpr = (AbstractMediaTypeExpression) other;
        return (this.mediaType.equals(otherExpr.mediaType) && this.negated == otherExpr.negated);
    }

    @Override
    public int hashCode() {
        return this.mediaType.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.negated) {
            builder.append('!');
        }
        builder.append(this.mediaType.toString());
        return builder.toString();
    }
}
