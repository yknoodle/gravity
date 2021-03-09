package com.noodle.gravity

import com.noodle.physics.BarnesHutEntity
import com.noodle.physics.BarnesHutEntityFactory
import com.noodle.physics.gravitation.Earth
import com.noodle.physics.gravitation.Moon
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import kotlin.random.Random

class GravityCalculatorTests {
    @FlowPreview
    @Test
    fun canCalculate() = runBlocking {
        val barnesHutObjects: List<BarnesHutEntity> =
                listOf(
                        BarnesHutEntity(100.0, 0.0, 0.0, 0.0),
                        BarnesHutEntity(100.0, 1.0, 1.0, 1.0)
                )
        GravityCalculator.compute(barnesHutObjects)
                .collect { println("$it") }
    }
    @FlowPreview
    @Test
    fun calculateEarthMoon() = runBlocking {
        val earthCenter: Double = 400000.0
        val entities: List<BarnesHutEntity> = listOf(
                BarnesHutEntityFactory.builder()
                        .from(Earth)
                        .states(listOf(earthCenter, 0.0, 0.0)).build(),
                BarnesHutEntityFactory.builder()
                        .from(Moon)
                        .states(listOf(earthCenter+384400.0, 0.0, 0.0)).build(),
                BarnesHutEntityFactory.builder()
                        .from(Moon)
                        .states(listOf(earthCenter-384400.0, 0.0, 0.0)).build()
        )
        GravityCalculator.compute(entities).collect { println(it) }
    }
    @FlowPreview
    @Test
    fun canCalculateMany() = runBlocking {
        val inserts = 200
        val maxMass: Long = 10e25.toLong()
        val maxRange: Long = 10e11.toLong()
        val barnesHutObjects: List<BarnesHutEntity> = (0 until inserts).map{
            BarnesHutEntity(
                    (0 until maxMass).random(Random(it)).toDouble(),
                    (0 until maxRange).random(Random(it+1))/1000.0,
                    (0 until maxRange).random(Random(it+2))/1000.0,
                    (0 until maxRange).random(Random(it+3))/1000.0,
                    _id = it.toString()
            )
        }
        println("started on $inserts inserts")
        GravityCalculator.compute(barnesHutObjects)
                .collect { println("$it") }
        println("started on $inserts inserts")
    }
}