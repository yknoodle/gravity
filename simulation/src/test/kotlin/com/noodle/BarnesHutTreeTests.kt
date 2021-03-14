package com.noodle

import com.noodle.bounding.CubeSpace
import com.noodle.datastructure.IBarnesHutTree
import com.noodle.math.IterableOperations.magnitude
import com.noodle.physics.*
import com.noodle.physics.gravitation.Earth
import com.noodle.math.IterableOperations.plus
import com.noodle.physics.barneshut.BarnesHutResultInterpreter
import com.noodle.physics.barneshut.BarnesHutTree
import com.noodle.physics.barneshut.BarnesHutSolver
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.math.pow
import kotlin.random.Random
import kotlin.test.assertTrue

class BarnesHutTreeTests {
    @Test
    fun canInsert() {
        val tree = BarnesHutTree(CubeSpace(15))
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
        val tree = BarnesHutTree(CubeSpace(resolution))
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
        val tree = BarnesHutTree(CubeSpace(resolution))
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
        val tree = BarnesHutTree(CubeSpace(resolution))
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
    fun canComputeHumanWeightOnEarth() = runBlocking {
        val resolution = 13L
        val tree = BarnesHutTree(CubeSpace(resolution))
        tree.insert(PointMassEntityFactory.builder()
                .from(Earth).build())
        tree.insert(PointMassEntityFactory.builder()
                .mass(5e1).states(listOf(6.317e3, 0.0, 0.0))
                .id("human1")
                .build())
        tree.insert(PointMassEntityFactory.builder()
                .mass(5e1).states(listOf(4.417e3, 4.4e3, 0.0))
                .id("human2")
                .build())
        val nodes: List<IBarnesHutTree<IPointMassEntity>> =
                tree.nodes().filter { it.localStates().isNotEmpty() }
        nodes.onEach { println("$it") }
        val interpreter = BarnesHutResultInterpreter()
        val resultMap: Map<String, IForceResult> = nodes
                .map { BarnesHutSolver.solve(it, tree) }
                .flatMap { interpreter.apply(it).toList() }
                .fold(mapOf<String, IForceResult>()){ acc, cur -> acc + (cur.id() to cur)}
                .onEach { println(it) }
        assert(nodes.isNotEmpty())

        val weight = (resultMap["human1"] ?: error("human1 not found"))
                .components().values
                .reduce { acc, cur -> acc plus cur }
                .magnitude()
        println("weight: $weight")
        assert(weight > 499 &&  weight < 500)
    }

    @Test
    fun computeMany() {
        val resolution = 16L
        val tree = BarnesHutTree(CubeSpace(resolution))
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
        tree.nodes().asSequence().filter { it.localStates().isNotEmpty() }
                .map { BarnesHutSolver.solve(it, tree)}
                .onEach { println(it) }.toList()
        println()
        assert(inserts.toLong() == tree.occupancy()) { "${tree.nodes().filter { it.localStates().isNotEmpty() }.count()}" }
    }

    @Test
    fun canIterate() {
        val tree = BarnesHutTree(CubeSpace(3L))
        tree.insert(PointMassEntityFactory.builder()
                .states(listOf(0.5, 0.5, 0.5))
                .mass(20.0)
                .build())
        tree.insert(PointMassEntityFactory.builder()
                .states(listOf(1.5, 1.5, 1.5))
                .build())
        tree.nodes().onEach {
            println(it)
        }

    }
}