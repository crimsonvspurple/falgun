package red.eminence.user.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties (prefix = "config.this")
public class ServiceConfig
{}
