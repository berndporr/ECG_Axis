package tech.glasgowneuro.ecg_axis

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * The main activity
 */
class ECGAxisActivity : AppCompatActivity() {
    private var herzwinkel:Double = 0.0
    private var WinkelOk = false
    var nattempts = 0
    var dice: Button? = null
    var solution: Button? = null
    var ok: Button? = null
    var angle: EditText? = null
    var ecgAxisView: ECGAxisView? = null
    var ecgTracesView: ECGTracesView? = null
    var evaluation: TextView? = null
    var attyslink: TextView? = null
    private fun neuerHerzwinkel() {
        herzwinkel = if ((Math.random() * 10.0).toInt() == 2) {
            (Math.random() * 360.0 - 150.0)
        } else {
            (Math.random() * 150.0 - 30.0)
        }
        ecgAxisView!!.herzwinkel = herzwinkel
        ecgTracesView!!.setAngle(herzwinkel)
        nattempts = 0
        angle!!.setText("")
    }

    private fun checkEntry() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(angle!!.windowToken, 0)
        try {
            nattempts++
            val Achse = angle!!.text.toString().toInt()
            WinkelOk = Math.abs(Achse - herzwinkel) < 10 ||
                    Math.abs(Achse - 360 - herzwinkel) < 6 ||
                    Math.abs(Achse + 360 - herzwinkel) < 6
        } catch (ignored: NumberFormatException) {
            evaluation!!.text = "Input error"
            return
        }
        if (WinkelOk) {
            evaluation!!.text = "Yass! That's right!"
            ecgAxisView!!.revealAngle(true)
            angle!!.isEnabled = false
            ok!!.isEnabled = false
        } else {
            evaluation!!.text = "Sorry, try again."
            if (nattempts > 3) {
                solution!!.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecgaxis)
        attyslink = findViewById(R.id.attyslink)
        attyslink!!.setOnClickListener {
            val url = "http://www.attys.tech"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        angle = findViewById(R.id.angle)
        angle!!.isEnabled = false
        angle!!.setOnEditorActionListener { _, _, _ ->
            checkEntry()
            true
        }
        ecgAxisView = findViewById(R.id.ecgaxisview)
        ecgTracesView = findViewById(R.id.ecgtracesview)
        evaluation = findViewById(R.id.evaluate)
        solution = findViewById(R.id.solution)
        solution!!.setOnClickListener {
            ecgAxisView!!.revealAngle(true)
            evaluation!!.text = " "
            angle!!.setText("")
            angle!!.isEnabled = false
            ok!!.isEnabled = false
            solution!!.visibility = View.INVISIBLE
        }
        solution!!.visibility = View.INVISIBLE
        dice = findViewById(R.id.dice)
        dice!!.setOnClickListener {
            neuerHerzwinkel()
            evaluation!!.text = " "
            ecgAxisView!!.revealAngle(false)
            solution!!.visibility = View.INVISIBLE
            angle!!.isEnabled = true
            ok!!.isEnabled = true
        }
        ok = findViewById(R.id.okbutton)
        ok!!.isEnabled = false
        ok!!.setOnClickListener { checkEntry() }
    }
}