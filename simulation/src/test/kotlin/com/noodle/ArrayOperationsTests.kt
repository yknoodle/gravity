package com.noodle

import com.noodle.math.ArrayOperations.dot
import com.noodle.math.ArrayOperations.plus
import org.junit.Test
import kotlin.test.assertTrue

class ArrayOperationsTests {
    @Test
    fun canUseTimesOperatorOnArrayDouble() {
        val array: Array<Double> = arrayOf<Double>(0.1, 0.2, 0.3)
        val product: Double = array dot array
        assertTrue("expected product") { product == 0.1 * 0.1 + 0.2 * 0.2 + 0.3 * 0.3 }
    }

    @Test
    fun canUsePlusOperatorOnArrayDouble() {
        val array: Array<Double> = arrayOf<Double>(0.1, 0.2, 0.3)
        val sum: Array<Double> = array plus array
        assertTrue("expected sum ${sum[0]}") {
            sum[0] == 0.1 + 0.1 &&
                    sum[1] == 0.2 + 0.2 &&
                    sum[2] == 0.3 + 0.3
        }
    }
}