package com.noodle.physics

import com.noodle.physics.gravitation.ICelestial
import java.util.*

class PointMassEntityFactory(
        private var _mass: Double = 1.0,
        private var _id: String = UUID.randomUUID().toString(),
        private var _states: List<Double> = listOf(0.0, 0.0, 0.0)
): IPointMassEntityFactory {
    fun from(celestial: ICelestial): PointMassEntityFactory = this.apply {
        _mass = celestial.mass()
        _id = celestial.name()
    }
    fun from(pointMassEntity: IPointMassEntity): PointMassEntityFactory = this.apply {
        _mass = pointMassEntity.mass()
        _id = pointMassEntity.id()
        _states = pointMassEntity.states()
    }
    override fun states(states: List<Double>) = this.apply{ _states = states }
    override fun id(id: String) = this.apply{ _id = id }
    override fun mass(mass: Double) = this.apply { _mass = mass }
    override fun build(): IPointMassEntity = PointMassEntity(
            _mass,
            *_states.toDoubleArray(),
            _id=_id)
    companion object {
        fun builder(): PointMassEntityFactory = PointMassEntityFactory()
    }
}