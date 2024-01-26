package com.ensias.eldycare.mobile.smarwatch.service

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.cos
import kotlin.math.sin

/**
 * Listener for the sensors. With fall detection algorithm
 * @param messageClient the messageClient instance
 */
class SensorListenerFallDetectorService(
    val messageClient: HandheldMessageClient,
): SensorEventListener{
    private val NS2S = 1.0f / 1000000000.0f
    private var timestamp: Long = 0L
    private var accelerationValues: FloatArray = floatArrayOf(0f, 0f, 0f)
    private var magneticValues: FloatArray = floatArrayOf(0f, 0f, 0f)
    private var gravityValues: FloatArray = floatArrayOf(0f, 0f, 0f)
    private var worldRotationMatrixX: FloatArray = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    private var worldRotationMatrixY: FloatArray = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    private var worldRotationMatrixZ: FloatArray = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    private var orientationValues: FloatArray = floatArrayOf(0f, 0f, 0f)
    private var rotationMatrix: FloatArray = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_LINEAR_ACCELERATION -> {
                // Update acceleration values when accelerometer data is received
                var x = event.values[0]
                var y = event.values[1]
                var z = event.values[2]
                // remove noise
                val eps = 0.05f
                if (x < eps && x > -eps) x = 0f
                if (y < eps && y > -eps) y = 0f
                if (z < eps && z > -eps) z = 0f
                // assign and low pass
                val alpha = 0.8f
                accelerationValues[0] = alpha * x + (1 - alpha) * accelerationValues[0]
                accelerationValues[1] = alpha * y + (1 - alpha) * accelerationValues[1]
                accelerationValues[2] = alpha * z + (1 - alpha) * accelerationValues[2]
//                Log.d("VerticalAccelerationSchenanagans", "Linear Acceleration :  ${accelerationValues.contentToString()}")
            }
            Sensor.TYPE_GYROSCOPE -> {
                // Update rotation matrix, which is needed to update orientation angles.
                val dT = (event.timestamp - timestamp) * NS2S
                timestamp = event.timestamp
                var omegaX = event.values[0]
                var omegaY = event.values[1]
                var omegaZ = event.values[2]

                // Remove noise
                val eps = 0.05f
                if (omegaX < eps && omegaX > -eps) omegaX = 0f
                if (omegaY < eps && omegaY > -eps) omegaY = 0f
                if (omegaZ < eps && omegaZ > -eps) omegaZ = 0f


                // Calculate the rotation matrix
                SensorManager.getRotationMatrix(
                    rotationMatrix,
                    null,
                    gravityValues,
                    magneticValues
                )
                SensorManager.getOrientation(rotationMatrix, orientationValues)
                // get acceleration in new world coordinates using orientation
                val sinX = sin(orientationValues[1])
                val cosX = cos(orientationValues[1])
                val sinY = sin(orientationValues[2])
                val cosY = cos(orientationValues[2])
                // rotation matrix around x axis
                worldRotationMatrixX = floatArrayOf(
                    1f, 0f, 0f,
                    0f, cosX, -sinX,
                    0f, sinX, cosX
                )
                // rotation matrix around y axis
                worldRotationMatrixY = floatArrayOf(
                    cosY, 0f, sinY,
                    0f, 1f, 0f,
                    -sinY, 0f, cosY
                )
                // multiply rotation matrices
                val worldRotationMatrix =  multiplyMatrices(worldRotationMatrixX, 3,3,worldRotationMatrixY,3,3)
                val worldAcceleration = multiplyMatrixVector(worldRotationMatrix, accelerationValues).map { if(it < 0.01) 0f else it }.toFloatArray()
                val verticalAcceleration = worldAcceleration[2]

                // Integration
                val verticalVelocity = verticalAcceleration * dT
                val verticalPosition = verticalVelocity * dT * 1000 // in mm

                // do Threshold Based Method on vertical acceleration
                val threshold = 0.1f
                if (verticalAcceleration > threshold) {
                    if (verticalVelocity > threshold) {
                        if (verticalPosition > threshold) {
                            // send the data to the handheld
                            messageClient.sendMessage("fall detected")
                        }
                    }
                }

            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                // Update magnetic field values when compass data is received
                magneticValues[0] = event.values[0]
                magneticValues[1] = event.values[1]
                magneticValues[2] = event.values[2]
            }
            Sensor.TYPE_GRAVITY -> {
                // Update gravity values when gravity data is received
                gravityValues[0] = event.values[0]
                gravityValues[1] = event.values[1]
                gravityValues[2] = event.values[2]
            }
        }



    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun multiplyMatrixVector(matrix: FloatArray, vector: FloatArray): FloatArray {
        val matrixRows = matrix.size / vector.size
        require(matrix.size % vector.size == 0) { "Invalid matrix and vector dimensions for multiplication" }

        val result = FloatArray(matrixRows)

        for (i in 0 until matrixRows) {
            var sum = 0.0f
            for (j in 0 until vector.size) {
                sum += matrix[i * vector.size + j] * vector[j]
            }
            result[i] = sum
        }

        return result
    }
    fun multiplyMatrices(matrixA: FloatArray, numRowsA: Int, numColsA: Int, matrixB: FloatArray, numRowsB: Int, numColsB: Int): FloatArray {
        require(numColsA == numRowsB) { "Invalid matrix dimensions for multiplication" }

        val result = FloatArray(numRowsA * numColsB)

        for (i in 0 until numRowsA) {
            for (j in 0 until numColsB) {
                var sum = 0.0f
                for (k in 0 until numColsA) {
                    sum += matrixA[i * numColsA + k] * matrixB[k * numColsB + j]
                }
                result[i * numColsB + j] = sum
            }
        }
        return result
    }
}
