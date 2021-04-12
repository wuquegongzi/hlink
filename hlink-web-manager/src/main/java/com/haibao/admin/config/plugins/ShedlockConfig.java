package com.haibao.admin.config.plugins;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

/**
 * @ClassName ShedlockConfig
 * @Description 配置 LockProvider
 * @Author ml.c
 * @Date 2020/3/7 9:49 下午
 * @Version 1.0
 */
@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "30s")
public class ShedlockConfig {

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {

//        clearCache();

        return new JdbcTemplateLockProvider(new JdbcTemplateLockProvider.Configuration.Builder()
                .withTableName("sys_shedlock")
                .withColumnNames(new JdbcTemplateLockProvider.ColumnNames("name", "lock_until", "locked_at", "locked_by"))
                .withJdbcTemplate(new JdbcTemplate(dataSource))
                .withLockedByValue("haibao_admin")
                .build());
    }


}
