package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerClassMemberAdapter;
import hl.iss.whu.edu.laboratoryproject.entity.Info;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ClassMemberActivity extends AppCompatActivity {

    @Bind(R.id.recycler_general)
    RecyclerView mRecyclerGeneral;

    private int type;
    private int clid;
    private RecyclerClassMemberAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_member);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        clid = intent.getIntExtra("clid", 0);
         type = intent.getIntExtra("type", 0);
        initView();
        initData();
    }

    private void initView() {
        mRecyclerGeneral.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerClassMemberAdapter(new ArrayList<Info>());
        mRecyclerGeneral.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Info>() {
            @Override
            public void onItemClick(View v, Info data) {
                Intent intent = new Intent(ClassMemberActivity.this,PersonalInfoActivity.class);
                intent.putExtra("uid",data.getUid());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        RetrofitUtils.getService().getClassMemberByClass(clid,type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<Info>>() {
                    @Override
                    public void accept(ArrayList<Info> infos) throws Exception {
                        mAdapter.setData(infos);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(ClassMemberActivity.this, "错误:"+throwable, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
