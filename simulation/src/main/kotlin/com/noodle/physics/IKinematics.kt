package com.noodle.physics

interface IKinematics<U, T> {
    fun displacement(u: U, a: U, t: T): U
}