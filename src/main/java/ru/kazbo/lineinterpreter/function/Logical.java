package ru.kazbo.lineinterpreter.function;

import ru.kazbo.lineinterpreter.annotation.*;

@ScriptsLibrary
public class Logical {
	
	@ScriptMethod
	public void printSomething(String line1, String line2, boolean canStartFromFirst) {
		String message = (canStartFromFirst) ? line1 : line2;
		System.out.println(message);
	}
}