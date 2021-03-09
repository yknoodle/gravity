package com.noodle.gravity.handler

import com.noodle.physics.gravitation.Gravitation
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.queryParamOrNull
import reactor.core.publisher.Mono

@Component
class CalculationHandler {
    fun force(req: ServerRequest): Mono<ServerResponse> {
        val m1: Double = req.queryParamOrNull("m1")?.toDouble()?:0.0
        val m2: Double = req.queryParamOrNull("m2")?.toDouble()?:0.0
        val exponent: Int = req.queryParamOrNull("exponent")?.toInt()?:0
        val r: Array<Double> = arrayOf(
                req.queryParamOrNull("r0")?.toDouble()?:0.0,
                req.queryParamOrNull("r1")?.toDouble()?:0.0,
                req.queryParamOrNull("r2")?.toDouble()?:0.0
        )
        val force: List<Double> = Gravitation.force(m1, m2, r.toList(), exponent)
        return ServerResponse.ok().body(BodyInserters.fromValue(force))
    }
}
