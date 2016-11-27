package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerChatAdapter;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Chatter;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;

/**
 * Created by fate on 2016/11/18.
 */

public class DiscussFragment extends Fragment {

    private RecyclerView recyclerChat;
    private EditText etSendWord;
    private XMPPTCPConnection mConnection;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initNetwork();
    }

    private void initNetwork() {
        new Thread() {
            @Override
            public void run() {
                XMPPTCPConnectionConfiguration configuration = XMPPTCPConnectionConfiguration.builder()
                        .setUsernameAndPassword(UserInfo.username, UserInfo.password)
                        .setHost(Constant.CHAT_HOST)
                        .setPort(Constant.CHAT_PORT)
                        .build();
                mConnection = new XMPPTCPConnection(configuration);
                try {
                    mConnection.connect();
                } catch (Exception e) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("连接服务器出错")
                            .setNegativeButton("确定", null)
                            .show();
                    e.printStackTrace();
                }

            }
        }.start();
    }

//    private void joinChatRoom() {
//        MultiUserChatManager multiUserChatManager = MultiUserChatManager.getInstanceFor(mConnection);
//
//        multiUserChatManager.getMultiUserChat();
//
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discuss, container, false);
        recyclerChat = ButterKnife.findById(rootView,R.id.recycler_chat);
        etSendWord = ButterKnife.findById(rootView,R.id.et_send_word);
        ArrayList<Chatter> chatters = new ArrayList<>();
        chatters.add(new Chatter("我", "1", "2"));
        recyclerChat.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerChat.setAdapter(new RecyclerChatAdapter(chatters));
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



}
