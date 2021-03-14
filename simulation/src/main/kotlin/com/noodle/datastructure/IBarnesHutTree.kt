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
}