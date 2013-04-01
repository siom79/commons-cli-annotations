package com.google.code.commons.cli.annotations.intern;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class StringsTest {

	@Test
	public void nullToEmpty() {
		assertThat(Strings.nullToEmpty("notNull"), is("notNull"));
		assertThat(Strings.nullToEmpty(null), is(""));
	}

}
