package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerMessageAdapter;
import hl.iss.whu.edu.laboratoryproject.entity.Chatter;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;

public class MessageActivity extends AppCompatActivity {

    @Bind(R.id.recycler_message)
    RecyclerView recyclerMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        recyclerMessage.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<Chatter> chatters = new ArrayList<>();
        chatters.add(new Chatter("","","",""));
        chatters.add(new Chatter("","","",""));
        chatters.add(new Chatter("","","",""));
        RecyclerMessageAdapter adapter = new RecyclerMessageAdapter(chatters);
        recyclerMessage.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Chatter>() {
            @Override
            public void onItemClick(View v, Chatter data) {
                Intent intent = new Intent(MessageActivity.this,ChatActivity.class);
                intent.putExtra("chatter",data);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.message,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_contact:
                Intent intent = new Intent(this,ContactActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
