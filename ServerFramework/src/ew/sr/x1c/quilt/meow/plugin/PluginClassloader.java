package ew.sr.x1c.quilt.meow.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class PluginClassloader extends URLClassLoader {

    private static final Set<PluginClassloader> ALL_LOADER;

    static {
        ALL_LOADER = new CopyOnWriteArraySet<>();
        ClassLoader.registerAsParallelCapable();
    }

    public PluginClassloader(URL[] url) {
        super(url);
        ALL_LOADER.add(this);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return loadClassInternal(name, resolve, true);
    }

    private Class<?> loadClassInternal(String name, boolean resolve, boolean checkOther) throws ClassNotFoundException {
        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException ex) {
        }
        if (checkOther) {
            for (PluginClassloader loader : ALL_LOADER) {
                if (loader != this) {
                    try {
                        return loader.loadClassInternal(name, resolve, false);
                    } catch (ClassNotFoundException ex) {
                    }
                }
            }
        }
        throw new ClassNotFoundException(name);
    }
}
