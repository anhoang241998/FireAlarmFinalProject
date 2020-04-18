package com.example.firealarmversion13;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.firealarmversion13.model.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements DataChangeListener {

    @BindView(R.id.txt_home_title)
    TextView txtHomeTitle;
    @BindView(R.id.txt_home_alarm)
    TextView txtHomeAlarm;
    @BindView(R.id.txt_home_wire_break)
    TextView txtHomeWireBreak;
    @BindView(R.id.txt_home_fault)
    TextView txtHomeFault;
    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.home_root)
    MotionLayout homeRoot;
    private Unbinder unbinder;
    private Query packageInfo = FirebaseDatabase.getInstance().getReference().child("list");

    private RoomViewModel roomViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        startService();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        roomViewModel= ViewModelProviders.of(getActivity()).get(RoomViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        MyService.setListener(this);
    }

    private void startService() {
        if (!checkServiceRunning()) {
            Intent intent = new Intent(getActivity(), MyService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getActivity().startForegroundService(intent);
            } else {
                getActivity().startService(intent);
            }
        }
    }

    private boolean checkServiceRunning() {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null)
            for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (MyService.class.getName().equals(serviceInfo.service.getClassName())) {
                    return true;
                }
            }
        return false;
    }
    private final ValueEventListener packageValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<Room> rooms = new ArrayList<>();
            if (dataSnapshot != null) {
                int alarm = 0;
                int fault = 0;
                int wire_break = 0;
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Room room = item.getValue(Room.class);
                    alarm +=room.getAlarm();
                    fault +=room.getFault();
                    wire_break +=room.getWire_break();
                    rooms.add(room);
                }
                txtHomeAlarm.setText((getResources().getString(R.string.alarm) + alarm));
                txtHomeWireBreak.setText((getResources().getString(R.string.wire_break) + wire_break));
                txtHomeFault.setText((getResources().getString(R.string.fault) + fault));
            }
            roomViewModel.setRooms(rooms);


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        Integer status = snapshot.getValue(Integer.class);
        Log.d("TEST_STATUS", "onDataChange: " + status);
        if (status == 1) {
            txtHomeTitle.setText(R.string.system_error);
            imgHome.setImageResource(R.drawable.firestate);
        } else {
            txtHomeTitle.setText(R.string.system_normal);
            imgHome.setImageResource(R.drawable.tick);

        }

        packageInfo.removeEventListener(packageValueListener);
        packageInfo.addValueEventListener(packageValueListener);
    }
}

