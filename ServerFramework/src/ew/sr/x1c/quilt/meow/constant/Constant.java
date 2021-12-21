package ew.sr.x1c.quilt.meow.constant;

public class Constant {

    public static final byte EXIT_SUCCESS = 0;
    public static final byte EXIT_WITH_ERROR = 1;

    public static final int USER_LIMIT = 1000;
    public static final int WAIT_TIMEOUT = 60;
    public static final int DEFAULT_PACKET_LENGTH_BYTE = 4;
    public static final int DEFAULT_PORT = 8093;

    public static final short PING_HEADER = 8092;
    public static final short PONG_HEADER = 8093;
    public static final int PING_WAIT = 30000;

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String DEFAULT_NAME = "棉被家族伺服器";
    public static final String DEFAULT_VERSION = "0.02";

    public static final int GUI_STATUS_UPDATE_INTERVAL = 5000;
}
