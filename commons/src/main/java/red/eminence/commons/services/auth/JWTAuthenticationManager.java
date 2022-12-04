package red.eminence.commons.services.auth;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
public class JWTAuthenticationManager implements ReactiveAuthenticationManager
{
    @Override
    public Mono<Authentication> authenticate (Authentication authentication)
    {
        return Mono.just(authentication);
    }
}
