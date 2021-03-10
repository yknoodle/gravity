package com.noodle.physics.gravitation

import com.noodle.physics.IPointMassEntity
import com.noodle.physics.PointMassEntity
import kotlinx.coroutines.flow.Flow

interface IGravitation {
    fun compute(entities: List<IPointMassEntity>, exponent: Int = 3): Flow<PointMassEntity>
}