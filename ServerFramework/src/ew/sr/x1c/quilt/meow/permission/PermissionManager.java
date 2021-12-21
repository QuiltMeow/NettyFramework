package ew.sr.x1c.quilt.meow.permission;

import ew.sr.x1c.quilt.meow.plugin.Plugin;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Synchronized;

public class PermissionManager implements Permissible {

    private final List<PermissionAttachment> attachment;
    private final Map<String, PermissionAttachmentInfo> permission;

    public PermissionManager() {
        attachment = new LinkedList<>();
        permission = new HashMap<>();
        recalculatePermission();
    }

    @Override
    public boolean isPermissionSet(String name) {
        return permission.containsKey(name.toLowerCase());
    }

    @Override
    public boolean hasPermission(String name) {
        String lower = name.toLowerCase();
        if (isPermissionSet(lower)) {
            return permission.get(lower).getValue();
        }
        return false;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        PermissionAttachment result = addAttachment(plugin);
        result.setPermission(name, value);
        recalculatePermission();
        return result;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        PermissionAttachment result = new PermissionAttachment(plugin, this);
        attachment.add(result);
        recalculatePermission();
        return result;
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        if (this.attachment.contains(attachment)) {
            this.attachment.remove(attachment);
            recalculatePermission();
        } else {
            throw new IllegalArgumentException("指定權限附加物件不是該權限管理器的一部份");
        }
    }

    @Override
    public void recalculatePermission() {
        clearPermission();
        attachment.forEach((attach) -> {
            calculateChildPermission(attach.getPermission(), attach);
        });
    }

    @Synchronized
    public void clearPermission() {
        permission.clear();
    }

    private void calculateChildPermission(Map<String, Boolean> children, PermissionAttachment attachment) {
        children.keySet().forEach((name) -> {
            boolean value = children.get(name);
            String lower = name.toLowerCase();
            permission.put(lower, new PermissionAttachmentInfo(this, lower, attachment, value));
        });
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermission() {
        return new HashSet<>(permission.values());
    }
}
