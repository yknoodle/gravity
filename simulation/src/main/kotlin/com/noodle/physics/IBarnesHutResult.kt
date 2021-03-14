package com.noodle.physics

import com.noodle.datastructure.IBarnesHutTree

interface IBarnesHutResult<T> {

    operator fun plus(other: IBarnesHutResult<T>): IBarnesHutResult<T>
    fun effector(): List<IBarnesHutTree<T>>
    fun affected(): IBarnesHutTree<T>
}
