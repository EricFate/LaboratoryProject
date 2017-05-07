package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;


/**
 * @author way
 */
public class ChatActivity extends AppCompatActivity {
    @Bind(R.id.tv_name)
    TextView tvName;
    private String mTargetId;
    private String mType;
    private int POPUP_MARGIN = 16;
    private PopupWindow popupWindow;
    private Toolbar toolbar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        init();

    }

    public void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Uri uri = getIntent().getData();
        mTargetId = uri.getQueryParameter("targetId").toString();
        String title = uri.getQueryParameter("title").toString();
        mType = uri.getPathSegments().get(1);
        if (title != null)
            tvName.setText(title);
        View window = getLayoutInflater().inflate(R.layout.popup_notification, null);
        TextView tvTitle = (TextView) window.findViewById(R.id.tv_title);
        TextView tvContent = (TextView) window.findViewById(R.id.tv_content);
        popupWindow = new PopupWindow(window,
                getWindowManager().getDefaultDisplay().getWidth() - 2 * POPUP_MARGIN, 400);
        // TODO: 2016/5/17 设置动画
        popupWindow.setAnimationStyle(R.style.popup_window_anim);
        // TODO: 2016/5/17 设置背景颜色
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
        // TODO: 2016/5/17 设置可以获取焦点
        popupWindow.setFocusable(true);
        // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
        popupWindow.setOutsideTouchable(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mType.equals("group")) {
            getMenuInflater().inflate(R.menu.notification, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification:
                // TODO：更新popupwindow的状态
                popupWindow.update();
                // TODO: 2016/5/17 以下拉的方式显示，并且可以设置显示的位置
                popupWindow.showAsDropDown(toolbar, POPUP_MARGIN, 10);

                break;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}