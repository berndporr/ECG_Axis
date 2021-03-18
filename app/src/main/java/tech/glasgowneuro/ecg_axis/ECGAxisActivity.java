package tech.glasgowneuro.ecg_axis;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    TextView attyslink;


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


    private void checkEntry() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(angle.getWindowToken(), 0);
        try {
            nattempts++;
            int Achse = Integer.parseInt(angle.getText().toString());
            WinkelOk = ((Math.abs(Achse - herzwinkel) < 10) ||
                    (Math.abs((Achse - 360) - herzwinkel) < 6) ||
                    (Math.abs((Achse + 360) - herzwinkel) < 6));
        } catch (NumberFormatException ignored) {
            evaluation.setText("Input error");
            return;
        }
        if (WinkelOk) {
            evaluation.setText("Yass! That's right!");
            ecgAxisView.revealAngle(true);
            angle.setEnabled(false);
            ok.setEnabled(false);

        } else {
            evaluation.setText("Sorry, try again.");
            if (nattempts > 3) {
                solution.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecgaxis);

        attyslink = (TextView) findViewById(R.id.attyslink);
        attyslink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.attys.tech";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        angle = (EditText) findViewById(R.id.angle);
        angle.setEnabled(false);
        angle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                checkEntry();
                return true;
            }
        });
        ecgAxisView = (ECGAxisView) findViewById(R.id.ecgaxisview);
        ecgTracesView = (ECGTracesView) findViewById(R.id.ecgtracesview);
        evaluation = (TextView) findViewById(R.id.evaluate);
        solution = (Button) findViewById(R.id.solution);
        solution.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ecgAxisView.revealAngle(true);
                evaluation.setText(" ");
                angle.setText("");
                angle.setEnabled(false);
                ok.setEnabled(false);
                solution.setVisibility(View.INVISIBLE);
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
                angle.setEnabled(true);
                ok.setEnabled(true);
            }
        });
        ok = (Button) findViewById(R.id.okbutton);
        ok.setEnabled(false);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkEntry();
            }
        });
    }
}
