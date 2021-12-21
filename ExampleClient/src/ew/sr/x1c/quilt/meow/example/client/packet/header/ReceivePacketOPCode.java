package ew.sr.x1c.quilt.meow.example.client.packet.header;

import ew.sr.x1c.quilt.meow.example.client.constant.Constant;

public enum ReceivePacketOPCode {

    GET_HELLO(0x00),
    ECHO_MESSAGE(0x01),
    RANDOM_RESPONSE(0x02),
    FILE_SEND(0x03),
    PING(Constant.PING_HEADER);

    private final int header;

    public int getHeader() {
        return header;
    }

    private ReceivePacketOPCode(int header) {
        this.header = header;
    }

    public static ReceivePacketOPCode getOPCode(short header) {
        for (ReceivePacketOPCode type : values()) {
            if (type.getHeader() == header) {
                return type;
            }
        }
        return null;
    }
}
