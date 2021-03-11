package com.noodle.datastructure

import com.noodle.bounding.ISplittable
import com.noodle.physics.IPointMass

interface IBarnesHutTree<T> :
        ITree<T>,
        ISplittable,
        IPointMass {
    fun force(node: IBarnesHutTree<T>, exponent: Int, theta: Double = 0.5): List<Double>
    fun occupancy(): Long
}