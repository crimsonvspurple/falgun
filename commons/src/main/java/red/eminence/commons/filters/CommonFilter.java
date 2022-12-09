//package red.eminence.commons.filters;
//
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.lang.NonNull;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//
//@Component
//@Log4j2
//@Data
//@RequiredArgsConstructor
//@EqualsAndHashCode (callSuper = false)
//public class CommonFilter extends BaseFilter implements WebFilter
//{
//    @Override
//    @NonNull
//    public Mono<Void> filter (
//            @NonNull
//                    ServerWebExchange exchange,
//            @NonNull
//                    WebFilterChain chain)
//    {
//        exchange.getResponse()
//                .getHeaders()
//                .add("Content-Type", "application/json;charset=UTF-8");
//        return chain.filter(exchange);
//    }
//}
