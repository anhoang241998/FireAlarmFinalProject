package com.example.firealarmversion13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TextView firebase = (TextView) findViewById(R.id.firebase);
        TextView arduino = (TextView) findViewById(R.id.arduino);
        TextView horing = (TextView) findViewById(R.id.horing);
        firebase.setMovementMethod(LinkMovementMethod.getInstance());
        arduino.setMovementMethod(LinkMovementMethod.getInstance());
        horing.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
