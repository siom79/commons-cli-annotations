package com.google.code.commons.cli.annotations;

import com.google.code.commons.cli.annotations.CliOption;


public class TstOption {

	@CliOption(opt = "v")
	private boolean verbose;

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
}
