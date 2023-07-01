package com.se.softengineer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 **/
public class TimeUtil {

    public static Date getTime(){
        return new Date();
    }

    /**
     * 获取当前时间与指定时间的间隔（分钟转化为天）
     * @param time 指定的时间（就是数据表中读入的时间）
     * @return
     */
    static public long timeDifference(String time){
        String pattern = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date lastDate;
        try {
            System.out.println(time);
            lastDate = sdf.parse(time);
            LocalDateTime lastDateTime = LocalDateTime.ofInstant(lastDate.toInstant(), ZoneId.systemDefault());
//            LocalDateTime dateTime2 = LocalDateTime.ofInstant(date2.toInstant(), ZoneId.systemDefault());

            LocalDateTime nowDateTime = LocalDateTime.now();
            Duration duration = Duration.between(lastDateTime, nowDateTime);

//            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
//            long seconds = duration.toSecondsPart();

//            System.out.println("Hours difference: " + hours);
            System.out.println("Minutes difference: " + minutes);
//            System.out.println("Seconds difference: " + seconds);
            return minutes/(60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
