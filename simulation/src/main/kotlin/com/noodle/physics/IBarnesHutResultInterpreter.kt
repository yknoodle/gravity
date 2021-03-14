package com.noodle.physics

interface IBarnesHutResultInterpreter<T>{
    fun apply(result: IBarnesHutResult<T>): List<IForceResult>
}