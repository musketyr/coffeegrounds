package cz.orany.coffeegrounds

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class CoffeeHouseSpec extends Specification {

    @Rule TemporaryFolder tmp
    File root
    GroovyScriptEngine engine

    def setup() {
        root = tmp.newFolder('root')
        engine = new GroovyScriptEngine(root.absolutePath, getClass().classLoader)
    }

    def "class is available in the package"() {
        script 'test/Cup.cgs', '''
            class Cup
                drink: -> 'GLO GLO'
        '''
        script 'test/FarBugs.groovy', '@cz.orany.coffeegrounds.CoffeeHouse package test'
        script 'test/Runner.groovy',  '''
            package test
            Class cls = Cup
            assert cls.drink() == 'GLO GLO'
            cls

        '''

        when:
        run 'test/FarBugs.groovy'
        def result = run('test/Runner.groovy')

        then:
        result instanceof Class
    }


    private run(String path) {
        engine.run path, new Binding()
    }

    private File script(String path, String content) {
        File info = new File(root, path)
        info.getParentFile().mkdirs()
        info.createNewFile()
        info << content
        info
    }

}
