package com.noodle.datastructure

interface ITree<T> {
    fun insert(state: T): ITree<T>?
    fun localNodes(): List<ITree<T>>
    fun localStates(): List<T>
    fun states(): List<T>
    fun nodes(): List<ITree<T>>
    fun size(): Long
}
