package com.noodle.physics

interface IPointEntityFactory {

    fun states(states: List<Double>): PointEntityFactory
    fun id(id: String): PointEntityFactory
    fun build(): IPointMassEntity
    fun mass(mass: Double): PointEntityFactory
}
