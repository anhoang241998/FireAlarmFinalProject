package com.example.firealarmversion13.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "room_table")
public class Room implements Cloneable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int floorAddress;
    private int roomAddress;
    private int alarm;
    private int wire_break;
    private int fault;
    //private String time;


    public Room(int alarm, int fault, int floorAddress, int roomAddress, int wire_break) {
        this.floorAddress = floorAddress;
        this.roomAddress = roomAddress;
        this.alarm = alarm;
        this.wire_break = wire_break;
        this.fault = fault;
    }

    public Room() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFloorAddress() {
        return floorAddress;
    }

    public void setFloorAddress(int floorAddress) {
        this.floorAddress = floorAddress;
    }

    public int getRoomAddress() {
        return roomAddress;
    }

    public void setRoomAddress(int roomAddress) {
        this.roomAddress = roomAddress;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    public int getWire_break() {
        return wire_break;
    }

    public void setWire_break(int wire_break) {
        this.wire_break = wire_break;
    }

    public int getFault() {
        return fault;
    }

    public void setFault(int fault) {
        this.fault = fault;
    }

    @Override
    public String toString() {
        return "HistoryData{" +
                "id=" + id +
                ", floorAddress=" + floorAddress +
                ", roomAddress=" + roomAddress +
                ", alarm=" + alarm +
                ", wire_break=" + wire_break +
                ", fault=" + fault +
                '}';
    }
}
