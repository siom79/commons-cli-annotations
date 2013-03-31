package com.google.code.commons.cli.annotations.intern;

import org.apache.commons.cli.Option;

public class AnnotatedOption {
	private final Option option;
	private final String fieldName;

	public AnnotatedOption(Option option, String fieldName) {
		super();
		this.option = option;
		this.fieldName = fieldName;
	}

	public Option getOption() {
		return option;
	}

	public String getFieldName() {
		return fieldName;
	}
}
