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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Chronometer mChronometer;
    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView timerValue = findViewById(R.id.tv_timer_value);

        Button ButtonRaschitat = findViewById(R.id.buttonRaschitat);
        Button startButton = findViewById(R.id.btn_start);
        Button pauseButton = findViewById(R.id.btn_pause);
        Button stopButton = findViewById(R.id.btn_stop);
        startButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        ButtonRaschitat.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                try {
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                }catch (Exception e) {
                    Toast.makeText(this,"ошибка тут",Toast.LENGTH_LONG).show();
                }


                break;
            case R.id.btn_pause:
                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);
                break;
            case R.id.btn_stop:
                startTime = 0L;
                customHandler.removeCallbacks(updateTimerThread);
                timerValue.setText(getString(R.string.timer_val));
                break;
            case R.id.buttonRaschitat:
                EditText Impuls = findViewById(R.id.editTextImpuls);
                EditText Postoyannay = findViewById(R.id.editTextPostoyannay);
                final EditText Volts = findViewById(R.id.editTextVolts);
                final EditText Ampers = findViewById(R.id.editTextAmpers);
                final EditText Time = findViewById(R.id.editTextTime);

                final TextView Mosnost = findViewById(R.id.textViewMosnost);
                final TextView Itog = findViewById(R.id.textViewItog);
                final TextView pogresnost = findViewById(R.id.textViewPogresnost);
                // учтенная мощность
                int n = Integer.parseInt(Impuls.getText().toString());
                int a = Integer.parseInt(Postoyannay.getText().toString());
                float t = Float.parseFloat(Time.getText().toString());
                float pogresn = (3600 * n / a * t) * 100;
                String s = Float.toString(pogresn);
                pogresnost.setText(s);


                //общая мощность
                float A = Float.parseFloat(Ampers.getText().toString());
                float V =  Float.parseFloat(Volts.getText().toString());
                float summosnost = A * V;
                Mosnost.setText(Float.toString(summosnost));

                //погрешность
                float pog = (pogresn-summosnost)/summosnost*100;
                String formatdobl = String.format("%.2f",pog);
                Itog.setText(formatdobl);
                break;
        }
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText("" + mins + ":" + String.format("%02d", secs) + ":" + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }
    };
}





