package com.github.propogand.math

import com.github.propogand.mathsdk.Math
import com.github.propogand.mathsdk.MathProvider

class MathTest {

    private val math: Math = MathProvider.getMath()

    init {
        println(math.sum(1, 2))
    }

}