package com.noodle.gravity

import com.noodle.bounding.CubeSpace
import com.noodle.datastructure.IBarnesHutTree
import com.noodle.physics.*
import com.noodle.physics.gravitation.IGravitation
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

object BarnesHutGravityCalculator : IGravitation {
    @FlowPreview
    override fun compute(
            entities: List<IPointMassEntity>,
            resolution: Long,
            scale: Int): Flow<IForceResult> {
        val tree: IBarnesHutTree<IPointMassEntity> = BarnesHutTree(CubeSpace(resolution))
        val interpreter: IBarnesHutResultInterpreter<IPointMassEntity> = BarnesHutResultInterpreter()
        entities.mapNotNull { tree.insert(it) }
        return tree.nodes().asFlow()
                .filter { it.localStates().isNotEmpty() }
                .map { BarnesHutTreeSolver.solve(it, tree) }
                .flatMapMerge { interpreter.apply(it).asFlow() }
    }

}