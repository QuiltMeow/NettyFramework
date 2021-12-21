package ew.sr.x1c.quilt.meow.server;

public interface Server {

    int getPort();

    void start() throws Exception;

    void stop();
}
