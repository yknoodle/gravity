package com.noodle.physics

import com.noodle.math.IterableOperations.minus
import com.noodle.physics.gravitation.Gravitation

class BarnesHutResultInterpreter(
        private val _interaction: IMassInteraction = Gravitation
) : IBarnesHutResultInterpreter<IPointMassEntity> {
    override fun apply(result: IBarnesHutResult<IPointMassEntity>): List<IForceResult> =
            result.affected().localStates().map { affected ->
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