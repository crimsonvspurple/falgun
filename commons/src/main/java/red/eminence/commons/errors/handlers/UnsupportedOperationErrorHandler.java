package red.eminence.commons.errors.handlers;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Priority;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;
import red.eminence.commons.filters.BaseFilter;
import red.eminence.commons.meta.Message;


@RestControllerAdvice
@Priority (12)
public class UnsupportedOperationErrorHandler extends BaseFilter implements WebExceptionHandler
{
    @Override
    @ExceptionHandler (value = {UnsupportedOperationException.class})
    @Nonnull
    public Mono<Void> handle (
            @NonNull
            ServerWebExchange exchange,
            @NonNull
            Throwable ex)
    {
        val response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        //        response.getHeaders()
        //                .setContentType(MediaType.APPLICATION_JSON_UTF8);
        Message message = Message.UNSUPPORTED_OPERATION_EXCEPTION;
        return composeError(response, message, ex.getCause().getMessage());
    }
}
