package red.eminence.commons.services.auth;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.function.Function;


@Log4j2
@Component
public class JWTAuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>>
{
    private final JWTConfig  jwtConfig;
    private final JWTHandler jwtHandler;
    
    public JWTAuthenticationConverter (@NonNull JWTHandler jwtHandler, @NonNull JWTConfig jwtConfig)
    {
        this.jwtHandler = jwtHandler;
        this.jwtConfig  = jwtConfig;
    }
    
    @Override
    public Mono<Authentication> apply (ServerWebExchange exchange)
    {
        ServerHttpRequest request   = exchange.getRequest();
        String            authToken = null;
        val bearerRequestHeader = request.getHeaders().getFirst(jwtConfig.getHeaderName());
        val tokenPrefix = jwtConfig.getTokenPrefix();
        if (bearerRequestHeader != null && bearerRequestHeader.startsWith(tokenPrefix + " ")) {
            authToken = bearerRequestHeader.substring(tokenPrefix.length() + 1);
        }
        try {
            if (authToken != null) {
                val username = jwtHandler.getUsernameFromToken(authToken);
                log.debug("Checking auth token for " + username);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtHandler.validateToken(authToken)) {
                        log.debug("Successfully authenticated " + username + ", setting security context.");
                        val user = jwtHandler.getDecodedUserFromToken(authToken);
                        log.debug("Decoded one user from the token: " + user.toString());
                        final String token = authToken;
                        return Mono.just(user).publishOn(Schedulers.parallel()).switchIfEmpty(Mono.empty()).map(x -> new JWTAuthenticationToken(token, x));
                    }
                }
            }
            else {
                log.debug("No bearer token found, ignoring...");
            }
        }
        catch (IllegalArgumentException e) {
            log.debug("Could not extract the principal identifier from the token.", e);
        }
        catch (Exception e) {
            log.debug("The bearer token (JWT) provided has either expired or is invalid.", e);
        }
        return Mono.empty();
    }
}
