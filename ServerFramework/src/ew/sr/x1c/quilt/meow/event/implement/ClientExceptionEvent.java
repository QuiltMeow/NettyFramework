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
public class ClientExceptionEvent extends Event implements Cancellable {

    private final ChannelHandlerContext context;
    private final Throwable cause;
    private final Client client;

    private boolean cancel;

    public ClientExceptionEvent(Client client, ChannelHandlerContext context, Throwable cause) {
        this.client = client;
        this.context = context;
        this.cause = cause;
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
