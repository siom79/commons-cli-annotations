package com.google.code.commons.cli.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CliOption {

	String opt();

	String longOpt() default "";

	String description() default "";

	boolean required() default false;

	boolean hasArg() default false;

	boolean hasOptionalArg() default false;
}
