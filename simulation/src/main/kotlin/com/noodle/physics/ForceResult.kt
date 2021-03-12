package com.noodle.physics

class ForceResult(
        private val id: String,
        private val components: MutableMap<String, List<Double>> = mutableMapOf()
) : IForceResult{
    override fun components(): MutableMap<String, List<Double>> = components
    override fun id(): String = id

}
