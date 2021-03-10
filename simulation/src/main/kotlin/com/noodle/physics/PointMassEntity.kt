package com.noodle.physics

import java.util.*

class PointMassEntity(
        private val _mass: Double,
        vararg states: Double,
        private val _id: String=UUID.randomUUID().toString()):
        IPointMassEntity {
    private val _states: Array<Double> = states.toTypedArray()
    override fun id(): String = _id
    override fun mass(): Double = _mass
    override fun position(dimension: Int): List<Double> = _states.toList()
    override fun toString(): String =
            "{id: $_id, mass: $_mass, position: ${_states.toList()}}"
}