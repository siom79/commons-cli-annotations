package com.google.code.commons.cli.annotations.intern;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.Option;

import com.google.code.commons.cli.annotations.CliOption;

public class AnnotationParser {

	public <T> List<AnnotatedOption> createOptions(Class<T> clazz) {
		List<AnnotatedOption> options = new LinkedList<AnnotatedOption>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			CliOption annotation = field.getAnnotation(CliOption.class);
			if (annotation != null) {
				Option option = createOption(annotation);
				String fieldName = field.getName();
				AnnotatedOption annotatedOption = createAnnotatedOption(option, fieldName);
				options.add(annotatedOption);
			}
		}
		return options;
	}

	private AnnotatedOption createAnnotatedOption(Option option, String fieldName) {
		AnnotatedOption annotatedOption = new AnnotatedOption(option, fieldName);
		return annotatedOption;
	}

	private Option createOption(CliOption annotation) {
		String opt = Strings.nullToEmpty(annotation.opt());
		String description = Strings.nullToEmpty(annotation.description());
		Option option = new Option(opt, description);
		String longOpt = Strings.nullToEmpty(annotation.longOpt());
		if (longOpt.length() > 0) {
			option.setLongOpt(longOpt);
		}
		option.setRequired(annotation.required());
		boolean hasArg = annotation.hasArg();
		if (hasArg) {
			option.setArgs(1);
		}
		boolean hasOptionalArg = annotation.hasOptionalArg();
		if (hasOptionalArg) {
			option.setOptionalArg(hasOptionalArg);
			option.setArgs(1);
		}
		return option;
	}
}
