package red.eminence.commons.errors.handlers;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Priority;
import lombok.val;
import org.springframework.dao.OptimisticLockingFailureException;
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
public class DAOOptimisticLockingErrorHandler extends BaseFilter implements WebExceptionHandler
{
    @Override
    @ExceptionHandler (value = {OptimisticLockingFailureException.class})
    @Nonnull
    public Mono<Void> handle (
            @NonNull
            ServerWebExchange exchange,
            @NonNull
            Throwable ex)
    {
        val response = exchange.getResponse();
        response.setStatusCode(HttpStatus.PRECONDITION_FAILED);
        //        response.getHeaders()
        //                .setContentType(MediaType.APPLICATION_JSON_UTF8);
        Message message = Message.ENTITY_VERSION_MISMATCH;
        return composeError(response, message);
    }
}
