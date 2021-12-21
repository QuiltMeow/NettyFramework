package ew.sr.x1c.quilt.meow.server;

import ew.sr.x1c.quilt.meow.command.CommandSender;
import ew.sr.x1c.quilt.meow.constant.Constant;
import ew.sr.x1c.quilt.meow.packet.data.PacketLittleEndianWriter;
import ew.sr.x1c.quilt.meow.permission.Permissible;
import ew.sr.x1c.quilt.meow.permission.PermissionAttachmentInfo;
import ew.sr.x1c.quilt.meow.permission.PermissionManager;
import ew.sr.x1c.quilt.meow.schedule.TaskScheduler.ScheduleTimer;
import ew.sr.x1c.quilt.meow.util.Randomizer;
import io.netty.util.AttributeKey;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

public class Client implements CommandSender, Serializable {

    public static final AttributeKey<Client> CLIENT_KEY = AttributeKey.valueOf("Client");

    @Getter
    @Setter
    private String account;

    @Getter
    @Setter
    private String name;

    @Getter
    private final Session session;

    @Getter
    private final UUID uuid;

    @Getter
    private long lastPong, lastPing;

    @Getter
    private final Date onlineDate;

    @Getter
    private final Permissible permission;

    @Getter
    private final long randomId;

    public Client(Session session) {
        this.session = session;
        uuid = UUID.randomUUID();
        randomId = Randomizer.nextLong();
        onlineDate = new Date();
        permission = new PermissionManager();
    }

    public void disconnect() {
        GeneralManager.getInstance().getClientStorage().deregisterClient(uuid);
    }

    public UUID getUUID() {
        return uuid;
    }

    public long getLatency() {
        return lastPong - lastPing;
    }

    public void sendPing() {
        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(Constant.PING_HEADER);
        session.write(mplew.getPacket());
        lastPing = System.currentTimeMillis();

        ScheduleTimer.getInstance().schedule(() -> {
            if (getLatency() < 0) {
                disconnect();
                session.close();
            }
        }, Constant.PING_WAIT);
    }

    public void pongReceive() {
        lastPong = System.currentTimeMillis();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.permission.hasPermission(permission);
    }

    @Override
    public Set<PermissionAttachmentInfo> getPermission() {
        return permission.getEffectivePermission();
    }
}
