package com.noodle.physics

import com.noodle.math.IterableOperations.plus
import com.noodle.math.IterableOperations.times
import kotlin.math.pow

object StepKinematics : IKinematics<List<Double>, Long> {
    override fun displacement(u: List<Double>, a: List<Double>, t: Long): List<Double> =
            (u * t.toDouble()) plus (a * t.toDouble().pow(2) * 0.5)

    override fun velocity(u: List<Double>, a: List<Double>, t: Long): List<Double> =
            u plus (a * t.toDouble())
}