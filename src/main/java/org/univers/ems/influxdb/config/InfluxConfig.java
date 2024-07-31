package org.univers.ems.influxdb.config;

import lombok.Data;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jie.xi
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "influx")
public class InfluxConfig {
    /**
     * url地址
     */
    private String influxUrl;
    /**
     * 桶(表)
     */
    private String bucket;
    /**
     * 组织
     */
    private String org;
    /**
     * token
     */
    private String token;
    /**
     * 初始化bean
     */
    @Bean(name = "influx")
    public InfluxBean InfluxBean() {
        return new InfluxBean(influxUrl, bucket, org, token);
    }
}