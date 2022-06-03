package ru.kazbo.lineinterpreter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.stream.Stream;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import ru.kazbo.lineinterpreter.annotation.*;
import ru.kazbo.lineinterpreter.exception.*;
import ru.kazbo.lineinterpreter.function.*;
import ru.kazbo.lineinterpreter.CodeParser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * The interpreter of code
 * @author Kazbo
 */
public class Interpreter {
	
	private Set<Class<?>> classes;
	private final Map<String,Object> variables = new HashMap();
	/**
	 * Create new instance of Interpreter and
	 * add classes that have a annotated functional
	 * @param functions annotated classes by <code>ScriptsLibrary</code>
	 */
	public Interpreter(Class<?>... functions) {
		classes = new HashSet<>();
		Stream.of(functions)
			.filter(clazz -> clazz.isAnnotationPresent(ScriptsLibrary.class))
			.forEach(classes::add);
	}
	
	/**
	 * Has default classes for script-library
	 * @return Default annotated classes
	 */
	public static Class<?>[] getDefaultModules() {
		return new Class<?>[]{
			Println.class,
			Arichmetic.class,
			Logical.class
		};
	}
	
	/**
	 * Loading and execute script from file
	 * @param file The file with script
	 * @throws CodeParsingException If script have syntax errors
	 * @throws IOException If we have some problems with file
	 */
	public void parse(File file) throws CodeParsingException, IOException {
		String script = Files.readString(file.toPath());
		parse(script);
	}
	
	/**
	 * Execute script from string
	 * @param script The line that contains script
	 * @throws CodeParsingException If script have syntax errors
	 */
	public void parse(String script) throws CodeParsingException {
		String[] lines = CodeParser.splitScriptByLines(script);
		parseLines(lines);
	}
	
	private void parseLines(String[] lines) throws CodeParsingException {
		int currentLineOfScript = 0;
		try {
			for(int i = 0; i < lines.length; i++) {
				currentLineOfScript = i + 1;
				String line = lines[i];
				if(CodeParser.isLineMethod(line))
					parseMethod(line);
				else if(CodeParser.isInitializeVariableWithValue(line))
					parseVariable(line);
				else if(!(CodeParser.isSingleLineComment(line) || line.isBlank()))
					throw new CodeParsingException("Bad line");
			}
		} catch(Exception exp) {
			String errorMessage = "[line: %d] %s".formatted(currentLineOfScript, exp.getMessage());
			throw new CodeParsingException(errorMessage);
		}
	}
	
	private void parseVariable(String line) throws CodeParsingException {
		CodeParser.addVariableToMap(variables, line);
	}
	
	private void parseMethod(String line) throws InvocationTargetException, CodeParsingException, 
	NoSuchMethodException, InstantiationException, IllegalAccessException {
		String name = CodeParser.getMethodNameFromLine(line);
		String[] params = CodeParser.getMethodParameters(line);
		prepareMethodAndExecute(name, params);
	}
	
	private void prepareMethodAndExecute(String name, String[] params) throws InvocationTargetException, 
	CodeParsingException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		Method method = getMethodByNameAndParams(name, params);
		Class<?>[] typeParams = method.getParameterTypes();
		Object targetObject = method.getDeclaringClass().getConstructor().newInstance();
		Object[] objectsParams = CodeParser.getObjectsArguments(variables, params);
		method.invoke(targetObject, objectsParams);
	}
	
	private Method getMethodByNameAndParams(String name, String[] params) throws CodeParsingException {
		Method targetMethod = null;
		int paramsCount = params.length;
		for(Class<?> clazz : classes) {
			targetMethod = findAnnotatedMethodInClassByNameAndParamsCount(clazz, name, paramsCount);
			if(targetMethod != null) break;
		}
		if(targetMethod == null) {
			String exceptionMessage = "Method \"%s\" and %d params not found".formatted(name, paramsCount);
			throw new CodeParsingException(exceptionMessage);
		}
		return targetMethod;
	}
	
	private Method findAnnotatedMethodInClassByNameAndParamsCount(Class<?> clazz, String name, int paramsCount) {
		Method[] methods = clazz.getMethods();
		for(Method method : methods) {
			boolean isAnnotated = method.getAnnotation(ScriptMethod.class) != null;
			if(isAnnotated) {
				String methodName = method.getName();
				int methodParamsCount = method.getParameterCount();
				if((methodName.equals(name)) && (methodParamsCount == paramsCount)) {
					return method;
				}
			}
		}
		return null;
	}
}