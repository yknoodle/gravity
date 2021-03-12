package com.noodle.physics.gravitation

import com.noodle.physics.IForceResult
import com.noodle.physics.IPointMassEntity
import com.noodle.physics.PointMassEntity
import kotlinx.coroutines.flow.Flow

interface IGravitation {
    fun compute(entities: List<IPointMassEntity>,resolution: Long = 25, exponent: Int = 3): Flow<IForceResult>
}