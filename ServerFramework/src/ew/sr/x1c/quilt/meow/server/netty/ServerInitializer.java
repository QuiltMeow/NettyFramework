package ew.sr.x1c.quilt.meow.server.netty;

import ew.sr.x1c.quilt.meow.constant.Constant;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import java.nio.ByteOrder;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("idle", new IdleStateHandler(Constant.WAIT_TIMEOUT, 0, 0));
        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, Integer.MAX_VALUE, 0, Constant.DEFAULT_PACKET_LENGTH_BYTE, 0, Constant.DEFAULT_PACKET_LENGTH_BYTE, true));
        pipeline.addLast("frameEncoder", new LengthFieldPrepender(ByteOrder.LITTLE_ENDIAN, Constant.DEFAULT_PACKET_LENGTH_BYTE, 0, false));
        pipeline.addLast("decoder", new ByteArrayDecoder());
        pipeline.addLast("encoder", new ByteArrayEncoder());
        pipeline.addLast("handler", new ServerHandler());
    }
}
