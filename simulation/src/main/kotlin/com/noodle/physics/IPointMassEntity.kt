package com.noodle.physics

interface IPointMassEntity : IEntity, IPointMass {
    fun velocity(dimension: Int = 3): List<Double>
    fun acceleration(dimension: Int = 3): List<Double>
    fun states(): List<Double>
}
