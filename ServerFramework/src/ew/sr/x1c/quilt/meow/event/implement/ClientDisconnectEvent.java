package ew.sr.x1c.quilt.meow.event.implement;

import ew.sr.x1c.quilt.meow.plugin.Event;
import ew.sr.x1c.quilt.meow.server.Client;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class ClientDisconnectEvent extends Event {

    private final Client client;
    private final ChannelHandlerContext context;

    public ClientDisconnectEvent(Client client, ChannelHandlerContext context) {
        this.client = client;
        this.context = context;
    }
}
