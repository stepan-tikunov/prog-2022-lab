package edu.ifmo.tikunov.lab4.console;

@FunctionalInterface
public interface InputFunction {
	String accept(String message) throws ExitSignal;
}
