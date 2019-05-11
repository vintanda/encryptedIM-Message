package org.linux.encrypted_im.encryptedUtils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class MDUtil {
    private static String src = "java security Message Digest Algorithm...中文中文~";

    public static void main(String[] args) {
        jdkMD2Test();
        System.out.println("========================================");
        ccMD2Test();
        System.out.println("========================================");
        bcMD4Test();
        System.out.println("========================================");
        bcMD5Test();
        System.out.println("========================================");
        jdkMD5Test();
        System.out.println("========================================");
        ccMD5Test();
    }

    // jdk提供MD5的实现
    private static void jdkMD5Test() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = messageDigest.digest(src.getBytes());

            // 借助第三方工具类将十六进制转成字符串
            System.out.println("JDK MD5：" + Hex.encodeHexString(md5Bytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // jdk提供MD2的实现 -- 与MD5基本上相同，只是使用算法不同
    private static void jdkMD2Test() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD2");
            byte[] md5Bytes = messageDigest.digest(src.getBytes());

            // 借助第三方工具类将十六进制转成字符串
            System.out.println("JDK MD2：" + Hex.encodeHexString(md5Bytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // bc提供MD4的实现
    private static void bcMD4Test() {
        try {
            // 为jdk添加一个BouncyCastle的Provider，从而使用第三方的加密实现
            Security.addProvider(new BouncyCastleProvider());
            MessageDigest messageDigest = MessageDigest.getInstance("MD4");
            byte[] md4Bytes = messageDigest.digest(src.getBytes());
            System.out.println("BC Provider MD4: "
                    + org.bouncycastle.util.encoders.Hex.toHexString(md4Bytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    // bc提供MD5的实现
    private static void bcMD5Test() {
        Digest digest = new MD5Digest();

        // param0:明文 param1:起始位置 param2:长度
        digest.update(src.getBytes(), 0, src.getBytes().length);
        byte[] md4Bytes = new byte[digest.getDigestSize()];

        // param0:进行摘要后的输出的内容 param1:起始位置
        digest.doFinal(md4Bytes, 0);
        System.out.println("BC MD5：" + org.bouncycastle.util.encoders.Hex.toHexString(md4Bytes));
    }

    // cc提供的实现都是对jdk进行了简化！！又神奇又无语！看源码就可以看到熟悉的MessageDigest
    // cc提供MD5的实现
    private static void ccMD5Test() {
        System.out.println("cc MD5: " + DigestUtils.md5Hex(src.getBytes()));
    }

    // cc提供MD2的实现
    private static void ccMD2Test() {
        System.out.println("cc MD2: " + DigestUtils.md2Hex(src.getBytes()));
    }
}
