package com.noodle.gravity

import com.noodle.bounding.CubeSpace
import com.noodle.datastructure.IBarnesHutTree
import com.noodle.physics.*
import com.noodle.physics.gravitation.Gravitation
import com.noodle.physics.gravitation.IGravitation
import com.noodle.math.IterableOperations.minus
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

object BarnesHutGravityCalculator : IGravitation {
    @FlowPreview
    override fun compute(
            entities: List<IPointMassEntity>,
            resolution: Long,
            exponent: Int): Flow<IForceResult> {
        val tree: IBarnesHutTree<IPointMassEntity> = BarnesHutTree(CubeSpace(resolution))
        entities.mapNotNull { tree.insert(it) }
        return tree.nodes().filter { it.nodeStates().isNotEmpty() }.asFlow().flatMapMerge { node ->
            val barnesHutResult: BarnesHutResult = tree.solve(node as IBarnesHutTree<IPointMassEntity>, exponent)
            barnesHutResult.affectNode.nodeStates().map { affect ->

                barnesHutResult.effectNode.fold(ForceResult(affect.id())) { F, effect ->
                    val r: List<Double> = affect.position() minus effect.position()
                    val forceComponents = effect.treeStates()
                            .map { it.id() to Gravitation.force(it.mass(), effect.mass(), r) }
                            .fold(mutableMapOf<String, List<Double>>()) { acc, cur ->
                                acc[cur.first] = cur.second
                                acc
                            }
                    F.components() += forceComponents
                    F
                }
            }.asFlow()
        }
    }
}