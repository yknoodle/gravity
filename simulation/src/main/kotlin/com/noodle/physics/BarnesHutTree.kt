package com.noodle.physics

import com.noodle.bounding.CubeSpace
import com.noodle.bounding.IBoundary
import com.noodle.datastructure.IBarnesHutTree
import com.noodle.math.ArrayOperations.magnitude

class BarnesHutTree(
        private val _interaction: IMassInteraction,
        private val _boundary: IBoundary<Double, Long> = CubeSpace(50L),
        private val _capacity: Int = 1,
        private val _states: MutableList<IPointMassEntity> = mutableListOf(),
        private val _nodes: MutableList<IBarnesHutTree<IPointMassEntity>> = mutableListOf()
) : IBarnesHutTree<IPointMassEntity> {
    override fun insert(state: IPointMassEntity): IBarnesHutTree<IPointMassEntity>? {
        if (_states.size < _capacity && state.position() in _boundary && _nodes.isEmpty()) {
            _states.add(state)
            return this
        }
        if (state.position() in _boundary && _nodes.isNotEmpty())
            for (node in _nodes) if (node.insert(state) != null) return this
        if (_states.size >= _capacity && state.position() in _boundary && split().isNotEmpty()) {
            while (_states.isNotEmpty()) {
                val popped: IPointMassEntity = _states.removeFirst()
                for (node in _nodes) if (node.insert(popped) != null) break
            }
            for (node in _nodes) if (node.insert(state) != null) return this
        }
        if (_states.size >= _capacity && state.position() in _boundary && split().isEmpty()) {
            _states.add(state)
            return this
        }
        return null
    }

    override fun treeStates(): List<IPointMassEntity> =
            _states + _nodes.flatMap { it.treeStates() }

    override fun size(): Long =
            _nodes.size + _nodes.map { it.size() }.sum()

    override fun occupancy(): Long =
            _states.size + _nodes.map { it.occupancy() }.sum()

    override fun split(): Array<IBarnesHutTree<IPointMassEntity>> {
        if (_nodes.isNotEmpty()) return _nodes.toTypedArray()
        val splitBoundaries: Array<IBoundary<Double, Long>> = _boundary.split()
        if (splitBoundaries.isEmpty()) return emptyArray()
        _boundary.split()
                .map { BarnesHutTree(_interaction, it) }
                .onEach { _nodes.add(it) }
        return _nodes.toTypedArray()
    }

    override fun mass(): Double =
            _states.map { it.mass() }.sum() + _nodes.map { it.mass() }.sum()

    override fun position(dimension: Int): List<Double> {
        if (treeStates().isEmpty()) return (0 until dimension).map { 0.0 }.toList()
        val totalMass: Double = treeStates().filter { it.position().size==dimension }
                .map{it.mass()}.sum()
        return treeStates().filter { it.position().size==dimension }
                .map{it.position().map { x -> x*it.mass()/totalMass } }
                .reduce{s1, s2->s1.zip(s2){ x1, x2 -> x1+x2 }}
    }
    override fun toString(): String = "$_boundary, $_nodes"
    override fun force(node: IBarnesHutTree<IPointMassEntity>, exponent: Int, theta: Double): List<Double> {
        if (node == this) return listOf(0.0, 0.0, 0.0)

        val occupancy: Long = occupancy()

        if (occupancy == 0L) return listOf(0.0, 0.0, 0.0)

        val r: List<Double> = node.position().zip(this.position()) { r1, r2 -> r1 - r2 }
        val quotient: Double = this._boundary.size() / r.toTypedArray().magnitude()

        if (occupancy > 0 && (quotient <= theta || _nodes.isEmpty()))
            return _interaction.force( node.mass(), mass(), r, exponent )

        if (_nodes.isNotEmpty() && occupancy > 0)
            return _nodes
                    .map { it.force(node, exponent) }
                    .reduce { acc, component -> acc.zip(component) { x1, x2 -> x1 + x2 } }
        return listOf(0.0, 0.0, 0.0)
    }

    override fun children(): List<IBarnesHutTree<IPointMassEntity>> = _nodes.toList()

    override fun nodeStates(): List<IPointMassEntity> = _states.toList()

    override fun iterator(): Iterator<IBarnesHutTree<IPointMassEntity>> = (_nodes+_nodes.flatMap { it.children() }).iterator()

}
