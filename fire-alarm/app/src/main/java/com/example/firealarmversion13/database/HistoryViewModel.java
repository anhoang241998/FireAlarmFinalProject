package com.example.firealarmversion13.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.firealarmversion13.model.Room;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {
    private HistoryRepository repository;
    private LiveData<List<Room>> allHistoryData;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        repository = new HistoryRepository(application);
        allHistoryData = repository.getAllHistoryData();
    }

    public LiveData<List<Room>> getAllHistoryData() {
        return allHistoryData;
    }

    public void insertHistory(Room historyData) {
        repository.insertHistoryData(historyData);
    }

    public void updateHistory(Room historyData) {
        repository.updateHistoryData(historyData);
    }

    public void deleteHistory(Room historyData) {
        repository.deleteHistoryData(historyData);
    }
}
