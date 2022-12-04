package red.eminence.user.services.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import red.eminence.commons.services.auth.Token;


@Data ()
@EqualsAndHashCode (callSuper = false)
@NoArgsConstructor
public class AuthResponse
{
    private Token access;
    private Token refresh;
}
