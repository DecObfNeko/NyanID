package moe.takanashihoshino.nyaniduserserver.utils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RsaUtil {
    private static final String RSA_KEY_ALGORITHM = "RSA";
    public static final String RSA_SIGNATURE_ALGORITHM = "SHA1withRSA";
    public static final String RSA2_SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final int KEY_SIZE = 2048;

    /**
     * 生成密钥对
     *
     * @return 返回包含公私钥的map
     */
    public static Map<String, String> generateKey() {
        KeyPairGenerator keygen;
        try {
            keygen = KeyPairGenerator.getInstance(RSA_KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("RSA初始化密钥出现错误,算法异常");
        }
        SecureRandom secrand = new SecureRandom();
        //初始化随机产生器
        secrand.setSeed(OtherUtils.RandomNumber(9).getBytes());
        //初始化密钥生成器
        keygen.initialize(KEY_SIZE, secrand);
        KeyPair keyPair = keygen.genKeyPair();
        //获取公钥并转成base64编码
        byte[] pub_key = keyPair.getPublic().getEncoded();
        String publicKeyStr = Base64.getEncoder().encodeToString(pub_key);
        //获取私钥并转成base64编码
        byte[] pri_key = keyPair.getPrivate().getEncoded();
        String privateKeyStr = Base64.getEncoder().encodeToString(pri_key);
        //创建一个Map返回结果
        Map<String, String> keyPairMap = new HashMap<>();
        keyPairMap.put("publicKeyStr", publicKeyStr);
        keyPairMap.put("privateKeyStr", privateKeyStr);
        return keyPairMap;
    }

    // 清理PEM格式密钥的辅助方法
    private static String cleanKey(String key) {
        return key.replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");
    }

    public static String decrypt(String data, String key) {
        try {
            // 清理密钥并解码
            String cleanedKey = cleanKey(key);
            byte[] k = Base64.getDecoder().decode(cleanedKey);

            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(k);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

            Cipher cipher = Cipher.getInstance(RSA_KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // 修复点：先对data进行Base64解码
            byte[] encryptedData = Base64.getDecoder().decode(data);
            byte[] decrypt = cipher.doFinal(encryptedData);

            return new String(decrypt);
        } catch (Exception e) {
            Logger.getLogger("NyanID").warning("Decrypt error: " + e);
            return "false";
        }
    }

    public static String encrypt(String data, String key) {
        try {
            // 清理密钥并解码
            String cleanedKey = cleanKey(key);
            byte[] k = Base64.getDecoder().decode(cleanedKey);

            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(k);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);

            Cipher cipher = Cipher.getInstance(RSA_KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encrypt = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypt);
        } catch (Exception e) {
            Logger.getLogger("NyanID").warning("Encrypt error: " + e);
            return "false";
        }
    }



    /**
     * RSA签名
     *
     * @param data     待签名数据
     * @param priKey   私钥
     * @param signType RSA或RSA2
     * @return 签名
     * @throws Exception
     */
    public static String sign(byte[] data, String priKey, String signType) throws Exception {
        byte[] pkey = Base64.getDecoder().decode(cleanKey(priKey));
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(pkey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM); // 仍用RSA生成密钥

        // 直接使用传入的 signType 作为签名算法
        Signature signature = Signature.getInstance("SHA1withRSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        signature.initSign(privateKey);
        signature.update(data);
        byte[] sign = signature.sign();
        return Base64.getEncoder().encodeToString(sign);
    }

    /**
     * RSA校验数字签名
     *
     * @param data     待校验数据
     * @param sign     数字签名
     * @param pubKey   公钥
     * @param signType RSA或RSA2
     * @return boolean 校验成功返回true，失败返回false
     */
    public static boolean verify(byte[] data, byte[] sign, byte[] pubKey, String signType) throws Exception {
        //返回转换指定算法的KeyFactory对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
        //创建X509编码密钥规范
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
        //根据X509编码密钥规范产生公钥对象
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        //标准签名算法名称(RSA还是RSA2)
        String algorithm = RSA_KEY_ALGORITHM.equals(signType) ? RSA_SIGNATURE_ALGORITHM : RSA2_SIGNATURE_ALGORITHM;
        //用指定算法产生签名对象Signature
        Signature signature = Signature.getInstance("SHA1withRSA");
        //用公钥初始化签名对象,用于验证签名
        signature.initVerify(publicKey);
        //更新签名内容
        signature.update(data);
        //得到验证结果
        return signature.verify(sign);
    }
}
