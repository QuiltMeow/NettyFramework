package ew.sr.x1c.quilt.meow.permission;

import ew.sr.x1c.quilt.meow.plugin.Plugin;
import java.util.Set;

public interface Permissible {

    boolean isPermissionSet(String name);

    boolean hasPermission(String name);

    PermissionAttachment addAttachment(Plugin plugin, String name, boolean value);

    PermissionAttachment addAttachment(Plugin plugin);

    void removeAttachment(PermissionAttachment attachment);

    void recalculatePermission();

    Set<PermissionAttachmentInfo> getEffectivePermission();
}
