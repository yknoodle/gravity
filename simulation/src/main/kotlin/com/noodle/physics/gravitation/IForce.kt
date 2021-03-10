package com.noodle.physics.gravitation

import com.noodle.physics.IEntity

interface IForce: IEntity {
    fun components(): Map<String, Array<Double>>
    fun net(): Array<Double>
}
