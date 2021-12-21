package ew.sr.x1c.quilt.meow.command;

import ew.sr.x1c.quilt.meow.permission.PermissionAttachmentInfo;
import java.util.Collections;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsoleCommandSender implements CommandSender {

    @Getter
    private static final ConsoleCommandSender instance = new ConsoleCommandSender();

    @Override
    public String getName() {
        return "主控台";
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public Set<PermissionAttachmentInfo> getPermission() {
        return Collections.emptySet();
    }
}
