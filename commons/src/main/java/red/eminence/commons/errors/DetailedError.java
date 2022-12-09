package red.eminence.commons.errors;

import org.springframework.http.HttpStatus;
import red.eminence.commons.meta.Message;


// What's a user-friendly error? It's an error that can be disclosed to the API consumer.
public class DetailedError extends BaseError
{
    public DetailedError (Message message, HttpStatus code, Object detail)
    {
        super(message, code, detail);
    }
    
    public DetailedError (HttpStatus code, String errorMessage)
    {
        super(code, errorMessage);
    }
}
