package io.github.rothschil.common.utils;


import io.github.rothschil.common.annotation.Cacheable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolUtils {


    /**
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param cacheable
     * @param pattern
     * @return String
     **/
    public static String buildContent(Cacheable cacheable, String pattern) {
        String keyOirgin = cacheable.key();
//        String pattern = "\\w+\\.\\w+";
        if(ToolUtils.operation(keyOirgin, pattern)){
            return "{"+keyOirgin+"}";
        }
        return keyOirgin;
    }

    /**
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param content
     * @param pattern
     * @return boolean
     **/
    public static boolean operation(String content,String pattern){
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(content);
        return m.matches();
    }
}
