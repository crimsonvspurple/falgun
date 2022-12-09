package red.eminence.commons.errors;

import org.springframework.http.HttpStatus;
import red.eminence.commons.meta.Message;
// What's a fatal error? It's an error that we triggered ourselves for some reason.
// Default is a bad request return type, this is NOT disclosed to the user (but is still handled)


public class FatalError extends BaseError
{
    public FatalError (Message message, HttpStatus code)
    {
        super(message, code);
    }
}
