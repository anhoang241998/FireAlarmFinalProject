package com.example.firealarmversion13;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MyService extends Service implements ValueEventListener {

    private Query status = FirebaseDatabase.getInstance().getReference().child("status");
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private static DataChangeListener listener;
    private static DataSnapshot snapshot;


    public static void setListener(DataChangeListener mListener) {
        MyService.listener=null;
        MyService.listener = mListener;
        if (snapshot!=null)
            MyService.listener.onDataChange(snapshot);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        status.addValueEventListener(this);
        createNotification();
        showNotification();


        return START_STICKY;
    }


    private void createNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        notificationBuilder = new NotificationCompat.Builder(this, "CHANNEL_TITLE")
                .setSmallIcon(R.drawable.ic_notification)
                .setOngoing(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("")
                .setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("CHANNEL_TITLE",
                    getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }


    }

    private void showNotification() {
        startForeground(123, notificationBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeAllNotification();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        snapshot=dataSnapshot;
        if (listener != null)
            listener.onDataChange(dataSnapshot);
        Integer status = dataSnapshot.getValue(Integer.class);
        if (status != null) {
            if (status == 1) {
                notificationBuilder.setContentText(this.getResources().getText(R.string.system_error));
            } else {
                notificationBuilder.setContentText(this.getResources().getText(R.string.system_normal));
            }
            showNotification();
        }
    }

    private void removeAllNotification() {
        if (notificationManager != null)
            notificationManager.cancelAll();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }
}
