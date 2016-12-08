package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.android.AndroidSmackInitializer;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerChatAdapter;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Chatter;
import hl.iss.whu.edu.laboratoryproject.manager.SmackManager;

/**
 * Created by fate on 2016/11/18.
 */

public class DiscussFragment extends Fragment {

    private RecyclerView recyclerChat;
    private EditText etSendWord;
    private MultiUserChat chat;
    private RecyclerChatAdapter mAdapter;
    private Button btSend;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AndroidSmackInitializer().initialize();
        new ChatAsyncTask().execute();
    }

//    private void initNetwork() throws InterruptedException, XMPPException, SmackException, IOException {
//        XMPPTCPConnectionConfiguration configuration = XMPPTCPConnectionConfiguration.builder()
//                .setHost(Constant.CHAT_HOST)
//                .setPort(Constant.CHAT_PORT)
//                .setXmppDomain(JidCreate.domainBareFrom(Constant.CHAT_DOMAIN))
//                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
//                .setCompressionEnabled(false)
//                .build();
//        mConnection = new XMPPTCPConnection(configuration);
//        mConnection.connect();
//        mConnection.login("fate","1234");
//
//    }

    private void joinChatRoom(String name) throws Exception {
        MultiUserChatManager multiUserChatManager = MultiUserChatManager.getInstanceFor(SmackManager.getConnection());
        chat = multiUserChatManager.getMultiUserChat (JidCreate.entityBareFrom(name + Constant.SMACK_CONFERENCE +SmackManager.getConnection().getServiceName()) );
        Resourcepart nickname = Resourcepart.from("命运13号");
        chat.join(nickname);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discuss, container, false);
        recyclerChat = ButterKnife.findById(rootView, R.id.recycler_chat);
        etSendWord = ButterKnife.findById(rootView, R.id.et_send_word);
        btSend = ButterKnife.findById(rootView, R.id.bt_send);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    chat.sendMessage(etSendWord.getText().toString());
                } catch (Exception e) {
                    new AlertDialog.Builder(getActivity()).setMessage("发送失败:"+e).show();
                    e.printStackTrace();
                }
            }
        });


        ArrayList<Chatter> chatters = new ArrayList<>();
        recyclerChat.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new RecyclerChatAdapter(chatters);
        recyclerChat.setAdapter(mAdapter);


        return rootView;
    }

    @Override
    public void onDestroy() {
        try {
            chat.leave();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private class ChatAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
//                initNetwork();
                joinChatRoom("test");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(getActivity(), "加入群组成功", Toast.LENGTH_SHORT).show();
                chat.addMessageListener(new MessageListener() {
                    @Override
                    public void processMessage(Message message) {
                        Chatter chatter = new Chatter(message.getFrom().asFullJidIfPossible().getResourcepart().toString(), message.getBody(), "","");
                        mAdapter.addChatter(chatter);
                    }
                });
            } else {
                Toast.makeText(getActivity(), "加入群组失败", Toast.LENGTH_SHORT).show();
                btSend.setEnabled(false);
            }
        }
    }
}
