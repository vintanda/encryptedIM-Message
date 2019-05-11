package org.linux.encrypted_im.encryptedUtils;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * @Created By LZD on 2019/03/25
 * Base64算法的三种实现：
 *   - jdk
 *   - CommonsCodes(cc)
 *   - BouncyCastle(bc)
 * 由于Base64算法的算法和参照表都是公开的，所以很容易被破解，不安全，仅供入门玩一下啦~~
 */
public class Base64Util {
    private static String src = "this is a test for Base64...试试中文~";

    public static void main(String[] args) {
        jdkBase64();
        System.out.println("==================================================");
        commonsCodesBase64();
        System.out.println("==================================================");
        bouncyCastleBase64();
    }

    // jdk方式实现Base64
    private static void jdkBase64() {
        try {
            // 编码处理
            BASE64Encoder encoder = new BASE64Encoder();
            String encode = encoder.encode(src.getBytes());
            System.out.println("encode: " + encode);

            // 解码处理
            BASE64Decoder decoder = new BASE64Decoder();
            System.out.println("decoder: " + new String(decoder.decodeBuffer(encode)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // commonsCodes(cc)方式实现Base64
    private static void commonsCodesBase64() {
        byte[] encoderBytes = Base64.encodeBase64(src.getBytes());
        System.out.println("encoder: " + new String(encoderBytes));

        byte[] decoderBytes = Base64.decodeBase64(encoderBytes);
        System.out.println("decoder: " + new String(decoderBytes));
    }

    // Bouncy Castle(bc)方式实现Base64
    private static void bouncyCastleBase64() {
        byte[] encoderBytes = org.bouncycastle.util.encoders.Base64.encode(src.getBytes());
        System.out.println("encoder: " + new String(encoderBytes));

        byte[] decoderBytes = org.bouncycastle.util.encoders.Base64.decode(encoderBytes);
        System.out.println("decoder: " + new String(decoderBytes));
    }
}
