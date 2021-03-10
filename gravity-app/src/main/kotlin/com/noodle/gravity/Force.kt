package com.noodle.gravity

import com.noodle.physics.gravitation.IForce
import com.noodle.math.ArrayOperations.plus

data class Force(
        private val _id: String,
        private val _components: Map<String, Array<Double>> = mapOf()) : IForce {
    override fun components(): Map<String, Array<Double>> = _components.toMap()

    override fun net(): Array<Double> = _components.values.reduce { acc, doubles ->  acc plus doubles}

    override fun id(): String {
        TODO("Not yet implemented")
    }

}
