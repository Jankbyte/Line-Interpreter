package ru.kazbo.lineinterpreter;

import java.util.stream.Stream;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Stream;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import ru.kazbo.lineinterpreter.exception.CodeParsingException;

class CodeParser {
	
	public static String[] splitScriptByLines(String script) {
		return script.split("(\n|$)");
	}
	
	public static boolean isSingleLineComment(String line) {
		/* Description:
		 * ^ (new line)
		 * // (watching for backslashes)
		 * .*? (line can have symbols or empty)
		 * (\\n|$) (comment finishing by new line or ending of line)
		 */
		String commentRegExp = "^//.*?(\\n|$)";
		return getMatcher(commentRegExp, line).find();
	}
	
	public static boolean isLineMethod(String line) {
		/* Description:
		 * ^ (new/first line)
		 * \\s* (we can have spaces)
		 * [a-zA-z\\_]+ (function name: latin and downslash)
		 * \\( (open bracket for arguments)
		 * .*(allow all symbols inside brakets)
		 * \\)(close braket)
		 * \\s* (we can have spaces)
		 * $(end of line)
		 */
		String methodRegExp = "^\\s*[a-zA-Z\\_]+\\(.*\\)\\s*$";
		return getMatcher(methodRegExp, line).find();
	}
	
	public static boolean isInitializeVariableWithValue(String line) {
		String variableRegExp = "^\\s*var\\s+[a-zA-z_]+\\s*\\=\\s*.+\\s*$";
		return getMatcher(variableRegExp, line).find();
	}
	
	public static String getInitializedVariableName(String line) throws CodeParsingException {
		String variableRegExp = "^\\s*var\\s+(?<name>[a-zA-z_]+)\\s*\\=\\s*.+\\s*$";
		return findValueOrThrow(variableRegExp, "name", line);
	}
	
	public static String getInitializedVariableValue(String line) throws CodeParsingException {
		String variableRegExp = "^\\s*var\\s+[a-zA-z_]+\\s*\\=\\s*(?<name>.+)\\s*$";
		return findValueOrThrow(variableRegExp, "name", line);
	}
	
	public static Object[] getObjectsArguments(Map<String,Object> variables, String[] arguments) throws CodeParsingException {
		List<Object> objects = new ArrayList<>();
		for(String argument : arguments) {
			String trimmedArgument = argument.strip();
			Object object = getObjectFromVariables(variables, trimmedArgument);
			if(object == null)
				object = formatObject(trimmedArgument);
			objects.add(object);
		}
		return objects.toArray(new Object[objects.size()]);
	}
	
	public static String getMethodNameFromLine(String line) throws CodeParsingException {
		String getFunctionNamePattern = "^\\s*(?<name>[a-zA-Z\\_]+)\\(.*\\)\\s*$";
		Matcher matcher = getMatcher(getFunctionNamePattern, line);
		return findValueOrThrow(getFunctionNamePattern, "name", line);
	}
	
	public static String[] getMethodParameters(String line) {
		String cleanArguments = line
			.replaceAll("^\\s*[a-zA-Z\\_]+\\(", "")
			.replaceAll("\\)\\s*","");
		// разделяем запятой, но игнорируем запятые, которые находятся в скобках
		String[] arguments = cleanArguments.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
		String[] removedEmptyLines = (String[]) Stream.of(arguments)
			.filter(argument -> !argument.isEmpty())
			.toArray(size -> new String[size]);
		return removedEmptyLines;
	}
	
	public static String removeFirstQuotesFromString(String argument) {
		int length = argument.length();
		argument = argument.substring(1, length - 1);
		return argument;
	}
	
	public static boolean isMultilineComment(String line) {
		String commentRegExp = "\\/\\*" + "[\\w\\s\\p{Punct}]*" + "\\*\\/";
		return getMatcher(commentRegExp, line).find();
	}
	
	public static boolean isBooleanParam(String argument) {
		String booleanRegExp = "^(true|false)$";
		return getMatcher(booleanRegExp, argument).find();
	}
	
	public static boolean isStringParam(String argument) {
		char[] symbols = argument.toCharArray();
		char symbol = '"';
		boolean isFirstAndLatestQuotes = (symbols[0] == symbol) && (symbols[symbols.length - 1] == symbol);
		return isFirstAndLatestQuotes;
	}
	
	public static boolean isIntegerParam(String argument) {
		try {
			Integer.parseInt(argument);
			return true;
		} catch(NumberFormatException exp) {
			return false;
		}
	}
	
	public static void addVariableToMap(Map<String,Object> variables, String line) throws CodeParsingException {
		String name = getInitializedVariableName(line);
		String value = getInitializedVariableValue(line);
		Object object = formatObject(value);
		variables.put(name, object);
	}
	
	private static Matcher getMatcher(String regexp, String source) {
		return Pattern.compile(regexp).matcher(source);
	}
	
	private static Object getObjectFromVariables(Map<String,Object> variables, String name) {
		Object object = null;
		if(variables.containsKey(name))
			object = variables.get(name);
		return object;
	}
	
	private static Object formatObject(String argument) throws CodeParsingException {
		Object object = null;
		if(isStringParam(argument))
			object = removeFirstQuotesFromString(argument);
		else if(isIntegerParam(argument))
			object = Integer.parseInt(argument);
		else if(isBooleanParam(argument))
			object = Boolean.parseBoolean(argument);
		else
			throw new CodeParsingException("Unknown object type (" + argument + ")");
		return object;
	}
	
	private static String findValueOrThrow(String pattern, String value, String line) throws CodeParsingException {
		Matcher matcher = getMatcher(pattern, line);
		while(matcher.find())
			return matcher.group(value);
		throw new CodeParsingException("Name not found");
	}
}