package appkill.explodingstar.com.appkill;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class AppKillTimer extends AppCompatActivity {

    private static final String COUNT_TIME = "CountdownTime";

    private Button startButton;
    private Button pauseButton;
    private Button resetButton;
    private Button timePickerButton;

    private TextView timerValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_kill_timer);

        final CountDownTimerWithPause countdown = new CountDownTimerWithPause(10*1000, 1000, true) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerValue.setText("Seconds remaining: "+millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                timerValue.setText(R.string.done);
            }
        };

        //toolbar stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //mail button stuff
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.insult), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        timerValue = (TextView) findViewById(R.id.timerValue);
        timerValue.setText(R.string.input_wait);

        timePickerButton = (Button) findViewById(R.id.timePicker);
        timePickerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //set time button actions
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = new TimePickerFragment();;
                dialog.show(manager, COUNT_TIME);

            }
        });

        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //start button actions
                startButton.setEnabled(false);
                countdown.create();
            }
        });

        pauseButton = (Button) findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //pause button actions
                if (countdown.isPaused()) {
                    countdown.resume();
                } else {
                    countdown.pause();
                }
            }
        });

        resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //reset button actions
                startButton.setEnabled(true);
                countdown.cancel();
            }
        });
    }
}




