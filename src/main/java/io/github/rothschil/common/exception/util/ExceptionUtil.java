package io.github.rothschil.common.exception.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
public class ExceptionUtil {

    /**
     * 打印异常信息
     */
    public static String getMessage(Exception e) {
        String swStr = null;
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            pw.flush();
            sw.flush();
            swStr = sw.toString();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return swStr;
    }
}
