package ru.kazbo.lineinterpreter;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.io.File;

//@Disabled
class InterpreterTests {
	
	static Interpreter iterpreter = new Interpreter(Interpreter.getDefaultModules());
	
	//@Disabled
	@Test
	void testScriptFromString() throws Exception {
		String code = """
			
			// Print simple messages
			println("Hello, iterpreter!")
			println(78328427)
			println(true)
			
			// Print variables
			var message = "Thats string variable"
			println(message)
			
			// Logical example
			printSomething("First line", "Second line", true)
			printSomething("First line", "Second line", false)
			
			// Sum 1 + 1
			sum(1,1)""";
		iterpreter.parse(code);
	}
	
	@Disabled
	@Test
	void testScriptFromFile() throws Exception {
		var file = new File("D:/script.txt");
		iterpreter.parse(file);
	}
	
	
}