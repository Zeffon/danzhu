package com.zeffon.danzhu.util;

import com.zeffon.danzhu.bo.PageCounter;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Create by Zeffon on 2020/10/1
 */
public class CommonUtil {

    public static PageCounter convertToPageParameter(Integer start, Integer count) {
        int pageNum = start / count;

        PageCounter pageCounter = PageCounter.builder()
                .page(pageNum)
                .count(count)
                .build();
        return pageCounter;
    }

    public static Calendar addSomeDays(Calendar calendar, int days) {
        calendar.add(Calendar.DATE, days);
        return calendar;
    }

    public static Calendar addSomeSeconds(Calendar calendar, int seconds) {
        calendar.add(Calendar.SECOND, seconds);
        return calendar;
    }

    public static Calendar dataToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static int differentDaysByMillisecond(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }

    public static Boolean isInTimeLine(Date date, Date start, Date end) {
        Long time = date.getTime();
        Long startTime = start.getTime();
        Long endTime = end.getTime();
        if (time > startTime && time < endTime) {
            return true;
        }
        return false;
    }

    public static Boolean isOutOfDate(Date startTime, Long period) {
        Long now = Calendar.getInstance().getTimeInMillis();
        Long startTimeStamp = startTime.getTime();
        Long periodMillSecond = period * 1000;
        if (now > (startTimeStamp + periodMillSecond)) {
            return true;
        }
        return false;
    }

    public static Boolean isOutOfDate(Date expiredTime) {
        Long now = Calendar.getInstance().getTimeInMillis();
        Long expiredTimeStamp = expiredTime.getTime();
        if (now > expiredTimeStamp) {
            return true;
        }
        return false;
    }

    public static String yuanToFenPlainString(BigDecimal p) {
        p = p.multiply(new BigDecimal("100"));
        return CommonUtil.toPlain(p);
    }

    private static String toPlain(BigDecimal p) {
        return p.stripTrailingZeros().toPlainString();
    }

    public static String timestamp10() {
        Long timestamp13 = Calendar.getInstance().getTimeInMillis();
        String timestamp13Str = timestamp13.toString();
        return timestamp13Str.substring(0, timestamp13Str.length() - 3);
    }
}
