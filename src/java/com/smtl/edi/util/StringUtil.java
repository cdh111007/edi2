package com.smtl.edi.util;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nm
 */
public class StringUtil {

    /**
     *
     * @param vals
     * @return
     */
    public static String buildSqlInClause(String... vals) {
        String r = Arrays.asList(vals).toString().replaceAll("\\[", "").replaceAll("\\]", "");
        StringBuilder sb = new StringBuilder();
        for (String v : r.split("\\,")) {
            sb.append("'").append(v.trim()).append("'").append(",");
        }

        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 是否是数字，包含小数点
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("\\d*(\\.\\d*)?");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     *
     * @param str
     * @param tag
     * @return
     */
    public static String[] str2Arr(String str, String tag) {
        if (isNotEmpty(str)) {
            return str.split(tag);
        }
        return null;
    }

    /**
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        return str.trim().equals("");
    }

    /**
     *
     * @param values
     * @param value
     * @return
     */
    public static boolean contains(String[] values, String value) {
        if (ValidationUtil.isValid(values)) {
            for (String s : values) {
                if (s.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param arr
     * @return
     */
    public static String arr2Str(Object[] arr) {
        String temp = "";
        if (ValidationUtil.isValid(arr)) {
            for (Object s : arr) {
                temp = temp + s + ",";
            }
            return temp.substring(0, temp.length() - 1);
        }
        return temp;
    }

    /**
     *
     * @param str
     * @return
     */
    public static String getShortDesc(String str) {
        if ((str != null) && (str.trim().length() > 30)) {
            return str.substring(0, 30) + "...";
        }
        return str;
    }

    public static String blankIfEmpty(String billNo) {
        if (isEmpty(billNo)) {
            return "";
        }
        return billNo;
    }

    public static String blankIfNull(Object obj) {
        if (null == obj) {
            return "";
        }
        return obj.toString();
    }
}
