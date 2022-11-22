package com.fuerte.riskcontrol.util;

import java.util.Collection;

public class CollectionUtil {


    public static boolean isNullOrEmpty(Collection c){
        return c == null || c.isEmpty();
    }

    public static boolean isIndexValid(Collection c, int index){
        if (c == null || c.isEmpty() || index < 0){
            return false;
        } else {
            return index < c.size();
        }
    }

}
