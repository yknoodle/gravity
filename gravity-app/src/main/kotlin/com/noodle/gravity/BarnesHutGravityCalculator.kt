package com.noodle.gravity

import com.noodle.physics.IPointMassEntity
import com.noodle.physics.BarnesHutTree
import com.noodle.datastructure.IBarnesHutTree
import com.noodle.math.ArrayOperations.times
import com.noodle.physics.PointMassEntity
import com.noodle.physics.gravitation.Gravitation
import com.noodle.physics.gravitation.IForce
import com.noodle.physics.gravitation.IGravitation
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

object BarnesHutGravityCalculator : IGravitation {
    @FlowPreview
    override fun compute(
            entities: List<IPointMassEntity>,
            exponent: Int): Flow<PointMassEntity> {
        val tree: IBarnesHutTree<IPointMassEntity> = BarnesHutTree(Gravitation)
        entities.mapNotNull { tree.insert(it) }
        return tree.asSequence().asFlow().flatMapMerge { node ->
            val force: List<Double> = tree.force(tree, exponent)
//            node.nodeStates().scan(mutableMapOf<String, List<Array<Double>>>()) { acc, state ->
//                val forceComponents: List<Array<Double>> = components.map { component -> component.toTypedArray() * (state.mass() / node.mass())}
//                acc[state.id()] = forceComponents
//                acc
//            }.reduce { acc, mutableMap ->  acc.putAll(mutableMap)}.map { Force(it.) }
//        }
            node.nodeStates().asFlow().map { state ->
                PointMassEntity(
                        state.mass(),
                        *force.toDoubleArray(),
                        _id = state.id())
            }
        }
    }
}