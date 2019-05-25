package org.linux.encrypted_im.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class WSServerInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 从channel获取到pipeline
        ChannelPipeline pipeline = socketChannel.pipeline();

        // ======================== 以下是用于支持http协议 ============================
        // websocket基于http协议，需要一个Http的编解码器
        pipeline.addLast(new HttpServerCodec());

        // 对写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());

        // 对httpMsg进行聚合，聚合成FullHttpRequest或FullHTTPResponse
        // 几乎在netty中的编程都会使用到此handler
        pipeline.addLast(new HttpObjectAggregator(1024*64));

        // 支持心跳机制
        // 激活心跳支持
        // 针对客户端,如果在1分钟内没有向服务端发送读写心跳（ALL），主动断开连接
        // 如果是读空闲/写空闲 不作处理
        pipeline.addLast(new IdleStateHandler(8, 10, 12));
        // 自定义的空闲状态检测
        pipeline.addLast(new HeartBeatHandler());

        /**
         * websocket服务器处理的协议，用于指定给客户端连接访问的路由：/ws
         * 该Handler会帮你处理一些繁重的复杂的事
         * 处理握手操作：handshaking(close, ping, pong) ping + pong = 心跳
         * 对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        // 自定义handler
        pipeline.addLast(new ChatHandler());
    }
}
