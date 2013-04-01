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
					if (option.hasArg() || option.hasOptionalArg()) {
						Class<?> type = annotatedOption.getField().getType();
						String optionValue = line.getOptionValue(opt);
						Object convertedObject = convertStringToObject(opt, optionValue, type);
						try {
							PropertyUtils.setSimpleProperty(instanceOfT, annotatedOption.getField().getName(),
									convertedObject);
						} catch (Exception e) {
							throw new ParserException(Reason.SettingValueFailed, String.format(
									"Setting value %s to property %s of class %s failed: %s.", optionValue,
									annotatedOption.getField().getName(), instanceOfT.getClass().getName(),
									e.getMessage()), e);
						}
					} else {
						try {
							PropertyUtils.setSimpleProperty(instanceOfT, annotatedOption.getField().getName(), true);
						} catch (Exception e) {
							throw new ParserException(Reason.SettingValueFailed, String.format(
									"Setting value %s to property %s of class %s failed: %s.", true, annotatedOption
											.getField().getName(), instanceOfT.getClass().getName(), e.getMessage()), e);
						}
					}
				}
			}
		} catch (ParseException e) {
			throw new ParserException(Reason.CommonsCliParseException, e.getMessage(), e);
		}
		return instanceOfT;
	}

	private Object convertStringToObject(String opt, String optionValue, Class<?> type) throws ParserException {
		if (String.class.equals(type)) {
			return optionValue;
		} else if (int.class.equals(type) || Integer.class.equals(type)) {
			try {
				return Integer.parseInt(optionValue);
			} catch (NumberFormatException e) {
				throw new ParserException(Reason.ConvertingValueFailed, String.format(
						"Converting value for option '%s' to an integer value failed: %s.", opt, e.getMessage()), e);
			}
		} else if (long.class.equals(type) || Long.class.equals(type)) {
			try {
				return Long.parseLong(optionValue);
			} catch (NumberFormatException e) {
				throw new ParserException(Reason.ConvertingValueFailed, String.format(
						"Converting value for option '%s' to a long value failed: %s.", opt, e.getMessage()), e);
			}
		} else if (float.class.equals(type) || Float.class.equals(type)) {
			try {
				return Float.parseFloat(optionValue);
			} catch (NumberFormatException e) {
				throw new ParserException(Reason.ConvertingValueFailed, String.format(
						"Converting value for option '%s' to a floating value failed: %s.", opt, e.getMessage()), e);
			}
		} else if (double.class.equals(type) || Double.class.equals(type)) {
			try {
				return Double.parseDouble(optionValue);
			} catch (NumberFormatException e) {
				throw new ParserException(Reason.ConvertingValueFailed,
						String.format("Converting value for option '%s' to a double precision value failed: %s.", opt,
								e.getMessage()), e);
			}
		} else {
			throw new IllegalStateException(String.format("Unsupported type '%s' of field.", type.getName()));
		}
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
