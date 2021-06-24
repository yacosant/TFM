package com.es.ucm.yaco.loraconnect_example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactsListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> list;

    public ContactsListAdapter(Context context, ArrayList<String> list) {
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

        nombre.setText(list.get(position));
        msg.setText("");
        img.setImageResource(android.R.drawable.presence_online);

        return convertView;
    }

}
