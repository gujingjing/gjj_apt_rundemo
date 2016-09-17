package com.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//这是一个编译时注解，用@Retention(RetentionPolicy.CLASS)修饰
@Retention(RetentionPolicy.CLASS)
//这个注解只能修饰方法。用@Target(ElementType.METHOD)修饰
@Target(ElementType.METHOD)
public @interface GetMsg {
    int id();
    String name() default "default";
}
