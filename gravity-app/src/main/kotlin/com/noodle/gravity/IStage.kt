package com.noodle.gravity

interface IStage {
    fun frames(): List<IFrame>
    fun buildFrame(): IFrame

}