package com.noodle.physics.gravitation

object Moon: ICelestial {
    override fun name(): String = "Moon"
    override fun mass(): Double = 7.34767309e22
}

object Earth: ICelestial {
    override fun name(): String = "Earth"
    override fun mass(): Double = 5.972e24
}

