package com.cgbsoft.lib.utils.tools;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class RSAUtils {

//    private static Logger logger = LoggerFactory.getLogger(RSAUtils.class);

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";
//	private static final String PUBLIC_KEY = "qmx123";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";
//	private static final String PRIVATE_KEY = "qmx234";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static String INPUT_CHARSET = "utf-8";

    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map<String, Object> keyMap = new HashMap<String, Object>(2);
            keyMap.put(PUBLIC_KEY, publicKey);
            keyMap.put(PRIVATE_KEY, privateKey);
            return keyMap;
        } catch (NoSuchAlgorithmException e) {
//            logger.error("生成密钥对失败！");
        }
        return null;
    }

    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param content    已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
//    public static String sign(String content, String privateKey) {
//        try {
//            byte[] keyBytes = Base64Utils.decode(privateKey);
//            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
//            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
//            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
//            signature.initSign(privateK);
//            signature.update(content.getBytes(INPUT_CHARSET));
//            return Base64Utils.encode(signature.sign());
//        } catch (Exception e) {
//            logger.error("用私钥对信息生成数字签名失败！");
//        }
//        return null;
//    }

    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param content   已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
//    public static boolean verify(String content, String publicKey, String sign) {
//        try {
//            byte[] keyBytes = Base64Utils.decode(publicKey);
//            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
//            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//            PublicKey publicK = keyFactory.generatePublic(keySpec);
//            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
//            signature.initVerify(publicK);
//            signature.update(content.getBytes(INPUT_CHARSET));
//            return signature.verify(Base64Utils.decode(sign));
//        } catch (Exception e) {
//            logger.error("校验数字签名失败！");
//        }
//        return false;
//    }

    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) {
        try {
            byte[] keyBytes = Base64Utils.decode(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateK);
            int inputLen = encryptedData.length;

            ByteArrayOutputStream out = null;
            try {
                out = new ByteArrayOutputStream();
                int offSet = 0;
                byte[] cache;
                int i = 0;
                // 对数据分段解密
                while (inputLen - offSet > 0) {
                    if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                        cache = cipher
                                .doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                    } else {
                        cache = cipher
                                .doFinal(encryptedData, offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * MAX_DECRYPT_BLOCK;
                }

                byte[] decryptedData = out.toByteArray();
                return decryptedData;
            } catch (Exception ex) {
//                logger.error("私钥解密失败:" + ex.getMessage());
            } finally {
                if (out != null) {
                    out.close();
                    out = null;
                }
            }
        } catch (Exception e) {
//            logger.error("私钥解密失败！");
        }
        return null;
    }

    public static byte[] decryptByPrivateKey(String encryptedDataString, String privateKey) {
        byte[] encryptedData = new byte[0];
        try {
            encryptedData = Base64Utils.decode(encryptedDataString);
        } catch (Exception e) {
//            logger.error("私钥解密失败:" + e.getMessage());
        }
        return decryptByPrivateKey(encryptedData, privateKey);
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param source    已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
//    public static String decryptByPublicKey(String source, String publicKey) {
//        try {
//            byte[] keyBytes = Base64Utils.decode(publicKey);
//            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
//            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//            Key publicK = keyFactory.generatePublic(x509KeySpec);
//            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//            cipher.init(Cipher.DECRYPT_MODE, publicK);
//            byte[] encryptedData = source.getBytes();
//            int inputLen = encryptedData.length;
//
//            ByteArrayOutputStream out = null;
//            try {
//                out = new ByteArrayOutputStream();
//                int offSet = 0;
//                byte[] cache;
//                int i = 0;
//                // 对数据分段解密
//                while (inputLen - offSet > 0) {
//                    if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
//                        cache = cipher
//                                .doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
//                    } else {
//                        cache = cipher
//                                .doFinal(encryptedData, offSet, inputLen - offSet);
//                    }
//                    out.write(cache, 0, cache.length);
//                    i++;
//                    offSet = i * MAX_DECRYPT_BLOCK;
//                }
//                byte[] decryptedData = out.toByteArray();
//                out.close();
//                return Base64Utils.encode(decryptedData);
//            } catch (Exception ex) {
//                logger.error("公钥解密失败:" + ex.getMessage());
//            } finally {
//                if (out != null) {
//                    out.close();
//                    out = null;
//                }
//            }
//        } catch (Exception e) {
//            logger.error("公钥解密失败:"+e.getMessage());
//        }
//        return null;
//    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) {
        try {
            byte[] keyBytes = Base64Utils.decode(publicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicK);
            int inputLen = data.length;
            ByteArrayOutputStream out = null;
            try {
                out = new ByteArrayOutputStream();
                int offSet = 0;
                byte[] cache;
                int i = 0;
                // 对数据分段加密
                while (inputLen - offSet > 0) {
                    if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                        cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                    } else {
                        cache = cipher.doFinal(data, offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * MAX_ENCRYPT_BLOCK;
                }
                byte[] encryptedData = out.toByteArray();
                out.close();
                return encryptedData;
            } catch (Exception ex) {
//                logger.error("公钥加密失败:" + ex.getMessage());
            } finally {
                if (out != null) {
                    out.close();
                    out = null;
                }
            }
        } catch (Exception e) {
//            logger.error("公钥加密失败：" + e.getMessage());
        }
        return null;
    }

    public static String encryptByPublicKey(String source, String publicKey)
            throws Exception {
        byte[] data = source.getBytes();
        byte[] data1 = encryptByPublicKey(data, publicKey);
        return Base64Utils.encode(data1);
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param source     源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
//    public static String encryptByPrivateKey(String source, String privateKey) {
//        try {
//            byte[] keyBytes = Base64Utils.decode(privateKey);
//            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
//            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
//            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//            cipher.init(Cipher.ENCRYPT_MODE, privateK);
//            byte[] data = source.getBytes(INPUT_CHARSET);
//            int inputLen = data.length;
//
//            ByteArrayOutputStream out = null;
//            try {
//                out = new ByteArrayOutputStream();
//                int offSet = 0;
//                byte[] cache;
//                int i = 0;
//                // 对数据分段加密
//                while (inputLen - offSet > 0) {
//                    if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
//                        cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
//                    } else {
//                        cache = cipher.doFinal(data, offSet, inputLen - offSet);
//                    }
//                    out.write(cache, 0, cache.length);
//                    i++;
//                    offSet = i * MAX_ENCRYPT_BLOCK;
//                }
//                byte[] encryptedData = out.toByteArray();
//                out.close();
//                return Base64Utils.encode(encryptedData);
//            } catch (Exception ex) {
//                logger.error("私钥解密失败:" + ex.getMessage());
//            } finally {
//                if (out != null) {
//                    out.close();
//                    out = null;
//                }
//            }
//        } catch (Exception e) {
//            logger.error("私钥加密失败！");
//        }
//        return null;
//    }

    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        try {
            return Base64Utils.encode(key.getEncoded());
        } catch (Exception e) {
//            logger.error("获取私钥失败");
        }
        return null;
//		return "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKnykPECGMY8zHFThUJajWJ/h2PPKrEzp1JwaFvmr7/Cop9j/dK88ftcqG0XVD/SRGMIze0UWl0YnKpeXQGXfijdGUagJ4IZtTFVGPYhe+yvJo54XXKLy9bsO067ZU949L4e/r1JQINolhF1LPX5uFprWW0Whgi/JOOGv5Xy6aM1AgMBAAECgYBg9z7N1GVwTmZTztS83E/JQHxubVitjIxOlEZnEUN7xUDmcrXzVM04n1CWFfaDB6TvYKmmOLOqZI2XA4pLizV2iV3YaJUzv7M0kioZ/0+QG5NwGhaF4wCNOPK9MSmNcSX5qLm0PbOixDc/+E/YY8N9XwfmWgi/CxQs58vBK7Fo7QJBAO0ZafOd841e8YXT9pxZ6jIUAocgKWds9U7SJbxCN4VHHX+cvAUsLS/L9hWfZx0ejBcWrIFuW7hka2mrJfYnP28CQQC3fsIEzt2RVb9klmC4NyJdEA6M8fNF/wTuVwmLvUCPCUWhvEW34//Z5qlBxfPHA/uXuKsq/UiC+0O0xD+FH/WbAkEAlXCuOjG1H8bW3i4CQvvdQ+Ee0sJvtlOTrjGAPU9TJTr0mclVLMFyXazlly1YVZ86VxcgdZf0UZ1hokGQdLy6GwJAQ6OoJVmT9yTinlOIZ597PU7T7kSp5l1xFeJjlG04xQEn98yM7pJPF6WdMq+jgvMG5RCfmAMxnYa9mH7W4126jQJAOoXPUTZoGP1LYtIEUrNLIM5GsngmktrgjVj2HbzYhdvAVk8Zcbu0BoexC8Gg7Ka17sWWuCKqxGoKqLaV56X0Jw==";
    }

    /**
     * <p>
     * 获取公钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        try {
            return Base64Utils.encode(key.getEncoded());
        } catch (Exception e) {
//            logger.error("获取公钥失败");
        }
        return null;
//		return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCp8pDxAhjGPMxxU4VCWo1if4djzyqxM6dScGhb5q+/wqKfY/3SvPH7XKhtF1Q/0kRjCM3tFFpdGJyqXl0Bl34o3RlGoCeCGbUxVRj2IXvsryaOeF1yi8vW7DtOu2VPePS+Hv69SUCDaJYRdSz1+bhaa1ltFoYIvyTjhr+V8umjNQIDAQAB";
    }

    /**
     * 将一个数组以某一元素分界，将这个元素之前的部分与之后的部分互换位置
     * 主要思路:  将之前的部分与之后的部分分别逆序，再将整体逆序即可
     *
     * @param str 要进行操作的字符串
     * @param i   作为分界线的元素的下标
     * @return 转化之后生成的字符串
     */
    public static String replaceBeginAndEnd(String str, int i) {
        if (str == null || i < 0 || str.length() <= i) {
            return str;
        }
        char[] chars = str.toCharArray();
        reverseCharArray(chars, 0, i - 1);
        reverseCharArray(chars, i + 1, chars.length - 1);
        reverseCharArray(chars, 0, chars.length - 1);
        return new String(chars);
    }

    /**
     * 用来翻转一个数组的某一部分
     *
     * @param charArray 要进行操作的数组
     * @param begin     要翻转的部分第一个元素的下标
     * @param end       要翻转的部分最后一个元素的下标
     */
    public static void reverseCharArray(char[] charArray, int begin, int end) {
        char tmp;
        while (begin < end) {
            tmp = charArray[begin];
            charArray[begin] = charArray[end];
            charArray[end] = tmp;
            begin++;
            end--;
        }
    }

//    public static String subString(String token) {
//        if (!BStrUtils.isEmpty(token) && token.length() > 3) {
//            String fir = token.substring(0, 1);
//            String last = token.substring(1, 2);
//            token = token.substring(2, token.length());
//            String result = last + fir + token;
//            return result;
//        } else {
////            throw new ApplicationException("token格式不正确");
//        }
//    }

}
