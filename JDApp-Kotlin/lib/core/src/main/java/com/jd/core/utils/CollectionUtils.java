package com.jd.core.utils;

import android.util.SparseArray;

import java.util.Collection;
import java.util.Map;

/**
 * 集合数组判空
 */
public abstract class CollectionUtils {

    public static boolean isEmpty (Collection<?> collection) {

        return null == collection || collection.isEmpty();
    }

    public static boolean isEmpty (Map<?, ?> map) {

        return null == map || map.isEmpty();
    }

    public static boolean isEmpty (SparseArray<?> sa) {

        return null == sa || sa.size() == 0;
    }

    public static <T> boolean isEmpty (T[] array) {

        return null == array || array.length == 0;
    }

    public static  boolean isEmpty (char[] array) {

        return null == array || array.length == 0;
    }

    public static  boolean isEmpty (int[] array) {

        return null == array || array.length == 0;
    }

    public static  boolean isEmpty (long[] array) {

        return null == array || array.length == 0;
    }

    public static  boolean isEmpty (float[] array) {

        return null == array || array.length == 0;
    }
}
