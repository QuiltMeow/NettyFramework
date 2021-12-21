package ew.sr.x1c.quilt.meow.example.plugin.packet.header;

public enum ReceivePacketOPCode {

    GET_HELLO(0x00),
    ECHO_MESSAGE(0x01),
    RANDOM_REQUEST(0x02),
    FILE_DOWNLOAD(0x03);

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
