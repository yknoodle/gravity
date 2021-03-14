package com.noodle.physics.barneshut

import com.noodle.datastructure.IBarnesHutTree

interface IBarnesHutSolver<T>{
    fun solve(node: IBarnesHutTree<T>,
              root: IBarnesHutTree<T>,
              theta: Double = 0.5,
              scale: Int = 3
    ): IBarnesHutResult<T>
}