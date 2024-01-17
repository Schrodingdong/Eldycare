package com.ensias.eldycare.mobile.smarwatch

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.opengl.Matrix
import android.util.Log
import kotlin.math.cos
import kotlin.math.sin

class VerticalAccelerationSchenanagans: SensorEventListener{
    private val NS2S = 1.0f / 1000000000.0f
    private var timestamp: Long = 0L
    private var accelerationValues: FloatArray = floatArrayOf(0f, 0f, 0f)
    private var magneticValues: FloatArray = floatArrayOf(0f, 0f, 0f)
    private var worldRotationMatrixX: FloatArray = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    private var worldRotationMatrixY: FloatArray = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    private var worldRotationMatrixZ: FloatArray = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    private var orientationValues: FloatArray = floatArrayOf(0f, 0f, 0f)

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
                var omegaX = event.values[0]
                var omegaY = event.values[1]
                var omegaZ = event.values[2]

                // Remove noise
                val eps = 0.05f
                if (omegaX < eps && omegaX > -eps) omegaX = 0f
                if (omegaY < eps && omegaY > -eps) omegaY = 0f
                if (omegaZ < eps && omegaZ > -eps) omegaZ = 0f

                // Integrate angular velocities around x, y, and z axes to get the
                // rotation matrix. thetaX is the rotation angle around x axis, etc.
                val thetaX = omegaX * dT
                val thetaY = omegaY * dT
                val thetaZ = omegaZ * dT
                val sinThetaX = sin(thetaX)
                val cosThetaX = cos(thetaX)
                val rotationMatrixX = floatArrayOf(
                    1f, 0f, 0f,
                    0f, cosThetaX, -sinThetaX,
                    0f, sinThetaX, cosThetaX
                )
                val rotationMatrixY = floatArrayOf(
                    cos(thetaY), 0f, sin(thetaY),
                    0f, 1f, 0f,
                    -sin(thetaY), 0f, cos(thetaY)
                )
                val rotationMatrixZ = floatArrayOf(
                    cos(thetaZ), -sin(thetaZ), 0f,
                    sin(thetaZ), cos(thetaZ), 0f,
                    0f, 0f, 1f
                )
                SensorManager.getRotationMatrix(
                    worldRotationMatrixX,
                    null,
                    rotationMatrixX,
                    magneticValues
                )
                SensorManager.getRotationMatrix(
                    worldRotationMatrixY,
                    null,
                    rotationMatrixY,
                    magneticValues
                )
                SensorManager.getRotationMatrix(
                    worldRotationMatrixZ,
                    null,
                    rotationMatrixZ,
                    magneticValues
                )
                val rotationMatrixMultiplied = multiplyMatrices(
                    multiplyMatrices(rotationMatrixX, 3,3 , rotationMatrixY, 3,3),
                    3,3,
                    rotationMatrixZ, 3,3
                )
//                Log.d("VerticalAccelerationSchenanagans", "Rotation Matrix :  ${rotationMatrix.contentToString()}")
//                val worldAcceleration = floatArrayOf(0f, 0f, 0f)
                val worldAcceleration = multiplyMatrixVector(rotationMatrixMultiplied, accelerationValues)
                Log.d("VerticalAccelerationSchenanagans", "World Acceleration :  ${worldAcceleration.contentToString()}")
//                Log.d("VerticalAccelerationSchenanagans", "STJB Vertical Acceleration :  ${worldAcceleration[2]}")
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                // Update magnetic field values when compass data is received
                magneticValues[0] = event.values[0]
                magneticValues[1] = event.values[1]
                magneticValues[2] = event.values[2]
//                Log.d("VerticalAccelerationSchenanagans", "Magnetic Field :  ${magneticValues.contentToString()}")
            }
        }
        // multiply rotation matrix by acceleration values to get acceleration in world coordinates
        // this is the same as using the accelerometer in world coordinates




//        if (accelerationValues != null && magneticValues != null && rotationMatrix != null) {
//            // get the rotation matrix
//            SensorManager.getRotationMatrix(
//                rotationMatrix,
//                null,
//                accelerationValues,
//                magneticValues
//            )
//            // get accelration in world coordinates :
//            val worldAccel = floatArrayOf(0f, 0f, 0f)
//            Matrix.multiplyMV(worldAccel, 0, rotationMatrix, 0, accelerationValues, 0)
//            Log.d("VerticalAccelerationSchenanagans", "World Acceleration :  ${worldAccel.contentToString()}")
//        }
//        Log.d("VerticalAccelerationSchenanagans", "=============================================")



    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes if needed
    }
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
