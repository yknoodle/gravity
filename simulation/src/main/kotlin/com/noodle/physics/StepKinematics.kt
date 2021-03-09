package com.noodle.physics

import com.noodle.math.ArrayOperations.plus
import com.noodle.math.ArrayOperations.times
import kotlin.math.pow

object StepKinematics: IKinematics<Array<Double>, Long> {
    override fun displacement(u: Array<Double>, a: Array<Double>, t: Long): Array<Double> =
        u * t.toDouble() plus a * t.toDouble().pow(2) * 0.5
}