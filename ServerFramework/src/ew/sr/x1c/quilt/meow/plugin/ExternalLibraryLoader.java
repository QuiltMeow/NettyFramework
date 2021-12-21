package ew.sr.x1c.quilt.meow.plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ExternalLibraryLoader {

    public static URL getJarURL(File file) throws IOException {
        return new URL("jar:" + file.toURI().toURL().toExternalForm() + "!/");
    }

    public static void loadLibrary(URL url) throws Exception {
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{
            URL.class
        });
        method.setAccessible(true);
        method.invoke((URLClassLoader) ClassLoader.getSystemClassLoader(), new Object[]{
            url
        });
    }
}
