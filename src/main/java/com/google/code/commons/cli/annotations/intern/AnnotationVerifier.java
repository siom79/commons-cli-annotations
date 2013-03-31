package com.google.code.commons.cli.annotations.intern;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

import com.google.code.commons.cli.annotations.ParserException;
import com.google.code.commons.cli.annotations.ParserException.Reason;

public class AnnotationVerifier {

	public void verify(List<AnnotatedOption> options, Class<?> clazz) throws ParserException {
		verifyAllAnnotatedPropertiesWriteable(options, clazz);
		verifyNoArgsOptionsAreBoolean(options, clazz);
	}

	void verifyNoArgsOptionsAreBoolean(List<AnnotatedOption> options, Class<?> clazz) throws ParserException {
		for (AnnotatedOption annotatedOption : options) {
			if (annotatedOption.getOption().getArgs() <= 0) {
				String fieldName = annotatedOption.getFieldName();
				try {
					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, clazz);
					Class<?> propertyType = propertyDescriptor.getPropertyType();
					if (!Boolean.class.equals(propertyType) && !boolean.class.equals(propertyType)) {
						throw new ParserException(Reason.AnnotatedPropertyNotBoolean, String.format(
								"The annotated property '%s' for class '%s' is not boolean.", fieldName,
								clazz.getName()));
					}
				} catch (IntrospectionException e) {
					throwReflectionException(clazz, fieldName, e);
				}
			}
		}
	}

	void verifyAllAnnotatedPropertiesWriteable(List<AnnotatedOption> options, Class<?> clazz) throws ParserException {
		for (AnnotatedOption annotatedOption : options) {
			String fieldName = annotatedOption.getFieldName();
			try {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, clazz);
				Method writeMethod = propertyDescriptor.getWriteMethod();
				if (writeMethod == null) {
					throw new ParserException(Reason.AnnotatedPropertyNotWriteable, String.format(
							"The annotated property '%s of class '%s' is not writeable.", fieldName, clazz.getName()));
				}
			} catch (IntrospectionException e) {
				throwReflectionException(clazz, fieldName, e);
			}
		}
	}

	private void throwReflectionException(Class<?> clazz, String fieldName, IntrospectionException e)
			throws ParserException {
		throw new ParserException(Reason.ReflectionException, String.format(
				"Failed to verify that annotated property '%s' of class '%s' is useable via reflection: %s.",
				fieldName, clazz.getName(), e.getMessage()));
	}
}
