package red.eminence.commons.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import red.eminence.commons.meta.Message;


@ResponseStatus (HttpStatus.INTERNAL_SERVER_ERROR)
public class BaseError extends RuntimeException
{
    // TODO: Refactor error system completely
    @Getter
    private final HttpStatus status;
    @Getter
    private final String     realMessage;
    @Getter
    private final Object     detail;
    
    BaseError (Message message, HttpStatus status)
    {
        super(message.toString());
        this.status = status;
        realMessage = message.toString();
        detail      = null;
    }
    
    BaseError (Message message, HttpStatus status, Object detail)
    {
        super(message.toString());
        this.status = status;
        realMessage = message.toString();
        this.detail = detail;
    }
    
    BaseError (HttpStatus status, String errorMessage)
    {
        super(errorMessage);
        this.status      = status;
        this.realMessage = errorMessage;
        this.detail      = null;
    }
}