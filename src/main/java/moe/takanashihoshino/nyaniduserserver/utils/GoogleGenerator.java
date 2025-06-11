package moe.takanashihoshino.nyaniduserserver.utils;

import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Logger;

public class GoogleGenerator {
    public static final String ISSUER = "NyanId";
    public static final int SECRET_SIZE = 32;
    public static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";
    static int window_size = 1;
    static long second_per_size = 30L;

    /**
     * 生成一个SecretKey，外部绑定到用户
     *
     * @return SecretKey
     */
    public static String generateSecretKey() {
        SecureRandom sr;
        try {
            sr = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM);
            sr.setSeed(getSeed());
            byte[] buffer = sr.generateSeed(SECRET_SIZE);
            Base32 codec = new Base32();
            byte[] bEncodedKey = codec.encode(buffer);
            String ret = new String(bEncodedKey);
            return ret.replaceAll("=+$", "");// 移除末尾的等号
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成二维码所需的字符串，注：这个format不可修改，否则会导致身份验证器无法识别二维码
     *
     * @param user   绑定到的用户名
     * @param secret 对应的secretKey
     * @return 二维码字符串
     */
    public static String getQRBarcode(String user, String secret) {
        user = ISSUER + ":" + user;
        String format = "otpauth://totp/%s?secret=%s";
        String ret = String.format(format, user, secret);
        ret += "&issuer=" + ISSUER;
        return ret;
    }

    /**
     * 验证用户提交的code是否匹配
     *
     * @param secret 用户绑定的secretKey
     * @param code   用户输入的code
     * @return 匹配成功与否
     */
    public static boolean checkCode(String secret, int code) {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);
        long timeMsec = System.currentTimeMillis();
        long t = (timeMsec / 1000L) / second_per_size;
        for (int i = -window_size; i <= window_size; ++i) {
            int hash;
            try {
                hash = verifyCode(decodedKey, t + i);
            } catch (Exception e) {
                Logger.getLogger("NyanID").warning(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            if (code == hash) {
                return true;
            }
        }
        return false;
    }

    private static int verifyCode(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);
        int offset = hash[20 - 1] & 0xF;
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;
        return (int) truncatedHash;
    }

    private static byte[] getSeed() {
        String str = ISSUER + System.currentTimeMillis() + ISSUER;
        return str.getBytes(StandardCharsets.UTF_8);
    }

}
