package com.luban.xbongbong.api.helper.utils;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 签名工具类
 *
 * @author xbb
 */
@Slf4j
@UtilityClass
public class XbbDigestUtil {

    /**
     * Parameter strSrc is a string will be encrypted,
     * parameter encName is the algorithm name will be used.
     * encName default to “MD5.”
     */
    public static String encrypt(String strSrc, String encName) {
        MessageDigest md;
        String strDes;
        try {
            byte[] bt = strSrc.getBytes(StandardCharsets.UTF_8);
            if (StrUtil.isEmpty(encName)) {
                encName = "MD5";
            }
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error("No such algorithm.", e);
            return null;
        }
        return strDes;
    }

    private static String bytes2Hex(byte[] bts) {
        StringBuilder des = new StringBuilder();
        String tmp;
        for (byte bt : bts) {
            tmp = (Integer.toHexString(bt & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }
}
