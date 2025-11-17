package io.github.rothschil.common.config;

import io.github.rothschil.common.utils.NativeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class Version {
    public static final String module  = "MMCC_MAINTAIN";
    public static final String version = "v1.07.000";

    public static void printlnVersionInfo(Environment env) {
        try {
            String path = env.getProperty("server.servlet.context-path");
            if (StringUtils.isEmpty(path)) {
                path = "";
            }

            String appName =env.getProperty("spring.application.name");
            String port = env.getProperty("server.port");
            String[] profiles = env.getActiveProfiles();
            String address = InetAddress.getLocalHost().getHostAddress();

            log.info(
                    "\t\n----------------------------------------------------------\t\n"
                            + "Application '{}' is running! \t\n"
                            + "Profile(s): \t{}\n"
                            + "Access URLs: http://{}:{}{}\n"
                            + "Application Network Environment \t{}\n"
                            + "Enable Configuration \t{}"
                            +"\t\n----------------------------------------------------------\t\n",
                    appName, profiles,address,port,path,NativeUtil.ipNetworkSegment(),NativeUtil.judgmentEnv().getCode());

            VersionCommit.print();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getVersion() {
        return module + "  " + version;
    }
}
