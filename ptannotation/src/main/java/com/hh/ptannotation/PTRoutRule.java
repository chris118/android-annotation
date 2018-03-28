package com.hh.ptannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by chrisw on 2018/3/27.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface PTRoutRule {
    String[] value();
}
