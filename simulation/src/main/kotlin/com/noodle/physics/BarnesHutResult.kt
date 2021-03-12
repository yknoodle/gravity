package com.noodle.physics

import com.noodle.datastructure.IBarnesHutTree
import com.noodle.physics.gravitation.Gravitation

data class BarnesHutResult(
        val affectNode: IBarnesHutTree<IPointMassEntity>,
        val effectNode: List<IBarnesHutTree<IPointMassEntity>> = mutableListOf(),
        val interaction: IMassInteraction = Gravitation
) {
    operator fun plus(other: BarnesHutResult): BarnesHutResult =
            BarnesHutResult(affectNode, effectNode + other.effectNode)

}
