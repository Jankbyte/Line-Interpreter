<h2>Line interpreter</h2>
<p>The simple line interpreter created with Reflection API</p>
<h2>How to use</h2>
<ul>
  <li>Create interpreter:<code>Interpreter iterpreter = new Interpreter(Interpreter.getDefaultModules())</code></li>
  <li>Invoke parse method: <code>iterpreter.parse("var message = \"Hello world!\"\nprintln(message)")</code></li>
  <li>For adding class into script, annotate him by <a href="https://github.com/ByteC0d3/Line-Interpreter/blob/main/src/main/java/ru/kazbo/lineiterpreter/annotation/ScriptsLibrary.java">this</a> annotation. Annotate methods for importing to script: <a href="https://github.com/ByteC0d3/Line-Interpreter/blob/main/src/main/java/ru/kazbo/lineiterpreter/annotation/ScriptMethod.java">this</a> annotation</li>
</ul>
<li>Example of library methods: <a href="https://github.com/ByteC0d3/Line-Interpreter/blob/main/src/main/java/ru/kazbo/lineiterpreter/function/Println.java">Println</a></li>
