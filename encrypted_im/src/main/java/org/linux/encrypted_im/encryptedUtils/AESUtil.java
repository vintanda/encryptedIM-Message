package org.linux.encrypted_im.encryptedUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class AESUtil {

    /**
     * @Description 加密
     * @param content 需要被加密的字符串
     * @param key     加密需要的密码
     * @return 密文
     */
    public static byte[] encrypt(String content, String key) throws Exception {

        byte[] byteContent = content.getBytes("utf-8");

        Cipher cipher = getCipher(byteContent, key, Cipher.ENCRYPT_MODE);

        byte[] result = cipher.doFinal(byteContent);// 加密

        return result;

    }

    /**
     * @Description 解密
     * @param content AES加密过的内容
     * @param key     加密时的密码
     * @return 明文
     */
    public static byte[] decrypt(byte[] content, String key) throws Exception {
        Cipher cipher = getCipher(content, key, Cipher.DECRYPT_MODE);
        byte[] result = cipher.doFinal(content);

        return result; // 明文
    }

    public static Cipher getCipher(byte[] content, String key, int enOrDe) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
        kgen.init(128, new SecureRandom(key.getBytes()));// 利用用户密码作为随机数初始化出
        // 128位的key生产者
        //加密没关系，SecureRandom是生成安全随机数序列，key.getBytes()是种子，
        // 只要种子相同，序列就一样，所以解密只要有key就行
        SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
        byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回null

        SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥

        Cipher cipher = Cipher.getInstance("AES");// 创建密码器

        cipher.init(enOrDe, keySpec);// 初始化为解密模式的密码器

        return cipher;
    }

}