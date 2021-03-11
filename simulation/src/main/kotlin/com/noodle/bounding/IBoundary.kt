package com.noodle.bounding

interface IBoundary<T, C>: ISplittable{
    fun resolution(): Long
    fun origin(): List<C>
    operator fun contains(state: List<T>): Boolean
    fun size(): Long
}