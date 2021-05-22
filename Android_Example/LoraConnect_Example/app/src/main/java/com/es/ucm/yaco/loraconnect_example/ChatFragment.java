package com.es.ucm.yaco.loraconnect_example;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.es.ucm.yaco.loraConnect.data.Message;
import com.es.ucm.yaco.loraconnect_example.data.Chat;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    ListView listViewMessages;
    ArrayList<Message> l;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);

        listViewMessages = (ListView)view.findViewById(R.id.listViewConversationId);

        MsgInChatAdapter adapter = new MsgInChatAdapter(getActivity().getApplicationContext(), getData());
        listViewMessages.setAdapter(adapter);

        return view;
    }

    private ArrayList<Message> getData(){
        l= new ArrayList<Message>();
        Message m;
        for(int i=0; i<3; i++){
            m = new Message();
            m.setDestination("pepe"+i);
            m.setMsg("Prueba"+50+i);
            l.add(m);
        }
        return l;
    }
}
