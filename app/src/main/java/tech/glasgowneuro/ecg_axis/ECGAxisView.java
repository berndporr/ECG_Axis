package tech.glasgowneuro.ecg_axis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by bp1 on 18/02/17.
 */

public class ECGAxisView extends View {

    String TAG="ECGAxisView";
    int width;
    int height;
    Paint paint_black;
    Paint paint_blue;
    Paint paint_red;
    float herzwinkel = 0;
    boolean winkelanzeigen = false;

    public ECGAxisView(Context context) {
        super(context);
        doInit();
    }

    public ECGAxisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        doInit();
    }

    public ECGAxisView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        doInit();
    }

    private void doInit() {
        paint_black = new Paint();
        paint_black.setColor(Color.rgb(0, 0, 0));
        paint_blue = new Paint();
        paint_blue.setColor(Color.rgb(0, 0, 255));
        paint_red = new Paint();
        paint_red.setColor(Color.rgb(255, 0, 0));
    }

    void setHerzwinkel(float _herzwinkel) {
        herzwinkel = _herzwinkel;
        invalidate();
    }

    void revealAngle(boolean showvector) {
        winkelanzeigen = showvector;
        invalidate();
    }


    public void WinkelZuXY(Point p, Point m, double Herzw, double r) {
        int cx;
        if (width>height) {
            cx = height * 9 / 10;
        } else {
            cx = width * 9 / 10;
        }
        int xx = cx / 3;
        int yy = xx;
        m.x = width / 2;
        m.y = height / 2;
        double pf = 180.0 / (Math.acos(0) * 2.0);
        p.x = m.x + (int) (Math.cos(Herzw / pf) * ((double) xx) * r);
        p.y = m.y + (int) (Math.sin(Herzw / pf) * ((double) yy) * r);
    }


    private void MaleDreieck(Canvas g) { // Male Dreieck
        Point p = new Point();
        Point md = new Point();
        WinkelZuXY(p, md, -30, 1.2);
        Point p1 = new Point();
        Point p2 = new Point();
        Point p3 = new Point();
        Point m = new Point();
        // Dreieck:
        WinkelZuXY(p1, m, -30, 1.2);
        WinkelZuXY(p2, m, 90, 1.2);
        g.drawLine(p1.x, p1.y, p2.x, p2.y,paint_black);

        WinkelZuXY(p3, m, -150, 1.2);
        g.drawLine(p2.x, p2.y, p3.x, p3.y,paint_black);
        g.drawLine(p1.x, p1.y, p3.x, p3.y,paint_black);

        //Log.d(TAG,"w="+width+" h="+height+" x="+p1.x+" y="+p1.y+" x="+p2.x+" y="+p2.y+" x="+p3.x+" y="+p3.y);

        // Beschriftung:
        paint_black.setTextSize(getHeight() / 10);
        int dd = getWidth() / 75;
        WinkelZuXY(p1, m, -30, 1.3);
        g.drawText("L", p1.x - dd, p1.y,paint_black);
        WinkelZuXY(p1, m, -150, 1.3);
        g.drawText("R", p1.x - dd, p1.y,paint_black);
        WinkelZuXY(p1, m, -90, 0.7);
        g.drawText("I", p1.x - dd, p1.y,paint_black);
        WinkelZuXY(p1, m, 30, 0.8);
        g.drawText("III", p1.x - dd, p1.y,paint_black);
        WinkelZuXY(p1, m, 150, 0.9);
        g.drawText("II", p1.x - dd, p1.y,paint_black);
        WinkelZuXY(p1, m, 90, 1.5);
        g.drawText("F", p1.x - dd, p1.y + dd,paint_black);
        if (winkelanzeigen) { // Pfeil
            WinkelZuXY(p1, m, herzwinkel, 1);
            g.drawLine(m.x, m.y, p1.x, p1.y,paint_black);
            WinkelZuXY(p2, m, herzwinkel + 5, 0.9);
            WinkelZuXY(p3, m, herzwinkel - 5, 0.9);
            g.drawLine(p1.x, p1.y, p3.x, p3.y,paint_black);
            g.drawLine(p1.x, p1.y, p2.x, p2.y,paint_black);
            WinkelZuXY(p3, m, 90, 1.5);
            g.drawText("  " + String.valueOf(herzwinkel) + "°", 0, p3.y,paint_black);
        } // Pfeil

        WinkelZuXY(p, md, 90, 1.6);

    } // Dreieck


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = canvas.getWidth();
        height = canvas.getHeight();

        float sw = 0;
        if (width>height) {
            sw = width/100;
        } else {
            sw = height/100;
        }

        paint_black.setStrokeWidth(sw);

        //Log.d(TAG,"Drawing ECG");
        MaleDreieck(canvas);

    } // Paint


}
