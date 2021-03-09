package com.noodle.gravity

import com.noodle.physics.IBarnesHutEntity
import com.noodle.physics.BarnesHutTree
import com.noodle.datastructure.ISpaceTree
import com.noodle.physics.BarnesHutEntity
import com.noodle.physics.gravitation.Gravitation
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map

object GravityCalculator {
    @FlowPreview
    fun compute(
            entity: List<IBarnesHutEntity>,
            exponent: Int=3,
            tree: ISpaceTree<IBarnesHutEntity> = BarnesHutTree(Gravitation)): Flow<IBarnesHutEntity> {
        entity.mapNotNull { tree.insert(it) }
        return tree.nodes().asFlow().flatMapMerge {
            val force = tree.force(it, exponent)
            it.nodeStates().asFlow().map { state -> BarnesHutEntity(
                    state.mass(),
                    *force.toDoubleArray(),
                    _id = state.id()) }
        }
    }
}