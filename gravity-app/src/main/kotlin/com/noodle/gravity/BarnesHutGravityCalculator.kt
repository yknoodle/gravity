package com.noodle.gravity

import com.noodle.bounding.CubeSpace
import com.noodle.datastructure.IBarnesHutTree
import com.noodle.physics.*
import com.noodle.physics.barneshut.*
import com.noodle.physics.gravitation.Gravitation
import com.noodle.physics.gravitation.IForceCalculator
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

class BarnesHutGravityCalculator(
        private val _solver: IBarnesHutSolver<IPointMassEntity> = BarnesHutSolver,
        private val _interpreter: IBarnesHutResultInterpreter<IPointMassEntity> = BarnesHutResultInterpreter(Gravitation)
) : IForceCalculator {
    @FlowPreview
    override fun compute(
            entities: List<IPointMassEntity>,
            scale: Int,
    ): Flow<IForceResult> {
        val tree: IBarnesHutTree<IPointMassEntity> = BarnesHutTree(CubeSpace(50L))
        entities.mapNotNull { tree.insert(it) }
        return tree.nodes().asFlow()
                .filter { it.localStates().isNotEmpty() }
                .map { _solver.solve(it, tree, scale = scale) }
                .flatMapMerge { _interpreter.apply(it) }
    }

}