package com.example.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface SimpleMessageConsumer {
    String[] topics() default{};
}
