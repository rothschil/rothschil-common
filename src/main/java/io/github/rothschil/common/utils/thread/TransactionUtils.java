package io.github.rothschil.common.utils.thread;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionUtils {

    private TransactionUtils() {
        throw new IllegalStateException("Utility class");
    }


    public static String getDateTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    public static String getTransactionId(String num) {
        String transactionId = "";
        transactionId = "JS00000009" + getDateTime("yyyyMMdd") + getRandomNum(9) + num;
        return transactionId;
    }

    public static String getStreamingNo() {
        String transactionId = "";
        transactionId = "nj1000" + getDateTime("yyyyMMddHHmmss");
        return transactionId;
    }

    public static String getRandomNum(int n) {
        int num = (int)((Math.random()*9+1)*Math.pow(10, n-1));
        return String.valueOf(num);
    }
}
