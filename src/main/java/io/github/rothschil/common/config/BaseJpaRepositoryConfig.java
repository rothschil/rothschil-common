package io.github.rothschil.common.config;


import io.github.rothschil.common.base.persistence.repository.BaseRepositoryFactoryBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"**.repository"}, repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
@EntityScan(basePackages = {"**.entity"})
public class BaseJpaRepositoryConfig {
}
