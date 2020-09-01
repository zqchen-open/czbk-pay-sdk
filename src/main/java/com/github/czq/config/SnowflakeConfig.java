package com.github.czq.config;

import com.github.czq.utils.SnowflakeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花算法生成唯一编号
 * Long.toString(snowflakeConfig.getIdWorker().nextId())
 * snowflakeConfig.getIdWorker().nextId("K", true)
 */
@Configuration
public class SnowflakeConfig {

    @Bean
    public SnowflakeUtils getIdWorker() {
        return new SnowflakeUtils(0 , 0);
    }
}
