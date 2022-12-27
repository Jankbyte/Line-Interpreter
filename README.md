# Line interpreter
The simple line interpreter created with Reflection API.
## How to use
```
var interpreter = new Interpreter(Interpreter.getDefaultModules())

var code = """
    var message = "Hello world!"
    println(message)
    """;

interpreter.parse(code);
```
