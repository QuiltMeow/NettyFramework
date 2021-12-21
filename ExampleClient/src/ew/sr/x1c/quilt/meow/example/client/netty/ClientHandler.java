package ew.sr.x1c.quilt.meow.example.client.netty;

import ew.sr.x1c.quilt.meow.example.client.Client;
import ew.sr.x1c.quilt.meow.example.client.Session;
import ew.sr.x1c.quilt.meow.example.client.SimpleVideoPlayer;
import ew.sr.x1c.quilt.meow.example.client.packet.data.ByteArrayByteStream;
import ew.sr.x1c.quilt.meow.example.client.packet.data.LittleEndianAccessor;
import ew.sr.x1c.quilt.meow.example.client.packet.data.PacketLittleEndianWriter;
import ew.sr.x1c.quilt.meow.example.client.packet.header.ReceivePacketOPCode;
import ew.sr.x1c.quilt.meow.example.client.packet.header.SendPacketOPCode;
import ew.sr.x1c.quilt.meow.example.client.util.AESCrypto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import javax.swing.JOptionPane;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static SimpleVideoPlayer svp;

    @Override
    public void channelActive(ChannelHandlerContext context) {
        Client.getInstance().setSession(new Session(context.channel()));
        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(SendPacketOPCode.GET_HELLO.getHeader());
        context.channel().writeAndFlush(mplew.getPacket());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object event) {
        context.channel().close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        JOptionPane.showMessageDialog(Client.getInstance(), "與伺服器中斷連線");
        System.exit(0);
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        byte[] data = (byte[]) message;
        LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(data));
        if (slea.available() < 2) {
            return;
        }

        ReceivePacketOPCode header = ReceivePacketOPCode.getOPCode(slea.readShort());
        if (header != null) {
            handlePacket(header, slea);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        context.channel().close();
    }

    private static <T> void logResponse(ReceivePacketOPCode header, T response) {
        Client.getInstance().getLogger().log(Level.INFO, "[{0}] 服務端回應 : {1}", new Object[]{
            header.name(), response.toString()
        });
    }

    private void handlePacket(ReceivePacketOPCode header, LittleEndianAccessor slea) {
        try {
            switch (header) {
                case GET_HELLO: {
                    String name = slea.readLengthAsciiString();
                    Client.getInstance().setClientName(name);
                    Client.getInstance().getLogger().log(Level.INFO, "服務端回應客戶端名稱 : {0}", name);
                    break;
                }
                case ECHO_MESSAGE: {
                    String response = slea.readLengthAsciiString();
                    logResponse(header, response);
                    break;
                }
                case RANDOM_RESPONSE: {
                    int response = slea.readInt();
                    logResponse(header, response);
                    break;
                }
                case FILE_SEND: {
                    int size = slea.readInt();
                    if (size == -1) {
                        Client.getInstance().getLogger().log(Level.WARNING, "服務端傳送檔案時發生錯誤");
                        break;
                    }
                    byte[] data = AESCrypto.CBCDecrypt(slea.read(size));
                    Client.getInstance().getLogger().log(Level.INFO, "檔案接收完成");

                    File temp = File.createTempFile("receive" + Long.toString(System.nanoTime()), ".mp4");
                    String path = temp.getAbsolutePath();
                    Files.write(Paths.get(path), data);

                    Thread player = new Thread(() -> {
                        if (svp != null) {
                            svp.dispatchEvent(new WindowEvent(svp, WindowEvent.WINDOW_CLOSING));
                        }
                        svp = new SimpleVideoPlayer(path);
                        svp.setVisible(true);
                    });
                    player.setUncaughtExceptionHandler((thread, th) -> {
                        Client.getInstance().getLogger().log(Level.WARNING, "播放器發生例外狀況", th);
                    });
                    player.start();
                    break;
                }
                case PING: {
                    PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
                    mplew.writeShort(SendPacketOPCode.PONG.getHeader());
                    Client.getInstance().getSession().write(mplew.getPacket());
                    break;
                }
            }
        } catch (Exception ex) {
            Client.getInstance().getLogger().log(Level.WARNING, "處理封包時發生例外狀況", ex);
        }
    }
}
