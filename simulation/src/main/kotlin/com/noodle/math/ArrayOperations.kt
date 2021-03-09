package com.noodle.math

import kotlin.math.pow

object ArrayOperations {
    infix fun Array<Double>.dot(other: Array<Double>): Double =
            this.zip(other) { d1, d2 -> d1 * d2 }.sumByDouble { it }

    infix fun Array<Double>.plus(other: Array<Double>): Array<Double> =
            this.zip(other) { d1, d2 -> d1 + d2 }.toTypedArray()
    fun Array<Double>.magnitude(): Double =
            (this dot this).pow(0.5)
    operator fun Array<Double>.times(other: Double): Array<Double> =
            this.map { it * other }.toTypedArray()
}