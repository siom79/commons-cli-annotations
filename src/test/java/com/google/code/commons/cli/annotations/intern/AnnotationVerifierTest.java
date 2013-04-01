package com.google.code.commons.cli.annotations.intern;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.junit.Test;

import com.google.code.commons.cli.annotations.ParserException;
import com.google.code.commons.cli.annotations.ParserException.Reason;

public class AnnotationVerifierTest {

	class TestWriteableProperties {
		private boolean propertyWriteable;
		@SuppressWarnings("unused")
		private boolean propertyNotReadOrWriteable;

		public boolean isPropertyWriteable() {
			return propertyWriteable;
		}

		public void setPropertyWriteable(boolean propertyWriteable) {
			this.propertyWriteable = propertyWriteable;
		}
	}

	@Test
	public void allPropertiesWriteable() throws ParserException {
		AnnotationVerifier annotationVerifier = new AnnotationVerifier();
		List<AnnotatedOption> options = new LinkedList<AnnotatedOption>();
		options.add(new AnnotatedOption(new Option("opt", "desc"), "propertyWriteable"));
		annotationVerifier.verifyAllAnnotatedPropertiesWriteable(options, TestWriteableProperties.class);
	}

	@Test(expected = ParserException.class)
	public void allPropertyNotWriteable() throws ParserException {
		AnnotationVerifier annotationVerifier = new AnnotationVerifier();
		List<AnnotatedOption> options = new LinkedList<AnnotatedOption>();
		options.add(new AnnotatedOption(new Option("opt", "desc"), "propertyNotReadOrWriteable"));
		annotationVerifier.verifyAllAnnotatedPropertiesWriteable(options, TestWriteableProperties.class);
	}

	class TestBooleanProperties {
		private boolean booleanProperty;
		private String notBoolean;

		public boolean isBooleanProperty() {
			return booleanProperty;
		}

		public void setBooleanProperty(boolean booleanProperty) {
			this.booleanProperty = booleanProperty;
		}

		public String getNotBoolean() {
			return notBoolean;
		}

		public void setNotBoolean(String notBoolean) {
			this.notBoolean = notBoolean;
		}
	}

	@Test
	public void annotatedPropertiesWithNoArgsAreBoolean() throws ParserException {
		AnnotationVerifier annotationVerifier = new AnnotationVerifier();
		List<AnnotatedOption> options = new LinkedList<AnnotatedOption>();
		options.add(new AnnotatedOption(new Option("opt", "desc"), "booleanProperty"));
		annotationVerifier.verifyOptionTypeIsSupported(options, TestBooleanProperties.class);
	}

	@Test(expected = ParserException.class)
	public void annotatedPropertiesWithNoArgsAreBooleanButIsNot() throws ParserException {
		AnnotationVerifier annotationVerifier = new AnnotationVerifier();
		List<AnnotatedOption> options = new LinkedList<AnnotatedOption>();
		options.add(new AnnotatedOption(new Option("opt", "desc"), "notBoolean"));
		annotationVerifier.verifyOptionTypeIsSupported(options, TestBooleanProperties.class);
	}

	class AnnotatedTypeNotSupported {
		private Date dateIsNotSupported;

		public Date getDateIsNotSupported() {
			return dateIsNotSupported;
		}

		public void setDateIsNotSupported(Date dateIsNotSupported) {
			this.dateIsNotSupported = dateIsNotSupported;
		}
	}

	public void annotatedPropertyIsNotSupported() {
		AnnotationVerifier annotationVerifier = new AnnotationVerifier();
		List<AnnotatedOption> options = new LinkedList<AnnotatedOption>();
		options.add(new AnnotatedOption(new Option("opt", "desc"), "dateIsNotSupported"));
		boolean exeptionThrown = false;
		try {
			annotationVerifier.verifyOptionTypeIsSupported(options, AnnotatedTypeNotSupported.class);
		} catch (ParserException e) {
			exeptionThrown = true;
			assertThat(e.getReason(), is(Reason.AnnotatedPropertyTypeNotSupported));
		}
		assertThat(exeptionThrown, is(true));
	}
}
