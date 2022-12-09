package red.eminence.commons.errors.handlers;

import jakarta.annotation.Nonnull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;
import red.eminence.commons.filters.BaseFilter;
import red.eminence.commons.meta.Message;

import java.io.IOException;


@RestControllerAdvice
@Log4j2
@Component
public class RootExceptionHandler extends BaseFilter implements WebExceptionHandler
{
    @ExceptionHandler (value = {Exception.class})
    @Override
    @Nonnull
    public Mono<Void> handle (
            @NonNull
            ServerWebExchange exchange,
            @NonNull
            Throwable exception)
    {
        val response = exchange.getResponse();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        if (exception instanceof IOException && exception.getMessage().startsWith("An established connection was aborted by the software in your host machine")) {
            // ignore
            log.info("Silently ignoring 'established connection was aborted'");
        }
        else if (exception instanceof IOException && exception.getMessage().contains("Broken pipe")) {
            // ignore
            log.info("Silently ignoring 'Broken Pipe' exceptions");
        }
        else {
            log.error("Unhandled exception found: ", exception);
        }
        return composeError(response, Message.SOMETHING_WENT_WRONG);
    }
}
