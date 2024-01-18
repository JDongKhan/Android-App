package com.jd.core.utils;

/**
 * Base64 加解密工具类
 */
public class Base64Utils {

  /**
   * UNENCODED_BLOCK
   */
  private static final int BYTES_PER_UNENCODED_BLOCK = 3;
  /**
   * ENCODED_BLOCK
   */
  private static final int BYTES_PER_ENCODED_BLOCK = 4;

  /**
   * Mask used to extract 6 bits, used when encoding
   */
  private static final int SIX_BIT_MASK = 0x3f;

  /**
   * padding char
   */
  private static final byte PAD = '=';

  /**
   * This array is a lookup table that translates 6-bit positive integer index values into their "Base64 Alphabet"
   * equivalents as specified in Table 1 of RFC 2045.
   */
  private static final byte[] EncodeTable = {'A', 'B', 'C', 'D', 'E', 'F',
    'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
    'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
    'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
    't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
    '6', '7', '8', '9', '+', '/'};

  private static final int[] DecodeTable = new int[128];

  static {
    for (int i = 0; i < EncodeTable.length; i++) {
      DecodeTable[EncodeTable[i]] = i;
    }
  }

  /**
   * 解码字符串
   *
   * @param data
   * @return
   */
  public static String decodeData(String data, String charSet) {
    if (StringUtils.isEmptyWithBlank(data)) {
      return data;
    }
    try {
      Base64Utils base64Util = new Base64Utils();
      byte[] bytes = base64Util.decode(data);
      if (StringUtils.isEmptyWithBlank(charSet)) {
        charSet = "utf-8";
      }
      return new String(bytes, charSet);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * 加密对象
   *
   * @param object
   * @return
   */
  public static String encodeObj(Object object) {

    if (null != object) {
      String jsonString = JsonUtils.toJson(object);
      return Base64Utils.encode(jsonString.getBytes());
    } else {
      return null;
    }

  }

  /**
   * Translates the specified byte array into Base64 string.
   *
   * @param in the byte array (not null)
   * @return the translated Base64 string (not null)
   */
  public static String encode(byte[] in) {

    int modulus = 0;
    int bitWorkArea = 0;
    int numEncodedBytes = (in.length / BYTES_PER_UNENCODED_BLOCK) * BYTES_PER_ENCODED_BLOCK
      + ((in.length % BYTES_PER_UNENCODED_BLOCK == 0) ? 0 : 4);

    byte[] buffer = new byte[numEncodedBytes];
    int pos = 0;

    for (int b : in) {
      modulus = (modulus + 1) % BYTES_PER_UNENCODED_BLOCK;

      if (b < 0) {
        b += 256;
      }

      bitWorkArea = (bitWorkArea << 8) + b; //  BITS_PER_BYTE
      if (0 == modulus) { // 3 bytes = 24 bits = 4 * 6 bits to extract
        buffer[pos++] = EncodeTable[(bitWorkArea >> 18) & SIX_BIT_MASK];
        buffer[pos++] = EncodeTable[(bitWorkArea >> 12) & SIX_BIT_MASK];
        buffer[pos++] = EncodeTable[(bitWorkArea >> 6) & SIX_BIT_MASK];
        buffer[pos++] = EncodeTable[bitWorkArea & SIX_BIT_MASK];
      }
    }

    switch (modulus) { // 0-2
      case 1: // 8 bits = 6 + 2
        buffer[pos++] = EncodeTable[(bitWorkArea >> 2) & SIX_BIT_MASK]; // top 6 bits
        buffer[pos++] = EncodeTable[(bitWorkArea << 4) & SIX_BIT_MASK]; // remaining 2
        buffer[pos++] = PAD;
        buffer[pos] = PAD; // Last entry no need to ++
        break;

      case 2: // 16 bits = 6 + 6 + 4
        buffer[pos++] = EncodeTable[(bitWorkArea >> 10) & SIX_BIT_MASK];
        buffer[pos++] = EncodeTable[(bitWorkArea >> 4) & SIX_BIT_MASK];
        buffer[pos++] = EncodeTable[(bitWorkArea << 2) & SIX_BIT_MASK];
        buffer[pos] = PAD; // Last entry no need to ++
        break;
      default:
        break;
    }

    return new String(buffer);
  }

  /**
   * Translates the specified Base64 string into a byte array.
   *
   * @param s the Base64 string (not null)
   * @return the byte array (not null)
   */
  public byte[] decode(String s) {
    int delta = s.endsWith("==") ? 2 : s.endsWith("=") ? 1 : 0;
    byte[] buffer = new byte[s.length() * BYTES_PER_UNENCODED_BLOCK / BYTES_PER_ENCODED_BLOCK - delta];
    int mask = 0xFF;
    int pos = 0;
    for (int i = 0; i < s.length(); i += BYTES_PER_ENCODED_BLOCK) {
      int c0 = DecodeTable[s.charAt(i)];
      int c1 = DecodeTable[s.charAt(i + 1)];
      buffer[pos++] = (byte) (((c0 << 2) | (c1 >> 4)) & mask);
      if (pos >= buffer.length) {
        return buffer;
      }
      int c2 = DecodeTable[s.charAt(i + 2)];
      buffer[pos++] = (byte) (((c1 << 4) | (c2 >> 2)) & mask);
      if (pos >= buffer.length) {
        return buffer;
      }
      int c3 = DecodeTable[s.charAt(i + 3)];
      buffer[pos++] = (byte) (((c2 << 6) | c3) & mask);
    }
    return buffer;
  }


}