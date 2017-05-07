package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerGroupsAdapter;
import hl.iss.whu.edu.laboratoryproject.entity.ChatGroup;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GroupsActivity extends AppCompatActivity {

    @Bind(R.id.recycler_general)
    RecyclerView mRecyclerGeneral;
    private RecyclerGroupsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initData();
    }

    private void initView() {

        mRecyclerGeneral.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerGroupsAdapter(new ArrayList<ChatGroup>());
        mRecyclerGeneral.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<ChatGroup>() {
            @Override
            public void onItemClick(View v, ChatGroup data) {
                Intent intent = new Intent(Intent.ACTION_VIEW, UserInfo.getGroupChatUri("g" + data.getId(), data.getName()));
                startActivity(intent);
            }
        });
    }

    private void initData() {
        RetrofitUtils.getService().getGroups(UserInfo.uid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<ChatGroup>>() {
                    @Override
                    public void accept(ArrayList<ChatGroup> groups) throws Exception {
                        mAdapter.setData(groups);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(GroupsActivity.this, "错误:" + throwable, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
