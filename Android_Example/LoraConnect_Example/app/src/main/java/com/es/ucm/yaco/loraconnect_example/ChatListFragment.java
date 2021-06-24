package com.es.ucm.yaco.loraconnect_example;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.es.ucm.yaco.loraConnect.LoraConnect;
import com.es.ucm.yaco.loraConnect.controller.ControllerTcp;
import com.es.ucm.yaco.loraConnect.data.Message;
import com.es.ucm.yaco.loraConnect.utils.Constants;
import com.es.ucm.yaco.loraconnect_example.data.Chat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.util.ArrayList;

public class ChatListFragment extends Fragment {
    private ListView listViewChats;
    private static ArrayList<Chat> l = new ArrayList<Chat>();
    private static ChatListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.changeActiveItem(0,true); //activar la opcion Desconectar
        MainActivity.changeActiveItem(1,false); //desactivar la opcion Configuración

        l = MainActivity.getChatController().getChats();
        MainActivity.setBack(false);
        View view = inflater.inflate(R.layout.fragmen_chatslist, container, false);

        listViewChats = (ListView)view.findViewById(R.id.listViewId);

        adapter = new ChatListAdapter(getActivity().getApplicationContext(),
                l);

        listViewChats.setAdapter(adapter);

        listViewChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("nameChat", l.get(position).getDestination());

                NavHostFragment.findNavController(ChatListFragment.this)
                        .navigate(R.id.action_chatList_to_chatFragment,bundle);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_newChat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ChatListFragment.this)
                        .navigate(R.id.action_chatList_to_contactListFragment);
            }
        });

        return view;
    }

    public static void addMessage(Message msg){
        MainActivity.getMainAct().getChatController().addMsg(msg);
        l.clear();
        l.addAll( MainActivity.getMainAct().getChatController().getChats());
        adapter.notifyDataSetChanged();
        ConversationFragment.refreshChat();
    }

    public static class ConnectTask extends AsyncTask<String, String, ControllerTcp> {

        @Override
        protected ControllerTcp doInBackground(String... message) {

            LoraConnect.connectToESP32(new LoraConnect.OnMessageReceived() {
                @Override
                public void messageReceived(Message message) {
                    Log.println(Log.INFO,"Client_TCP_EXCAMPLE", message.toJson());
                    publishProgress(message.toJson());
                }
            });


            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Message msg = new Message();
            try {
                msg.parse(values[0]);

                if(msg.getType()== Constants.TYPE_MSG_MSG || msg.getType()== Constants.TYPE_MSG_TEST) //Mensaje
                    addMessage(msg);
                else if(msg.getType()== Constants.TYPE_MSG_HELLO || msg.getType()== Constants.TYPE_MSG_HELLO_ACK) { //Hello
                    if(MainActivity.getChatController().getChat(msg.getSource())!=null)
                        MainActivity.getChatController().getChat(msg.getSource()).setOnline(true);
                }
                else if(msg.getType()== Constants.TYPE_MSG_BYE) { //Bye
                    if(MainActivity.getChatController().getChat(msg.getSource())!=null)
                        MainActivity.getChatController().getChat(msg.getSource()).setOnline(false);
                }

                Log.println(Log.INFO,"Client_TCP_EXCAMPLE", MainActivity.getChatController().getChats().toString());

        } catch (JSONException e) {

        }
        }
    }
}
