package com.noodle.gravity

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GravityApplication

fun main(args: Array<String>) {
    runApplication<GravityApplication>(*args)
}
