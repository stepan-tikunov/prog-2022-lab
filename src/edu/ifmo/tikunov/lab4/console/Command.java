package edu.ifmo.tikunov.lab4.console;

/**
 * Instance of this class represents an executable command.
 * Should be instantiated within command executor as an anonymous class
 */
public abstract class Command {
    protected final ParameterList parameterList;
    public final String name;
    public final String description;

    protected abstract void execute(Object[] params) throws ExitSignal;

    /**
     * Returns information about command usage.
     * @return command usage
     */
    public String usage() {
        ParameterList onlySimple = parameterList.onlySimple();

        if (parameterList.equals(onlySimple)) {
            return "usage: " + name + " " + parameterList.toString();
        } else {
            return "usage (interactive): " + name + " " + onlySimple.toString() + "\n"
                    + "usage (one line): " + name + " " + parameterList.toString();
        }
    }

    /**
     * Tests string against command parameter list,
     * parses parameters and then executes the command.
     *
     * @param params string with command parameters
     * @throws BadParametersException if {@code params} doesn't match parameter list
     * @throws ExitSignal if command sends exit signal
     */
    public void executeIfValid(String params) throws BadParametersException, ExitSignal {
        if (parameterList.validate(params)) {
            Object[] parsed = parameterList.parse(params);
            execute(parsed);
        } else {
            if (!parameterList.onlySimple().validate(params)) {
                throw new BadParametersException("The specified string does not match the parameters list.");
            }

            Object[] parsed = parameterList.parseInteractive(params);
            execute(parsed);
        }
    }

    public Command(String name, String description, ParameterList parameterList) {
        this.name = name;
        this.description = description;
        this.parameterList = parameterList;
    }
}
