package io.github.rothschil;

import io.github.rothschil.common.config.DynamicDataSourceConfig;
import io.github.rothschil.common.config.Version;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Basic Constant Information，Can be extended
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@EnableJpaAuditing
@Slf4j
@Import({DynamicDataSourceConfig.class})
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class RothschilApplication {

	public static void main(String[] args) {
		SpringApplication app =new SpringApplication(RothschilApplication.class);
		Environment env = app.run(args).getEnvironment();
		Version.printlnVersionInfo(env);
	}

}
