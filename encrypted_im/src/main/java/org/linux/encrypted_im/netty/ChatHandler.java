package org.linux.encrypted_im.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.linux.encrypted_im.enums.MsgActionEnum;
import org.linux.encrypted_im.service.UserService;
import org.linux.encrypted_im.utils.JSONUtils;
import org.linux.encrypted_im.utils.MD5Utils;
import org.linux.encrypted_im.utils.SpringUtil;

import java.time.LocalDateTime;

/**
 * 处理消息的handler
 * TextWebSocketFrame：在netty中，是用于WebSocket专门处理文本的对象，frame是消息的载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 可以将一组channel都保存到ChannelGroup中，用于记录和管理所有客户端的channel
    private static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        // 获取客户端传入的信息
        String content = textWebSocketFrame.text();
        System.out.println("客户端发送的消息内容为：" + content);

        // 获取当前的channel
        Channel currentChannel = channelHandlerContext.channel();

        /*
        1. 获取客户端传输过来的信息
        2. 判断消息类型，根据不同的类型来处理不同的业务
            2.1 当websocket第一次open的时候，初始化channel，把channel和userId进行关联
            2.2 聊天类型的消息，把聊天记录保存到数据库，同事标记是否被接受者接收到【是否已读】
            2.3 签收消息类型，针对具体的消息进行签收，修改数据库中对应消息的状态【标记已读】
            2.4 心跳类型消息
         */
        // 1. 获取客户端传输过来的信息
        DataContent dataContent = JSONUtils.jsonToPojo(content, DataContent.class);
        Integer action = dataContent.getAction();

        // 2. 判断消息类型，根据不同的类型来处理不同的业务
        if (action == MsgActionEnum.CONNECT.type) {
            // 2.1 当websocket第一次open的时候，初始化channel，把channel和userId进行关联
            String senderId = dataContent.getChatMsg().getSenderId();
            UserChannelRel.put(senderId, currentChannel);
        } else if (action == MsgActionEnum.CHAT.type) {
            // 2.2 聊天类型的消息，把聊天记录保存到数据库，同时标记是否被接受者接收到【标记未读】
            // get各种属性值
            ChatMsg chatMsg = dataContent.getChatMsg();
            String msgText = chatMsg.getMsg();
            String receiverId = chatMsg.getReceiverId();
            String senderId = chatMsg.getSenderId();

            // 把聊天记录保存到数据库，同时标记未读
            UserService userService = (UserService) SpringUtil.getBean("userServiceImpl");
            String msgId = userService.saveMsg(chatMsg);
            chatMsg.setMsgId(msgId);

            // 发送消息给接受者


        } else if (action == MsgActionEnum.SIGNED.type) {
            // 2.3 签收消息类型，针对具体的消息进行签收，修改数据库中对应消息的状态【标记已读】
        } else if (action == MsgActionEnum.KEEPALIVE.type) {
            // 2.4 心跳类型消息
        }
    }

    // 客户端连接后触发
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 获取客户端的channel并且放到ChannelGroup中进行管理
        System.out.println("add channel..." + ctx.channel().id());
        users.add(ctx.channel());
    }

    // 客户端连接断开后触发
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 客户端断开连接时，clientGroup会自动将客户端的channel进行移除
//        clients.remove(ctx.channel());
        System.out.println("客户端断开连接，相应channel的长id为： " + ctx.channel().id().asLongText());
        System.out.println("客户端断开连接，相应channel的短id为： " + ctx.channel().id().asShortText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常后关闭连接(关闭channel)并将其从ChannelGroup中移除
        ctx.channel().close();
        users.remove(ctx.channel());
    }
}
