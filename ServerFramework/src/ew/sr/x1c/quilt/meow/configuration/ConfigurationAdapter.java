package ew.sr.x1c.quilt.meow.configuration;

import java.util.Collection;

public interface ConfigurationAdapter {

    int getInt(String path, int def);

    String getString(String path, String def);

    boolean getBoolean(String path, boolean def);

    void load();

    Collection<?> getList(String path, Collection<?> def);

    void set(String path, Object value);

    <T> T get(String path, T def);

    void save();
}
