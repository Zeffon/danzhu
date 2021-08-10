package com.zeffon.danzhu.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Random;

/**
 * Create by Zeffon on 2020/10/2
 */
@Component
public class CodeUtil {

    private static String[] yearCodes;

    @Value("${danzhu.year-codes}")
    public void setYearCodes(String yearCodes) {
        String[] chars = yearCodes.split(",");
        CodeUtil.yearCodes = chars;
    }

    public static String markUserCode() {
        return makeCode("U");
    }

    public static String markLinkCode() {
        return makeCode("L");
    }

    public static String markGroupCode() {
        return makeCode("G");
    }

    public static String markCollectCode() {
        return makeCode("C");
    }

    private static String makeCode(String type) {
        StringBuffer joiner = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        String dayStr = day < 10 ? ("0"+day) : day + "";
        String mills = String.valueOf(calendar.getTimeInMillis());
        String micro = LocalDateTime.now().toString();
        String random = String.valueOf(Math.random()*1000).substring(0,2);
        joiner.append(type)
                .append(CodeUtil.yearCodes[calendar.get(Calendar.YEAR) - 2020])
                .append(Integer.toHexString(calendar.get(Calendar.MONTH)+1).toUpperCase())
                .append(dayStr)
                .append(micro.substring(micro.length()-3, micro.length()))
                .append(random);
        return joiner.toString();
    }

    public static String getRandomString(int length){
        String str = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++){
            int number = random.nextInt(36);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
