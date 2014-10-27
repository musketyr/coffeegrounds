package cz.orany.coffeegrounds;

import org.codehaus.groovy.transform.GroovyASTTransformationClass;

import java.lang.annotation.*;

/**
 * Turns package into CoffeeHouse (host package for CoffeeScript scripts converted to Groovy).
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PACKAGE)
@GroovyASTTransformationClass("cz.orany.coffeegrounds.transform.CoffeeHouseTransformation")
public @interface CoffeeHouse {

}
