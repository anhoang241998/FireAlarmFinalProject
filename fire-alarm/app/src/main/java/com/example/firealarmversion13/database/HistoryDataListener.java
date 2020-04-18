package com.example.firealarmversion13.database;

import com.example.firealarmversion13.model.Room;

import java.util.List;

public interface HistoryDataListener {
    void onGetDataCompleted(List<Room> data, Room historyData);
}
