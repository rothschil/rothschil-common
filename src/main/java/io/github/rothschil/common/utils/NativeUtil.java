package io.github.rothschil.common.utils;

import cn.hutool.core.util.ObjectUtil;
import io.github.rothschil.common.constant.Constant;
import io.github.rothschil.common.enums.EnvironmentEnums;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 环境变量
 *
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Slf4j
public class NativeUtil {

    private static final String IPADDRESS_PATTERN ="[0-9]{1,3}.[0-9]{1,3}."+ Constant.IPADDRESS_MASTR+".[0-9]{1,3}";

    private static EnvironmentEnums ENVIRON_MENT = null;

    /**
     * 获取当前主机网段
     *
     * @return String
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     **/
    public static String ipNetworkSegment() {
        String ipNet = "";
        try {
            ipNet = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error(e.getMessage(), e);
        }
        return ipNet;
    }

    /**
     * 判断环境，主环境为，备用环境为
     *
     * @return String
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     **/
    public static EnvironmentEnums judgmentEnv() {
        if(ObjectUtil.isNotEmpty(ENVIRON_MENT)){
            return ENVIRON_MENT;
        }
        try {
            String ipNet = ipNetworkSegment();
            Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
            Matcher matcher = pattern.matcher(ipNet);
            if (matcher.find()) {
                ENVIRON_MENT = EnvironmentEnums.MASTER;
            } else{
                ENVIRON_MENT =  EnvironmentEnums.SLAVE;
            }
        } catch (Exception e) {
            ENVIRON_MENT=  EnvironmentEnums.MASTER;
        }
        return ENVIRON_MENT;
    }
}