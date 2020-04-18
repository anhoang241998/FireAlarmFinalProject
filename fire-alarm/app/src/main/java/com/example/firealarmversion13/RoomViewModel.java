package com.example.firealarmversion13;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.firealarmversion13.model.Room;

import java.util.List;

public class RoomViewModel extends ViewModel {
    private MutableLiveData<List<Room>> roomsLiveData = new MutableLiveData<>();

    public MutableLiveData<List<Room>> getRoomsLiveData() {
        return roomsLiveData;
    }

    public void setRooms(List<Room> rooms) {
        this.roomsLiveData.setValue(rooms);
    }

}
