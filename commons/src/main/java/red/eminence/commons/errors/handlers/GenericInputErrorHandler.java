package red.eminence.commons.errors.handlers;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Priority;
import lombok.val;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;
import red.eminence.commons.filters.BaseFilter;
import red.eminence.commons.meta.Message;

import java.util.ArrayList;
import java.util.List;


@RestControllerAdvice
@Priority (11)
public class GenericInputErrorHandler extends BaseFilter implements WebExceptionHandler
{
    @Override
    @ExceptionHandler (value = {ServerWebInputException.class})
    @Nonnull
    public Mono<Void> handle (
            @NonNull
            ServerWebExchange exchange,
            @NonNull
            Throwable ex)
    {
        val castError = (ServerWebInputException) ex;
        val response  = exchange.getResponse();
        response.setStatusCode(castError.getStatusCode());
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        Message      message = Message.UNEXPECTED_EMPTY_BODY;
        List<Object> details = new ArrayList<>();
        details.add(castError.getReason());
        if (castError.getCause() != null) {
            val cause = castError.getCause();
            if (cause instanceof DecodingException) {
                details.add(cause.getCause().toString().substring(0, Math.min(300, cause.getCause().toString().length())));
                message = Message.MALFORMED_BODY_CONTENTS;
            }
        }
        return composeError(response, message, details);
    }
}
