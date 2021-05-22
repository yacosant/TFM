package com.es.ucm.yaco.loraconnect_example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.es.ucm.yaco.loraconnect_example.data.Chat;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Chat> list;

    public ChatListAdapter(Context context, ArrayList<Chat> list) {
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
        ImageView img;
        TextView nombre;
        TextView msg;

        if(convertView==null)
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_itemchat,null);

        img = convertView.findViewById(R.id.imageViewChat);
        nombre = convertView.findViewById(R.id.textViewNombreChat);
        msg = convertView.findViewById(R.id.textViewUltimoMsgChat);

        nombre.setText(list.get(position).getLastMsg().getDestination());
        msg.setText(list.get(position).getLastMsg().getMsg());
        if(list.get(position).isOnline())
            img.setImageResource(R.drawable.online);
        else
            img.setImageResource(R.drawable.offline);
        return convertView;
    }
}
