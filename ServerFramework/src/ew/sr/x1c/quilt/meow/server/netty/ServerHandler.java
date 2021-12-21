package ew.sr.x1c.quilt.meow.server.netty;

import ew.sr.x1c.quilt.meow.constant.Constant;
import ew.sr.x1c.quilt.meow.event.implement.ClientConnectEvent;
import ew.sr.x1c.quilt.meow.event.implement.ClientDisconnectEvent;
import ew.sr.x1c.quilt.meow.event.implement.ClientExceptionEvent;
import ew.sr.x1c.quilt.meow.event.implement.ClientIdleEvent;
import ew.sr.x1c.quilt.meow.event.implement.ClientMessageReceiveEvent;
import ew.sr.x1c.quilt.meow.packet.data.ByteArrayByteStream;
import ew.sr.x1c.quilt.meow.packet.data.LittleEndianAccessor;
import ew.sr.x1c.quilt.meow.server.Client;
import ew.sr.x1c.quilt.meow.server.GeneralManager;
import ew.sr.x1c.quilt.meow.server.Session;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.logging.Level;

@Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        Client client = (Client) context.channel().attr(Client.CLIENT_KEY).get();
        if (!GeneralManager.getInstance().getPluginManager().callEvent(new ClientExceptionEvent(client, context, cause)).isCancel()) {
            if (client != null) {
                client.disconnect();
            }
            context.channel().close();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object event) {
        Client client = (Client) context.channel().attr(Client.CLIENT_KEY).get();
        if (client == null) {
            context.channel().close();
            return;
        }
        if (!GeneralManager.getInstance().getPluginManager().callEvent(new ClientIdleEvent(client, context, event)).isCancel()) {
            client.sendPing();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        byte[] data = (byte[]) message;
        LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(data));
        if (slea.available() < 2) {
            return;
        }

        Client client = (Client) context.channel().attr(Client.CLIENT_KEY).get();
        if (client == null) {
            return;
        }

        short header = slea.readShort();
        if (header == Constant.PONG_HEADER) {
            client.pongReceive();
            return;
        }
        GeneralManager.getInstance().getPluginManager().callEvent(new ClientMessageReceiveEvent(client, context, header, slea));
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        Client client = (Client) context.channel().attr(Client.CLIENT_KEY).get();
        GeneralManager.getInstance().getPluginManager().callEvent(new ClientDisconnectEvent(client, context));
        if (client != null) {
            client.disconnect();
            context.channel().attr(Client.CLIENT_KEY).set(null);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        String ipAddress = context.channel().remoteAddress().toString().split(":")[0].substring(1);
        GeneralManager.getInstance().getLogger().log(Level.INFO, "客戶端 IP : {0} 已連線到您的伺服器", ipAddress);

        Client client = new Client(new Session(context.channel()));
        context.channel().attr(Client.CLIENT_KEY).set(client);
        GeneralManager.getInstance().getClientStorage().registerClient(client);
        GeneralManager.getInstance().getPluginManager().callEvent(new ClientConnectEvent(client, ipAddress, context));
    }
}
