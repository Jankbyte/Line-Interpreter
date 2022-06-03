package ru.kazbo.lineinterpreter.function;

import ru.kazbo.lineinterpreter.annotation.*;

@ScriptsLibrary
public class Println {
	
	@ScriptMethod
	public void println(Object text) {
		System.out.println(text);
	}
}