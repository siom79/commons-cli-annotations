package com.google.code.commons.cli.annotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.cli.GnuParser;
import org.junit.Test;

import com.google.code.commons.cli.annotations.CliOption;
import com.google.code.commons.cli.annotations.CliParser;
import com.google.code.commons.cli.annotations.ParserException;
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

}
