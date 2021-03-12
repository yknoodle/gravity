package com.noodle.math

import kotlin.math.pow

object IterableOperations {
    infix fun Iterable<Number>.dot(other: Iterable<Number>): Double =
            this.zip(other) { d1, d2 -> d1.toDouble() * d2.toDouble() }.sum()
    infix fun Iterable<Number>.plus(other: Iterable<Number>): List<Double> =
            this.zip(other) { d1, d2 -> d1.toDouble() + d2.toDouble() }
    infix fun Iterable<Number>.minus(other: Iterable<Number>): List<Double> =
            this.zip(other) { d1, d2 -> d1.toDouble() - d2.toDouble() }
    fun Iterable<Number>.magnitude(): Double =
            (this dot this).pow(0.5)
    operator fun Iterable<Number>.times(other: Number): Iterable<Double> =
            this.map { it.toDouble() * other.toDouble() }
}