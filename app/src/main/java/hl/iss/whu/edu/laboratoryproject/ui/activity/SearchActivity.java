package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerQueryContactAdapter;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerQueryResultAdapter;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Info;
import hl.iss.whu.edu.laboratoryproject.entity.QueryItem;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {


    @Bind(R.id.recycler_search)
    RecyclerView recyclerSearch;

    @Bind(R.id.search_view)
    MaterialSearchView searchView;
    RecyclerQueryContactAdapter mAdapter;
    private ArrayAdapter<String> mArrayAdapter;
    private Spinner mSpinner;
    private EditText mEtRemark;
    private EditText mEtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
        Intent intent = getIntent();
        if (intent != null) {
            String query = intent.getStringExtra("query");
            queryContact(query);
        }
    }

    private void queryContact(String query) {
        RetrofitUtils.getService().queryContacts(UserInfo.uid,query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<QueryItem>>() {
                    @Override
                    public void accept(ArrayList<QueryItem> items) throws Exception {
                        mAdapter.setData(items);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final View dialogView = initDialogView();
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryContact(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        recyclerSearch.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerQueryContactAdapter(new ArrayList<QueryItem>());
        recyclerSearch.setAdapter(mAdapter);
        mAdapter.setOnAddFriendListener(new RecyclerQueryContactAdapter.OnAddFriendListener() {
            @Override
            public void onAddFriend(final Info info, final RecyclerQueryResultAdapter.MyViewHolder holder) {
                mEtRemark.setText(info.getNickname());
                new AlertDialog.Builder(SearchActivity.this).setView(dialogView)
                        .setTitle("添加好友")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                holder.showAdding();
                                addFriend(info,holder);
                                dialog.dismiss();
                            }
                        }).show();

            }
        });
        mAdapter.setOnQueryItemClickListener(new RecyclerQueryContactAdapter.OnQueryItemClickListener() {
            @Override
            public void onQueryItemClick(View v, Info data) {
                Intent intent = new Intent(SearchActivity.this,PersonalInfoActivity.class);
                intent.putExtra("uid",data.getUid());
                startActivity(intent);
            }
        });
    }

    private void addFriend(Info info, final RecyclerQueryResultAdapter.MyViewHolder holder) {
        String group = mSpinner.getSelectedItem().toString();
        String s = mEtRemark.getText().toString();
        String message = mEtMessage.getText().toString();
        if (TextUtils.isEmpty(s.trim()))
            s = info.getNickname();
        Log.i("uid", "addFriend: "+UserInfo.uid);
        RetrofitUtils.getService().requestFriend(info.getUid(),UserInfo.uid,group,s,message)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        if (result.getCode() == 0) {
                            holder.showAdded();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        new AlertDialog.Builder(SearchActivity.this)
                                .setMessage("添加失败"+throwable)
                                .setPositiveButton("确定",null)
                                .show();
                        holder.showAddFriend();
                    }
                });
    }

    private View initDialogView() {
        View view = View.inflate(this, R.layout.dialog_add_friend, null);
        mEtRemark = ButterKnife.findById(view, R.id.et_remark);
        final EditText etGroup = ButterKnife.findById(view,R.id.et_add_group);
        mSpinner = ButterKnife.findById(view, R.id.spinner_roster);
        ImageView ivAdd = ButterKnife.findById(view,R.id.iv_add_group);
        mEtMessage = ButterKnife.findById(view, R.id.et_message);
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, UserInfo.groupNames);
        mSpinner.setAdapter(mArrayAdapter);
        if (UserInfo.groupNames.size() == 0){
            getGroupNames();
        }
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etGroup.getText().toString();
                if (!TextUtils.isEmpty(str)){
                    mArrayAdapter.add(str);
                    mSpinner.setSelection(mArrayAdapter.getCount()-1);
                }else {
                    Toast.makeText(SearchActivity.this, "请输入组名", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void getGroupNames() {
        RetrofitUtils.getService().getGroupNames(UserInfo.uid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<String>>() {
                    @Override
                    public void accept(ArrayList<String> strings) throws Exception {
                        UserInfo.groupNames = strings;
                        mArrayAdapter.addAll(strings);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(SearchActivity.this, "获取组信息失败"+throwable, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return super.onCreateOptionsMenu(menu);
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
