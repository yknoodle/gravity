package com.noodle.datastructure

import com.noodle.bounding.ISplittable
import com.noodle.physics.IBarnesHutNode

interface ISpaceTree<T>:
        INode<T, ISpaceTree<T>>,
        ISplittable<ISpaceTree<T>>,
        IBarnesHutNode {
    fun nodeStates(): List<T>
    fun treeStates(): List<T>
    fun nodes(): List<ISpaceTree<T>>
}