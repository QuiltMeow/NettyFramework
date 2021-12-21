package ew.sr.x1c.quilt.meow.server.netty;

import ew.sr.x1c.quilt.meow.server.Server;
import ew.sr.x1c.quilt.meow.constant.Constant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;

public final class NettyServer implements Server {

    @Getter
    private final int port;

    private ServerBootstrap boot;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private Channel channel;

    public NettyServer(int port) {
        this.port = port;
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
    }

    @Override
    public void start() {
        try {
            boot = new ServerBootstrap().group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, Constant.USER_LIMIT)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ServerInitializer());

            channel = boot.bind(port).sync().channel().closeFuture().channel();
        } catch (Exception ex) {
            throw new RuntimeException("伺服器啟動失敗", ex);
        }
    }

    @Override
    public void stop() {
        if (channel != null) {
            channel.close();
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public int getPort() {
        return port;
    }
}
