package com.noodle.physics

import com.noodle.bounding.CubeSpace
import com.noodle.bounding.IBoundary
import com.noodle.bounding.ISplittable
import com.noodle.datastructure.IBarnesHutTree

class BarnesHutTree(
        private val _boundary: IBoundary<Double, Long> = CubeSpace(50L),
        private val _capacity: Int = 1,
        private val _states: MutableList<IPointMassEntity> = mutableListOf(),
        private val _nodes: MutableList<IBarnesHutTree<IPointMassEntity>> = mutableListOf(),
        private val _solver: IBarnesHutTreeSolver<IPointMassEntity> = BarnesHutTreeSolver()
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

    override fun states(): List<IPointMassEntity> =
            _states + _nodes.flatMap { it.states() }

    override fun size(): Long =
            _nodes.size + _nodes.map { it.size() }.sum()

    override fun occupancy(): Long =
            _states.size + _nodes.map { it.occupancy() }.sum()

    override fun nodes(): List<IBarnesHutTree<IPointMassEntity>> =
            _nodes + _nodes.flatMap { (it as BarnesHutTree).nodes() }

    override fun split(): Array<ISplittable> {
        if (_nodes.isNotEmpty()) return _nodes.toTypedArray()
        val splitBoundaries: Array<ISplittable> = _boundary.split()
        if (splitBoundaries.isEmpty()) return emptyArray()
        _boundary.split()
                .map { BarnesHutTree(it as CubeSpace) }
                .onEach { _nodes.add(it) }
        return _nodes.toTypedArray()
    }

    override fun mass(): Double =
            _states.map { it.mass() }.sum() + _nodes.map { it.mass() }.sum()

    override fun position(dimension: Int): List<Double> {
        if (states().isEmpty()) return (0 until dimension).map { 0.0 }.toList()
        val totalMass: Double = states().filter { it.position().size == dimension }
                .map { it.mass() }.sum()
        return states().filter { it.position().size == dimension }
                .map { it.position().map { x -> x * it.mass() / totalMass } }
                .reduce { s1, s2 -> s1.zip(s2) { x1, x2 -> x1 + x2 } }
    }

    override fun toString(): String = "$_boundary, nodes: $_nodes, states: $_states"

    override fun localNodes(): List<IBarnesHutTree<IPointMassEntity>> = _nodes.toList()

    override fun localStates(): List<IPointMassEntity> = _states.toList()

    override fun edge(): Long = _boundary.size()

    override fun solve(
            node: IBarnesHutTree<IPointMassEntity>,
            theta: Double, scale: Int): BarnesHutResult<IPointMassEntity> =
            _solver.solve(node, this, theta, scale)

}
