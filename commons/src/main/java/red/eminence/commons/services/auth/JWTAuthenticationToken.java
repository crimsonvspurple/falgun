package red.eminence.commons.services.auth;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import red.eminence.commons.services.user.User;


public class JWTAuthenticationToken extends UsernamePasswordAuthenticationToken
{
    @Getter
    private final Token token = new Token();
    
    public JWTAuthenticationToken (@NonNull String token, @NonNull User user)
    {
        super(user, null, user.getAuthorities());
        this.token.setToken(token);
    }
    
    @Override
    public Object getCredentials ()
    {
        return token;
    }
}
