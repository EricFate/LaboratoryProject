package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.Jid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.ExpandableContactAdapter;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Chatter;
import hl.iss.whu.edu.laboratoryproject.entity.Group;
import hl.iss.whu.edu.laboratoryproject.manager.SmackManager;
import hl.iss.whu.edu.laboratoryproject.ui.view.AnimatedExpandableListView;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

public class ContactActivity extends AppCompatActivity {

    @Bind(R.id.aelv_contact)
    AnimatedExpandableListView aelvContact;
    private ExpandableContactAdapter mAdapter;
    private ArrayList<Group> mData;
    private Roster mRoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        final ArrayList<Group> groups = new ArrayList<>();
//        ArrayList<Chatter> contacts = new ArrayList<>();
//        ArrayList<Chatter> contacts2 = new ArrayList<>();
//        contacts.add(new Chatter(0,"女神","","一花一世界",""));
//        contacts.add(new Chatter(1,"基友","","一叶一菩提",""));
//        contacts2.add(new Chatter(1,"老师","","君掌盛无边",""));
//        contacts2.add(new Chatter(0,"助教","","刹那含永劫",""));
//        groups.add(new Group("同学",contacts));
//        groups.add(new Group("老师",contacts2));
//        final ExpandableContactAdapter adapter = new ExpandableContactAdapter(groups);
//        aelvContact.setAdapter(adapter);
//        aelvContact.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Chatter child = (Chatter) adapter.getChild(groupPosition, childPosition);
//                Intent intent = new Intent(ContactActivity.this,ChatActivity.class);
//                intent.putExtra("chatter",child);
//                startActivity(intent);
//                return true;
//            }
//        });
            initRoster();
        new GetContactAsyncTask().execute();
    }

    class GetContactAsyncTask extends AsyncTask<Void, Void, ArrayList<Group>> {

        @Override
        protected ArrayList<Group> doInBackground(final Void... params) {
            mData = new ArrayList<>();


            Collection<RosterGroup> groups = mRoster.getGroups();
            Log.e("groups", mRoster.getGroupCount() + "");
            Log.e("contacts", mRoster.getEntries().size() + "");
            for (RosterGroup group : groups) {
                String groupName = group.getName();
                List<RosterEntry> entries = group.getEntries();
                ArrayList<Chatter> presences = new ArrayList<>();
                ArrayList<Chatter> absences = new ArrayList<>();
                for (RosterEntry entry :
                        entries) {
                    String entryName = entry.getName();
                    BareJid jid = entry.getJid();
                    Presence presence = mRoster.getPresence(jid);
                    byte[] image = new byte[0];
                    String signiture = "";
                    try {
                        VCard card = VCardManager.getInstanceFor(SmackManager.getConnection()).loadVCard(jid.asEntityBareJidIfPossible());
                        image = card.getAvatar();
                        signiture = card.getField(Constant.VCARD_SIGNITURE_FIELD);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (presence.isAvailable()) {
                        presences.add(new Chatter(0, entryName, image, signiture, jid));
                    } else {
                        absences.add(new Chatter(1, entryName, image, signiture, jid));

                    }
                }
                mData.add(new Group(groupName, presences, absences));
            }

            return mData;
        }

        @Override
        protected void onPostExecute(ArrayList<Group> groups) {
            mAdapter = new ExpandableContactAdapter(groups);
            aelvContact.setAdapter(mAdapter);
            aelvContact.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    Chatter child = (Chatter) mAdapter.getChild(groupPosition, childPosition);
                    Intent intent = new Intent(ContactActivity.this, ChatActivity.class);
                    intent.putExtra("chatter", child);
                    startActivity(intent);
                    return true;
                }
            });
        }
    }

    private void initRoster() {
        mRoster = Roster.getInstanceFor(SmackManager.getConnection());

        mRoster.addRosterListener(new RosterListener() {
            @Override
            public void entriesAdded(Collection<Jid> addresses) {
                new GetContactAsyncTask().execute();
            }

            @Override
            public void entriesUpdated(Collection<Jid> addresses) {
                new GetContactAsyncTask().execute();
            }

            @Override
            public void entriesDeleted(Collection<Jid> addresses) {
                new GetContactAsyncTask().execute();
            }

            @Override
            public void presenceChanged(Presence presence) {
                BareJid jid = presence.getFrom().asBareJid();

                boolean available = presence.isAvailable();
                modifyData(jid, available);
                UiUtils.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();

                    }
                });
            }
        });
    }

    private void modifyData(BareJid jid, boolean available) {
        for (Group group : mData) {
            for (Chatter chatter : group.getAbsence()) {
                if (chatter.getJid().toString().equals(jid.toString())) {
                    chatter.setState(available ? 0 : 1);
                    group.getAbsence().remove(chatter);
                    if (available) group.getPresence().add(chatter);
                    else group.getAbsence().add(chatter);
                    return;
                }
            }

            for (Chatter chatter : group.getPresence()) {
                if (chatter.getJid().toString().equals(jid.toString())) {
                    chatter.setState(available ? 0 : 1);
                    group.getPresence().remove(chatter);
                    if (available) group.getPresence().add(chatter);
                    else group.getAbsence().add(chatter);
                    return;
                }
            }
        }
    }
}
