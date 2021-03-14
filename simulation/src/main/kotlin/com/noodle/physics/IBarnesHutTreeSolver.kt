package com.noodle.physics

import com.noodle.datastructure.IBarnesHutTree

interface IBarnesHutTreeSolver<T>{
    fun solve(node: IBarnesHutTree<T>,
              root: IBarnesHutTree<T>,
              theta: Double = 0.5,
              scale: Int = 3
    ): BarnesHutResult<T>
}