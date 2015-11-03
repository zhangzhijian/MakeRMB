package com.dream.makermb.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {


    public static String getChronoFormat(long target) {
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;
        long residue = target - System.currentTimeMillis();

        long oneMinute = 1000 * 60;
        long oneHour = oneMinute * 60;
        long oneDay = oneHour * 24;

        if (residue >= oneDay) {
            days = residue / oneDay;
            residue -= days * oneDay;
        }
        if (residue >= oneHour) {
            hours = residue / oneHour;
            residue -= hours * oneHour;
        }
        if (residue >= oneMinute) {
            minutes = residue / oneMinute;
            residue -= minutes * oneMinute;
        }
        seconds = residue / 1000;
        String format = "";
        if (days > 0) {
            format = "%1$02d天%2$02d时%3$02d分%4$02d秒";
        } else if (hours > 0) {
            format = "还剩%2$02d时%3$02d分%4$02d秒";
        } else if (minutes > 0) {
            format = "%3$02d分%4$02d秒";
        } else if (seconds > 0) {
            format = "%4$02d秒";
        }
        return format;
    }

    public static String getTimeStr(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return sdf.format(calendar.getTime());
    }

    public static String getDateStr(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return sdf.format(calendar.getTime());
    }

    public static String formatDateTime(long time, SimpleDateFormat dateFormat) {
        if (0 == time || null == dateFormat) {
            return "";
        }
        return dateFormat.format(new Date(time));
    }

}
