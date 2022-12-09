package red.eminence.commons.errors;

import org.springframework.http.HttpStatus;
import red.eminence.commons.meta.Message;


// What's a user-friendly error? It's an error that can be disclosed to the API consumer.
public class UserFriendlyError extends BaseError
{
    public UserFriendlyError (Message message, HttpStatus code)
    {
        super(message, code);
    }
}
