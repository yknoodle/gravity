package com.noodle.physics

import com.noodle.physics.gravitation.ICelestial

object Moon: ICelestial {
    override fun mass(): Double = 7.34767309e22
    override fun name(): String = "Moon"
}

object Earth: ICelestial {
    override fun name(): String = "Earth"
    override fun mass(): Double = 5.972e24
}

