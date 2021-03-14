package com.noodle.bounding

interface IBoundary<T, C>: ISplittable<IBoundary<T,C>>{
    fun resolution(): Long
    fun origin(): List<C>
    operator fun contains(state: List<T>): Boolean
    fun size(): Long
}