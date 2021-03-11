package com.noodle.physics

import com.noodle.physics.gravitation.ICelestial
import java.util.*

class BarnesHutEntityFactory(
        private var _mass: Double = 1.0,
        private var _id: String = UUID.randomUUID().toString(),
        private var _states: Array<Double> = arrayOf(0.0, 0.0, 0.0)
): IBarnesHutEntityFactory {
    fun from(celestial: ICelestial): BarnesHutEntityFactory = this.apply {
        _mass = celestial.mass()
        _id = celestial.name()
    }
    override fun states(states: List<Double>) = this.apply{ _states = states.toTypedArray() }
    override fun id(id: String) = this.apply{ _id = id }
    override fun mass(mass: Double) = this.apply { _mass = mass }
    override fun build(): IPointMassEntity = PointMassEntity(
            _mass,
            *_states.toDoubleArray(),
            _id=_id)
    companion object {
        fun builder(): BarnesHutEntityFactory = BarnesHutEntityFactory()
    }
}