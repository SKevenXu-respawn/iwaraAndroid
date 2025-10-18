package com.sk.iwara.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {
    public static String FormatDate(String iso){
        LocalDateTime utc = LocalDateTime.parse(iso, DateTimeFormatter.ISO_DATE_TIME);
        // 如需东八区可自行加 8 小时： utc = utc.plusHours(8);
        // 格式化
        try{
            return utc.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分"));
        }catch (Exception e){
            return iso;
        }
    }
    public static String formatAgo(String iso8601) {
        try{
            Instant instant = Instant.parse(iso8601);
            ZonedDateTime time = instant.atZone(ZoneId.systemDefault());
            ZonedDateTime now = ZonedDateTime.now();

            long days    = ChronoUnit.DAYS.between(time, now);
            long hours   = ChronoUnit.HOURS.between(time, now);
            long minutes = ChronoUnit.MINUTES.between(time, now);

            StringBuilder sb = new StringBuilder();
            if (days > 0) {
                sb.append(days).append("天");
            } else if (hours > 0) {
                sb.append(hours).append("小时");
            } else {
                sb.append(Math.max(1, minutes)).append("分钟"); // 不足1分钟按1分钟算
            }
            return sb.append("前").toString();
        } catch (Exception e) {
            return iso8601;
        }

    }
}
