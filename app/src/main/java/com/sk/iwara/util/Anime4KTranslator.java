package com.sk.iwara.util;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 25140 on 2025/10/21 .
 */
public final class Anime4KTranslator {
    private static String replaceTexture2D(String shaderCode) {
        return shaderCode.replaceAll("texture2D\\(([^,]+), ([^)]+)\\)",
                "texture2D($1, $2)");
    }
    private static String replaceMacros(String shaderCode) {
        return shaderCode.replaceAll("#define ([\\w_]+)\\s+([\\w_]+)",
                "#define $1 $2");
    }
    public static String toES2(String mpv) {
        // 1. 去掉描述行
        mpv = mpv.replaceAll("(?m)^\\s*//!.*", "");

        // 2. 替换 texture2D 调用
        mpv = replaceTexture2D(mpv);

        // 3. 替换宏定义
        mpv = replaceMacros(mpv);

        // 4. 替换 HOOKED_pos 和 HOOKED_size
        mpv = mpv.replaceAll("\\bHOOKED_pos\\b", "vTexCoord");
        mpv = mpv.replaceAll("\\bHOOKED_size\\b", "uResolution");

        // 5. 替换 HOOKED_pt
        mpv = mpv.replaceAll("\\bHOOKED_pt\\b", "(1.0 / uResolution)");

        // 6. 替换 HOOKED_texOff
        mpv = mpv.replaceAll("HOOKED_texOff\\(([^)]+)\\)",
                "texture2D(HOOKED, vTexCoord + vec2($1) / uResolution)");

        // 7. 替换 conv2d_tf_texOff
        mpv = mpv.replaceAll("conv2d_tf_texOff\\(([^,]+), ([^)]+)\\)",
                "texture2D($1, vTexCoord + vec2($2) / uResolution)");

        // 8. 添加顶部声明
        StringBuilder es = new StringBuilder();
        es.append("precision highp float;\n");
        es.append("uniform sampler2D HOOKED;\n");
        es.append("uniform vec2 uResolution;\n");
        es.append("varying vec2 vTexCoord;\n");

        if (!mpv.contains("vec4 hook(vec2")) {
            es.append("vec4 hook(vec2 p) { return texture2D(HOOKED, p); }\n");
        }

        if (!mpv.contains("get_luma")) {
            es.append("float get_luma(vec4 c) { return dot(c.rgb, vec3(0.299, 0.587, 0.114)); }\n");
        }

        es.append(mpv);
        es.append("\nvoid main() { gl_FragColor = hook(vTexCoord); }\n");

        return es.toString();
    }
    private static String removeDuplicateFunctions(String src) {
        Pattern p = Pattern.compile(
                "((?:vec4|float|vec2|vec3|void|mat\\d)\\s+\\w+\\s*\\([^)]*\\)\\s*\\{[^{}]*+(?:\\{[^{}]*+\\}[^{}]*+)*+\\})");
        Matcher m = p.matcher(src);
        Set<String> seen = new HashSet<>();
        StringBuffer clean = new StringBuffer();
        while (m.find()) {
            String sig = m.group(1).substring(0, m.group(1).indexOf('{'));
            if (seen.add(sig)) {
                m.appendReplacement(clean, m.group(1));
            } else {
                m.appendReplacement(clean, "");
            }
        }
        m.appendTail(clean);
        return clean.toString();
    }
}