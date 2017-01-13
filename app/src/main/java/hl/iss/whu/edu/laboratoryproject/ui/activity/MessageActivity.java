package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jxmpp.jid.Jid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerMessageAdapter;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerRequestAdapter;
import hl.iss.whu.edu.laboratoryproject.entity.Chatter;
import hl.iss.whu.edu.laboratoryproject.entity.Request;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.manager.FullyLinearLayoutManager;
import hl.iss.whu.edu.laboratoryproject.manager.SmackManager;

public class MessageActivity extends AppCompatActivity {

    @Bind(R.id.recycler_message)
    RecyclerView recyclerMessage;
    @Bind(R.id.recycler_stanza)
    RecyclerView recyclerStanza;
    private ArrayList<Request> mRequests = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        recyclerMessage.setLayoutManager(new FullyLinearLayoutManager(this));
        recyclerStanza.setLayoutManager(new FullyLinearLayoutManager(this));
        recyclerStanza.setAdapter(new RecyclerRequestAdapter(mRequests));

        new GetOfflineMessageAsyncTask().execute();
        new GetFriendRequestAsyncTask().execute();

//        ArrayList<Chatter> chatters = new ArrayList<>();
//        chatters.add(new Chatter("女神","晚安","","2016-1-2"));
//        chatters.add(new Chatter("基友","好","","2016-1-30"));
//        chatters.add(new Chatter("老师","恩","","2016-2-10"));
//        RecyclerMessageAdapter adapter = new RecyclerMessageAdapter(chatters);
//        recyclerMessage.setAdapter(adapter);
//        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Chatter>() {
//            @Override
//            public void onItemClick(View v, Chatter data) {
//                Intent intent = new Intent(MessageActivity.this,ChatActivity.class);
//                intent.putExtra("chatter",data);
//                startActivity(intent);
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.message, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_contact:
                Intent intent = new Intent(this, ContactActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(new Date());
    }

    class GetOfflineMessageAsyncTask extends AsyncTask<Void, Void, ArrayList<Chatter>> {

        @Override
        protected ArrayList<Chatter> doInBackground(Void... params) {
            ArrayList<Chatter> chatters = new ArrayList<>();
            AbstractXMPPConnection connection = SmackManager.getConnection();
            try {
                Presence presence = new Presence(Presence.Type.unavailable);
                connection.sendStanza(presence);
                OfflineMessageManager offlineMessageManager = new OfflineMessageManager(connection);
                for (Message message : offlineMessageManager.getMessages()) {
                    Jid jid = message.getFrom();
                    String body = message.getBody();
                    String time = getDate();
                    VCard vCard = VCardManager.getInstanceFor(connection).loadVCard(jid.asEntityBareJidIfPossible());
                    byte[] image = vCard.getAvatar();
                    Roster roster = Roster.getInstanceFor(connection);
                    String name = roster.getEntry(jid.asBareJid()).getName();
                    chatters.add(new Chatter(name, body, image, time, jid.asBareJid()));
                }
                presence = new Presence(Presence.Type.available);
                connection.sendStanza(presence);
            } catch (Exception e) {
                e.printStackTrace();
                new AlertDialog.Builder(MessageActivity.this).setMessage("加载离线消息失败"+e).show();
            }
            return chatters;
        }

        @Override
        protected void onPostExecute(ArrayList<Chatter> chatters) {
            RecyclerMessageAdapter adapter = new RecyclerMessageAdapter(chatters);
            recyclerMessage.setAdapter(adapter);
            adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Chatter>() {
                @Override
                public void onItemClick(View v, Chatter data) {
                    Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
                    intent.putExtra("chatter", data);
                    startActivity(intent);
                }
            });
        }
    }
    class GetFriendRequestAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AbstractXMPPConnection connection = SmackManager.getConnection();
            try {
                StanzaFilter filter = new StanzaTypeFilter(Presence.class);
                StanzaListener listener = new StanzaListener() {
                    @Override
                    public void processPacket(Stanza packet) throws SmackException.NotConnectedException, InterruptedException {
                        if (packet instanceof Presence){
                            Presence presence = (Presence) packet;
                            Jid from = presence.getFrom();
                            if (presence.getType().equals(Presence.Type.subscribe)){
                                mRequests.add(new Request(from,Request.TYPE_SUBSCRIBE));
                            }else if (presence.getType().equals(Presence.Type.subscribed)){
                                mRequests.add(new Request(from,Request.TYPE_SUBSCRIBED));
                            }else if (presence.getType().equals(Presence.Type.unsubscribe)){
                                mRequests.add(new Request(from,Request.TYPE_SUBSCRIBE));
                            }
                        }
                    }
                };
                connection.addAsyncStanzaListener(listener,filter);
            } catch (Exception e) {
                e.printStackTrace();
                new AlertDialog.Builder(MessageActivity.this).setMessage("加载离线消息失败"+e).show();
            }
            return null;
        }


    }
}
