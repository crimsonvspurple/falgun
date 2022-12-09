package red.eminence.commons.errors.handlers;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Priority;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;
import red.eminence.commons.errors.BaseError;
import red.eminence.commons.errors.DetailedError;
import red.eminence.commons.errors.UserFriendlyError;
import red.eminence.commons.filters.BaseFilter;
import red.eminence.commons.meta.Message;


@RestControllerAdvice
@Log4j2
@Priority (100)
public class BaseErrorHandler extends BaseFilter implements WebExceptionHandler
{
    @ExceptionHandler (value = {BaseError.class})
    @Override
    @Nonnull
    public Mono<Void> handle (
            @NonNull
            ServerWebExchange exchange,
            @NonNull
            Throwable exception)
    {
        val response = exchange.getResponse();
        val baseCast = (BaseError) exception;
        response.setStatusCode(baseCast.getStatus());
        String error;
        Object detail = null;
        if (exception instanceof UserFriendlyError) {
            error = baseCast.getRealMessage();
        }
        else if (exception instanceof DetailedError) {
            error  = baseCast.getRealMessage();
            detail = baseCast.getDetail();
        }
        else {
            error = Message.SOMETHING_WENT_WRONG.toString();
            log.error("Unhandled exception found: ", exception);
        }
        return detail == null ? composeError(response, error) : composeError(response, error, detail);
    }
}
