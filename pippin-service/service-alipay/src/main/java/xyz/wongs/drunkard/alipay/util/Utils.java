package xyz.wongs.drunkard.alipay.util;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/** 杂物工具类
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @description //TODO
 * @github <a>https://github.com/rothschil</a>
 * @date 2021/9/23 - 10:16
 * @version 1.0.0
 */
public class Utils {

    private Utils() {
        // No instances.
    }

    public static String toAmount(long amount) {
        return new BigDecimal(amount).divide(new BigDecimal(100)).toString();
    }

    public static String toDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static boolean isEmpty(Object object) {
        if (object instanceof String) {
            return StringUtils.isEmpty((String) object);
        }
        return object == null;
    }

    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    public static <T> boolean isListNotEmpty(List<T> list) {
        return list != null && list.size() > 0;
    }

    public static <T> boolean isListEmpty(List<T> list) {
        return !isListNotEmpty(list);
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}