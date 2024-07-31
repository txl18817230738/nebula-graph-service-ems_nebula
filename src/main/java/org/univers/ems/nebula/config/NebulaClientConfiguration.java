package org.univers.ems.nebula.config;

import com.vesoft.nebula.client.graph.exception.IOErrorException;
import com.vesoft.nebula.client.graph.net.NebulaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.univers.ems.nebula.Confignature;

import javax.annotation.PreDestroy;
import java.net.UnknownHostException;

/**
 * @author jie.xi
 * 配置NebulaClient
 *
 */
@Configuration
public class NebulaClientConfiguration {

    @Autowired
    private Confignature confignature;

    private NebulaClient client;

    @Bean
    public NebulaClient nebulaClient() {


        String address = confignature.address;
        String user = confignature.username;
        String passwd = confignature.password;

        // 创建并配置NebulaClient实例
        try {
            client = NebulaClient.builder(address, user, passwd)
                    .setConnectTimeoutMills(10000)
                    .setRequestTimeoutMills(30000)
                    .setMaxSessionSize(10)
                    .setMinSessionSize(1)
                    .setRetryTimes(3)
                    .setIntervalTimeMills(1000)
                    .setReconnect(true)
                    .setBlockWhenExhausted(true)
                    .setMaxWaitMills(1000)
                    .setStrictlyServerHealthy(true)
                    .build();
        } catch (IOErrorException | UnknownHostException e) {
            throw new RuntimeException(e);
        }

        // 返回配置好的客户端实例
        return client;
    }

    @PreDestroy
    public void closeClient() {
        if (client != null) {
            // 关闭客户端连接
            client.close();
        }
    }
}