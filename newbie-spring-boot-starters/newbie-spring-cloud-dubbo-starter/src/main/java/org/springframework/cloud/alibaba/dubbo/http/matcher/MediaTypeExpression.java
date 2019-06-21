
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.springframework.http.MediaType;

/**
 * A contract for media type expressions (e.g. "text/plain", "!text/plain") as
 * defined in the for "consumes" and "produces".
 * <p>
 * The source code is scratched from org.springframework.web.servlet.mvc.condition.MediaTypeExpression
 *
 * @author Rossen Stoyanchev
 */
interface MediaTypeExpression {

    MediaType getMediaType();

    boolean isNegated();

}
