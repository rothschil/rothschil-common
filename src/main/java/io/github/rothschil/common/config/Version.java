package io.github.rothschil.common.config;

import io.github.rothschil.common.utils.NativeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

@Slf4j
public class Version {
    public static final String module  = "MMCC_MAINTAIN";
    public static final String version = "v1.07.000";

    public static void printlnVersionInfo(Environment env) {
        log.info(
                "\t\n----------------------------------------------------------\t\n"
                        + "Application '{}' is running! Access URLs:\t\n"
                        + "Profile(s): \t{}\t\n----------------------------------------------------------\t\n",
                env.getProperty("spring.application.name"), env.getActiveProfiles());
        VersionCommit.print();
        log.error("Application Network Environment {}, Enable Configuration {}", NativeUtil.ipNetworkSegment(),NativeUtil.judgmentEnv().getCode());
    }

    public static String getVersion() {
        return module + "  " + version;
    }
}
