package org.xdty.easylog.example;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.xdty.easylog.EasyLog;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    private boolean isStopped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        EasyLog.start(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // generate log
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                Random random = new Random();
                while (!isStopped) {
                    Log.d("xxx", "" + count++);
                    SystemClock.sleep(random.nextInt(1000) + 1000);
                }
            }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isStopped = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EasyLog.stop(this);
    }
}
