package com.noodle.gravity

import com.noodle.gravity.handler.CalculationHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class Controller {
    @Bean
    fun route(calculationHandler: CalculationHandler): RouterFunction<ServerResponse> = RouterFunctions.route()
            .path("/gravity") { context ->
                context
                        .GET("/up") {
                            ServerResponse.ok().body(BodyInserters
                                    .fromValue("ok"))
                        }
                        .path("/calculation") { l1 ->
                            l1
                                    .GET("/up") {
                                        ServerResponse.ok().body(BodyInserters
                                                .fromValue("ok"))
                                    }
                                    .GET("/force", RequestPredicates.queryParam("m0") { true }
                                            .and(RequestPredicates.queryParam("m1") { true }
                                            .and(RequestPredicates.queryParam("r0") { true })),
                                            calculationHandler::force)
                        }
            }.build()
}