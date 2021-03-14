package com.noodle.datastructure

import com.noodle.physics.BarnesHutResult
import com.noodle.physics.IPointMass

interface IBarnesHutTree<T> :
        ISplittableTree<T>,
        IPointMass {
    fun occupancy(): Long
    fun edge(): Long
    override fun nodes(): List<IBarnesHutTree<T>>
    override fun localNodes(): List<IBarnesHutTree<T>>
    fun solve(node: IBarnesHutTree<T>, theta: Double = 0.5, scale: Int): BarnesHutResult<T>
}