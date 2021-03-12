package com.noodle.datastructure

import com.noodle.physics.BarnesHutResult
import com.noodle.physics.IPointMass

interface IBarnesHutTree<T> :
        ISplittableTree<T>,
        IPointMass {
    fun solve(node: IBarnesHutTree<T>, exponent: Int, theta: Double = 0.5): BarnesHutResult
    fun occupancy(): Long
    fun edge(): Long
}