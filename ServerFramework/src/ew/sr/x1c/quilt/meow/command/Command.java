package ew.sr.x1c.quilt.meow.command;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.NONE)
public abstract class Command {

    private final String name;
    private final String permission;
    private final String[] alias;

    public Command(String name) {
        this(name, null);
    }

    public Command(String name, String permission, String... alias) {
        Preconditions.checkArgument(name != null, "name");
        this.name = name;
        this.permission = permission;
        this.alias = alias;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public boolean hasPermission(CommandSender sender) {
        return permission == null || permission.isEmpty() || sender.hasPermission(permission);
    }
}
