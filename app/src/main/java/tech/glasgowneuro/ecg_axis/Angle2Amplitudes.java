package tech.glasgowneuro.ecg_axis;

/**
 * Converts from Angle to ECG amplitudes
 */

public class Angle2Amplitudes {

    private float herzwinkel;

    Angle2Amplitudes(float _angle) {
        herzwinkel = _angle;
    }

    public void getAmplitudes(double[] a) {
        double pf = 180.0 / (Math.acos(0) * 2.0);
        double phiI = ((double) herzwinkel) / pf;

        a[0] = Math.cos(phiI); // I
        a[5] = Math.sin(phiI); // aVF

        double phiII = ((double) (herzwinkel - 30)) / pf;
        a[2] = Math.sin(phiII); // II

        double phiIII = ((double) (-herzwinkel + 150)) / pf;
        a[1] = Math.sin(phiIII); // III

        double phiaVR = ((double) (-herzwinkel + 210)) / pf;
        a[3] = Math.cos(phiaVR); // aVR

        double phiaVL = ((double) (herzwinkel + 30)) / pf;
        a[4] = Math.cos(phiaVL);  // aVL
    }
}
