package com.example.firealarmversion13;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firealarmversion13.model.Room;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class RoomFragment extends Fragment {
    @BindView(R.id.rv_rooms)
    RecyclerView rvRooms;

    private Unbinder unbinder;
    private RoomAdapter roomAdapter;

    public RoomFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_room, container, false);
        unbinder = ButterKnife.bind(this,view);
        roomAdapter =new RoomAdapter(getContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RoomViewModel roomViewModel= ViewModelProviders.of(getActivity()).get(RoomViewModel.class);
        roomViewModel.getRoomsLiveData().observe(getActivity(), rooms -> {
            Log.d("TEST_DATA", "onActivityCreated: "+rooms);
            roomAdapter.setRooms(rooms);
            rvRooms.setLayoutManager(new LinearLayoutManager(getContext()));
            rvRooms.setHasFixedSize(true);
            rvRooms.setAdapter(roomAdapter);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}

