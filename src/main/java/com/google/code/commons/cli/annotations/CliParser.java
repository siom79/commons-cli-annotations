package com.google.code.commons.cli.annotations;

import java.util.List;

import org.apache.commons.cli.CommandLineParser;

import com.google.code.commons.cli.annotations.intern.AnnotatedOption;
import com.google.code.commons.cli.annotations.intern.AnnotationParser;
import com.google.code.commons.cli.annotations.intern.AnnotationVerifier;
import com.google.code.commons.cli.annotations.intern.CliProcessor;

public class CliParser {

	private CommandLineParser commandLineParser;

	public CliParser(CommandLineParser commandLineParser) {
		this.commandLineParser = commandLineParser;
	}

	public <T> T parse(Class<T> clazz, String args[]) throws ParserException {
		List<AnnotatedOption> options = getOptionsFromClass(clazz);
		AnnotationVerifier annotationVerifier = new AnnotationVerifier();
		annotationVerifier.verify(options, clazz);
		CliProcessor cliParser = new CliProcessor(this.commandLineParser);
		T instanceOfT = cliParser.parse(clazz, options, args);
		return instanceOfT;
	}

	public List<AnnotatedOption> getOptionsFromClass(Class<?> clazz) {
		AnnotationParser annotationParser = new AnnotationParser();
		return annotationParser.createOptions(clazz);
	}
}
