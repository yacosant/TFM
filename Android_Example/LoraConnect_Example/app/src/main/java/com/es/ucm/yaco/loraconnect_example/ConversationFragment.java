package com.es.ucm.yaco.loraconnect_example;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.es.ucm.yaco.loraConnect.LoraConnect;
import com.es.ucm.yaco.loraConnect.data.Message;
import com.es.ucm.yaco.loraconnect_example.data.Chat;


public class ConversationFragment extends Fragment {
    //private static int pos;
    private static String dest;
    ListView listViewMessages;
    TextView nombreChat;
    ImageView img;
    private static Chat chat;
    EditText msg;
    Button enviar;
    static MsgInChatAdapter adapter;
    boolean testing =false;

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

        if(getArguments().get("nameChat")!=null) {
            dest = getArguments().get("nameChat").toString();
            chat = MainActivity.getChatController().getChat(dest);
        }

        chat.setNewMsg(false);//marca como leido

        nombreChat.setText(chat.getDestination());
        if(chat.isOnline())
            img.setImageResource(android.R.drawable.presence_online);
        else
            img.setImageResource(android.R.drawable.presence_offline);

        adapter = new MsgInChatAdapter(getActivity().getApplicationContext(),chat.getMsgs());
        listViewMessages.setAdapter(adapter);
        msg.setActivated(chat.isOnline());

        if(LoraConnect.isTest())
            msg.setHint("Formato TEST: 'tiempo;frec'");

        enviar.setActivated(chat.isOnline());
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text =msg.getText().toString();
                Message message;
                if(!text.isEmpty()){
                    msg.getText().clear();
                    if(LoraConnect.isTest()) {
                        message = LoraConnect.makeTest(chat.getDestination(), text);

                    }else
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
            adapter.updateList(MainActivity.getChatController().getChat(dest).getMsgs());
    }


}
