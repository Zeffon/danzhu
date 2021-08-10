package com.zeffon.danzhu.util;

import com.zeffon.danzhu.core.enumeration.ValueEnum;
import com.zeffon.danzhu.exception.http.ParameterException;

/**
 * Create by Zeffon on 2020/10/4
 */
public class EnumUtil {
    public static <T extends ValueEnum> T getByValue(Object value, Class<T> enumClass) {
        for (T each: enumClass.getEnumConstants()) {
            if (value.equals(each.getValue())) {
                return each;
            }
        }
        throw new ParameterException(10001);
    }
}
