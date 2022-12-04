package red.eminence.commons.services.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import red.eminence.commons.filters.BaseFilter;
import red.eminence.commons.meta.Message;


public class JWTAuthenticationEntryPoint extends BaseFilter implements ServerAuthenticationEntryPoint
{
    @Override
    public Mono<Void> commence (ServerWebExchange exchange, AuthenticationException e)
    {
        //return null;
        return composeError(exchange.getResponse(), Message.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }
}
