package com.inkfish.blog.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author HALOXIAO
 **/

@ConfigurationProperties(prefix = "inkfish.rocketmq")
@Configuration
public class NameServerProperty {


}
