package com.sk.iwara.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.*;

public final class MpvToEs2Translator {

    public static String toEs2(String mpv){
        // ① 去描述行
        mpv = mpv.replaceAll("(?m)^\\s*//!.*","");
        // ② 宏替换
        mpv = mpv.replaceAll("\\b(\\w+)_tex\\(([^)]+)\\)","texture2D($1,$2)");
        mpv = mpv.replaceAll("\\b(\\w+)_texOff\\(([^)]+)\\)",
                "texture2D($1,vTexCoord+$2/uResolution)");
        mpv = mpv.replaceAll("\\b(\\w+)_pos\\b","vTexCoord");
        mpv = mpv.replaceAll("\\b(\\w+)_size\\b","uResolution");
        mpv = mpv.replaceAll("\\bHOOKED_pt\\b","(1.0/uResolution)");
        // ③ 去重函数（同签名只留第一次）
        mpv = removeDuplicateFunctions(mpv);
        // ④ 补头 + main
        return  "precision highp float;\n" +
                "uniform sampler2D HOOKED;\n" +
                "uniform vec2 uResolution;\n" +
                "varying vec2 vTexCoord;\n" +
                mpv +
                "\nvoid main(){gl_FragColor=hook();}\n";
    }

    /** 删除重复函数体（同签名只留第一个） */
    private static String removeDuplicateFunctions(String src) {
        // 匹配 vec4 name(...){...} 或 float name(...){...}
        Pattern p = Pattern.compile("((?:vec4|float|vec2|vec3|mat\\d)\\s+\\w+\\s*\\([^)]*\\)\\s*\\{[^{}]*+(?:\\{[^{}]*+\\}[^{}]*+)*+\\})");
        Matcher m = p.matcher(src);
        Set<String> seen = new HashSet<>();
        StringBuffer clean = new StringBuffer();
        while (m.find()) {
            String sig = m.group(1).substring(0, m.group(1).indexOf('{')); // 只取签名
            if (seen.add(sig)) {          // 第一次出现
                m.appendReplacement(clean, m.group(1));
            } else {                      // 重复
                m.appendReplacement(clean, "");
            }
        }
        m.appendTail(clean);
        return clean.toString();
    }
}