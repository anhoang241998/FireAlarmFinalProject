package com.example.firealarmversion13;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firealarmversion13.model.Room;
import com.ramotion.foldingcell.FoldingCell;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomHolder> {

    private List<Room> rooms;
    private Context context;

    public RoomAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate((R.layout.item), parent, false);
        return new RoomHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomHolder holder, int position) {
        holder.bind(rooms.get(position), context);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
        notifyDataSetChanged();
    }


    static class RoomHolder extends RecyclerView.ViewHolder {
        TextView txtItemAlarm;
        TextView txtItemWireBreak;
        TextView txtItemFault;
        FrameLayout cellContent;
        TextView txtItemTitle;
        TextView txtItemSubtitle;
        FrameLayout cellTitle;
        FoldingCell cellRoot;


        RoomHolder(@NonNull View itemView) {
            super(itemView);
            txtItemSubtitle=itemView.findViewById(R.id.txt_item_subtitle);
            txtItemTitle=itemView.findViewById(R.id.txt_item_title);
            txtItemAlarm=itemView.findViewById(R.id.txt_item_alarm);
            txtItemFault=itemView.findViewById(R.id.txt_item_fault);
            txtItemWireBreak=itemView.findViewById(R.id.txt_item_wire_break);
            cellContent=itemView.findViewById(R.id.cell_content);
            cellRoot=itemView.findViewById(R.id.cell_root);
            cellTitle=itemView.findViewById(R.id.cell_title);
        }

        void bind(Room room, Context context) {
            txtItemAlarm.setText((context.getResources().getString(R.string.alarm) + room.getAlarm()));
            txtItemWireBreak.setText((context.getResources().getString(R.string.wire_break) + room.getWire_break()));
            txtItemFault.setText((context.getResources().getString(R.string.fault) + room.getFault()));

            if (room.getFault() < 1 && room.getWire_break() < 1 && room.getAlarm() < 1){
                txtItemTitle.setText(R.string.system_normal);
                cellTitle.setBackgroundColor(Color.parseColor("#1dd1a1"));
                cellContent.setBackgroundColor(Color.parseColor("#1dd1a1"));
            }
            else {
                txtItemTitle.setText(R.string.system_error);

                cellTitle.setBackgroundColor(Color.parseColor("#ff7979"));
                cellContent.setBackgroundColor(Color.parseColor("#ff7979"));
            }

            txtItemSubtitle.setText((context.getResources().getString(R.string.room_address) + room.getRoomAddress() + "\n" +
                    context.getResources().getString(R.string.floor_address) + room.getFloorAddress()));

            cellRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cellRoot.toggle(false);
                }
            });


        }

    }
}
