package com.google.code.commons.cli.annotations.intern;

public class Strings {

	public static String nullToEmpty(String opt) {
		if (opt == null) {
			opt = "";
		}
		return opt;
	}

}
