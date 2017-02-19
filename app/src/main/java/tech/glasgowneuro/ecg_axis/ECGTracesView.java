package tech.glasgowneuro.ecg_axis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * Plots the ECGs
 */

public class ECGTracesView extends View {

    //String TAG = "ECGTracesView";
    int width;
    int height;
    Paint paint_traces;
    Paint paint_labels;
    float herzwinkel = 0;

    public ECGTracesView(Context context) {
        super(context);
        doInit();
    }

    public ECGTracesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        doInit();
    }

    public ECGTracesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        doInit();
    }

    public void setAngle(float angle) {
        herzwinkel = angle;
        invalidate();
    }

    private void doInit() {
        paint_traces = new Paint();
        paint_traces.setColor(Color.rgb(0, 0, 255));
        paint_labels = new Paint();
        paint_labels.setColor(Color.rgb(0, 0, 0));
    }

    private void MaleEKG(Canvas g,
                         int x1, int y1, int x2, int y2, double a) {
        double Propfaktor = 1.0 / 512.0; // Dehnungsfaktor
        int isoII = 40;     // Lage der isoelektrische Linie
        int dx = x2 - x1;
        if (dx > Ekg.n) {
            dx = Ekg.n;
        }
        int dy = y2 - y1;
        if ((dx < 0) || (dy < 0)) {
            return;
        }
        int y = dy / 2;
        y = y + y1;
        g.drawLine(0, y, width, y, paint_traces);
        for (int x = 1; x < dx; x++) {
            double ya = -(double) (Ekg.Daten[x - 1][2] - isoII);
            ya = ya * a * Propfaktor;
            ya = ya * dy;
            ya = ya + (dy / 2);
            ya = ya + y1;
            double yb = -(double) (Ekg.Daten[x][2] - isoII);
            yb = yb * a * Propfaktor;
            yb = yb * dy;
            yb = yb + (dy / 2);
            yb = yb + y1;
            g.drawLine(x + x1 - 1, (int) ya, x + x1, (int) yb, paint_traces);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String TheECGLeads[] = {"I", "II", "III", "aVR", "aVL", "aVF"};

        width = canvas.getWidth();
        height = canvas.getHeight();

        float sw;
        if (width > height) {
            sw = width / 100;
        } else {
            sw = height / 100;
        }

        paint_traces.setStrokeWidth(sw/2);
        paint_labels.setStrokeWidth(sw/2);

        paint_labels.setTextSize(getHeight() / 10);

        //Log.d(TAG, "Drawing ECG");

        double dy = ((double) height) / 7.0;

        Point p = new Point();
        p.x = width / 2 - Ekg.n / 2;
        p.y = 0;

        Angle2Amplitudes angle2Amplitudes = new Angle2Amplitudes(herzwinkel);
        double[] a = new double[6];
        angle2Amplitudes.getAmplitudes(a);

        for (int i = 0; i < 6; i++) {
            int y = (int) (((double) i) * dy + height / 7);
            MaleEKG(canvas,
                    p.x, p.y + y,
                    width, p.y + y + (int) dy,
                    a[i]);
            canvas.drawText(TheECGLeads[i],
                    width / 20,
                    y + p.y + (int) (dy / 2.0),
                    paint_labels);
        }

    } // Paint

}
