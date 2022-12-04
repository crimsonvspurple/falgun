package red.eminence.user.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;


@Configuration
public class CorsConfig implements WebFluxConfigurer
{
    @Override
    public void addCorsMappings (CorsRegistry registry)
    {
        // TODO: lock it down later
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*").allowCredentials(false).maxAge(3600);
    }
}