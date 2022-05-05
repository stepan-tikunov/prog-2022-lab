package edu.ifmo.tikunov.lab4.command;

/**
 * Instance of this class represents an executable command.
 * Should be instantiated within command executor as an anonymous class
 */
public abstract class Command {
    protected final ParameterList parameterList;
    public final String name;
    public final String description;

    public abstract void execute(Object[] params) throws ExitSignal;

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

    public Command(String name, String description, ParameterList parameterList) {
        this.name = name;
        this.description = description;
        this.parameterList = parameterList;
    }
}
