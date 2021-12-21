package ew.sr.x1c.quilt.meow.event.implement;

import ew.sr.x1c.quilt.meow.packet.data.LittleEndianAccessor;
import ew.sr.x1c.quilt.meow.plugin.Event;
import ew.sr.x1c.quilt.meow.server.Client;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class ClientMessageReceiveEvent extends Event {

    private final short header;
    private final Client client;
    private final ChannelHandlerContext context;
    private final LittleEndianAccessor packet;

    public ClientMessageReceiveEvent(Client client, ChannelHandlerContext context, short header, LittleEndianAccessor packet) {
        this.client = client;
        this.context = context;
        this.header = header;
        this.packet = packet;
    }
}
