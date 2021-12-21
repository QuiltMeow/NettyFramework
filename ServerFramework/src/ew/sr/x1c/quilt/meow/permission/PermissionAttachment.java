package ew.sr.x1c.quilt.meow.permission;

import ew.sr.x1c.quilt.meow.plugin.Plugin;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;

public class PermissionAttachment {

    private final Map<String, Boolean> permission;

    @Getter
    private final Permissible permissible;

    @Getter
    private final Plugin plugin;

    public PermissionAttachment(Plugin plugin, Permissible permissible) {
        permission = new LinkedHashMap<>();
        this.permissible = permissible;
        this.plugin = plugin;
    }

    public Map<String, Boolean> getPermission() {
        return new LinkedHashMap<>(permission);
    }

    public void setPermission(String name, boolean value) {
        permission.put(name.toLowerCase(), value);
        permissible.recalculatePermission();
    }

    public void unsetPermission(String name) {
        permission.remove(name.toLowerCase());
        permissible.recalculatePermission();
    }

    public boolean remove() {
        try {
            permissible.removeAttachment(this);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
