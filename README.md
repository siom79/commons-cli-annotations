#commons-cli-annotations#
Apache [commons-cli](http://commons.apache.org/proper/commons-cli/) is a standard library for parsing command line parameters in Java. Though its functionality is sufficient, the library offers no support for annotations.

This extension to the commons-cli library enables you to simply annotate the properties of a POJO class to populate these fields with the values given from the command line.

##Example##
Let's examine a simple example. Suppose you want to write a little program that accepts three command line parameters:

```
-v to indicate verbose behaviour
-f to pass the name of a file
-p to pass a port number
```

The first step is to write a simple value class (VO) with two properties annotated with `@CliOption`:

```
public class CliOptions {
        @CliOption(opt = "v", longOpt = "verbose")
        private boolean verbose;
        @CliOption(opt = "f", longOpt = "filename", hasArg = true)
        private String filename;
        @CliOption(opt = "p", longOpt = "port", hasArg = true)
        private int port;

        public boolean isVerbose() {
                return verbose;
        }

        public void setVerbose(boolean verbose) {
                this.verbose = verbose;
        }

        public String getFilename() {
                return filename;
        }

        public void setFilename(String filename) {
                this.filename = filename;
        }

        public int getPort() {
                return port;
        }

        public void setPort(int port) {
                this.port = port;
        }
}
```

The second step is to create an instance of CliParser and pass the arguments given on the command line to its parse() method:

```
public static void main(String args[]) {
        CliParser parser = new CliParser(new GnuParser());
        CliOptions cliOptions = parser.parse(CliOptions.class, args);
        ...
}
```

The class CliParser is just a decorator for the commons-cli's CommandLineParser, thus you can use all available implementations like e.g GnuParser. The returned instance of CliOptions is populated with the values from the command line.

##Features##

* Supports the following options of commons-cli 1.2:
    * opt and longOpt
    * description
    * required
    * hasArg
    * hasOptionalArg
* The following types can be annotated with @CliOption:
    * boolean for options without an argument
    * String, int, Integer, long, Long, float, Float, double, Double for options with one String argument

##Dependencies##

The library has the following dependencies:

* commons-cli:commons-cli:jar:1.2
* commons-beanutils:commons-beanutils:jar:1.8.3
* commons-logging:commons-logging:jar:1.1.1

##Development##

[![Build Status](https://travis-ci.org/siom79/commons-cli-annotations.svg?branch=master)](https://travis-ci.org/siom79/commons-cli-annotations)

##Download##

The latest version can be downloaded from the [release page](https://github.com/siom79/commons-cli-annotations/releases).