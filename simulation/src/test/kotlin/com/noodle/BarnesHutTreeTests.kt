package com.noodle

import com.noodle.physics.BarnesHutTree
import com.noodle.bounding.CubeSpace
import com.noodle.physics.IBarnesHutEntity
import com.noodle.datastructure.ISpaceTree
import com.noodle.physics.gravitation.Gravitation
import com.noodle.math.ArrayOperations.magnitude
import com.noodle.physics.BarnesHutEntity
import org.junit.Test
import kotlin.math.pow
import kotlin.random.Random
import kotlin.test.assertTrue

class BarnesHutTreeTests {
    @Test
    fun canInsert(){
        val tree = BarnesHutTree(Gravitation, CubeSpace(15 ))
        tree.insert(BarnesHutEntity(10.0,1.0,1.0,1.0))
        tree.insert(BarnesHutEntity(10.0,1.0,1.0,2.0))
        println("$tree")
        assertTrue("expected 2 but got ${tree.occupancy()}"){
            tree.occupancy()==2L
        }
    }

    @Test
    fun insertMany(){
        val resolution = 10L
        val tree = BarnesHutTree(Gravitation, CubeSpace(resolution))
        val inserts = 500
        val bound = 2.0.pow(resolution.toInt()).toLong()
        (0 until inserts).onEach{tree.insert(
                BarnesHutEntity(100.0,
                        (0 until bound).random(Random(it+1)).toDouble()/100.0,
                        (0 until bound).random(Random(it+2)).toDouble()/100.0,
                        (0 until bound).random(Random(it+3)).toDouble()/100.0
                )
        )}
        assert(tree.occupancy()==500L)
        println("$tree")
    }
    @Test
    fun centerMassIsCorrect(){
        val resolution = 5L
        val tree = BarnesHutTree(Gravitation, CubeSpace(resolution))
        val origin: IBarnesHutEntity = BarnesHutEntity(100.0, 0.0, 0.0, 0.0)
        val far: IBarnesHutEntity = BarnesHutEntity(100.0, 10.0, 0.0, 0.0)
        tree.insert(origin)
        tree.insert(far)
        println("$tree")
        val center: List<Double> = tree.centerOfMass()
        println(tree.centerOfMass().joinToString { "$it" })
        assert(center == listOf(5.0, 0.0, 0.0))
        val middle: IBarnesHutEntity = BarnesHutEntity(10.0, 5.0, 0.0, 0.0)
        tree.insert(middle)
        assert(tree.centerOfMass() == listOf(5.0, 0.0, 0.0))
        assert(tree.aggregateMass() == far.mass()+origin.mass()+middle.mass())
    }
    @Test
    fun centerMassIsCorrect2(){
        val resolution = 5L
        val tree = BarnesHutTree(Gravitation, CubeSpace(resolution))
        val origin: IBarnesHutEntity = BarnesHutEntity(100.0, 0.0, 0.0, 0.0)
        val far: IBarnesHutEntity = BarnesHutEntity(300.0, 2.0, 4.0, 4.0)
        tree.insert(origin)
        tree.insert(far)
        println("$tree")
        val center: List<Double> = tree.centerOfMass()
        println(tree.centerOfMass().joinToString { "$it" })
        assert(center == listOf(1.5, 3.0, 3.0))
        tree.insert(far)
        println(tree.centerOfMass().joinToString())
    }
    @Test
    fun canComputeHumanWeightOnEarth(){
        val resolution = 13L
        val tree = BarnesHutTree(Gravitation, CubeSpace(resolution))
        val earth: IBarnesHutEntity = BarnesHutEntity(5.972e24, 0.0, 0.0, 0.0)
        val human: IBarnesHutEntity = BarnesHutEntity(5e1, 6.317e3, 0.0, 0.0)
        val human2: IBarnesHutEntity = BarnesHutEntity(5e1, 4.417e3, 4.4e3, 0.0)
        tree.insert(earth)
        tree.insert(human)
        tree.insert(human2)
        val nodes: List<ISpaceTree<IBarnesHutEntity>> =
                tree.nodes().filter { it.nodeStates().isNotEmpty() }
        nodes.onEach { println("$it") }
        val weight = nodes
                .map { tree.force(it,3) }
                .map { it.toTypedArray().magnitude()}
                .onEach { println("$it") }
        assert(nodes.isNotEmpty())
        assert(weight[1]> 499 && weight[1]<500)
    }
    @Test
    fun computeMany(){
        val resolution = 16L
        val tree = BarnesHutTree(Gravitation, CubeSpace(resolution))
        val inserts = 100
        val bound = 2.0.pow(resolution.toInt()).toLong()
        (0 until inserts).onEach{tree.insert(
                BarnesHutEntity( 100.0,
                        (0 until bound).random(Random(it+1)).toDouble()/100.0,
                        (0 until bound).random(Random(it+2)).toDouble()/100.0,
                        (0 until bound).random(Random(it+3)).toDouble()/100.0
                )
        )}
        tree.nodes().filter { it.nodeStates().isNotEmpty() }
                .onEach { println("$it") }
                .map { tree.force(it, 3) }
                .onEach { println("$it") }
        println("${tree.nodes().filter{it.nodeStates().isNotEmpty()}.count()}, ${Gravitation.computations()}")
        println("$tree")
    }
}