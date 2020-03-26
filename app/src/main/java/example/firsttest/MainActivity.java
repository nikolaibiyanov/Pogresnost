package example.firsttest;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    long startTime = 0;
    TextView timerTextView,Mosnost,Itog,pogresnost;
    private Handler customHandler = new Handler();
    String  pTime;

    TextView tv5;
    EditText Ampers,Impuls,Postoyannay,Volts,Time,et_V1, et_V2,et_V3;
    Button SummaTok,SredVolts;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            long milliseconds = millis % 1000;
            timerTextView.setText(String.format("%d:%02d:%02d", minutes, seconds, milliseconds));
            timerHandler.postDelayed(this, 0);
            pTime = timerTextView.getText().toString();
            ParseTime();
        }
    };

    private void ParseTime() {
      //  Toast.makeText(this,pTime,Toast.LENGTH_LONG).show();
        String min = pTime.split(":")[0];
        String sec =  pTime.split(":")[1];
        String mil = pTime.split(":")[2];
       // Toast.makeText(this,min+sec+mil,Toast.LENGTH_LONG).show();
        String vremy = sec + "." + mil;
     //   Toast.makeText(this,vremy,Toast.LENGTH_LONG).show();
        final EditText Time = findViewById(R.id.editTextTime);
        Time.setText(vremy);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button ButtonRaschitat = findViewById(R.id.buttonRaschitat);
        Button startButton = findViewById(R.id.btn_start);
        SummaTok = findViewById(R.id.buttonSumTok);
        SredVolts = findViewById(R.id.buttonSredVolts);
        startButton.setOnClickListener(this);
        ButtonRaschitat.setOnClickListener(this);
        SummaTok.setOnClickListener(this);
        SredVolts.setOnClickListener(this);

        timerTextView = findViewById(R.id.timerTextView);
        Ampers = findViewById(R.id.editTextAmpers);
        et_V1 = findViewById(R.id.editText1);
        et_V2 = findViewById(R.id.editText2);
        et_V3 = findViewById(R.id.editText3);

        Impuls = findViewById(R.id.editTextImpuls);
        Postoyannay = findViewById(R.id.editTextPostoyannay);
        Volts = findViewById(R.id.editTextVolts);
        Time = findViewById(R.id.editTextTime);

        Mosnost = findViewById(R.id.textViewMosnost);
        Itog = findViewById(R.id.textViewItog);
        pogresnost = findViewById(R.id.textViewPogresnost);
        tv5 = findViewById(R.id.textView5);

        Button b =  findViewById(R.id.btn_start);
        b.setText("start");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("stop")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText("start");
                } else {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    b.setText("stop");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRaschitat:
                try {

                    // учтенная мощность
                    if (Time.getText().equals("")) {
                        Toast.makeText(this, "проверьте импульсы", Toast.LENGTH_LONG).show();
                    }
                    if (Volts.getText().equals("")) {
                        Toast.makeText(this, "проверьте импульсы", Toast.LENGTH_LONG).show();
                    }
                    int n = Integer.parseInt(Impuls.getText().toString());
                    int a = Integer.parseInt(Postoyannay.getText().toString());
                    float t = Float.parseFloat(Time.getText().toString());
                    float pogresn = ((3600 * n) / (a * t)) * 1000;
                    String s = Float.toString(pogresn);
                    pogresnost.setText(s);


                    //общая мощность
                    float A = Float.parseFloat(Ampers.getText().toString());
                    float V = Float.parseFloat(Volts.getText().toString());
                    float summosnost = A * V;
                    Mosnost.setText(Float.toString(summosnost));

                    //погрешность
                    float pog = (pogresn - summosnost) / summosnost * 100;
                    String formatdobl = String.format("%.2f", pog);
                    Itog.setText(formatdobl);
                    break;

                } catch (ArithmeticException es) {
                    Toast.makeText(this, "Ошибка" + es.toString(), Toast.LENGTH_LONG).show();
                } catch (Exception es) {
                    Toast.makeText(this, "Проверьте заполненость всех строк", Toast.LENGTH_LONG).show();
                }
            case R.id.buttonSumTok:
                try {
                    if (et_V1.getText().equals("") || et_V2.getText().equals("") || et_V3.getText().equals("")) {
                        Toast.makeText(this, "Не должно быть пустого поля", Toast.LENGTH_LONG).show();
                    }
                    float a1 = Float.parseFloat(et_V1.getText().toString());
                    float a2 = Float.parseFloat(et_V2.getText().toString());
                    float a3 = Float.parseFloat(et_V3.getText().toString());
                    float SumTok = a1 + a2 + a3;

                    Ampers.setText(Float.toString(SumTok));
                    break;
                }catch (Exception e){};

            case R.id.buttonSredVolts:
                try {
                    if (et_V1.getText().equals("") || et_V2.getText().equals("") || et_V3.getText().equals("")) {
                        Toast.makeText(this, "Не должно быть пустого поля", Toast.LENGTH_LONG).show();
                    }
                    float v1 = Float.parseFloat(et_V1.getText().toString());
                    float v2 = Float.parseFloat(et_V2.getText().toString());
                    float v3 = Float.parseFloat(et_V3.getText().toString());
                    float sredvolt = (v1 + v2 + v3) / 3;
                    Volts.setText(Float.toString(sredvolt));
                    break;
                }catch (Exception e){};
            }
        }
    }









