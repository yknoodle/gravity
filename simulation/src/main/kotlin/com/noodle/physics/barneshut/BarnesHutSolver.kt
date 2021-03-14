package com.noodle.physics.barneshut

import com.noodle.datastructure.IBarnesHutTree
import com.noodle.math.ArrayOperations.magnitude
import com.noodle.physics.IPointMassEntity

object BarnesHutSolver : IBarnesHutSolver<IPointMassEntity> {
    override fun solve(
            node: IBarnesHutTree<IPointMassEntity>,
            root: IBarnesHutTree<IPointMassEntity>,
            theta: Double,
            scale: Int
    ): IBarnesHutResult<IPointMassEntity> {
        if (node == root) return BarnesHutResult(node)

        val occupancy: Long = root.occupancy()

        if (occupancy == 0L) return BarnesHutResult(node)

        val r: List<Double> = node.position().zip(root.position()) { r1, r2 -> r1 - r2 }
        val quotient: Double = root.edge() / r.toTypedArray().magnitude()

        if (occupancy > 0 && (quotient <= theta || root.nodes().isEmpty()))
            return BarnesHutResult(node, mutableListOf(root))

        if (root.nodes().isNotEmpty() && occupancy > 0)
            return root.localNodes()
                    .map { solve(node, it, theta, scale) }
                    .reduce { acc, component -> acc + component }
        return BarnesHutResult(node)
    }
}