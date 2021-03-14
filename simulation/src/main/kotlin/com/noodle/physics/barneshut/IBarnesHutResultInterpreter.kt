package com.noodle.physics.barneshut

import com.noodle.physics.IForceResult
import kotlinx.coroutines.flow.Flow

interface IBarnesHutResultInterpreter<T>{
    fun apply(result: IBarnesHutResult<T>): Flow<IForceResult>
}