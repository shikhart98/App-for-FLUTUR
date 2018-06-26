package com.example.shikh.flutur;


import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private double width, height;

    ImageView imgView;
    public static int X = 0;
    public static int Y = 0;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        making my canvas full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(Color.WHITE, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = findViewById(R.id.imgView);
        tv = findViewById(R.id.tv);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Display display = getWindowManager().getDefaultDisplay();
//        calculating the borders
        width = display.getWidth() - (50 * 1.5);
        height = display.getHeight() - (50 * 1.5);
//        subtracted values above are in order to keep ball inside the physical view (some correction is required),
//        because if you see ball at 0,0 its center is not at 0,0 rather the top left corner of image view is at 0,0.
//        Therfore we are required to subtract a correction from the max height and max width of a phone.
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//          multiplied by 4 in order to increase the speed of the ball
            X -= (int) (event.values[0]) * 4;
            Y += (int) (event.values[1]) * 4;
//          setting up the boundary conditions
            if ((X < 0 || X > width) && (Y >= 0 && Y <= height)) {
                if (X < 0) {
                    X = 0;
                } else {
                    X = (int) width;
                }
            } else if ((X >= 0 && X <= width) && (Y < 0 || Y > height)) {
                if (Y < 0) {
                    Y = 0;
                } else {
                    Y = (int) height;
                }
            } else if (X < 0 && Y < 0) {
                X = 0;
                Y = 0;
            } else if (X > width && Y > height) {
                X = (int) width;
                Y = (int) height;
            } else if (X > width && Y < 0) {
                X = (int) width;
                Y = 0;
            } else if (X < 0 && Y > height) {
                X = 0;
                Y = (int) height;
            }

            imgView.setY(Y);
            imgView.setX(X);
//            logging values here
            Log.d("Values", "X: " + X + " Y: " + Y);
            String result = "X: " + X + "\nY: " + Y;
            tv.setText(result);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
