package com.es.ucm.yaco.loraconnect_example;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.es.ucm.yaco.loraConnect.LoraConnect;
import com.es.ucm.yaco.loraConnect.data.Message;
import com.es.ucm.yaco.loraConnect.utils.TcpClient;
import com.es.ucm.yaco.loraconnect_example.data.Chat;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ConversationFragment extends Fragment {
    private static int pos;
    ListView listViewMessages;
    TextView nombreChat;
    ImageView img;
    private static Chat chat;
    EditText msg;
    Button enviar;
    static MsgInChatAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.setBack(true);
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        nombreChat = (TextView)view.findViewById(R.id.card_Nombre_chat);
        listViewMessages = (ListView)view.findViewById(R.id.listViewConversationId);
        enviar = (Button)view.findViewById(R.id.button_enviar);
        img =  (ImageView)view.findViewById(R.id.imageViewConversation);
        msg =  (EditText) view.findViewById(R.id.editTextText_Conversacion);

        pos= Integer.parseInt(getArguments().get("idChat").toString());
        chat= MainActivity.getChatController().getChats().get(pos);
        nombreChat.setText(chat.getDestination());
        if(chat.isOnline())
            img.setImageResource(android.R.drawable.presence_online);
        else
            img.setImageResource(android.R.drawable.presence_offline);

        adapter = new MsgInChatAdapter(getActivity().getApplicationContext(),chat.getMsgs());
        listViewMessages.setAdapter(adapter);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text =msg.getText().toString();
                Message message;
                if(!text.isEmpty()){
                    msg.getText().clear();
                    message = LoraConnect.sendMessage(chat.getDestination(),text);
                    MainActivity.getChatController().addMsg(message);
                    refreshChat();
                }
            }
        });

        return view;
    }

    static public void refreshChat(){
        if (adapter!=null)
        //    adapter.notifyDataSetChanged();
            adapter.updateList(MainActivity.getChatController().getChats().get(pos).getMsgs());
    }


}
