package com.es.ucm.yaco.loraconnect_example;


import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.es.ucm.yaco.loraConnect.LoraConnect;

import java.util.ArrayList;


public class ContactListFragment extends Fragment {
    private ArrayList<String> l;
    private ListView listView;
    private ContactsListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        l = LoraConnect.getConnected();
        MainActivity.setBack(true);
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        listView = (ListView)view.findViewById(R.id.listViewId_contact);

        adapter = new ContactsListAdapter(getActivity().getApplicationContext(),
                l);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MainActivity.getChatController().addChat(l.get(position));
                MainActivity.setBack2(true);
                Bundle bundle = new Bundle();
                bundle.putString("nameChat", l.get(position));

                NavHostFragment.findNavController(ContactListFragment.this)
                        .navigate(R.id.action_contactListFragment_to_chatFragment,bundle);
            }
        });

        return view;
    }

}