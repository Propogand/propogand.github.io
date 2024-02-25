package com.github.propogand.mathsdk

public object MathProvider {
    private val math: Math = MathImpl()

    fun getMath(): Math = math

}