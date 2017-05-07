package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerChatAdapter;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Chatter;

/**
 * Created by fate on 2016/11/18.
 */

public class DiscussFragment extends Fragment {

    private RecyclerView recyclerChat;
    private EditText etSendWord;
//    private RecyclerChatAdapter mAdapter;
    private Button btSend;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discuss, container, false);
        recyclerChat = ButterKnife.findById(rootView, R.id.recycler_chat);
        etSendWord = ButterKnife.findById(rootView, R.id.et_send_word);
        btSend = ButterKnife.findById(rootView, R.id.bt_send);

//        btSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    chat.sendMessage(etSendWord.getText().toString());
//                } catch (Exception e) {
//                    new AlertDialog.Builder(getActivity()).setMessage("发送失败:"+e).show();
//                    e.printStackTrace();
//                }
//            }
//        });


        ArrayList<Chatter> chatters = new ArrayList<>();
        recyclerChat.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mAdapter = new RecyclerChatAdapter(chatters);
//        recyclerChat.setAdapter(mAdapter);


        return rootView;
    }


}
