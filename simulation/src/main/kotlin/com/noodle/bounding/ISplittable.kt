package com.noodle.bounding

interface ISplittable<T> {
    fun split(): List<T>
}