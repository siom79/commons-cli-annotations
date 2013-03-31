package com.google.code.commons.cli.annotations.intern;

import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.code.commons.cli.annotations.ParserException;
import com.google.code.commons.cli.annotations.ParserException.Reason;

public class CliProcessor {

	private CommandLineParser commandLineParser;

	public CliProcessor(CommandLineParser commandLineParser) {
		this.commandLineParser = commandLineParser;
	}

	public <T> T parse(Class<T> clazz, List<AnnotatedOption> annotatedOptions, String args[]) throws ParserException {
		T instanceOfT = createInstanceOfT(clazz);
		Options options = createOptionsFromAnnotatedOptions(annotatedOptions);
		try {
			CommandLine line = this.commandLineParser.parse(options, args);
			for (AnnotatedOption annotatedOption : annotatedOptions) {
				Option option = annotatedOption.getOption();
				String opt = option.getOpt();
				if (line.hasOption(opt)) {
					if (option.hasArg()) {
						String optionValue = line.getOptionValue(opt);
						try {
							PropertyUtils.setSimpleProperty(instanceOfT, annotatedOption.getFieldName(), optionValue);
						} catch (Exception e) {
							throw new ParserException(Reason.SettingValueFailed, String.format(
									"Setting value %s to property %s of class %s failed: %s.", optionValue,
									annotatedOption.getFieldName(), instanceOfT.getClass().getName(), e.getMessage()),
									e);
						}
					} else {
						try {
							PropertyUtils.setSimpleProperty(instanceOfT, annotatedOption.getFieldName(), true);
						} catch (Exception e) {
							throw new ParserException(Reason.SettingValueFailed, String.format(
									"Setting value %s to property %s of class %s failed: %s.", true,
									annotatedOption.getFieldName(), instanceOfT.getClass().getName(), e.getMessage()),
									e);
						}
					}
				}
			}
		} catch (ParseException e) {
			throw new ParserException(Reason.CommonsCliParseException, e.getMessage(), e);
		}
		return instanceOfT;
	}

	private Options createOptionsFromAnnotatedOptions(List<AnnotatedOption> annotatedOptions) {
		Options options = new Options();
		for (AnnotatedOption annotatedOption : annotatedOptions) {
			options.addOption(annotatedOption.getOption());
		}
		return options;
	}

	private <T> T createInstanceOfT(Class<T> clazz) throws ParserException {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new ParserException(Reason.Instantiation, String.format("Creating an instance of %s failed: %s.",
					clazz.getName(), e.getMessage()), e);
		}
	}
}
