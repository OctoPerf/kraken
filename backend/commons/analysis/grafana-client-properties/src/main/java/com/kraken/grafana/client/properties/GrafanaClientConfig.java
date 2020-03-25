package com.kraken.grafana.client.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GrafanaClientProperties.class)
class GrafanaClientConfig {

}
