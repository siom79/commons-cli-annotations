package com.google.code.commons.cli.annotations;

public class ParserException extends Exception {
	private static final long serialVersionUID = 1L;

	public enum Reason {
		Instantiation, CommonsCliParseException, SettingValueFailed, ReflectionException, AnnotatedPropertyNotWriteable, AnnotatedPropertyNotBoolean, AnnotatedPropertyTypeNotSupported
	};

	private Reason reason;

	public ParserException(Reason reason, String msg) {
		super(msg);
		this.reason = reason;
	}

	public ParserException(Reason reason, String msg, Throwable t) {
		super(msg, t);
		this.reason = reason;
	}

	public Reason getReason() {
		return reason;
	}
}
