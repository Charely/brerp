package org.jeecg.common.aspect.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BillCodeRule {

    String billcodeRule() default "";
}