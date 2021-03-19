package tech.glasgowneuro.ecg_axis

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View

/**
 * Plots the ECGs
 */
class ECGTracesView : View {
    //String TAG = "ECGTracesView";
    var w = 0
    var h = 0
    val paint_traces: Paint = Paint()
    val paint_labels: Paint = Paint()
    var herzwinkel = 0.0
        set(value) {
            field = value
            invalidate()
        }

    constructor(context: Context?) : super(context) {
        doInit()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        doInit()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        doInit()
    }

    private fun doInit() {
        paint_traces.color = Color.rgb(0, 0, 255)
        paint_labels.color = Color.rgb(0, 0, 0)
    }

    private fun MaleEKG(g: Canvas,
                        x1: Int, y1: Int, x2: Int, y2: Int, a: Double) {
        val Propfaktor = 1.0 / 512.0 // Dehnungsfaktor
        val isoII = 40 // Lage der isoelektrische Linie
        var dx = x2 - x1
        if (dx > Ekg.n) {
            dx = Ekg.n
        }
        val dy = y2 - y1
        if (dx < 0 || dy < 0) {
            return
        }
        var y = dy / 2
        y = y + y1
        g.drawLine(0f, y.toFloat(), w.toFloat(), y.toFloat(), paint_traces)
        for (x in 1 until dx) {
            var ya = (-(Ekg.Daten[x - 1][2] - isoII)).toDouble()
            ya = ya * a * Propfaktor
            ya = ya * dy
            ya = ya + dy / 2.0
            ya = ya + y1
            var yb = (-(Ekg.Daten[x][2] - isoII)).toDouble()
            yb = yb * a * Propfaktor
            yb = yb * dy
            yb = yb + dy / 2.0
            yb = yb + y1
            g.drawLine((x + x1 - 1).toFloat(), ya.toFloat(), (x + x1).toFloat(), yb.toFloat(), paint_traces)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val TheECGLeads = arrayOf("I", "II", "III", "aVR", "aVL", "aVF")
        w = getWidth()
        h = getHeight()
        val sw: Float
        sw = if (w > h) {
            (w / 100).toFloat()
        } else {
            (h / 100).toFloat()
        }
        paint_traces.strokeWidth = sw / 2
        paint_labels.strokeWidth = sw / 2
        paint_labels.textSize = (getHeight() / 10).toFloat()

        //Log.d(TAG, "Drawing ECG");
        val dy = h.toDouble() / 7.0
        val p = Point()
        p.x = w / 2 - Ekg.n / 2
        p.y = 0
        val angle2Amplitudes = Angle2Amplitudes(herzwinkel)
        val a = DoubleArray(6)
        angle2Amplitudes.getAmplitudes(a)
        for (i in 0..5) {
            val y = (i.toDouble() * dy + h / 7).toInt()
            MaleEKG(canvas,
                    p.x, p.y + y,
                    w, p.y + y + dy.toInt(),
                    a[i])
            canvas.drawText(TheECGLeads[i], (
                    w / 20).toFloat(), (
                    y + p.y + (dy / 2.0).toInt()).toFloat(),
                    paint_labels)
        }
    } // Paint
}