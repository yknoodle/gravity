package com.noodle.physics

interface IPointMassEntityFactory {

    fun states(states: List<Double>): PointMassEntityFactory
    fun id(id: String): PointMassEntityFactory
    fun build(): IPointMassEntity
    fun mass(mass: Double): PointMassEntityFactory
}
