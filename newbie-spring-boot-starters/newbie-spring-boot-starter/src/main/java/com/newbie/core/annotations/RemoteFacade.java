package com.newbie.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * remote facade annotation
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RemoteFacade {
    /**
     * 是否为内部接口
     * @return
     */
     boolean isInner() default  false;

    /**
     * 权限Key
     * @return
     */
     String  value() default "";
}
