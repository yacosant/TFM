package com.es.ucm.yaco.loraconnect_example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.es.ucm.yaco.loraConnect.LoraConnect;
import com.es.ucm.yaco.loraConnect.data.Message;

import java.util.ArrayList;

public class MsgInChatAdapter extends BaseAdapter {
    Context context;
    ArrayList<Message> list;

    public MsgInChatAdapter(Context context, ArrayList<Message> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView nombre;
        TextView msg;

        //if(convertView==null)
        if(list.get(position).getDestination().equals("pepe0"))
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_msg_left,null);
        else
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_msg_right,null);

        msg = convertView.findViewById(R.id.show_message);
        msg.setText(list.get(position).getMsg());

        return convertView;
    }
}
