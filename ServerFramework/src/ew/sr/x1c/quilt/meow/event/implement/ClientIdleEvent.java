package ew.sr.x1c.quilt.meow.event.implement;

import ew.sr.x1c.quilt.meow.plugin.Cancellable;
import ew.sr.x1c.quilt.meow.plugin.Event;
import ew.sr.x1c.quilt.meow.server.Client;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class ClientIdleEvent extends Event implements Cancellable {

    private final Client client;
    private final ChannelHandlerContext context;
    private final Object event;

    private boolean cancel;

    public ClientIdleEvent(Client client, ChannelHandlerContext context, Object event) {
        this.client = client;
        this.context = context;
        this.event = event;
    }

    @Override
    public boolean isCancel() {
        return cancel;
    }

    @Override
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }
}
