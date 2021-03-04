package com.smtl.edi.util;

import java.util.Collection;

/**
 * 校验工具类
 */
public class ValidationUtil {

    /**
     * 判断对象有效性
     *
     * @param obj
     * @return
     */
    public static boolean isValid(Object obj) {
        return !(obj == null);
    }

    /**
     * 判断字符串有效性
     *
     * @param src
     * @return
     */
    public static boolean isValid(String src) {
        return !(src == null || "".equals(src.trim()));
    }

    /**
     * 判断集合的有效性
     *
     * @param collection
     * @return
     */
    public static boolean isValid(Collection collection) {
        return !(collection == null || collection.isEmpty());
    }

    /**
     * 判断数组是否有效，对于可变参数的判断，如果可变参数的类型是非包装类型的话， 会验证通过，导致取值时出现索引越界异常，BUG
     *
     * @param arr
     * @return
     */
    public static boolean isValid(Object[] arr) {
        return !(arr == null || arr.length == 0);
    }
}
