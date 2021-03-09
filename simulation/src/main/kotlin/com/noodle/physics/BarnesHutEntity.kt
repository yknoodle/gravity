package com.noodle.physics

import java.util.*

class BarnesHutEntity(
        private val _mass: Double,
        vararg states: Double,
        private val _id: String=UUID.randomUUID().toString()):
        IBarnesHutEntity {
    private val _states: List<Double> = states.toList()
    override fun id(): String = _id
    override fun mass(): Double = _mass
    override fun states(): List<Double> = _states.toList()
    override fun toString(): String =
            "id: $_id, mass: $_mass, states: $_states"
}