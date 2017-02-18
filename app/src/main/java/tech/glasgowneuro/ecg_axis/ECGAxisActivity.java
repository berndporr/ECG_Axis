package tech.glasgowneuro.ecg_axis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * The main activity
 */


public class ECGAxisActivity extends AppCompatActivity {


    private float herzwinkel;
    private boolean WinkelOk;
    int nattempts = 0;

    Button dice;
    Button solution;
    Button ok;
    EditText angle;
    ECGAxisView ecgAxisView;
    ECGTracesView ecgTracesView;
    TextView evaluation;

    private void pruefeEingabe() {
        try {
            nattempts++;
            int Achse = Integer.parseInt(angle.getText().toString());
            WinkelOk = ((Math.abs(Achse - herzwinkel) < 10) ||
                    (Math.abs((Achse - 360) - herzwinkel) < 6) ||
                    (Math.abs((Achse + 360) - herzwinkel) < 6));
        } catch (NumberFormatException exception) {
        }
    }

    private void neuerHerzwinkel() {
        if (((int) (Math.random() * 10.0)) == 2) {
            herzwinkel = (int) (Math.random() * 360.0 - 150.0);
        } else {
            herzwinkel = (int) (Math.random() * 150.0 - 30.0);
        }
        ecgAxisView.setHerzwinkel(herzwinkel);
        ecgTracesView.setAngle(herzwinkel);
        nattempts = 0;
        angle.setText("");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecgaxis);

        angle = (EditText) findViewById(R.id.angle);
        ecgAxisView = (ECGAxisView) findViewById(R.id.ecgaxisview);
        ecgTracesView = (ECGTracesView) findViewById(R.id.ecgtracesview);
        evaluation = (TextView) findViewById(R.id.evaluate);
        solution = (Button) findViewById(R.id.solution);
        solution.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ecgAxisView.revealAngle(true);
            }
        });
        solution.setVisibility(View.INVISIBLE);
        dice = (Button) findViewById(R.id.dice);
        dice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                neuerHerzwinkel();
                evaluation.setText(" ");
                ecgAxisView.revealAngle(false);
                solution.setVisibility(View.INVISIBLE);
            }
        });
        ok = (Button) findViewById(R.id.okbutton);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pruefeEingabe();
                if (WinkelOk) {
                    evaluation.setText("Yass! That's right! ");
                    ecgAxisView.revealAngle(true);
                } else {
                    evaluation.setText("Sorry, try again. ");
                    if (nattempts>3) {
                        solution.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
}
