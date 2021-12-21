package ew.sr.x1c.quilt.meow.permission;

import lombok.Data;

@Data
public class PermissionAttachmentInfo {

    private final Permissible permissible;
    private final String permission;
    private final PermissionAttachment attachment;
    private final boolean value;

    public PermissionAttachmentInfo(Permissible permissible, String permission, PermissionAttachment attachment, boolean value) {
        this.permissible = permissible;
        this.permission = permission;
        this.attachment = attachment;
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
