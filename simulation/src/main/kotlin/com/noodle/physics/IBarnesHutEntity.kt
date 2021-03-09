package com.noodle.physics

interface IBarnesHutEntity {
    fun id(): String
    fun mass(): Double
    fun states(): List<Double>
}
