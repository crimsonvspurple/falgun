package red.eminence.commons.services.meta;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties ("meta.config")
public class MetaConfig
{
    private String message;
}
