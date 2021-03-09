package com.noodle.physics.gravitation

import com.noodle.math.IterableOperations.dot
import com.noodle.math.IterableOperations.magnitude
import com.noodle.physics.IMassInteraction
import kotlin.math.pow

object Gravitation: IMassInteraction {
    private var i: Long = 0
    private const val constant: Double = 6.6743e-11
    override fun force(m1: Double, m2: Double, r: List<Double>, exponent: Int): List<Double> {
        i++
        val direction: List<Double> = r.map{ it / r.magnitude() }
        val scalar: Double = constant * m1 * m2 / (r dot r) / 10.0.pow(exponent*2)
        return direction.map { it*scalar }
    }
    fun computations(): Long = i
    fun reset() = run { i = 0 }
    fun constant(): Double = constant
}