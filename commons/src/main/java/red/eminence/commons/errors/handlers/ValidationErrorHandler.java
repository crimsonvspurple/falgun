package red.eminence.commons.errors.handlers;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Priority;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;
import red.eminence.commons.filters.BaseFilter;
import red.eminence.commons.meta.Message;


@RestControllerAdvice
@Priority (10)
public class ValidationErrorHandler extends BaseFilter implements WebExceptionHandler
{
    @ExceptionHandler (value = {WebExchangeBindException.class})
    @Override
    @Nonnull
    public Mono<Void> handle (
            @NonNull
            ServerWebExchange exchange,
            @NonNull
            Throwable ex)
    {
        val response            = exchange.getResponse();
        val validationException = (WebExchangeBindException) ex;
        response.setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        return composeValidationError(response, Message.VALIDATION_FAILED, validationException);
    }
}
