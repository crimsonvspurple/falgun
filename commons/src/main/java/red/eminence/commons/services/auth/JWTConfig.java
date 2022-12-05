package red.eminence.commons.services.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.security.Key;


@Data
@Configuration
@ConfigurationProperties (prefix = "core.jwt")
public class JWTConfig
{
    private String headerName;
    private String tokenPrefix;
    private Key    symmetricSigningKey;
    private long   expirationMinutes;
    private long   refreshExpirationMinutes;
}
