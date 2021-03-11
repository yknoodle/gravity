package com.noodle.datastructure

interface ITree<T> {
    fun insert(state: T): ITree<T>?
    fun children(): List<ITree<T>>
    fun nodeStates(): List<T>
    fun treeStates(): List<T>
    fun size(): Long
}
