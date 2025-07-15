package com.example.utils;

public class SearchFormatUtils {
    // 判断是否为纯数字（员工ID）
    public static boolean isEmployeeId(String text) {
        return text.matches("^\\d+$");
    }

    // 判断是否为年月份（YYYY-MM）
    public static boolean isMonth(String text) {
        return text.matches("^\\d{4}-\\d{2}$");
    }
    public static boolean istext(String text) {
        // 处理null或空字符串情况
        if (text == null || text.length() == 0) {
            return false;
        }

        // 遍历每个字符检查是否为数字
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // 检查是否是0-9的数字
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
