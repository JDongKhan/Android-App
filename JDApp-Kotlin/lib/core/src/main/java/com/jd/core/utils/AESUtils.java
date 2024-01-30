package com.jd.core.utils;

import android.text.TextUtils;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加解密工具类
 * 名称：    AESUtil
 *
 * @author 17093029
 * 创建日期：    2018/7/23
 * 包名： com.cnsuning.apos.library.utils
 */
public class AESUtils {

  private final static String HEX = "0123456789ABCDEF";

  /**
   * 加解密算法
   */
  private static final String ALGORITHM = "AES";

  private static final String CIPHER = "AES/CBC/PKCS5Padding";

  private static final String IVPARAMETERSPEC = "0102030405060708";
  private static final String AES_KEY = "h6710f778421dffc";
  private static Base64Utils base64Codec = new Base64Utils();

  private AESUtils() {
    throw new UnsupportedOperationException("Can not be created！");
  }

  /**
   * 加密 默认KEY
   *
   * @param src
   * @return
   */
  public static String encrypt(String src) {
    return encrypt(src, AES_KEY, false);
  }

  /**
   * 加密
   *
   * @param src
   * @return
   */
  public static String encrypt(String src, String key, boolean keyBase64) {
    byte[] raw = null;
    try {
      if (keyBase64) {
        raw = base64Codec.decode(new String(key.getBytes("ASCII")));
      } else {
        raw = key.getBytes();
      }
    } catch (Exception e) {
      Log.w("Sonar Catch Exception", e);
      e.printStackTrace();
    }

    SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
    Cipher cipher;
    try {
      cipher = Cipher.getInstance(CIPHER);
      IvParameterSpec iv = new IvParameterSpec(IVPARAMETERSPEC.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
      cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
      byte[] encrypted;
      encrypted = cipher.doFinal(src.getBytes("UTF-8"));
      return Base64Utils.encode(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    } catch (Exception e) {
      Log.w("Sonar Catch Exception", e);
    }
    return null;
  }

  /**
   * 解密 默认密码
   *
   * @param src
   * @return
   */
  public static String decrypt(String src) {
    return decrypt(src, AES_KEY, false);
  }

  /**
   * 解密
   *
   * @param src
   * @return
   */
  public static String decrypt(String src, String key, boolean keyBase64) {
    if (!TextUtils.isEmpty(src)) {
      try {
        byte[] raw = null;
        if (keyBase64) {
          raw = base64Codec.decode(new String(key.getBytes("ASCII")));
        } else {
          raw = key.getBytes("ASCII");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER);
        IvParameterSpec iv = new IvParameterSpec(IVPARAMETERSPEC.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] encrypted1 = base64Codec.decode(src);// 先用base64解密
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original, "UTF-8");
        return originalString;
      } catch (Exception ex) {
        Log.w("Sonar Catch Exception", ex);
        return null;
      }
    }
    return "";
  }

}
