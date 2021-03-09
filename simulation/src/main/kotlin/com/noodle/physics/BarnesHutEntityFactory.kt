package com.noodle.physics

import com.noodle.physics.gravitation.ICelestial
import java.util.*

class BarnesHutEntityFactory(
        private var _mass: Double = 0.0,
        private var _id: String = UUID.randomUUID().toString(),
        private var _states: List<Double> = listOf(0.0, 0.0, 0.0)
) {
    fun from(celestial: ICelestial): BarnesHutEntityFactory = this.apply {
        _mass = celestial.mass()
        _id = celestial.name()
    }
    fun states(states: List<Double>) = this.apply{ _states = states }
    fun id(id: String) = this.apply{ _id = id }
    fun build(): BarnesHutEntity = BarnesHutEntity(
            _mass,
            *_states.toDoubleArray(),
            _id=_id)
    fun from(celestial: ICelestial, vararg states: Double, id: String?=null): IBarnesHutEntity =
            BarnesHutEntity(
                    celestial.mass(),
                    *states,
                    _id = id?:celestial.name()
            )
    companion object {
        fun builder(): BarnesHutEntityFactory = BarnesHutEntityFactory()
    }
}