package com.github.czq.tea;


import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class TeaConverter {
    public TeaConverter() {
    }

    public static <T> Map<String, T> buildMap(TeaPair... pairs) {
        Map<String, T> map = new HashMap();
        for (int i = 0; i < pairs.length; ++i) {
            TeaPair pair = pairs[i];
            if (StringUtils.isEmpty((T) pair.value))
                continue;
            map.put(pair.key, (T) pair.value);
        }
        return map;
    }

}