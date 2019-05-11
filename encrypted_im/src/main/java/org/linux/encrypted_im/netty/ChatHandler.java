package org.linux.encrypted_im.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.linux.encrypted_im.utils.MD5Utils;

import java.time.LocalDateTime;

/**
 * 处理消息的handler
 * TextWebSocketFrame：在netty中，是用于WebSocket专门处理文本的对象，frame是消息的载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 可以将一组channel都保存到ChannelGroup中，用于记录和管理所有客户端的channel
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String content = textWebSocketFrame.text();
        System.out.println("客户端发送的消息内容为：" + content);

        System.out.println("after Digest: " + MD5Utils.getMD5Str(content));

        // 循环轮询每个channel进行writeAndFlush
//        for (Channel client : clients) {
//            client.writeAndFlush(new TextWebSocketFrame(
//                    "[服务器在]" + LocalDateTime.now() + "接收到消息，其内容为：" + content));
//        }

        // 使用ChannelGroup进行writeAndFlush，与轮询的效果相同
        clients.writeAndFlush(new TextWebSocketFrame(
                "[服务器在]" + LocalDateTime.now() + "接收到消息，其内容为：" + content));
    }

    // 客户端连接后触发
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 获取客户端的channel并且放到ChannelGroup中进行管理
        clients.add(ctx.channel());
    }

    // 客户端连接断开后触发
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 客户端断开连接时，clientGroup会自动将客户端的channel进行移除
//        clients.remove(ctx.channel());
        System.out.println("客户端断开连接，相应channel的长id为： " + ctx.channel().id().asLongText());
        System.out.println("客户端断开连接，相应channel的短id为： " + ctx.channel().id().asShortText());
    }
}
