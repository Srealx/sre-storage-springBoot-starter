package com.sre.storage.utils;

/**
 * @Author chengang
 * @Date 2025/7/21
 */
public class StringUtil {

    /**
     * 使用正则表达式匹配末尾的数字
     * @param str str
     * @return str
     */
    public static String getTrailingNumbers(String str) {

        if (str != null && !str.isEmpty()) {
            String regex = "\\d+$";
            if (str.matches(".*" + regex)) {
                return str.replaceAll(".*" + regex, "").trim();
            }
        }
        return "";
    }
}
