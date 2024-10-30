package com.m2dstudio.otpman.dataModel

data class ShakeConfig(
    val iterations: Int,
    val intensity: Float = 100_000f,
    val rotate: Float = 0f,
    val rotateX: Float = 0f,
    val rotateY: Float = 0f,
    val scaleX: Float = 0f,
    val scaleY: Float = 0f,
    val translateX: Float = 0f,
    val translateY: Float = 0f,
    val trigger: Long = System.currentTimeMillis(),
) {
    companion object {
        val error = ShakeConfig(
            iterations = 4,
            intensity = 2_000f,
            rotateY = 30f,
            translateX = 20f,
        )

        val success = ShakeConfig(
            iterations = 4,
            intensity = 2_000f,
            rotateX = 30f,
            translateY = 20f,
        )

        val none = ShakeConfig(iterations = 0)
    }

}