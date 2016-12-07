package me.protox.jersey.ext.config_property;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fengzh on 12/7/16.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigProperty {
    String name();
    String defaultValue() default "";
    boolean mustPresent() default true;
    boolean allowBlank() default false;
}
