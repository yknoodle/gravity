package com.noodle.bounding

interface ISplittable<T> {
    fun split(): Array<T>
}