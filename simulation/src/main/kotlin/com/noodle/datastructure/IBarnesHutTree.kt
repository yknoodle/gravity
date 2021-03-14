package com.noodle.datastructure

import com.noodle.bounding.ISplittable
import com.noodle.physics.IPointMass

interface IBarnesHutTree<T> :
        ISplittable<IBarnesHutTree<T>>,
        ITree<T>,
        IPointMass {
    fun occupancy(): Long
    fun edge(): Long
    override fun nodes(): List<IBarnesHutTree<T>>
    override fun localNodes(): List<IBarnesHutTree<T>>
}