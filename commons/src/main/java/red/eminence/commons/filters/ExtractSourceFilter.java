package red.eminence.commons.filters;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import red.eminence.commons.meta.App;
import red.eminence.commons.meta.Platform;

import java.util.List;
import java.util.Objects;


@Component
@Log4j2
@Data
@RequiredArgsConstructor
@EqualsAndHashCode (callSuper = false)
public class ExtractSourceFilter extends BaseFilter implements WebFilter
{
    public static final String SOURCE_HEADER = "f-source";  // frontend source
    public static final String APP           = "app";
    public static final String PLATFORM      = "platform";
    
    @Override
    @NonNull
    public Mono<Void> filter (
            @NonNull
            ServerWebExchange exchange,
            @NonNull
            WebFilterChain chain)
    {
        //don't want to process this for actuator
        if (Objects.equals(exchange.getRequest().getURI().getRawPath(), "/actuator/health")) {
            return chain.filter(exchange);
        }
        //don't want to process this for CORS
        if (Objects.equals(exchange.getRequest().getMethod(), HttpMethod.OPTIONS)) {
            return chain.filter(exchange);
        }
        // val response = exchange.getResponse(); // don't need this
        List<String> sourceHeader = exchange.getRequest().getHeaders().getOrEmpty(SOURCE_HEADER);
        String       app          = "";
        String       platform     = "";
        if (!sourceHeader.isEmpty()) {
            String[] source = sourceHeader.get(0).split("-");
            try {
                app      = App.valueOf(source[0]).toString();
                platform = Platform.valueOf(source[1]).toString();
            }
            catch (Exception e) {
                log.error("Invalid source/platform header: " + e.getMessage());
            }
        }
        exchange.getAttributes().put(APP, app);
        exchange.getAttributes().put(PLATFORM, platform);
        return chain.filter(exchange);
    }
}