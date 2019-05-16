package org.linux.encrypted_im.netty;

import io.netty.channel.Channel;

import java.util.HashMap;

/**
 * @Description: 用户id和channel的关联关系处理
 */
public class UserChannelRel {

    // 管理userId和Channel对应关系的map
    private static HashMap<String, Channel> manager = new HashMap<>();

    // 放入关联关系
    public static void put(String senderId, Channel channel) {
        manager.put(senderId, channel);
    }

    public static Channel get(String senderId) {
        return manager.get(senderId);
    }

    public static void output() {
        for (HashMap.Entry<String, Channel> entry : manager.entrySet()) {
            System.out.println("UserId:" + entry.getKey()
                    + ", ChannelId:" + entry.getValue().id().asLongText());
        }
    }
}
