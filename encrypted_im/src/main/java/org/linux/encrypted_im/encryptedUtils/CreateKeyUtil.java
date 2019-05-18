package org.linux.encrypted_im.encryptedUtils;

import org.apache.commons.codec.binary.Hex;

/**
 * @description: 根据用户的id和客户端发送过来的PublicKey动态生成秘钥的种子值
 */
public class CreateKeyUtil {

    public static String createAESKeyForChat(String id, String senderPublicKey) {
        byte[] idBytes = id.getBytes();
        byte[] senderPublicKeyBytes = senderPublicKey.getBytes();
        byte[] resultBytes = new byte[(id.length() + senderPublicKey.length()) / 2];

        int indexId = 1;
        int indexPsd = 5;
        for (int i = 0;i < resultBytes.length;) {
            if (indexId < idBytes.length - 1) {
                resultBytes[i] = idBytes[indexId];
                indexId++;
                i++;
            }
            if (indexPsd < senderPublicKeyBytes.length - 1) {
                resultBytes[i] = senderPublicKeyBytes[indexPsd];
                indexPsd++;
                i++;
            }
        }

        return Hex.encodeHexString(resultBytes);
    }
}
