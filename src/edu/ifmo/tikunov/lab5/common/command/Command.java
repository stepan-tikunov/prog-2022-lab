package edu.ifmo.tikunov.lab5.common.command;

/**
 * Instance of this class represents an executable command.
 * Should be instantiated within command executor as an anonymous class
 */
public abstract class Command {
    public final CommandSignature signature;

    public abstract void execute(ExecutionQuery query) throws ExitSignal;

    /**
     * Returns information about command usage.
     * @return command usage
     */
    public String usage() {
        ParameterList onlySimple = signature.parameters.onlySimple();

        if (signature.parameters.equals(onlySimple)) {
            return "  Usage: " + signature.name + " " + signature.parameters.toString();
        } else {
            return "  Usage (interactive): " + signature.name + " " + onlySimple.toString() + "\n"
                    + "  Usage (one line): " + signature.name + " " + signature.parameters.toString();
        }
    }

    public Command(CommandSignature signature) {
        this.signature = signature;
    }
}
