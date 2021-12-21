package ew.sr.x1c.quilt.meow.command;

import ew.sr.x1c.quilt.meow.permission.PermissionAttachmentInfo;
import java.util.Set;

public interface CommandSender {

    String getName();

    boolean hasPermission(String permission);

    Set<PermissionAttachmentInfo> getPermission();
}
