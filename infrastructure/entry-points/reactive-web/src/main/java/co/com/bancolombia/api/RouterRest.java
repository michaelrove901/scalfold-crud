package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/api/v1/users"), handler::findAll)
                .andRoute(POST("/api/v1/users"), handler::save)
                .andRoute(POST("/api/v1/users/update"), handler::update)
                .andRoute(GET("/api/v1/users/name"), handler::getByName)
                .andRoute(GET("/api/v1/users/id"), handler::getById);

    }
}
