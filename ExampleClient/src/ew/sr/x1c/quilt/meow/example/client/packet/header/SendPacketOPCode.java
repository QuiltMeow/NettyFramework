package ew.sr.x1c.quilt.meow.example.client.packet.header;

import ew.sr.x1c.quilt.meow.example.client.constant.Constant;

public enum SendPacketOPCode {

    GET_HELLO(0x00),
    ECHO_MESSAGE(0x01),
    RANDOM_REQUEST(0x02),
    FILE_DOWNLOAD(0x03),
    PONG(Constant.PONG_HEADER);

    private final int header;

    public int getHeader() {
        return header;
    }

    private SendPacketOPCode(int header) {
        this.header = header;
    }

    public static SendPacketOPCode getOPCode(short header) {
        for (SendPacketOPCode type : values()) {
            if (type.getHeader() == header) {
                return type;
            }
        }
        return null;
    }
}
