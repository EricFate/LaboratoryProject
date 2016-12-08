package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.ExpandableContactAdapter;
import hl.iss.whu.edu.laboratoryproject.entity.Chatter;
import hl.iss.whu.edu.laboratoryproject.entity.Group;
import hl.iss.whu.edu.laboratoryproject.manager.SmackManager;
import hl.iss.whu.edu.laboratoryproject.ui.view.AnimatedExpandableListView;

public class ContactActivity extends AppCompatActivity {

    @Bind(R.id.aelv_contact)
    AnimatedExpandableListView aelvContact;
    private ExpandableContactAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ArrayList<Group> groups = new ArrayList<>();
        ArrayList<Chatter> contacts = new ArrayList<>();
        contacts.add(new Chatter(0,"jack","","1111",""));
        contacts.add(new Chatter(1,"rose","","2222",""));
        groups.add(new Group("同学",contacts));
        groups.add(new Group("老师",contacts));
        final ExpandableContactAdapter adapter = new ExpandableContactAdapter(groups);
        aelvContact.setAdapter(adapter);
        aelvContact.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Chatter child = (Chatter) adapter.getChild(groupPosition, childPosition);
                Intent intent = new Intent(ContactActivity.this,ChatActivity.class);
                intent.putExtra("chatter",child);
                startActivity(intent);
                return true;
            }
        });
//        new GetContactAsyncTask().execute();
    }

    class GetContactAsyncTask extends AsyncTask<Void, Void, ArrayList<Group>> {

        @Override
        protected ArrayList<Group> doInBackground(Void... params) {
            ArrayList<Group> result = new ArrayList<>();
            Roster roster = Roster.getInstanceFor(SmackManager.getConnection());
            Collection<RosterGroup> groups = roster.getGroups();
            Log.e("groups",roster.getGroupCount()+"");
            Log.e("contacts",roster.getEntries().size()+"");
            for (RosterGroup group : groups) {
                String groupName = group.getName();
                List<RosterEntry> entries = group.getEntries();
                ArrayList<Chatter> chatters = new ArrayList<>();
                for (RosterEntry entry :
                        entries) {
                    String entryName = entry.getName();
                    Presence presence = roster.getPresence(entry.getJid());
                    chatters.add(new Chatter(presence.isAvailable()?0:1,entryName,"","",entry.getJid().toString()));
                }
                result.add(new Group(groupName,chatters));
            }

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Group> groups) {
            mAdapter = new ExpandableContactAdapter(groups);
            aelvContact.setAdapter(mAdapter);
        }
    }
}
