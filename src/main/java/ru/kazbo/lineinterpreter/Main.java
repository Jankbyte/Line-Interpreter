package ru.kazbo.lineinterpreter;

import java.io.File;
import ru.kazbo.lineinterpreter.exception.CodeParsingException;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		if(!isHasArguments(args))
			return;
		String filePath = args[0];
		File script = new File(filePath);
		if(isFileExists(script))
			runScriptAndCheckErrors(script);
	}
	
	private static boolean isFileExists(File file) {
		if(!file.exists()) {
			System.out.println("File " + file.getAbsolutePath() + " not found");
			return false;
		}
		return true;
	}
	
	private static boolean isHasArguments(String[] args) {
		if(args.length == 0) {
			System.out.println("Write path to script in first argument");
			return false;
		}
		return true;
	}
	
	private static void runScriptAndCheckErrors(File file) throws IOException {
		Interpreter iterpreter = new Interpreter(Interpreter.getDefaultModules());
		try {
			iterpreter.parse(file);
		} catch(CodeParsingException exp) {
			System.out.println(exp.getMessage());
		}
	}
}