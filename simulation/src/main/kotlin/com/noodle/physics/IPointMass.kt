package com.noodle.physics

interface IPointMass {
    fun mass(): Double
    fun position(dimension: Int = 3): List<Double>
}
