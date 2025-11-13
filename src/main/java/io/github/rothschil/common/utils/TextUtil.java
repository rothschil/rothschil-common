package io.github.rothschil.common.utils;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaoyang on 2018/8/22.
 */
@Slf4j
public class TextUtil {
    private static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};


    // 仅保留数字部分
    private static final String REGEX_LEAVE_NUM = "[^(0-9)]";

    // 仅保留数字部分
    private static final Pattern PATTERN_AMOUNT = Pattern.compile("(\\d+)元");

    // 形如yyyyMM
    private static final Pattern YEAR_MONTH_AMOUNT = Pattern.compile("[1-9][0-9]{3}[0-9]{2}");

    //判断是否是正整数
    public static boolean isNumeric(String string) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(string).matches();
    }

    //判断是否是IP地址
    public static boolean isIpAddr(String string) {
        String ip_ip = "(25[0-5]|2[0-4]\\d|1\\d\\d|\\d\\d|\\d)";
        String ip_ipdot = ip_ip + "\\.";
        String ip_port = "(:(\\d\\d\\d\\d|\\d\\d\\d|\\d\\d|\\d))?";
        String ipReg = "^" + ip_ipdot + ip_ipdot + ip_ipdot + ip_ip + ip_port + "$";
        return Pattern.matches(ipReg, string);
    }

    /**
     * 生成8位不重复的随机码
     *
     * @return short_uuid
     */
    public static String generateShortUUID() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    /**
     * 生成32位小写UUID
     *
     * @return uuid
     */
    public static String createDefaultUUID() {
        UUID rand_uuid = UUID.randomUUID();
        String uuid = rand_uuid.toString();
        uuid = uuid.replace("-", "");
        return uuid;
    }

    /**
     * 字符串数组[1,2,3] 转换成用于SQL查询的 1,2,3 格式
     *
     * @param arr
     * @return
     */
    public static String ArrToSqlCellStr(String[] arr) {

        if (arr == null) {
            return "";
        }

        // 数组转换为X,X
        String idArr = Arrays.toString(arr).replace("[", "").replace("]", "");
        return idArr;
    }

    /**
     * 获取最大length长度的文本
     *
     * @param txt
     * @param length
     * @return
     */
    public static String getTxt(String txt, int length) {
        if (StringUtils.isBlank(txt)) {
            return "";
        }

        if (txt.length() > length) {
            txt = txt.substring(0, length);
        }
        return txt;
    }


    /** 英文半角字符含有："[**]" 全部置为空，其中 ** 为通配！
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @param source 原始字符串
     * @return String
     **/
    public static String takeOffBrackets(String source) {
        return source.replaceAll("\\[.*?\\]","");
    }

    /**
     * 正则匹配
     *
     * @param txt
     * @param reg
     * @return
     */
    public static Boolean validReg(String txt, String reg) {
        return txt.matches(reg);
    }

    /**
     * 字符串转数字
     *
     * @param txt
     * @return
     */
    public static int str2Number(String txt) {
        return str2Number(txt, 0);
    }

    /**
     * 检查是否连续
     * @return
     */
    public static boolean checkNumContinuous(String value){
        int length = value.length();
        //是否不合法
        boolean isValidate = false;
        //
        int i = 0;
        //计数器
        int counter = 1;
        //
        for(; i < value.length() -1;) {
            //当前ascii值
            int currentAscii = Integer.valueOf(value.charAt(i));
            //下一个ascii值
            int nextAscii = Integer.valueOf(value.charAt(i + 1));
            //满足区间进行判断
            if( (rangeInDefined(currentAscii, 48, 57) || rangeInDefined(currentAscii, 65, 90) || rangeInDefined(currentAscii, 97, 122))
                    && (rangeInDefined(nextAscii, 48, 57) || rangeInDefined(nextAscii, 65, 90) || rangeInDefined(nextAscii, 97, 122)) ) {
                //计算两数之间差一位则为连续
                if(Math.abs((nextAscii - currentAscii)) == 1){
                    //计数器++
                    counter++;
                }else{
                    //否则计数器重新计数
                    counter = 1;
                }
            }
            //满足连续数字或者字母
            if(counter >= length) return !isValidate;
            //
            i++;
        }

        //
        return isValidate;
    }
    /**
     * SM 判断一个数字是否在某个区间
     * @param current 当前比对值
     * @param min   最小范围值
     * @param max   最大范围值
     * @return
     */
    public static boolean rangeInDefined(int current, int min, int max) {
        //
        return Math.max(min, current) == Math.min(current, max);
    }
    /**
     * 字符串转数字
     *
     * @param txt
     * @param defaultVal
     * @return
     */
    public static int str2Number(String txt, int defaultVal) {
        int number = defaultVal;
        // 非数值
        if (StringUtils.isEmpty(txt) || !StringUtils.isNumeric(txt)) {
            return number;
        }

        try {
            number = Integer.parseInt(txt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return number;
    }

    /**
     * 将错误信息转为字符串
     *
     * @param e
     * @return
     */
    public static String exToStr(Throwable e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        return baos.toString();
    }

    public static String exToStr(Exception e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        return baos.toString();
    }


    /**
     * xls字段转Sting
     *
     * @param o
     * @return
     */
    public static String xlsObj2Str(Object o) {
        if (o != null) {
            return String.valueOf(o).replaceAll("\r|\n|\\s", "");
        } else {
            return null;
        }
    }

    /**
     * 保留文本的数字部分
     *
     * @param txt
     * @return
     */
    public static String txtLeaveNum(String txt) {
        return StringUtils.trimToEmpty(Pattern.compile(REGEX_LEAVE_NUM).matcher(StringUtils.trimToEmpty(txt)).replaceAll(""));
    }

    public static String format(String msg, Object... args) {
        if (null != args && args.length > 0) {
            for (Object arg : args) {
                msg = StringUtils.replaceOnce(msg, "{}", String.valueOf(arg));
            }
        }
        return msg;
    }

    /**
     * 获取mongo字段的值，并转换为字符串类型
     *
     * @param columnVal
     * @return
     */
    public static String mapVal2Text(Object columnVal) {
        return mapVal2Text(columnVal, "");
    }

    /**
     * 获取mongo字段的值，并转换为字符串类型
     *
     * @param columnVal
     * @param defVal
     * @return
     */
    public static String mapVal2Text(Object columnVal, String defVal) {
        if (columnVal == null) {
            return StringUtils.trimToEmpty(defVal);
        }

        String backRst = "";

        try {
            if (columnVal instanceof String) {
                backRst = (String) columnVal;
            } else if (columnVal instanceof Number) {
                backRst = columnVal.toString();
            } else {
                backRst = columnVal.toString();
            }
        } catch (Exception e) {
            log.error("字段值类型转换异常: " + exToStr(e));
        }


        // 判断默认值
        if (StringUtils.isEmpty(backRst)) {
            backRst = StringUtils.trimToEmpty(defVal);
        }

        return backRst;
    }

    /**
     * 获取mongo字段的值，并转换为整型
     *
     * @param columnVal
     * @return
     */
    public static int mapVal2Int(Object columnVal) {
        String val = mapVal2Text(columnVal);

        if (StringUtils.isEmpty(val) || !StringUtils.isNumeric(val)) {
            return 0;
        } else {
            return Integer.parseInt(val);
        }
    }

    /**
     * 生成count位不重复的随机码
     *
     * @param count 指定位数
     * @return short_uuid
     */
    public static String generateShortUUID(int count) {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 6; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    //取出**元
    public static Integer getAmount(String string) {
        Integer amount = null;
        Matcher matcher = PATTERN_AMOUNT.matcher(string);
        while (matcher.find()) {
            String matchWord = matcher.group(0);
            String[] str = matchWord.split("元");
            if (!ObjectUtil.isEmpty(str) && str.length > 0) {
                String amountStr = str[0];
                if (StringUtils.isNotEmpty(amountStr)) {
                    return Integer.valueOf(amountStr);
                }
            }

        }
        return amount;
    }

    public static String replaceYearMonth(String string) {
        Matcher matcher = YEAR_MONTH_AMOUNT.matcher(string);
        while (matcher.find()) {
            String matchWord = matcher.group(0);
            string = string.replace(matchWord, "");
        }
        return string;
    }


}
