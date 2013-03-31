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
}
