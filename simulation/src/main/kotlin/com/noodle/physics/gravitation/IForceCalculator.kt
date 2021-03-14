package com.noodle.physics.gravitation

import com.noodle.physics.IForceResult
import com.noodle.physics.IPointMassEntity
import kotlinx.coroutines.flow.Flow

interface IForceCalculator {
    fun compute(entities: List<IPointMassEntity>, resolution: Long = 25, scale: Int = 3): Flow<IForceResult>
}