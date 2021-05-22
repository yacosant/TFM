package com.es.ucm.yaco.loraconnect_example;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.es.ucm.yaco.loraConnect.data.Message;
import com.es.ucm.yaco.loraconnect_example.data.Chat;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ChatListFragment extends Fragment {
    ListView listViewChats;
    ArrayList<Chat> l;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmen_chatslist, container, false);

        listViewChats = (ListView)view.findViewById(R.id.listViewId);

        ChatListAdapter adapter = new ChatListAdapter(getActivity().getApplicationContext(), getData());//LoraConnect.getListOfChats());
        listViewChats.setAdapter(adapter);

        listViewChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity().getApplicationContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();
                Snackbar.make(view, String.valueOf(position), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Toast.makeText(getActivity().getApplicationContext(),String.valueOf(l.get(position).getDestination()),Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(ChatListFragment.this)
                        .navigate(R.id.action_chatList_to_chatFragment);
            }
        });
        //return inflater.inflate(R.layout.fragmen_chatslist, container, false);
        return view;
    }

    private ArrayList<Chat> getData(){
        l= new ArrayList<Chat>();
        Chat c;
        Message m;
        for(int i=0; i<3; i++){
            c =  new Chat("pepe"+i);
            m = new Message();
            m.setDestination("pepe"+i);
            m.setMsg("Prueba"+50+i);
            c.addMsg(m);
            c.setOnline(i%2==0);
            l.add(c);
        }
        return l;
    }
}
