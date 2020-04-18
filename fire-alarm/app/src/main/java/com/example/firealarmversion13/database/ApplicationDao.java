package com.example.firealarmversion13.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.firealarmversion13.model.Room;

import java.util.List;

@Dao
public interface ApplicationDao {

    @Insert
    void insertHistoryData(Room historyData);

    @Update
    void updateHistoryData(Room historyData);

    @Delete
    void deleteHistoryData(Room historyData);



    @Query("select * from room_table")
    LiveData<List<Room>> getAllHistoryData();

    @Query("select * from room_table")
    List<Room> getAllHistory();

}
