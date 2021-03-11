package com.noodle.bounding

interface ISplittable {
    fun split(): Array<ISplittable>
}