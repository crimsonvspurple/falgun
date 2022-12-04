package red.eminence.commons.services.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties (prefix = "core.jwt")
public class JWTConfig
{
    private String headerName;
    private String tokenPrefix;
    private String symmetricSigningKey;
    private long   expirationMinutes;
    private long   refreshExpirationMinutes;
}
