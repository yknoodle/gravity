package com.noodle.physics

interface IForceCalculator<T> {
    fun calculate(entities: List<T>): IForceResult
}