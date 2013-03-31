package com.google.code.commons.cli.annotations.intern;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.junit.Test;

import com.google.code.commons.cli.annotations.ParserException;
import com.google.code.commons.cli.annotations.TstOption;
import com.google.code.commons.cli.annotations.intern.AnnotatedOption;
import com.google.code.commons.cli.annotations.intern.CliProcessor;

public class CliParserTest {

	@Test
	public void test() throws ParserException {
		CliProcessor cliParser = new CliProcessor(new GnuParser());
		List<AnnotatedOption> annotatedOptions = new LinkedList<AnnotatedOption>();
		Option option = new Option("v", "desc");
		AnnotatedOption ao = new AnnotatedOption(option, "verbose");
		annotatedOptions.add(ao);
		TstOption testOption = cliParser.parse(TstOption.class, annotatedOptions, new String[] { "-v" });
		assertThat(testOption.isVerbose(), is(true));
	}
}
