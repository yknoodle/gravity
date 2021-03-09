package com.noodle.datastructure

interface INode<T, N> {
    fun insert(state: T): N?
    fun size(): Long
    fun occupancy(): Long
}