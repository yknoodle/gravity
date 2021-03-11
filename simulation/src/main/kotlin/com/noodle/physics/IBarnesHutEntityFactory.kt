package com.noodle.physics

interface IBarnesHutEntityFactory {

    fun states(states: List<Double>): BarnesHutEntityFactory
    fun id(id: String): BarnesHutEntityFactory
    fun build(): IPointMassEntity
    fun mass(mass: Double): BarnesHutEntityFactory
}
