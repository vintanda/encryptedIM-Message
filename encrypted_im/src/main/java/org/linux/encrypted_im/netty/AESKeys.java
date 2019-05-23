package org.linux.encrypted_im.netty;

import io.netty.channel.Channel;

import java.util.HashMap;

public class AESKeys {

    // 管理userId和aesKey对应关系的map
    private static HashMap<Channel, String> keys = new HashMap<>();

    // 放入关联关系
    public static void put(Channel userChannel, String aesKey) {
        keys.put(userChannel, aesKey);
    }

    public static String get(Channel userChannel) {
        return keys.get(userChannel);
    }

    public static void remove(Channel userChannel) {
        keys.remove(userChannel);
    }

    public static void output() {
        for (HashMap.Entry<Channel, String> entry : keys.entrySet()) {
            System.out.println("UserChannel:" + entry.getKey().id().asShortText()
                    + ", AESKey:" + entry.getValue());
        }
    }
}
