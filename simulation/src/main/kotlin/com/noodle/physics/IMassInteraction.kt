package com.noodle.physics

interface IMassInteraction {
    fun force(m1: Double, m2: Double, r: List<Double>, exponent: Int): List<Double>
}