package com.noodle.physics

import java.util.*

class PointMassEntity(
        private val _mass: Double,
        vararg states: Double,
        private val _id: String=UUID.randomUUID().toString()):
        IPointMassEntity {
    private val _states: Array<Double> = Array(State.values().size*3){
        if (it < states.size) states[it]
        else 0.0
    }
    override fun position(dimension: Int): List<Double> = _states.slice(STATE_RANGES[State.POSITION]!!)
    override fun velocity(dimension: Int): List<Double> = _states.slice(STATE_RANGES[State.VELOCITY]!!)
    override fun acceleration(dimension: Int): List<Double> = _states.slice(STATE_RANGES[State.ACCELERATION]!!)
    override fun states(): List<Double> = _states.toList()

    override fun id(): String = _id
    override fun mass(): Double = _mass
    override fun toString(): String =
            "{id: $_id, mass: $_mass, states: ${_states.toList()}}"
    companion object StaticProperties {
        enum class State{
            POSITION,
            VELOCITY,
            ACCELERATION
        }
        val STATE_RANGES: Map<State, IntRange> = mapOf(
                State.POSITION to (0 until 3),
                State.VELOCITY to (3 until 6),
                State.ACCELERATION to (6 until 9),
        )
    }
}