package io.github.rothschil.common.config;


import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import io.github.rothschil.common.constant.DataSourceNamesConstant;
import io.github.rothschil.common.datasource.DynamicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;



@Configuration
public class DynamicDataSourceConfig {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicDataSourceConfig.class);

    /**
     * 创建 DataSource Bean
     */
    @Bean("oneDataSource")
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource oneDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean("twoDataSource")
    @ConfigurationProperties("spring.datasource.druid.salve")
    public DataSource twoDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 如果还有数据源,在这继续添加 DataSource Bean
     */
    @Bean
    @Primary
    public DynamicDataSource dataSource(DataSource oneDataSource, DataSource twoDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put(DataSourceNamesConstant.ONE, oneDataSource);
        targetDataSources.put(DataSourceNamesConstant.TWO, twoDataSource);
        // 还有数据源,在targetDataSources中继续添加
        LOG.info("DataSources {}",targetDataSources);
        return new DynamicDataSource(oneDataSource, targetDataSources);
    }
}
