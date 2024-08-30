package moe.takanashihoshino.nyaniduserserver.utils;

import java.util.Random;

public class OtherUtils {

    /**
    * @param length 随机数长度
     */
    public static String RandomNumber(int length) {
        String characters = "0123456789";
        StringBuilder flt = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            flt.append(characters.charAt(index));
        }
        return flt.toString();
    }

    public static int RandomIntNumberW() {
        Random random = new Random();
        return random.nextInt(90) + 10;
    }

    /**
     * @param length 字符串长度
     */
    public static String RandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder flt = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            flt.append(characters.charAt(index));
        }
        return flt.toString();
    }
}
