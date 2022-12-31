package org.jeecg.common.aspect.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomCode {
    String objectCode() default "";
}
