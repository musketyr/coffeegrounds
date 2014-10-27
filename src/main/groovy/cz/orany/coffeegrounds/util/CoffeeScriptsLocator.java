package cz.orany.coffeegrounds.util;

import java.io.InputStream;

/**
 * Interface of classes which are able to locate CoffeeScript files on the classpath.
 */
public interface CoffeeScriptsLocator {

    /**
     * Returns all CoffeeScript files for given package as input streams.
     *
     * @param pkg package for which the interface is looking for related CoffeeScripts
     * @return all CoffeeScript files for given package as input streams
     */
    public abstract Iterable<InputStream> getCoffeeScripts(Package pkg);
}
