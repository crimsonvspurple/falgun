package red.eminence.commons.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import red.eminence.commons.base.APIResponse;
import red.eminence.commons.meta.Message;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Log4j2
public class BaseFilter
{
    protected Mono<Void> composeError (ServerHttpResponse response, Message message, HttpStatus status)
    {
        response.setStatusCode(status);
        return composeError(response, message);
    }
    
    protected Mono<Void> composeError (ServerHttpResponse response, Message message)
    {
        // TODO: Make this more dynamic, someday. JSON is all we support for now
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        val bufferFactory = response.bufferFactory();
        byte[] outBytes = getJsonBytes(new APIResponse(Message.REQUEST_FAILED, List.of(message), null, 1, Instant.now().getEpochSecond()));
        return outBytes == null ? Mono.empty() : response.writeWith(Mono.just(bufferFactory.wrap(outBytes)));
    }
    
    protected byte[] getJsonBytes (APIResponse response)
    {
        byte[] outBytes;
        try {
            outBytes = new ObjectMapper().writeValueAsBytes(response);
        }
        catch (JsonProcessingException exception) {
            // Should never happen, there's nothing complex about our POJO here
            // But the JVM said so, it must be true.
            log.error(exception);
            return null;
        }
        return outBytes;
    }
    
    protected Mono<Void> composeError (ServerHttpResponse response, String message)
    {
        // TODO: Make this more dynamic, someday. JSON is all we support for now
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        val bufferFactory = response.bufferFactory();
        byte[] outBytes = getJsonBytes(new APIResponse(Message.REQUEST_FAILED, List.of(message), null, 1, Instant.now().getEpochSecond()));
        return outBytes == null ? Mono.empty() : response.writeWith(Mono.just(bufferFactory.wrap(outBytes)));
    }
    
    protected Mono<Void> composeError (ServerHttpResponse response, Message message, Object errorDetail)
    {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        val bufferFactory = response.bufferFactory();
        byte[] outBytes = getJsonBytes(new APIResponse(Message.REQUEST_FAILED, List.of(message, errorDetail), null, 1, Instant.now().getEpochSecond()));
        return outBytes == null ? Mono.empty() : response.writeWith(Mono.just(bufferFactory.wrap(outBytes)));
    }
    
    protected Mono<Void> composeError (ServerHttpResponse response, String message, Object errorDetail)
    {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        val bufferFactory = response.bufferFactory();
        byte[] outBytes = getJsonBytes(new APIResponse(Message.REQUEST_FAILED, List.of(message, errorDetail), null, 1, Instant.now().getEpochSecond()));
        return outBytes == null ? Mono.empty() : response.writeWith(Mono.just(bufferFactory.wrap(outBytes)));
    }
    
    protected Mono<Void> composeValidationError (ServerHttpResponse response, Message message, WebExchangeBindException exception)
    {
        // TODO: Make this more dynamic, someday. JSON is all we support for now
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        val bufferFactory  = response.bufferFactory();
        val renderedErrors = new ArrayList<String>();
        for (val error : exception.getFieldErrors()) {
            val str = message.toString() + ":" + error.getField() + ":" + error.getCode() + ":" + error.getObjectName() + ":" + error.getRejectedValue() + ":" + error.getDefaultMessage();
            renderedErrors.add(str);
        }
        byte[] outBytes = getJsonBytes(new APIResponse(Message.REQUEST_FAILED, renderedErrors, null, 1, Instant.now().getEpochSecond()));
        return outBytes == null ? Mono.empty() : response.writeWith(Mono.just(bufferFactory.wrap(outBytes)));
    }
}
