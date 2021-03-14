package com.noodle.physics.barneshut

import com.noodle.datastructure.IBarnesHutTree

data class BarnesHutResult<T>(
        val affectedNode: IBarnesHutTree<T>,
        val effectorNode: List<IBarnesHutTree<T>> = mutableListOf()
) : IBarnesHutResult<T> {
    override operator fun plus(other: IBarnesHutResult<T>): BarnesHutResult<T> =
            BarnesHutResult(affectedNode, effectorNode + other.effector())

    override fun effector(): List<IBarnesHutTree<T>> = effectorNode

    override fun affected(): IBarnesHutTree<T> = affectedNode

}
