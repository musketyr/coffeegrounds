package cz.orany.coffeegrounds.util

import javax.script.Invocable
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

/**
 * CoffeeScriptParser parses the CoffeeScript file into the CoffeeScript AST.
 * Inspired by https://github.com/netopyr/coffee4java/blob/master/src/main/java/com/netopyr/coffee4java/CoffeeScriptCompiler.java
 */
class CoffeeScriptParser {

    private static final String COFFEE_SCRIPT_JS = "coffee-script.js";
    private static final String COFFEE_SCRIPT_NAMESPACE = "CoffeeScript";
    private static final String PARSE_METHOD_NAME = "nodes";

    private Object coffeeScript
    private Invocable parser

    CoffeeScriptParser() {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName('javascript')
        assert engine instanceof Invocable
        engine.eval(getClass().getResourceAsStream(COFFEE_SCRIPT_JS).newReader())
        parser = engine
        coffeeScript = engine.get(COFFEE_SCRIPT_NAMESPACE);
    }

    def parse(InputStream stream, String sourceName = null) {
        try {
            parser.invokeMethod(coffeeScript, PARSE_METHOD_NAME, stream.text)
        } catch (e) {
            throw new IllegalArgumentException("Failed to parse '${sourceName ?: 'Given script'}'", e)
        }
    }

}
