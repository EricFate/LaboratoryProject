package hl.iss.whu.edu.laboratoryproject.ui.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.ChatAdapter;
import hl.iss.whu.edu.laboratoryproject.entity.ChatInformation;


/**
 * @author way
 */
public class ChatActivity extends AppCompatActivity implements OnClickListener {

    private Button mBtnSend;// 发送btn
    //private Button mBtnBack;// 返回btn
    private EditText mEditTextContent;
    private ListView mListView;
    private ChatAdapter mAdapter;// 消息视图的Adapter
    private List<ChatInformation> mDataArrays = new ArrayList<ChatInformation>();// 消息对象数组

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_main);
        initView();// 初始化view
        initData();// 初始化数据
        mListView.setSelection(mAdapter.getCount() - 1);
    }

    /**
     * 初始化view
     */
    public void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mBtnSend = (Button) findViewById(R.id.btn_send);
            mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mBtnSend.setOnClickListener(this);
        //mBtnBack = (Button) findViewById(R.id.btn_back);
        //mBtnBack.setOnClickListener(this);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
    }

    private String[] msgArray = new String[]{"老师在么？", "在的", "上一次课的有个地方我不明白", "说吧，现在正好有空"
            , "为什么...", "因为...","那为什么...","这样...", "噢噢，这样啊，谢谢老师。", "不客气哈，好好理解就好了","恩，老师再见","恩"};

    private String[] dataArray = new String[]{"2016-12-08 18:00:02",
            "2016-12-08 18:10:22", "2016-12-08 18:11:24",
            "2016-12-08 18:20:23", "2016-12-08 18:30:31",
            "2016-12-08 18:35:37", "2016-12-08 18:40:13",
            "2016-12-08 18:50:26", "2016-12-08 18:52:57",
            "2016-12-08 18:55:11", "2016-12-08 18:56:45",
            "2016-12-08 18:57:33",};
    private final static int COUNT = 12;// 初始化数组总数

    /**
     * 模拟加载消息历史，实际开发可以从数据库中读出
     */
    public void initData() {
        for (int i = 0; i < COUNT; i++) {
            ChatInformation entity = new ChatInformation();
            entity.setDate(dataArray[i]);
            if (i % 2 == 0) {
                entity.setName("学生");
                entity.setMsgType(true);// 收到的消息
            } else {
                entity.setName("老师");
                entity.setMsgType(false);// 自己发送的消息
            }
            entity.setMessage(msgArray[i]);
            mDataArrays.add(entity);
        }

        mAdapter = new ChatAdapter(this, mDataArrays);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:// 发送按钮点击事件
                send();
                break;
//            case R.id.btn_back:// 返回按钮点击事件
//                finish();// 结束,实际开发中，可以返回主界面
//                break;
        }
    }

    /**
     * 发送消息
     */
    private void send() {
        String contString = mEditTextContent.getText().toString();
        if (contString.length() > 0) {
            ChatInformation entity = new ChatInformation();
            entity.setName("老师");
            entity.setDate(getDate());
            entity.setMessage(contString);
            entity.setMsgType(false);

            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变

            mEditTextContent.setText("");// 清空编辑框数据

            mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
        }
    }

    /**
     * 发送消息时，获取当前事件
     *
     * @return 当前时间
     */
    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(new Date());
    }
}