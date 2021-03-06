package com.noodle

import com.noodle.bounding.CubeSpace
import com.noodle.bounding.IBoundary
import com.noodle.bounding.ISplittable
import org.junit.Test
import kotlin.test.assertTrue

class CubeSpaceTests {
    @Test
    fun canSplit(){
        val resolution = 2L
        val boundedSpace = CubeSpace(resolution)
        val splitSpace: List<CubeSpace> = boundedSpace.split().map{it as CubeSpace}
        splitSpace.forEach{ println(it) }
        assertTrue("expected 8 but got ${splitSpace.size}"){
            splitSpace.size == 8
        }
        assertTrue("expected ${resolution/2} but got ${splitSpace[0].resolution()}"){
            splitSpace[0].resolution() == 1L
        }
        assertTrue("expected centre"){
            splitSpace[0].origin()[0] == 0L
        }
        val splitSpace2 = splitSpace[0].split().map { it as CubeSpace }
        splitSpace2.forEach{println(it)}
        assert(splitSpace2[0].resolution() == 0L)
    }
}