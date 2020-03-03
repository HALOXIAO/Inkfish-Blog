package com.inkfish.blog.email.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "email.rocketmq.configuration")
public class MqConfig {
    String nameSrvAddress;

    public String getNameSrvAddress() {
        return nameSrvAddress;
    }

    public void setNameSrvAddress(String nameSrvAddress) {
        this.nameSrvAddress = nameSrvAddress;
    }

    @Override
    public String toString() {
        return "MqConfig{" +
                "nameSrvAddress='" + nameSrvAddress + '\'' +
                '}';
    }
}
