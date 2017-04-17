/*
 * Decompiled with CFR 0_121.
 */
package stac.parser;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CommandLine {
    private static final String NL = String.format("%n", new Object[0]);
    private final LinkedList<Option> options = new LinkedList();
    private final ExceptionBuilder exceptionBuilder;

    public CommandLine(String motd) {
        this.exceptionBuilder = new ExceptionBuilder(this.options, motd);
    }

    public OptionBuilder newOption() {
        return new OptionBuilder(this.options, this);
    }

    public Options parse(String[] args) {
        Options options = new Options(this.options, this.exceptionBuilder.motd);
        this.helpBrownout(args, options);
        this.fulfillOptions(args, options);
        this.missingOptionsBailout();
        return options;
    }

    private void missingOptionsBailout() {
        for (Option option : this.options) {
            if (!option.required || option.present) continue;
            String arg = option.shopt != null ? "-" + option.shopt : "--" + option.lopt;
            throw new ParseException(this, "Missing required argument. See --help " + arg + " for details.");
        }
    }

    private void fulfillOptions(String[] args, Options options) {
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            try {
                Option option;
                if (arg.startsWith("--")) {
                    option = options.findByLongOption(arg);
                    if (option.arg) {
                        int beginIndex = arg.indexOf(61) + 1;
                        if (beginIndex > 2 && beginIndex < arg.length()) {
                            option.value = arg.substring(beginIndex);
                            option.present = true;
                            continue;
                        }
                        throw new ParseHelpfulException("Parsing option " + arg + " failed (missing =?). For help see --help " + option.lopt);
                    }
                    option.set = true;
                    option.present = true;
                    continue;
                }
                if (arg.startsWith("-")) {
                    option = options.findByShortOption(arg);
                    if (option.arg) {
                        if (i < args.length - 1) {
                            option.value = args[++i];
                            option.present = true;
                            continue;
                        }
                        throw new ParseHelpfulException("Parsing option " + arg + " failed. Missing option value");
                    }
                    option.set = true;
                    option.present = true;
                    continue;
                }
                throw new InvalidOptionException(arg);
            }
            catch (InvalidOptionException e) {
                throw new ParseHelpfulException("Parsing option " + arg + " failed. Invalid argument.", e);
            }
        }
    }

    private void helpBrownout(String[] args, Options options) {
        if (args.length == 0) {
            return;
        }
        if (args[0].equals("--help") || args[0].equals("-h")) {
            Option state = null;
            try {
                if (args.length == 2) {
                    if (args[1].startsWith("--")) {
                        state = options.findByLongOption(args[1]);
                    } else if (args[1].startsWith("-")) {
                        state = options.findByShortOption(args[1]);
                    }
                }
            }
            catch (InvalidOptionException e) {
                throw new ParseException(this, "Cannot provide help on option " + args[1] + ". Invalid argument.");
            }
            this.throwHelp(state);
        }
    }

    public void throwHelp(Option state) {
        throw this.exceptionBuilder.build(state);
    }

    public static class ParseHelpfulException
    extends RuntimeException {
        private static final long serialVersionUID = -5017929931944916471L;
        private final String message;

        public ParseHelpfulException(List<Option> options, String motd, Option state) {
            StringBuilder sb = new StringBuilder(motd).append(NL).append(NL).append("Usage:").append(NL);
            if (state == null) {
                for (Option option : options) {
                    String desc = option.shdesc != null ? option.shdesc : (option.ldesc != null ? option.ldesc : "Missing description");
                    Scanner scanner = new Scanner(desc);
                    scanner.useDelimiter("%n");
                    sb.append("-").append(option.shopt != null ? option.shopt : "<undefined>").append(" --").append(option.lopt != null ? option.lopt : "<undefined>").append(": ").append(NL);
                    while (scanner.hasNext()) {
                        sb.append('\t').append(scanner.nextLine()).append(NL);
                    }
                    sb.append(NL);
                }
            } else {
                String desc = state.ldesc != null ? state.ldesc : (state.shdesc != null ? state.shdesc : "Missing description");
                Scanner scanner = new Scanner(desc);
                scanner.useDelimiter("%n");
                sb.append(state.shopt != null ? "-" + state.shopt : "-<undefined>").append(state.lopt != null ? " --" + state.lopt : " --<undefined>").append(": ").append(NL);
                while (scanner.hasNext()) {
                    sb.append('\t').append(scanner.nextLine()).append(NL);
                }
                sb.append(NL);
            }
            this.message = sb.toString();
        }

        public ParseHelpfulException(String s) {
            this.message = s;
        }

        public ParseHelpfulException(String s, Throwable e) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            e.printStackTrace(printWriter);
            this.message = s + System.lineSeparator() + outputStream.toString();
        }

        @Override
        public String getMessage() {
            return this.message;
        }
    }

    protected class ParseException
    extends RuntimeException {
        private static final long serialVersionUID = -4612359786474361320L;
        final /* synthetic */ CommandLine this$0;

        public ParseException(CommandLine commandLine, String message) {
            super(message);
            this.this$0 = commandLine;
        }

        public ParseException(CommandLine commandLine, String message, Throwable cause) {
            super(message, cause);
            this.this$0 = commandLine;
        }
    }

    public static class InvalidOptionException
    extends RuntimeException {
        private static final long serialVersionUID = 8689565177678826574L;
        private final String longOption;

        public InvalidOptionException(String longOption) {
            this.longOption = longOption;
        }

        @Override
        public String getMessage() {
            return this.longOption + ": " + super.getMessage();
        }
    }

    protected static class IllegalOptionStateException
    extends RuntimeException {
        private static final long serialVersionUID = -8403076555609965422L;

        public IllegalOptionStateException(String message) {
            super(message);
        }
    }

    private class ExceptionBuilder {
        private final List<Option> options;
        private final String motd;

        public ExceptionBuilder(List<Option> options, String motd) {
            this.options = options;
            this.motd = motd;
        }

        public ParseHelpfulException build(Option state) {
            return new ParseHelpfulException(this.options, this.motd, state);
        }
    }

    public static class OptionBuilder {
        private List<Option> options;
        private Option opt;
        private CommandLine cmdline;

        private OptionBuilder(LinkedList<Option> options, CommandLine commandLine) {
            this.options = options;
            this.cmdline = commandLine;
            this.opt = new Option();
        }

        public CommandLine done() {
            if (this.opt == null) {
                throw new RuntimeException("done() re-called on finalized commandline option.");
            }
            this.options.add(this.opt);
            if (this.opt.lopt == null && this.opt.shopt == null) {
                throw new RuntimeException("done() called on un-finished commandline option.");
            }
            CommandLine c = this.cmdline;
            this.cmdline = null;
            this.opt = null;
            this.options = null;
            return c;
        }

        public OptionBuilder longDescription(String desc) {
            this.opt.ldesc = desc;
            return this;
        }

        public OptionBuilder shortDescription(String desc) {
            this.opt.shdesc = desc;
            return this;
        }

        public OptionBuilder shortOption(String shopt) {
            this.opt.shopt = shopt;
            return this;
        }

        public OptionBuilder longOption(String lopt) {
            this.opt.lopt = lopt;
            return this;
        }

        public OptionBuilder required(boolean required) {
            this.opt.required = required;
            this.opt.value = null;
            this.opt.set = false;
            return this;
        }

        public OptionBuilder hasValue(boolean arg) {
            return this.hasValue(arg, null);
        }

        public OptionBuilder hasValue(boolean arg, String defaultValue) {
            if (this.opt.required && defaultValue != null) {
                throw new IllegalOptionStateException("Required options cannot have default values");
            }
            this.opt.arg = arg;
            this.opt.value = defaultValue;
            this.opt.set = false;
            return this;
        }

        public OptionBuilder hasValue(boolean arg, boolean defaultValue) {
            if (this.opt.required) {
                throw new IllegalOptionStateException("Required options cannot have default values");
            }
            if (arg && defaultValue) {
                throw new IllegalOptionStateException("Argument options cannot be set by default");
            }
            this.opt.arg = arg;
            this.opt.set = false;
            return this;
        }
    }

    public static class Option {
        private String shopt = null;
        private String lopt = null;
        private String shdesc = null;
        private String ldesc = null;
        private boolean required = false;
        private boolean arg = false;
        private String value = null;
        private boolean present = false;
        private boolean set;

        public String getValue() {
            return this.value;
        }

        public boolean isSet() {
            return this.set;
        }
    }

    public static class Options {
        private final List<Option> options;
        private String motd;

        public Options(List<Option> options, String motd) {
            this.options = options;
            this.motd = motd;
        }

        public Option findByLongOption(String longOption) throws InvalidOptionException {
            int endIndex = longOption.indexOf(61);
            longOption = longOption.substring(longOption.startsWith("--") ? 2 : 0, endIndex < 0 ? longOption.length() : endIndex);
            for (Option option : this.options) {
                if (option.lopt == null || !option.lopt.equals(longOption)) continue;
                return option;
            }
            throw new InvalidOptionException(longOption);
        }

        public Option findByShortOption(String shortOption) throws InvalidOptionException {
            shortOption = shortOption.substring(shortOption.startsWith("-") ? 1 : 0);
            for (Option option : this.options) {
                if (option.shopt == null || !option.shopt.equals(shortOption)) continue;
                return option;
            }
            throw new InvalidOptionException(shortOption);
        }

        public List<Option> getOptions() {
            return this.options;
        }

        public String getMotd() {
            return this.motd;
        }
    }

}

