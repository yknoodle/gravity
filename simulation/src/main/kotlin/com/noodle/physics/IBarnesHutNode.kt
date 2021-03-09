package com.noodle.physics

interface IBarnesHutNode {
    fun aggregateMass(): Double
    fun centerOfMass(dimension: Int = 3): List<Double>
    fun force(node: IBarnesHutNode, exponent: Int, theta: Double=0.5): List<Double>
}