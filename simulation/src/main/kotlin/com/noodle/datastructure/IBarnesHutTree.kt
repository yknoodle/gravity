package com.noodle.datastructure

import com.noodle.physics.IPointMass

interface IBarnesHutTree<T> :
        ISplittableTree<T>,
        IPointMass,
        Iterable<IBarnesHutTree<T>>{
    fun force(node: IBarnesHutTree<T>, exponent: Int, theta: Double = 0.5): List<Double>
    fun occupancy(): Long
    fun edge(): Long
}