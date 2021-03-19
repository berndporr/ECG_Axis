package tech.glasgowneuro.ecg_axis

/**
 * Converts from Angle to ECG amplitudes
 */
class Angle2Amplitudes internal constructor(private val herzwinkel: Double) {
    fun getAmplitudes(a: DoubleArray) {
        val pf = 180.0 / (Math.acos(0.0) * 2.0)
        val phiI = herzwinkel.toDouble() / pf
        a[0] = Math.cos(phiI) // I
        a[5] = Math.sin(phiI) // aVF
        val phiII = (herzwinkel - 30) / pf
        a[2] = Math.sin(phiII) // II
        val phiIII = (-herzwinkel + 150) / pf
        a[1] = Math.sin(phiIII) // III
        val phiaVR = (-herzwinkel + 210) / pf
        a[3] = Math.cos(phiaVR) // aVR
        val phiaVL = (herzwinkel + 30) / pf
        a[4] = Math.cos(phiaVL) // aVL
    }
}