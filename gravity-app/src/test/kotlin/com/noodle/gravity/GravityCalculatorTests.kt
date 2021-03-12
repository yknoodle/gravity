package com.noodle.gravity

import com.noodle.math.IterableOperations.magnitude
import com.noodle.physics.PointMassEntity
import com.noodle.physics.BarnesHutEntityFactory
import com.noodle.physics.IForceResult
import com.noodle.physics.IPointMassEntity
import com.noodle.physics.gravitation.Earth
import com.noodle.physics.gravitation.Moon
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import kotlin.random.Random

class GravityCalculatorTests {
    @FlowPreview
    @Test
    fun canCalculate() = runBlocking {
        val entities: List<PointMassEntity> =
                listOf(
                        PointMassEntity(100.0, 0.0, 0.0, 0.0),
                        PointMassEntity(100.0, 1.0, 1.0, 1.0)
                )
        BarnesHutGravityCalculator.compute(entities)
                .collect { println("$it") }
    }

    @FlowPreview
    @Test
    fun calculateEarthMoon(): Unit = runBlocking {
        val earthCenter: Double = 400000.0
        val entities: List<IPointMassEntity> = listOf(
                BarnesHutEntityFactory.builder()
                        .from(Earth)
                        .states(listOf(earthCenter, 0.0, 0.0)).build(),
                BarnesHutEntityFactory.builder()
                        .from(Moon)
                        .states(listOf(earthCenter + 384400.0, 0.0, 0.0)).build(),
                BarnesHutEntityFactory.builder()
                        .from(Moon)
                        .id("Moon2")
                        .states(listOf(earthCenter - 384400.0, 0.0, 0.0)).build(),
                BarnesHutEntityFactory.builder()
                        .mass(100.0)
                        .id("poop")
                        .states(listOf(earthCenter, 0.0, 0.0)).build()
        )
        val result = BarnesHutGravityCalculator.compute(entities)
                .onEach { println("${it.id()}, ${it.components()}") }
                .toList()
                .fold(mutableMapOf<String, IForceResult>()){ acc, cur ->
                    acc[cur.id()] = cur
                    acc
                }
        assert(result["poop"]?.components()?.size == 2)
    }

    @FlowPreview
    @Test
    fun canCalculateMany() = runBlocking {
        val inserts = 200
        val maxMass: Long = 10e25.toLong()
        val maxRange: Long = 10e11.toLong()
        val entities: List<PointMassEntity> = (0 until inserts).map {
            PointMassEntity(
                    (0 until maxMass).random(Random(it)).toDouble(),
                    (0 until maxRange).random(Random(it + 1)) / 1000.0,
                    (0 until maxRange).random(Random(it + 2)) / 1000.0,
                    (0 until maxRange).random(Random(it + 3)) / 1000.0,
                    _id = it.toString()
            )
        }
        println("started on $inserts inserts")
        BarnesHutGravityCalculator.compute(entities, 50)
                .collect()
        println("completed $inserts inserts")
    }
}