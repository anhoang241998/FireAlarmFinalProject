package com.example.firealarmversion13.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.firealarmversion13.model.Room;

@Database(entities = {Room.class},version = 1)
public abstract class FireAlarmDatabase extends RoomDatabase {
    private volatile static FireAlarmDatabase instance;

    abstract ApplicationDao getApplicationDao();

    public synchronized static FireAlarmDatabase getInstance(Context context) {
        if (instance == null) {
            instance = androidx.room.Room.databaseBuilder(
                    context.getApplicationContext(),
                    FireAlarmDatabase.class,
                    "FireAlarmDatabase.db"
            )
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }

}
