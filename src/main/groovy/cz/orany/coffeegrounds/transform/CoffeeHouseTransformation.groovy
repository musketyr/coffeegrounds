package cz.orany.coffeegrounds.transform

import cz.orany.coffeegrounds.util.CoffeeScriptParser
import groovy.transform.CompilationUnitAware
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.PackageNode
import org.codehaus.groovy.control.CompilationUnit
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import java.lang.reflect.Modifier

/**
 * Transformation triggered by CoffeeHouse annotation.
 */
@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class CoffeeHouseTransformation extends AbstractASTTransformation implements CompilationUnitAware {

    public static final String COFFEE_GROUNDS_FILE_EXTENSIONS = '.cgs'

    CompilationUnit compilationUnit
    CoffeeScriptParser parser = new CoffeeScriptParser()

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        if (nodes.size() != 2) return
        if (!(nodes[0] instanceof AnnotationNode)) return
        if (!(nodes[1] instanceof PackageNode)) return

        PackageNode pkg = nodes[1] as PackageNode

        if (!source.name) {
            addError("Cannot determine source location", pkg)
            return
        }

        URL url = null

        try {
           url = new URL(source.name)
        } catch (MalformedURLException ignored) {
            addError("Source URL ${source.name} is not valid URL", pkg)
            return
        }

        if (url.protocol != 'file') {
            addError("Source URL ${source.name} is not file URL", pkg)
            return
        }

        File packageRoot = new File(url.toURI()).parentFile

        packageRoot.listFiles ({ File dir, String name ->
            name.endsWith(COFFEE_GROUNDS_FILE_EXTENSIONS)
        } as FilenameFilter).each { File it ->
            source.AST.addClass(createClassForCoffeeScript(pkg, it))
        }
    }

    ClassNode createClassForCoffeeScript(PackageNode pkg, File coffeeScript) {
        ClassNode classNode = new ClassNode(pkg.name + coffeeScript.name - COFFEE_GROUNDS_FILE_EXTENSIONS, Modifier.PUBLIC, ClassHelper.OBJECT_TYPE)
        def coffeeAST = parser.parse(coffeeScript.newInputStream(), coffeeScript.absolutePath)

        return classNode
    }
}
