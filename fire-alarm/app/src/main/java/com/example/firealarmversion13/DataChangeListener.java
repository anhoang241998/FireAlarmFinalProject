package com.example.firealarmversion13;

import com.google.firebase.database.DataSnapshot;

public interface DataChangeListener {
    void onDataChange(DataSnapshot snapshot);
}
