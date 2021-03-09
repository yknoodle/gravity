package com.noodle.bounding

import kotlin.math.pow

data class CubeSpace(
        private val _resolution: Long,
        private val _origin: List<Long> = listOf(0, 0, 0)
) : IBoundary<Double, Long> {
    override fun split(): Array<IBoundary<Double, Long>> {
        val newResolution = _resolution - 1
        if (newResolution < 0) {
            return arrayOf()
        }
        val newSize: Long = 2.0.pow(newResolution.toInt()).toLong()
        return steps
                .map { it.map { x -> newSize * x } }
                .map { it.zip(_origin) { x1, x2 -> x1 + x2 } }
                .map { CubeSpace(newResolution, it) }
                .toTypedArray()
    }

    override operator fun contains(state: List<Double>): Boolean {
        val interval: Long = size()
        return state.zip(_origin).map {
            val upper: Long = it.second + interval
            val lower: Long = it.second
            lower <= it.first && it.first < upper
        }.all { it }
    }

    override fun toString(): String =
            "resolution: ${_resolution}, origin: (${_origin[0]}, ${_origin[1]}, ${_origin[2]})"

    override fun resolution(): Long = _resolution
    override fun origin(): List<Long> = _origin
    override fun size(): Long = 2.0.pow(_resolution.toInt()).toLong()

    companion object CubeSpace {
        val steps = listOf(
                listOf(0, 0, 0), listOf(0, 0, 1), listOf(0, 1, 0), listOf(1, 0, 0),
                listOf(1, 0, 1), listOf(1, 1, 0), listOf(0, 1, 1), listOf(1, 1, 1)
        )
    }
}

