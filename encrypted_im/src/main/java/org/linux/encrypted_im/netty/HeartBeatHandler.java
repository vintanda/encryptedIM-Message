package org.linux.encrypted_im.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 用于监测channel的心跳handler
 * 直接继承ChannelInboundHandlerAdapter, 不需要继承channelRead0这个抽象方法
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // IdleStateEvent 是一个当有一个Channel空闲时会触发的事件(触发的事件包含：读空闲/写空闲/读写空闲)
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;

            // 读空闲
            if (event.state() == IdleState.READER_IDLE) {
                System.out.println("进入读空闲");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                System.out.println("进入写空闲");
            } else if (event.state() == IdleState.ALL_IDLE) {
                System.out.println("channel关闭前，users的数量：" + ChatHandler.users.size());
                // 客户端已经进入飞行模式或信号中断等
                Channel channel = ctx.channel();
                // 关闭无用的Channel以防资源浪费
                channel.close();
                System.out.println("channel关闭后，users的数量：" + ChatHandler.users.size());
            }
        }
    }

}
