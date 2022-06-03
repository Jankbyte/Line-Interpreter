<h2>Line interpreter</h2>
<p>Простой интерпретатор функций, основанный на рефлексии</p>
<h2>Использование</h2>
<ul>
  <li>Создайте объект интерпретатора:<code>Interpreter iterpreter = new Interpreter(Interpreter.getDefaultModules())</code></li>
  <li>Вызовите метод parse: <code>iterpreter.parse("println(\"Hello world\")")</code></li>
  <li>Чтобы добавить свой класс, аннотируйте его <a href="https://github.com/ByteC0d3/Line-Interpreter/blob/main/src/main/java/ru/kazbo/lineiterpreter/annotation/ScriptsLibrary.java">этой</a> аннотацией. Затем методы, которые собираетесь импортировать: <a href="https://github.com/ByteC0d3/Line-Interpreter/blob/main/src/main/java/ru/kazbo/lineiterpreter/annotation/ScriptMethod.java">этой</a> аннотацией</li>
</ul>
<li>Пример класса, который импортирует функцию println в скрипты: <a href="https://github.com/ByteC0d3/Line-Interpreter/blob/main/src/main/java/ru/kazbo/lineiterpreter/function/Println.java">Println</a></li>
