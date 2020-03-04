package com.inkfish.blog.server.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author HALOXIAO
 **/

@Configuration
@ConfigurationProperties(prefix = "server.rocketmq.configuration")
public class MqConfig {
    String nameSrvAddress;

    public String getNameSrvAddress() {
        return nameSrvAddress;
    }

    public void setNameSrvAddress(String nameSrvAddress) {
        this.nameSrvAddress = nameSrvAddress;
    }
}
