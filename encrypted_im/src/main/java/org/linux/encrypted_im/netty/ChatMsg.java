package org.linux.encrypted_im.netty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMsg implements Serializable {

    private static final long serialVersionUID = -4094739578202698617L;

    // 消息内容
    private String msg;

    // 发送者id
    private String senderId;

    // 接收者id
    private String receiverId;

    // 用于标记是否已读
    private String msgId;
}
