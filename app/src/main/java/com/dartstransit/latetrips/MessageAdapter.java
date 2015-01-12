package com.dartstransit.latetrips;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends BaseAdapter {

    private List<DartsTrip> listData;
    private LayoutInflater layoutInflater;

    public MessageAdapter(Context context, List<DartsTrip> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.message_list, null);
            holder = new ViewHolder();
            holder.IdView = (TextView) convertView.findViewById(R.id.eventId);
            holder.clientIdView = (TextView) convertView.findViewById(R.id.tvClientId);
            holder.timeView = (TextView) convertView.findViewById(R.id.tvTime);
            holder.runView = (TextView) convertView.findViewById(R.id.tvRun);
            holder.timeHeaderView = (TextView) convertView.findViewById(R.id.tvTimeHeader);
            holder.clientLayout = (LinearLayout) convertView.findViewById(R.id.clientLayout);
            holder.timeLayout = (LinearLayout) convertView.findViewById(R.id.timeLayout);
            holder.runLayout = (LinearLayout) convertView.findViewById(R.id.runLayout);
            holder.pickUpLayourView = (LinearLayout) convertView.findViewById(R.id.pickupLayout);
            holder.pickupHeaderView = (TextView) convertView.findViewById(R.id.tvpickupHeader);
            holder.pickupView = (TextView) convertView.findViewById(R.id.tvPickup);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DartsTrip mi = listData.get(position);

        Log.d("lolo",mi.toString());
        String clientId = mi.ClientID;
        try {
            holder.IdView.setText(String.valueOf(mi._id));
            holder.clientIdView.setText(clientId);
            if (!mi.IsLateTrip) {
                Log.d("lola", "Long On Board Trip");
                holder.timeHeaderView.setText("On Board");
                holder.timeView.setText(mi.On_Board);

                if (mi.ClientIsOnBoard()){
                    holder.pickupHeaderView.setText("DropOff");
                    holder.pickupView.setText(mi.GetDropffTime());
                    Log.d("lola", "Client On Board");
                }else{
                    holder.pickupHeaderView.setText("PickUp");
                    holder.pickupView.setText(mi.GetPickUpTime());
                    Log.d("lola", "Client Waiting");
                }
            } else {
                Log.d("lola", "Late PickUp Trip");
                holder.timeHeaderView.setText("Late");
                holder.timeView.setText(mi.MinsLate);
                holder.pickupView.setText(mi.GetPickUpTime());
                holder.pickupHeaderView.setText("PickUp");
            }
            holder.runView.setText(mi.Run_Name);
            //row.setBackgroundColor(mi.BackGroundColor);
            holder.timeView.setBackgroundColor(mi.BackGroundColor);
            holder.runLayout.setBackgroundColor(mi.BackGroundColor);
            holder.timeLayout.setBackgroundColor(mi.BackGroundColor);
            holder.clientLayout.setBackgroundColor(mi.BackGroundColor);
            holder.pickUpLayourView.setBackgroundColor(mi.BackGroundColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder {
        TextView IdView;
        TextView clientIdView;
        TextView timeView;
        TextView runView;
        TextView timeHeaderView;
        LinearLayout clientLayout;
        LinearLayout timeLayout;
        LinearLayout runLayout;
        LinearLayout pickUpLayourView;
        TextView pickupHeaderView;
        TextView pickupView;
    }
}
