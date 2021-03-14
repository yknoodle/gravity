package com.noodle.physics

import com.noodle.math.IterableOperations.minus
import com.noodle.physics.gravitation.Gravitation

object BarnesHutResultInterpreter : IBarnesHutResultInterpreter {
    fun f(barnesHutResult: IBarnesHutResult<IPointMassEntity>): List<IForceResult> =
            barnesHutResult.affected().localStates().map { affected ->
                barnesHutResult.effector().fold(ForceResult(affected.id())) { F, effector ->
                    val r: List<Double> = affected.position() minus effector.position()
                    val forceComponents = effector.states()
                            .map { it.id() to Gravitation.force(affected.mass(), it.mass(), r) }
                            .fold(mutableMapOf<String, List<Double>>()) { acc, cur ->
                                acc[cur.first] = cur.second
                                acc
                            }
                    F.components() += forceComponents
                    F
                }
            }
}