package cz.orany.coffeegrounds.util

import groovy.json.JsonOutput
import spock.lang.Specification

class CoffeeScriptParserSpec extends Specification {

    CoffeeScriptParser parser = new CoffeeScriptParser()

    def "parses valid script"() {
        def result = parser.parse(new ByteArrayInputStream("""

        main = ->
            console.log 'Hello World'

        do main

        """.bytes))

        println JsonOutput.prettyPrint(JsonOutput.toJson(result))

        expect:
        result
        result.expressions != null
        result.expressions.size() == 2
    }

    def "fails with invalid script"() {
        when:
        parser.parse(new ByteArrayInputStream("""

        main = ->
            console.log 'Hello World'

                do main

        """.bytes), "test.coffee")

        then:
        IllegalArgumentException e = thrown(IllegalArgumentException)
        e.message == "Failed to parse 'test.coffee'"
    }

}
