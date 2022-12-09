package red.eminence.commons.errors;

import org.springframework.http.HttpStatus;
import red.eminence.commons.meta.Message;


// What's a user friendly error? It's an error that can be disclosed to the API consumer.
public class Detailed404Error extends DetailedError
{
    public <T> Detailed404Error (Class<T> entity, String id)
    {
        this(entity.getSimpleName(), id);
    }
    
    public <T> Detailed404Error (String entity, String id)
    {
        super(Message.ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND, String.join(": ", entity, id));
    }
}
