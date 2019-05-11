package org.linux.encrypted_im.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密，CC实现MD5算法结合Base64进行加密
 */
public class MD5Utils {
    public static String getMD5Str(String strValue) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        String newStr = Base64.encodeBase64String(digest.digest(strValue.getBytes()));
        return newStr;
    }
}
