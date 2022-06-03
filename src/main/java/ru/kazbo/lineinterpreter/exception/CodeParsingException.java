package ru.kazbo.lineinterpreter.exception;
/**
 * The exception for control a syntax errors of script
 */
public class CodeParsingException extends Exception {
	/**
	 * Default construction of exception
	 * @param message The error description
	 */
	public CodeParsingException(String message) {
		super(message);
	}
}