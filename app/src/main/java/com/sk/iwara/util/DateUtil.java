package com.sk.iwara.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
}
