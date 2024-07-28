package threeax.productivity.ideagen.core

import Generator
import android.content.Context


fun GenerateMOTD(context: Context): QOTDData {
    val generator = Generator(context)
    val todaysNumber = generator.getTodaysNumber()

    return generator.getQOTD(todaysNumber)
}