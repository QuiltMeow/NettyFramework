package ew.sr.x1c.quilt.meow.example.plugin.packet.header;

public enum SendPacketOPCode {

    GET_HELLO(0x00),
    ECHO_MESSAGE(0x01),
    RANDOM_RESPONSE(0x02),
    FILE_SEND(0x03);

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
