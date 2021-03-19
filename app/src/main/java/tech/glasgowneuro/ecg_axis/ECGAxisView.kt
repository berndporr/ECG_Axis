package tech.glasgowneuro.ecg_axis

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import kotlin.math.roundToInt

/**
 * Plots the Einthoven Triangle
 */
class ECGAxisView : View {
    private var w = 0
    private var h = 0
    private val paint_black = Paint()
    private val paint_blue = Paint()
    private val paint_green = Paint()
    public var herzwinkel = 0.0
        set(value) {
            field = value
            invalidate()
        }

    var winkelanzeigen = false

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
        paint_black.color = Color.rgb(0, 0, 0)
        paint_blue.color = Color.rgb(0, 0, 255)
        paint_green.color = Color.rgb(255, 0, 0)
    }

    fun revealAngle(showvector: Boolean) {
        winkelanzeigen = showvector
        invalidate()
    }

    fun WinkelZuXY(p: Point, m: Point, Herzw: Double, r: Double) {
        val cx: Int
        cx = if (w > h) {
            h * 9 / 10
        } else {
            w * 9 / 10
        }
        val xx = cx / 3
        m.x = w / 2
        m.y = h / 2
        val pf = 180.0 / (Math.acos(0.0) * 2.0)
        p.x = m.x + (Math.cos(Herzw / pf) * xx.toDouble() * r).toInt()
        p.y = m.y + (Math.sin(Herzw / pf) * xx.toDouble() * r).toInt()
    }

    private fun MaleDreieck(g: Canvas) {
        val p = Point()
        val md = Point()
        WinkelZuXY(p, md, -30.0, 1.2)
        val p1 = Point()
        val p2 = Point()
        val p3 = Point()
        val m = Point()

        // Dreieck:
        WinkelZuXY(p1, m, -30.0, 1.2)
        WinkelZuXY(p2, m, 90.0, 1.2)
        g.drawLine(p1.x.toFloat(), p1.y.toFloat(), p2.x.toFloat(), p2.y.toFloat(), paint_blue)
        WinkelZuXY(p3, m, -150.0, 1.2)
        g.drawLine(p2.x.toFloat(), p2.y.toFloat(), p3.x.toFloat(), p3.y.toFloat(), paint_blue)
        g.drawLine(p1.x.toFloat(), p1.y.toFloat(), p3.x.toFloat(), p3.y.toFloat(), paint_blue)

        // Beschriftung:
        paint_black.textSize = (getHeight() / 10).toFloat()
        val dd = getWidth() / 75
        WinkelZuXY(p1, m, -30.0, 1.3)
        g.drawText("L", (p1.x - dd).toFloat(), p1.y.toFloat(), paint_black)
        WinkelZuXY(p1, m, -150.0, 1.3)
        g.drawText("R", (p1.x - dd).toFloat(), p1.y.toFloat(), paint_black)
        WinkelZuXY(p1, m, -90.0, 0.7)
        g.drawText("I", (p1.x - dd).toFloat(), p1.y.toFloat(), paint_black)
        WinkelZuXY(p1, m, 30.0, 0.8)
        g.drawText("III", (p1.x - dd).toFloat(), p1.y.toFloat(), paint_black)
        WinkelZuXY(p1, m, 150.0, 0.9)
        g.drawText("II", (p1.x - dd).toFloat(), p1.y.toFloat(), paint_black)
        WinkelZuXY(p1, m, 90.0, 1.5)
        g.drawText("F", (p1.x - dd).toFloat(), (p1.y + dd).toFloat(), paint_black)
        if (winkelanzeigen) { // Pfeil
            WinkelZuXY(p1, m, herzwinkel, 1.0)
            g.drawLine(m.x.toFloat(), m.y.toFloat(), p1.x.toFloat(), p1.y.toFloat(), paint_black)
            WinkelZuXY(p2, m, (herzwinkel + 5), 0.9)
            WinkelZuXY(p3, m, (herzwinkel - 5), 0.9)
            g.drawLine(p1.x.toFloat(), p1.y.toFloat(), p3.x.toFloat(), p3.y.toFloat(), paint_black)
            g.drawLine(p1.x.toFloat(), p1.y.toFloat(), p2.x.toFloat(), p2.y.toFloat(), paint_black)
            WinkelZuXY(p3, m, 90.0, 1.5)
            g.drawText("  ${herzwinkel.roundToInt()}°", 0f, p3.y.toFloat(), paint_black)
        } // Pfeil
        WinkelZuXY(p, md, 90.0, 1.6)
    } // Dreieck

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        w = width
        h = height
        val sw: Float
        sw = if (w > h) {
            (w / 100).toFloat()
        } else {
            (h / 100).toFloat()
        }
        paint_black.strokeWidth = sw
        MaleDreieck(canvas)
    } // Paint
}