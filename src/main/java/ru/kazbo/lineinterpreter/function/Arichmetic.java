package ru.kazbo.lineinterpreter.function;

import ru.kazbo.lineinterpreter.annotation.*;

@ScriptsLibrary
public class Arichmetic {
	
	@ScriptMethod
	public void sum(int num1, int num2) {
		int result = num1 + num2;
		System.out.println(result);
	}
}