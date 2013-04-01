package com.google.code.commons.cli.annotations.intern;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.junit.Test;

import com.google.code.commons.cli.annotations.CliOption;
import com.google.code.commons.cli.annotations.ParserException;

public class CliProcessorTest {

	public static class VerboseOption {

		@CliOption(opt = "v")
		private boolean verbose;

		public boolean isVerbose() {
			return verbose;
		}

		public void setVerbose(boolean verbose) {
			this.verbose = verbose;
		}
	}

	@Test
	public void test() throws ParserException, NoSuchFieldException, SecurityException {
		CliProcessor cliParser = new CliProcessor(new GnuParser());
		List<AnnotatedOption> annotatedOptions = new LinkedList<AnnotatedOption>();
		Option option = new Option("v", "desc");
		AnnotatedOption ao = new AnnotatedOption(option, VerboseOption.class.getDeclaredField("verbose"));
		annotatedOptions.add(ao);
		VerboseOption testOption = cliParser.parse(VerboseOption.class, annotatedOptions, new String[] { "-v" });
		assertThat(testOption.isVerbose(), is(true));
	}
}
