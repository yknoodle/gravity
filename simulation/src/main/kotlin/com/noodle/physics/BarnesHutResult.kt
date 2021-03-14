package com.noodle.physics

import com.noodle.datastructure.IBarnesHutTree
import com.noodle.physics.gravitation.Gravitation

data class BarnesHutResult<T>(
        val affectNode: IBarnesHutTree<T>,
        val effectNode: List<IBarnesHutTree<T>> = mutableListOf(),
        val interaction: IMassInteraction = Gravitation
) {
    operator fun plus(other: BarnesHutResult<T>): BarnesHutResult<T> =
            BarnesHutResult(affectNode, effectNode + other.effectNode)

}
