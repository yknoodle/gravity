package com.noodle.gravity

import com.noodle.physics.*
import com.noodle.physics.gravitation.Earth
import com.noodle.physics.gravitation.IForceCalculator
import com.noodle.physics.gravitation.Moon
import com.noodle.math.IterableOperations.plus
import com.noodle.math.IterableOperations.times
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
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
        val calculator = BarnesHutGravityCalculator()
        calculator.compute(entities)
                .collect { println("$it") }
    }

    @FlowPreview
    @Test
    fun calculateEarthMoon(): Unit = runBlocking {
        val earthCenter = 400000.0
        val entities: List<IPointMassEntity> = listOf(
                PointMassEntityFactory.builder()
                        .from(Earth)
                        .states(listOf(earthCenter, earthCenter, 0.0)).build(),
                PointMassEntityFactory.builder()
                        .from(Moon)
                        .states(listOf(earthCenter + 384400.0, earthCenter + 1.0, 0.0,0.0, 3600.0, 0.0)).build(),
                PointMassEntityFactory.builder()
                        .from(Moon)
                        .id("Moon2")
                        .states(listOf(earthCenter - 384400.0, earthCenter - 1.0, 0.0,0.0, -3600.0, 0.0)).build(),
                PointMassEntityFactory.builder()
                        .mass(100.0)
                        .id("poop")
                        .states(listOf(earthCenter, earthCenter, 0.0)).build()
        )
        val calculator = BarnesHutGravityCalculator()
        val result = calculator.compute(entities)
                .onEach { println(it) }
                .toList()
                .fold(mutableMapOf<String, IForceResult>()) { acc, cur ->
                    acc[cur.id()] = cur
                    acc
                }
        assert(result["poop"]?.components()?.size == 2)
        val entityMap: Map<String, IPointMassEntity> = entities.fold(mapOf()) { acc, cur -> acc + (cur.id() to cur) }
        calculator.compute(entities)
                .map {
                    val entity: IPointMassEntity = entityMap[it.id()] ?: error("${it.id()} does not exist")
                    val a: List<Double> =
                            it.components().values.reduce { acc, list -> acc plus list } *
                                    (1 / entity.mass())
                    val v: List<Double> =
                            StepKinematics.velocity(entity.velocity(), a, 1)
                    val s: List<Double> =
                            StepKinematics.displacement(entity.velocity(), a, 1)
                    s
                }
                .collect { println("disp: $it") }
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
        val speedCalculator: IKinematics<List<Double>, Long> = StepKinematics
        val entityMap: Map<String, IPointMassEntity> = entities.fold(mapOf()) { acc, cur -> acc + (cur.id() to cur) }
        val calculator: IForceCalculator = BarnesHutGravityCalculator()
        println("started on $inserts inserts")
        calculator.compute(entities)
                .map {
                    val entity: IPointMassEntity = entityMap[it.id()] ?: error("${it.id()} does not exist")
                    val a: List<Double> =
                            it.components().values.reduce { acc, list -> acc plus list } *
                                    (1 / (entityMap[it.id()]?.mass() ?: 1.0))
                    val v: List<Double> =
                            StepKinematics.velocity(entity.velocity(), a, 60)
                    val s: List<Double> =
                            StepKinematics.displacement(entity.velocity(), a, 60)
                    s
                }
                .collect { println("disp: $it") }
        println("completed $inserts inserts")
    }
}