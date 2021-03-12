package com.noodle.physics

interface IForceResult: IEntity {
    fun components(): Map<String, List<Double>>
}
