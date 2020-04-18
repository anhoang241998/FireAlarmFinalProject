package com.example.firealarmversion13;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.firealarmversion13.model.Room;
import com.google.android.material.card.MaterialCardView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {


    @BindView(R.id.card_room_1)
    MaterialCardView cardRoom1;
    @BindView(R.id.card_room_2)
    MaterialCardView cardRoom2;
    private Unbinder unbinder;

    public MapFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RoomViewModel roomViewModel = ViewModelProviders.of(getActivity()).get(RoomViewModel.class);
        roomViewModel.getRoomsLiveData().observe(getActivity(), rooms -> {
            Room room1 = rooms.get(0);
            Room room2 = rooms.get(1);
            if (room1.getFault() < 1 && room1.getWire_break() < 1 && room1.getAlarm() < 1) {
                cardRoom1.setCardBackgroundColor(Color.parseColor("#1dd1a1"));
            } else {
                cardRoom1.setCardBackgroundColor(Color.parseColor("#ff7979"));
            }

            if (room2.getFault() < 1 && room2.getWire_break() < 1 && room2.getAlarm() < 1) {
                cardRoom2.setCardBackgroundColor(Color.parseColor("#1dd1a1"));
            } else {
                cardRoom2.setCardBackgroundColor(Color.parseColor("#ff7979"));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
