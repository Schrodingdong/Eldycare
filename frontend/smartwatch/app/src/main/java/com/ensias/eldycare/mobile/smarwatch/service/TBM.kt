package com.ensias.eldycare.mobile.smarwatch.service

import java.util.Vector
import kotlin.math.sqrt

class TBM (
    val acc: List<List<Double>>,
    val gyro: List<List<Double>>
){

    fun apply(){
        lowPassFilter()

    }

    fun lowPassFilter(alpha: Double = 0.05){
        acc.map {
            it.map {
                if(it < alpha) 0 else it
            }
        }
    }

    /**
     * For a given sample
     */
    fun getWorldAccelerationVector(accSample: List<Double>): List<Double>{
        val accX = accSample[0]
        val accY = accSample[1]
        val accZ = accSample[2]

        val worldAcc = arrayOf(0.0,0.0,0.0)

        // Check if the movement is significant
        if(isMovementSignificant()){
            // Use ALSO gyroscope data

        } else {
            // Use ONLY Accelerometer
            worldAcc[0] = accX
            worldAcc[1] = accY
            worldAcc[2] = accZ
        }

        return worldAcc.toList()
    }


    val MovSigThresh = 0.1
    fun isMovementSignificant() : Boolean{
        var avg = 0.0
        for(gyroSample in gyro){
            avg += getMagnitude(gyroSample)
        }
        avg /= gyro.size
        return avg > MovSigThresh
    }

    fun getMagnitude(vector : List<Double>) : Double{
        var sum : Double = 0.0
        for(d in vector)
            sum += d*d
        return sqrt(sum)
    }
}
