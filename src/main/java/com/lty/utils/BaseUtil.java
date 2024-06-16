package com.lty.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lty
 */
public class BaseUtil {
    /**
     * 获取当前时间("yyyy-MM-dd HH:mm:ss")
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    public static String getFileSize(Long size) {
        final int num = 1024;
        if (size == null) {
            return "0 KB";
        }
        if (size < num) {
            return String.valueOf(size) + " Byte";
        }
        if (size < Math.pow(num, 2)) {
            return String.valueOf(size / num) + " KB";
        }
        if (size < Math.pow(num, 3)) {
            return String.valueOf(size / Math.pow(num, 2)) + " MB";
        }
        if (size < Math.pow(num, 4)) {
            return String.valueOf(size / Math.pow(num, 3)) + " GB";
        }
        return String.valueOf(size / Math.pow(num, 4)) + " TB";
    }

    /**
     * 点转斜线 (com.lty.code变为com/lty/code)
     * @param str
     * @return String
     */
    public static String dotToLine(String str) {
        return str.replace(".", "/");
    }


    public static String getSaveUrl(String fileUrl) {
        // 定义正则表达式模式，直到匹配到/file/
        String pattern = ".*/file/(.*)$";
        Pattern r = Pattern.compile(pattern);

        // 创建matcher对象
        Matcher m = r.matcher(fileUrl);
        if (m.find()) {
            // 提取匹配到的路径部分
            return m.group(1);
        } else {
            return "No match found";
        }
    }

}
