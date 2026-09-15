package io.github.rothschil.common.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author: <a href="mailto:WCNGS@QQ.COM">Sam</a>
 **/
@Configuration
public class JasyptConfig {

    @Bean
    public StringEncryptor stringEncryptor() {
        // 获取密码的方式，这里可以自定义
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        // 动态获取密码
        config.setPassword("@nhuihefei");
//        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPoolSize("1");
//        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
//        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
//        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        encryptor.initialize();
        return encryptor;
    }

}