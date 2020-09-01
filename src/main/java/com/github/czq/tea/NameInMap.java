package com.github.czq.tea;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface NameInMap {
    String value();

    String[] alternate() default {};
}