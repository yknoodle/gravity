package com.noodle

import com.noodle.physics.BarnesHutTree
import com.noodle.bounding.CubeSpace
import com.noodle.physics.IPointMassEntity
import com.noodle.datastructure.IBarnesHutTree
import com.noodle.physics.gravitation.Gravitation
import com.noodle.physics.BarnesHutEntityFactory
import com.noodle.physics.PointMassEntity
import org.junit.Test
import kotlin.math.pow
import kotlin.random.Random
import kotlin.test.assertTrue

class BarnesHutTreeTests {
    @Test
    fun canInsert() {
        val tree = BarnesHutTree(Gravitation, CubeSpace(15))
        tree.insert(PointMassEntity(10.0, 1.0, 1.0, 1.0))
        tree.insert(PointMassEntity(10.0, 1.0, 1.0, 2.0))
        println("$tree")
        assertTrue("expected 2 but got ${tree.occupancy()}") {
            tree.occupancy() == 2L
        }
    }

    @Test
    fun insertMany() {
        val resolution = 10L
        val tree = BarnesHutTree(Gravitation, CubeSpace(resolution))
        val inserts = 500
        val bound = 2.0.pow(resolution.toInt()).toLong()
        (0 until inserts).onEach {
            tree.insert(
                    PointMassEntity(100.0,
                            (0 until bound).random(Random(it + 1)).toDouble() / 100.0,
                            (0 until bound).random(Random(it + 2)).toDouble() / 100.0,
                            (0 until bound).random(Random(it + 3)).toDouble() / 100.0
                    )
            )
        }
        assert(tree.occupancy() == 500L)
        println("$tree")
    }

    @Test
    fun centerMassIsCorrect() {
        val resolution = 5L
        val tree = BarnesHutTree(Gravitation, CubeSpace(resolution))
        val origin: IPointMassEntity = PointMassEntity(100.0, 0.0, 0.0, 0.0)
        val far: IPointMassEntity = PointMassEntity(100.0, 10.0, 0.0, 0.0)
        tree.insert(origin)
        tree.insert(far)
        println("$tree")
        val center: List<Double> = tree.position()
        println(tree.position().joinToString { "$it" })
        assert(center == listOf(5.0, 0.0, 0.0))
        val middle: IPointMassEntity = PointMassEntity(10.0, 5.0, 0.0, 0.0)
        tree.insert(middle)
        assert(tree.position() == listOf(5.0, 0.0, 0.0))
        assert(tree.mass() == far.mass() + origin.mass() + middle.mass())
    }

    @Test
    fun centerMassIsCorrect2() {
        val resolution = 5L
        val tree = BarnesHutTree(Gravitation, CubeSpace(resolution))
        val origin: IPointMassEntity = PointMassEntity(100.0, 0.0, 0.0, 0.0)
        val far: IPointMassEntity = PointMassEntity(300.0, 2.0, 4.0, 4.0)
        tree.insert(origin)
        tree.insert(far)
        println("$tree")
        val center: List<Double> = tree.position()
        println(tree.position().joinToString { "$it" })
        assert(center == listOf(1.5, 3.0, 3.0))
        tree.insert(far)
        println(tree.position().joinToString())
    }

    @Test
    fun canComputeHumanWeightOnEarth() {
        val resolution = 13L
        val tree = BarnesHutTree(Gravitation, CubeSpace(resolution))
        val earth: IPointMassEntity = PointMassEntity(5.972e24, 0.0, 0.0, 0.0)
        val human: IPointMassEntity = PointMassEntity(5e1, 6.317e3, 0.0, 0.0)
        val human2: IPointMassEntity = PointMassEntity(5e1, 4.417e3, 4.4e3, 0.0)
        tree.insert(earth)
        tree.insert(human)
        tree.insert(human2)
        val nodes: List<IBarnesHutTree<IPointMassEntity>> =
                tree.toList().filter { it.nodeStates().isNotEmpty() }
        nodes.onEach { println("$it") }
        val weight = nodes
                .map { tree.force(it, 3) }
//                .map { it.toTypedArray().magnitude()}
                .onEach { println("$it") }
        assert(nodes.isNotEmpty())
//        assert(weight[1]> 499 && weight[1]<500)
    }

    @Test
    fun computeMany() {
        val resolution = 16L
        val tree = BarnesHutTree(Gravitation, CubeSpace(resolution))
        val inserts = 100
        val bound = 2.0.pow(resolution.toInt()).toLong()
        (0 until inserts).onEach {
            tree.insert(
                    PointMassEntity(100.0,
                            (0 until bound).random(Random(it + 1)).toDouble() / 100.0,
                            (0 until bound).random(Random(it + 2)).toDouble() / 100.0,
                            (0 until bound).random(Random(it + 3)).toDouble() / 100.0
                    )
            )
        }
        tree.asSequence().filter { it.nodeStates().isNotEmpty() }
                .onEach { println("$it") }
                .map { tree.force(it, 3) }
                .onEach { println("$it") }.toList()
        println("${tree.toList().filter { it.nodeStates().isNotEmpty() }.count()}, ${Gravitation.computations()}")
        println("$tree")
    }

    @Test
    fun canIterate() {
        val tree = BarnesHutTree(Gravitation, CubeSpace(3L))
        tree.insert(BarnesHutEntityFactory.builder()
                .states(listOf(0.5, 0.5, 0.5))
                .mass(20.0)
                .build())
        tree.insert(BarnesHutEntityFactory.builder()
                .states(listOf(1.5, 1.5, 1.5))
                .build())
        tree.onEach {
            println(it)
        }

    }
}