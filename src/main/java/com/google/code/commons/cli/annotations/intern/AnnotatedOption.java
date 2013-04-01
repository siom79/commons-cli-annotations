package com.google.code.commons.cli.annotations.intern;

import java.lang.reflect.Field;

import org.apache.commons.cli.Option;

public class AnnotatedOption {
	private final Option option;
	private final Field field;

	public AnnotatedOption(Option option, Field field) {
		super();
		this.option = option;
		this.field = field;
	}

	public Option getOption() {
		return option;
	}

	public Field getField() {
		return field;
	}
}
