package ru.kazbo.lineinterpreter;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static java.lang.System.out;
import ru.kazbo.lineinterpreter.exception.CodeParsingException;
import static ru.kazbo.lineinterpreter.CodeParser.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;

@Disabled
class CodeParserTests {
	
	private static List<String> functionsName = new ArrayList<>();
	
	@Disabled
	@Order(0)
	@ParameterizedTest
	@CsvSource({"helloWorld, false","functionWithoutParams(), true",
				"  function_with_spaces()  , true", "functionWithTwoParams('',123), true", 
				"functionWithParams '',123, false"})
	void testMethodsPatten(String line, boolean expected) throws CodeParsingException {
		boolean isMethod = isLineMethod(line);
		Assertions.assertEquals(expected, isMethod);
		if(isMethod) {
			String methodName = getMethodNameFromLine(line);
			System.out.println(methodName);
		}
	}
	
	@Disabled
	@Order(1)
	@ParameterizedTest
	@ValueSource(strings = {"functionWithoutParams", "helloWorld", "function_with_spaces", "functionWithTwoParams"})
	void testFunctionsNamePattern(String expected) {
		for(String name : functionsName)
			Assertions.assertEquals(expected, name);
	}
	
	@Disabled
	@Order(2)
	@Test
	void testParsingParamsFromFunction() {
		System.out.println("Params test:");
		String line = "testMethod(\"Hello, world!\",123)";
		boolean isMethod = isLineMethod(line);
		if(isMethod) {
			String[] parameters = getMethodParameters(line);
			Stream.of(parameters).forEach(System.out::println);
		}
		
	}
	
	@Disabled
	@Order(3)
	@Test
	void testCommentPattern() {
		String comment = "/* Hello world*/";
		boolean isComment = isMultilineComment(comment);
		Assertions.assertEquals(isComment, true);
	}
	
	//@Disabled
	@Order(4)
	@ParameterizedTest
	@CsvSource({"var test = 123, true", "  var variable = 'hello world'   , true",
	"test = 123, false"})
	void testVariableWithValue(String line, boolean expected) throws CodeParsingException {
		boolean isVariable = isInitializeVariableWithValue(line);
		Assertions.assertEquals(expected, isVariable);
		if(isVariable) {
			String name = getInitializedVariableName(line);
			String value = getInitializedVariableValue(line);
			System.out.println("Name : " + name + " Value: " + value);
		}
	}
	
	
}