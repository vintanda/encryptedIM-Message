/**
 * Create by LZD on 2019/03/18
 */
package org.linux.encrypted_im.encryptedUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
/*
    对称加密算法：
        DES(Date Encryption Standard)数据加密标准
            3DES
        AES
        PBE
        IDEA
 */

/**
 * DES ---- 根据不同的工作模式和填充方式, 其被破解的难易程度不同
 * 密钥长度     默认      工作模式            填充方式          实现方式
 * ------------------------------------------------------------------
 *   56        56    ECB/CBC/PCBC/CTR    NoPadding           jdk
 *                   CTS/CTB/CFB8到128   PKCS5Padding
 *                   OFB/OFB8到128       ISO10126Padding
 * ------------------------------------------------------------------
 *   64        56    同上                PKCS7Padding         BC
 *                                      ISO10126d2Padding
 *                                      X932Padding
 *                                      ISO7816d4Padding
 *                                      ZeroBytePadding
 */
public class DESUtil {
    private static String src = "des security...";

    public static void main(String[] args) {
        jdkDES();
    }

    // 把bc的provider加进来/使用bc原生的类, 更推荐使用前者
    public static void bcDES() {
        try {
//            Security.addProvider(new )
            // 生成key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES", "BC");
            keyGenerator.getProvider();     // 调试可查看provider, 需要在上一行的getInstance()中指定
            keyGenerator.init(56);      // 生成密钥的长度
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] bytesKey = secretKey.getEncoded();

            // KEY转换
            DESKeySpec desKeySpec = new DESKeySpec(bytesKey);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");     // 指定加密方式
            Key convertSecretKey = factory.generateSecret(desKeySpec);

            // 加密
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");     // 加密方式/工作方式/填充方式
            cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);     // param0:模式 param1:转换的key
            byte[] result = cipher.doFinal(src.getBytes());
            System.out.println("bc des encrypt:" + DatatypeConverter.printHexBinary(result));

            // 解密
            cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
            result = cipher.doFinal(result);
            System.out.println("bc des decrypt:" + new String(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void jdkDES() {
        try {
            // 生成key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
            keyGenerator.init(56);      // 生成密钥的长度
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] bytesKey = secretKey.getEncoded();

            // KEY转换
            DESKeySpec desKeySpec = new DESKeySpec(bytesKey);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");     // 指定加密方式
            Key convertSecretKey = factory.generateSecret(desKeySpec);

            // 加密
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");     // 加密方式/工作方式/填充方式
            cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);     // param0:模式 param1:转换的key
            byte[] result = cipher.doFinal(src.getBytes());
            System.out.println("jdk des encrypt:" + DatatypeConverter.printHexBinary(result));

            // 解密
            cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
            result = cipher.doFinal(result);
            System.out.println("jdk des decrypt:" + new String(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
