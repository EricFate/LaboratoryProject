package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerRanksAdapter;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Rank;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RanksActivity extends AppCompatActivity {

    @Bind(R.id.re_ranks)
    RecyclerView recyclerRanks;
    private RecyclerRanksAdapter mAdapter;
    private int cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranks);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        cid = intent.getIntExtra("cid",0);
        initView();
        initData();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAdapter = new RecyclerRanksAdapter(new ArrayList<Rank>());
        recyclerRanks.setLayoutManager(new LinearLayoutManager(this));
        recyclerRanks.setAdapter(mAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RanksActivity.this, RankActivity.class);
                intent.putExtra("cid", cid);
                startActivityForResult(intent, Constant.REQURST_RANK);
            }
        });

    }
    private void initData(){
        RetrofitUtils.getService().loadRanks(cid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<Rank>>() {
                    @Override
                    public void accept(ArrayList<Rank> ranks) throws Exception {
                        mAdapter.setData(ranks);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQURST_RANK && resultCode == RESULT_OK)
            initData();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
