package com.google.code.commons.cli.annotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.apache.commons.cli.GnuParser;
import org.junit.Test;

import com.google.code.commons.cli.annotations.ParserException.Reason;

public class CliParserTest {

	public static class VerboseOption {
		@CliOption(opt = "v", longOpt = "verbose")
		private boolean verbose;

		public boolean isVerbose() {
			return verbose;
		}

		public void setVerbose(boolean verbose) {
			this.verbose = verbose;
		}
	}

	@Test
	public void verboseOption() throws ParserException {
		CliParser parser = new CliParser(new GnuParser());
		VerboseOption simpleOption = parser.parse(VerboseOption.class, new String[] { "-v" });
		assertThat(simpleOption.isVerbose(), is(true));
		simpleOption = parser.parse(VerboseOption.class, new String[] { "--verbose" });
		assertThat(simpleOption.isVerbose(), is(true));
		simpleOption = parser.parse(VerboseOption.class, new String[] {});
		assertThat(simpleOption.isVerbose(), is(false));
	}

	public static class FilenameOption {
		@CliOption(opt = "f", longOpt = "filename", hasArg = true)
		private String filename;

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}
	}

	@Test
	public void filenameOption() throws ParserException {
		CliParser parser = new CliParser(new GnuParser());
		FilenameOption filenameOption = parser.parse(FilenameOption.class, new String[] { "-f", "file.txt" });
		assertThat(filenameOption.getFilename(), is("file.txt"));
	}

	@Test
	public void filenameOptionWithoutArg() {
		CliParser parser = new CliParser(new GnuParser());
		boolean exceptionThrown = false;
		try {
			parser.parse(FilenameOption.class, new String[] { "-f" });
		} catch (ParserException e) {
			exceptionThrown = true;
			assertThat(e.getReason(), is(Reason.CommonsCliParseException));
		}
		assertThat(exceptionThrown, is(true));
	}

	public static class OptionalArg {
		@CliOption(opt = "o", hasOptionalArg = true)
		private String optionalArg;

		public String getOptionalArg() {
			return optionalArg;
		}

		public void setOptionalArg(String optionalArg) {
			this.optionalArg = optionalArg;
		}
	}

	@Test
	public void testOptionalArgWithoutArg() throws ParserException {
		CliParser cliParser = new CliParser(new GnuParser());
		OptionalArg parse = cliParser.parse(OptionalArg.class, new String[] { "-o" });
		assertThat(parse.optionalArg, is(nullValue()));
	}

	@Test
	public void testOptionalArgWithArgProvided() throws ParserException {
		CliParser cliParser = new CliParser(new GnuParser());
		OptionalArg parse = cliParser.parse(OptionalArg.class, new String[] { "-o", "value" });
		assertThat(parse.optionalArg, is("value"));
	}

	public static class IntegerArg {
		@CliOption(opt = "pi", longOpt = "portInt", hasArg = true)
		private int portInt;
		@CliOption(opt = "pI", longOpt = "portInteger", hasArg = true)
		private Integer portInteger;

		public int getPortInt() {
			return portInt;
		}

		public void setPortInt(int portInt) {
			this.portInt = portInt;
		}

		public Integer getPortInteger() {
			return portInteger;
		}

		public void setPortInteger(Integer portInteger) {
			this.portInteger = portInteger;
		}
	}

	@Test
	public void testIntType() throws ParserException {
		CliParser cliParser = new CliParser(new GnuParser());
		IntegerArg integerArg = cliParser.parse(IntegerArg.class, new String[] { "-pi", "8787", "-pI", "8788" });
		assertThat(integerArg.portInt, is(8787));
		assertThat(integerArg.portInteger, is(8788));
		boolean expThrown = false;
		try {
			integerArg = cliParser.parse(IntegerArg.class, new String[] { "-pi", "abc", "-pI", "8788" });
		} catch (ParserException e) {
			expThrown = true;
			assertThat(e.getReason(), is(Reason.ConvertingValueFailed));
		}
		assertThat(expThrown, is(true));
	}

	public static class LongOption {
		@CliOption(opt = "l", hasArg = true)
		private long longPrimitive;
		@CliOption(opt = "L", hasArg = true)
		private Long longWrapper;

		public long getLongPrimitive() {
			return longPrimitive;
		}

		public void setLongPrimitive(long longPrimitive) {
			this.longPrimitive = longPrimitive;
		}

		public Long getLongWrapper() {
			return longWrapper;
		}

		public void setLongWrapper(Long longWrapper) {
			this.longWrapper = longWrapper;
		}
	}

	@Test
	public void testLongType() throws ParserException {
		CliParser cliParser = new CliParser(new GnuParser());
		LongOption longOption = cliParser.parse(LongOption.class, new String[] { "-l", "8787", "-L", "8788" });
		assertThat(longOption.getLongPrimitive(), is(8787L));
		assertThat(longOption.getLongWrapper(), is(8788L));
	}

	public static class FloatOption {
		@CliOption(opt = "ff", hasArg = true)
		private float optionfloat;
		@CliOption(opt = "fF", hasArg = true)
		private Float optionFloat;

		public float getOptionfloat() {
			return optionfloat;
		}

		public void setOptionfloat(float optionfloat) {
			this.optionfloat = optionfloat;
		}

		public Float getOptionFloat() {
			return optionFloat;
		}

		public void setOptionFloat(Float optionFloat) {
			this.optionFloat = optionFloat;
		}
	}

	@Test
	public void testFloatType() throws ParserException {
		CliParser cliParser = new CliParser(new GnuParser());
		FloatOption floatOption = cliParser.parse(FloatOption.class, new String[] { "-ff", "3.14", "-fF", "0.72" });
		assertThat(floatOption.optionfloat, is(3.14F));
		assertThat(floatOption.optionFloat, is(0.72F));
		boolean expThrown = false;
		try {
			floatOption = cliParser.parse(FloatOption.class, new String[] { "-ff", "abc" });
		} catch (ParserException e) {
			expThrown = true;
			assertThat(e.getReason(), is(Reason.ConvertingValueFailed));
		}
		assertThat(expThrown, is(true));
	}

	public static class DoubleOption {
		@CliOption(opt = "d", hasArg = true)
		private double doublePrim;
		@CliOption(opt = "D", hasArg = true)
		private Double doubleWrapper;

		public double getDoublePrim() {
			return doublePrim;
		}

		public void setDoublePrim(double doublePrim) {
			this.doublePrim = doublePrim;
		}

		public Double getDoubleWrapper() {
			return doubleWrapper;
		}

		public void setDoubleWrapper(Double doubleWrapper) {
			this.doubleWrapper = doubleWrapper;
		}

	}

	@Test
	public void testDoubleType() throws ParserException {
		CliParser cliParser = new CliParser(new GnuParser());
		DoubleOption doubleOption = cliParser.parse(DoubleOption.class, new String[] { "-d", "3.14", "-D", "0.72" });
		assertThat(doubleOption.doublePrim, is(3.14D));
		assertThat(doubleOption.doubleWrapper, is(0.72D));
	}
}
