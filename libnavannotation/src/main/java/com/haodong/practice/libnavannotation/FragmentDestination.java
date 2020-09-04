package com.haodong.practice.libnavannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * created by linghaoDo on 2020/9/2
 * description:
 * <p>
 * version:
 */
@Target(ElementType.TYPE)
public @interface FragmentDestination {
    String pageUrl();

    boolean needLogin() default false;

    boolean asStarter() default false;
}
