//package io.github.rothschil.common.config;
//
//
//import com.zaxxer.hikari.HikariDataSource;
//import io.github.rothschil.common.constant.DataSourceNamesConstant;
//import io.github.rothschil.common.datasource.DynamicDataSource;
//import jakarta.persistence.EntityManagerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//
//
//@Configuration
//public class DynamicDataSourceConfig2 {
//
//    private static final Logger LOG = LoggerFactory.getLogger(DynamicDataSourceConfig2.class);
//
//    /**
//     * 创建 DataSource Bean
//     */
//    @Primary
//    @Bean("oneDataSourceProperties")
//    @ConfigurationProperties("spring.datasource.master")
//    public DataSourceProperties masterDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Primary
//    @Bean("masterDataSource")
//    @Qualifier(value = "masterDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
//    public HikariDataSource masterDataSource() {
//        return masterDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
//    }
//
//
//    @Bean("salveDataSourceProperties")
//    @ConfigurationProperties("spring.datasource.salve")
//    public DataSourceProperties salveDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean("salveDataSource")
//    @Qualifier(value = "salveDataSource")
//    // 留意下面这行
//    @ConfigurationProperties(prefix = "spring.datasource.salve.hikari")
//    public HikariDataSource salveDataSource() {
//        return salveDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
//    }
//
//    @Bean(name = "masterJdbcTemplate")
//    public JdbcTemplate masterJdbcTemplate(@Qualifier("masterDataSource") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//
//    @Bean(name = "salveJdbcTemplate")
//    public JdbcTemplate salveJdbcTemplate(@Qualifier("salveDataSource") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//
//
//    @Primary
//    @Bean(name = "myEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean myEntityManagerFactory(HikariDataSource hds, Properties prop) {
//        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
//        bean.setPackagesToScan("com.example.entity");
//        HibernateJpaVendorAdapter hjva = new HibernateJpaVendorAdapter();
//        bean.setJpaVendorAdapter(hjva);
//        bean.setDataSource(hds);
//        bean.setJpaProperties(prop);
//        return bean;
//    }
//
//    @Primary
//    @Bean(name = "myTransactionManager")
//    public PlatformTransactionManager myTransactionManager(EntityManagerFactory myEntityManagerFactory) {
//        JpaTransactionManager jtm = new JpaTransactionManager();
//        jtm.setEntityManagerFactory(myEntityManagerFactory);
//        return jtm;
//    }
//
//    /**
//     * 如果还有数据源,在这继续添加 DataSource Bean
//     */
//    @Bean
//    @Primary
//    public DynamicDataSource dataSource(DataSource masterDataSource, DataSource salveDataSource) {
//        Map<Object, Object> targetDataSources = new HashMap<>(2);
//        targetDataSources.put(DataSourceNamesConstant.ONE, masterDataSource);
//        targetDataSources.put(DataSourceNamesConstant.TWO, salveDataSource);
//        // 还有数据源,在targetDataSources中继续添加
//        LOG.info("DataSources {}",targetDataSources);
//        return new DynamicDataSource(masterDataSource, targetDataSources);
//    }
//}
