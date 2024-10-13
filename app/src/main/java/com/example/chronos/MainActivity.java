package com.example.chronos;

import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
//import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        Chronometer.OnChronometerTickListener{

    Button btnStart, btnStop, btnReset;
    Chronometer timer;
    int count=0;
    long t=0;
    TextView tvTime;
    final String DIR_SD = "Files_of_GPS";
    final String FILENAME_SD = "gps_file001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart= (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);
        btnStop= (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(this);
        btnReset= (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);
        timer= (Chronometer) findViewById(R.id.timer);
        tvTime= (TextView) findViewById(R.id.tvTime);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v==btnStart) {
            count=0;
            timer.setBase(SystemClock.elapsedRealtime());
            timer.setOnChronometerTickListener(this);
            timer.start();
        }
        if(v==btnStop)
            timer.stop();
        if(v==btnReset) {
            timer.stop();
            timer.setBase(SystemClock.elapsedRealtime());
            count=0;
            tvTime.setText("Time=" + t + " & " + count);
        }
    }

    @Override
    public void onChronometerTick(Chronometer timer) {
        t=(SystemClock.elapsedRealtime()-timer.getBase())/1000;
        if (t%5==0) {
            tvTime.setText("Time=" + t + " & " + count);
            goWrite();
            count++;
        }
    }

    public void goWrite() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "SD-card not  " +
                    " access: " + Environment.getExternalStorageState(), Toast.LENGTH_LONG).show();
            return;
        }
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        sdPath.mkdirs();
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile,true));
            if (count==1)
                bw.write("Location #"+count+"\n");
            else
                bw.append("Location #"+count+"\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}