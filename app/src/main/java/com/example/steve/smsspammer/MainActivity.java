package com.example.steve.smsspammer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private EditText message;
    private EditText number;
    private EditText send_number;
    private TextView status;
    private Handler handler;
    private MainActivity main;
    private int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private int howmanytimes;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = this;
        handler = new Handler();

        message = (EditText) findViewById(R.id.message);
        number = (EditText) findViewById(R.id.number);
        send_number = (EditText) findViewById(R.id.send_number);
        status = (TextView) findViewById(R.id.status);

        Button start = (Button) findViewById(R.id.start);

        if(!hasPermission())
        {
            getPermission();
        }
        else
        {
            MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.getText().equals("") || number.getText().equals(""))
                {
                    Toast.makeText(main,"Please fill number and message",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (MY_PERMISSIONS_REQUEST_SEND_SMS == 0)
                    {
                        status.setText("Sending...");
                        howmanytimes = Integer.parseInt(send_number.getText().toString());
                        sendsms.run();
                    }
                    else
                    {
                        getPermission();
                    }
                }
            }
        });
    }

    Runnable sendsms = new Runnable() {
        @Override
        public void run() {
            try {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(number.getText().toString(), null, message.getText().toString(), null, null);
                counter++;
            } finally
            {
                handler.postDelayed(sendsms, 2000);
                if (counter >= howmanytimes)
                {
                    handler.removeCallbacks(sendsms);
                    counter = 0;
                    status.setText("Completed!");
                }
            }
        }
    };

    private boolean hasPermission()
    {
        int per = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.SEND_SMS);

        if (per == 0)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    private void getPermission()
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
                Toast.makeText(main,"Please fill number and message",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);

        }
    }
}
