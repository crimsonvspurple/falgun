package red.eminence.user.configs;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import red.eminence.commons.services.auth.JWTAuthenticationConverter;
import red.eminence.commons.services.auth.JWTAuthenticationEntryPoint;
import red.eminence.commons.services.auth.JWTAuthenticationManager;

import java.util.List;
import java.util.function.Function;


@EnableWebFluxSecurity ()
@EnableReactiveMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig
{
    private final JWTAuthenticationManager   authenticationManager;
    private final JWTAuthenticationConverter authenticationConverter;
    
    @Bean
    SecurityWebFilterChain configure (ServerHttpSecurity httpSecurity)
    {
        final List<Endpoint> publicEndpoints = List.of(new Endpoint(HttpMethod.POST, "/v1/auth"),
                                                       new Endpoint(HttpMethod.POST, "/v1/auth/refresh"),
                                                       new Endpoint(HttpMethod.POST, "/v1/auth/passwordStrength"),
                                                       new Endpoint(HttpMethod.POST, "/v1/user"),
                                                       new Endpoint(HttpMethod.POST, "/v1/user/exists"),
                                                       new Endpoint(HttpMethod.GET, "/v1/meta"),
                                                       new Endpoint(HttpMethod.GET, "/v1/seeder"));
        // lambda to filter out public endpoints based on HTTP method
        Function<HttpMethod, String[]> filterEndpoints = (HttpMethod method) -> publicEndpoints.parallelStream().filter(e -> e.method == method).map(e -> e.path).toArray(String[]::new);
        // we only want pure stateless REST API authenticated by JWT only
        httpSecurity.httpBasic().disable();
        httpSecurity.formLogin().disable();
        httpSecurity.csrf().disable();
        httpSecurity.logout().disable();
        // let's allow the following endpoints to be accessed without authentication and everything else requires authentication
        httpSecurity.authorizeExchange()
                    // allow all OPTIONS calls
                    .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    // allow all public GETs; supply array instead of varargs
                    .pathMatchers(HttpMethod.GET, filterEndpoints.apply(HttpMethod.GET)).permitAll()
                    // allow all public POSTs
                    .pathMatchers(HttpMethod.POST, filterEndpoints.apply(HttpMethod.POST)).permitAll()
                    // https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#actuator.endpoints
                    // https://docs.spring.io/spring-boot/docs/current/actuator-api/htmlsingle/
                    .pathMatchers("/actuator/**", "/ipc/**").permitAll()
                    // everything else needs to be authenticated
                    .anyExchange().authenticated();
        httpSecurity.securityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        httpSecurity.authenticationManager(authenticationManager);
        httpSecurity.addFilterAt(configureWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION);
        return httpSecurity.build();
    }
    
    private AuthenticationWebFilter configureWebFilter ()
    {
        val f = new AuthenticationWebFilter(authenticationManager);
        f.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(new JWTAuthenticationEntryPoint()));
        f.setAuthenticationConverter(authenticationConverter);
        f.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        return f;
    }
    
    static class Endpoint
    {
        HttpMethod method;
        String     path;
        
        public Endpoint (final HttpMethod method, final String path)
        {
            this.method = method;
            this.path   = path;
        }
    }
}
