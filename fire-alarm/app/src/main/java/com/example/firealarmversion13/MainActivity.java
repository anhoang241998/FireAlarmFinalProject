package com.example.firealarmversion13;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.toolbar_main)
    Toolbar toolbarMain;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarMain);
        dialog = new Dialog(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();

                });


        //Bottom Navigation + animation
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Home()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(wifiStateReceiver, intentFilter);
    }

    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showDialog();
        }
    };

    public void showDialog() {
        //Initialize connectivityManager
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get active network info
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //Check network status
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            //When internet is inactive
            //set content view
            dialog.setContentView(R.layout.no_internet_alert_dialog);
            //Set outside touch
            dialog.setCanceledOnTouchOutside(false);
            //Set dialog width and height
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            //Set transparent background
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //Set animation
            dialog.getWindow().getAttributes().windowAnimations =
                    android.R.style.Animation_Dialog;
            //Initialize dialog variable
            Button btTryAgain = dialog.findViewById(R.id.bt_try_again);
            //Perform onClick listener
            btTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
            //show dialog
            dialog.show();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }


    Home homeFragment = new Home();
    RoomFragment roomFragment = new RoomFragment();
    MapFragment mapFragment = new MapFragment();
    private boolean isItem1 = true;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());
        switch (menuItem.getItemId()) {
            case R.id.item1: {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.fragment_container, homeFragment).addToBackStack(null).commit();
                isItem1 = true;
                return true;
            }
            case R.id.item2: {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.fragment_container, roomFragment).addToBackStack(null).commit();
                isItem1 = false;
                return true;
            }

            case R.id.item3: {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.fragment_container, mapFragment).addToBackStack(null).commit();
                isItem1 = false;
                return true;
            }
        }
        return false;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (!isItem1) {
            isItem1 = true;
            bottomNavigationView.setSelectedItemId(R.id.item1);
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.abouttoolbar:
                openActivity2();
                return true;

            case R.id.privacytoolbar:
                openActivity3();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openActivity2() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    public void openActivity3() {
        Intent intent = new Intent(MainActivity.this, PrivacyActivity.class);
        startActivity(intent);
    }
}




