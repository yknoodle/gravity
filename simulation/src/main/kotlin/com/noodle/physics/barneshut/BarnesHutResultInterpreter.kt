package com.noodle.physics.barneshut

import com.noodle.math.IterableOperations.minus
import com.noodle.physics.ForceResult
import com.noodle.physics.IForceResult
import com.noodle.physics.IMassInteraction
import com.noodle.physics.IPointMassEntity
import com.noodle.physics.gravitation.Gravitation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

class BarnesHutResultInterpreter(
        private val _interaction: IMassInteraction = Gravitation
) : IBarnesHutResultInterpreter<IPointMassEntity> {
    override fun apply(result: IBarnesHutResult<IPointMassEntity>): Flow<IForceResult> =
            result.affected().localStates().asFlow().map { affected ->
                result.effector().fold(ForceResult(affected.id())) { F, effector ->
                    val r: List<Double> = affected.position() minus effector.position()
                    val forceComponents = effector.states()
                            .map { it.id() to _interaction.force(affected.mass(), it.mass(), r) }
                            .fold(mutableMapOf<String, List<Double>>()) { acc, cur ->
                                acc[cur.first] = cur.second
                                acc
                            }
                    F.components() += forceComponents
                    F
                }
            }
}