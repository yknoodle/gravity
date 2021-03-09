package com.noodle.physics

import com.noodle.bounding.CubeSpace
import com.noodle.bounding.IBoundary
import com.noodle.datastructure.ISpaceTree
import com.noodle.math.ArrayOperations.magnitude

open class BarnesHutTree(
        private val _interaction: IMassInteraction,
        private val _boundary: IBoundary<Double, Long> = CubeSpace(50L),
        private val _capacity: Int = 1,
        private val _states: MutableList<IBarnesHutEntity> = mutableListOf(),
        private val _nodes: MutableList<ISpaceTree<IBarnesHutEntity>> = mutableListOf()
) : ISpaceTree<IBarnesHutEntity> {
    override fun insert(state: IBarnesHutEntity): ISpaceTree<IBarnesHutEntity>? {
        if (_states.size < _capacity && state.states() in _boundary && _nodes.isEmpty()) {
            _states.add(state)
            return this
        }
        if (state.states() in _boundary && _nodes.isNotEmpty())
            for (node in _nodes) if (node.insert(state) != null) return this
        if (_states.size >= _capacity && state.states() in _boundary && split().isNotEmpty()) {
            while (_states.isNotEmpty()) {
                val popped: IBarnesHutEntity = _states.removeFirst()
                for (node in _nodes) if (node.insert(popped) != null) break
            }
            for (node in _nodes) if (node.insert(state) != null) return this
        }
        if (_states.size >= _capacity && state.states() in _boundary && split().isEmpty()) {
            _states.add(state)
            return this
        }
        return null
    }

    override fun treeStates(): List<IBarnesHutEntity> =
            _states + _nodes.flatMap { it.treeStates() }

    override fun nodes(): List<ISpaceTree<IBarnesHutEntity>> =
            _nodes + _nodes.flatMap { it.nodes() }

    override fun size(): Long =
            _nodes.size + _nodes.map { it.size() }.sum()

    override fun occupancy(): Long =
            _states.size + _nodes.map { it.occupancy() }.sum()

    override fun split(): Array<ISpaceTree<IBarnesHutEntity>> {
        if (_nodes.isNotEmpty()) return _nodes.toTypedArray()
        val splitBoundaries: Array<IBoundary<Double, Long>> = _boundary.split()
        if (splitBoundaries.isEmpty()) return emptyArray()
        _boundary.split()
                .map { BarnesHutTree(_interaction, it) }
                .onEach { _nodes += it }
        return _nodes.toTypedArray()
    }

    override fun aggregateMass(): Double =
            _states.map { it.mass() }.sum() + _nodes.map { it.aggregateMass() }.sum()

    override fun centerOfMass(dimension: Int): List<Double> {
        if (treeStates().isEmpty()) return (0 until dimension).map { 0.0 }.toList()
        val totalMass: Double = treeStates().filter { it.states().size==dimension }
                .map{it.mass()}.sum()
        return treeStates().filter { it.states().size==dimension }
                .map{it.states().map { x -> x*it.mass()/totalMass } }
                .reduce{s1, s2->s1.zip(s2){ x1, x2 -> x1+x2 }}
    }
    override fun toString(): String = "$_boundary, $_nodes"
    override fun force(node: IBarnesHutNode, exponent: Int, theta: Double): List<Double> {
        if (node == this) return listOf(0.0, 0.0, 0.0)

        val occupancy: Long = occupancy()

        if (occupancy == 0L) return listOf(0.0, 0.0, 0.0)

        val r: List<Double> = node.centerOfMass().zip(this.centerOfMass()) { r1, r2 -> r1 - r2 }
        val quotient: Double = this._boundary.size() / r.toTypedArray().magnitude()

        if (occupancy > 0 && (quotient <= theta || _nodes.isEmpty()))
            return _interaction.force( node.aggregateMass(), aggregateMass(), r, exponent )

        if (_nodes.isNotEmpty() && occupancy > 0)
            return _nodes
                    .map { it.force(node, exponent) }
                    .reduce { acc, component -> acc.zip(component) { x1, x2 -> x1 + x2 } }
        return listOf(0.0, 0.0, 0.0)
    }

    override fun nodeStates(): List<IBarnesHutEntity> {
        return _states.toList()
    }
}
