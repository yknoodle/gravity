package com.noodle.gravity

import com.noodle.physics.IForceResult
import com.noodle.physics.PointMassEntity
import com.noodle.physics.gravitation.IGravitation
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

class Stage(
        private val _frames: Array<IFrame>,
        private val _gravityCalculator: IGravitation
        // use channels to supply new IFrameEntity
): IStage {
    override fun frames(): List<IFrame> = _frames.toList()
    @FlowPreview
    override fun buildFrame(): IFrame {
        val lastFrame: IFrame = _frames.last()
        val entities: List<IFrameEntity> = _frames.last().entities()
        val gravitationForces: Flow<IForceResult> = _gravityCalculator.compute(entities)
        return lastFrame
    }

}