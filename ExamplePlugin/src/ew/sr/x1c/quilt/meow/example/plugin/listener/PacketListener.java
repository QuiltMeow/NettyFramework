package ew.sr.x1c.quilt.meow.example.plugin.listener;

import ew.sr.x1c.quilt.meow.event.EventHandler;
import ew.sr.x1c.quilt.meow.event.implement.ClientMessageReceiveEvent;
import ew.sr.x1c.quilt.meow.example.plugin.crypto.AESCrypto;
import ew.sr.x1c.quilt.meow.example.plugin.packet.header.ReceivePacketOPCode;
import ew.sr.x1c.quilt.meow.example.plugin.packet.header.SendPacketOPCode;
import ew.sr.x1c.quilt.meow.packet.data.LittleEndianAccessor;
import ew.sr.x1c.quilt.meow.packet.data.PacketLittleEndianWriter;
import ew.sr.x1c.quilt.meow.plugin.Listener;
import ew.sr.x1c.quilt.meow.server.GeneralManager;
import ew.sr.x1c.quilt.meow.util.Randomizer;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.logging.Level;

public class PacketListener implements Listener {

    @EventHandler
    public void onPacketReceive(ClientMessageReceiveEvent event) {
        LittleEndianAccessor slea = event.getPacket();
        ReceivePacketOPCode header = ReceivePacketOPCode.getOPCode(event.getHeader());
        if (header == null) {
            return;
        }
        try {
            switch (header) {
                case GET_HELLO: {
                    String uuid = event.getClient().getUUID().toString();
                    PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
                    mplew.writeShort(SendPacketOPCode.GET_HELLO.getHeader());
                    mplew.writeLengthAsciiString(uuid);
                    event.getClient().getSession().write(mplew.getPacket());
                    break;
                }
                case ECHO_MESSAGE: {
                    String read = slea.readLengthAsciiString();
                    PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
                    mplew.writeShort(SendPacketOPCode.ECHO_MESSAGE.getHeader());
                    mplew.writeLengthAsciiString(read);
                    event.getClient().getSession().write(mplew.getPacket());
                    break;
                }
                case RANDOM_REQUEST: {
                    int max = slea.readInt();
                    PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
                    mplew.writeShort(SendPacketOPCode.RANDOM_RESPONSE.getHeader());
                    try {
                        if (max <= 0) {
                            mplew.writeInt(-1);
                            break;
                        }
                        int ret = Randomizer.nextInt(max);
                        mplew.writeInt(ret);
                    } finally {
                        event.getClient().getSession().write(mplew.getPacket());
                    }
                    break;
                }
                case FILE_DOWNLOAD: {
                    try (InputStream is = getClass().getResourceAsStream("/ew/sr/x1c/sample/sample.mp4")) {
                        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                            byte[] buffer = new byte[32768];
                            int length;
                            while ((length = is.read(buffer)) != -1) {
                                baos.write(buffer, 0, length);
                            }

                            byte[] data = AESCrypto.CBCEncrypt(baos.toByteArray());
                            PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
                            mplew.writeShort(SendPacketOPCode.FILE_SEND.getHeader());
                            mplew.writeInt(data.length);
                            mplew.write(data);
                            event.getClient().getSession().write(mplew.getPacket());
                        }
                    } catch (Exception ex) {
                        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
                        mplew.writeShort(SendPacketOPCode.FILE_SEND.getHeader());
                        mplew.writeInt(-1);
                        event.getClient().getSession().write(mplew.getPacket());
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            GeneralManager.getInstance().getLogger().log(Level.WARNING, "處理封包時發生例外狀況", ex);
        }
    }
}
