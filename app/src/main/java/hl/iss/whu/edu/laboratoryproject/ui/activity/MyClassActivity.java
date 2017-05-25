package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerSubjectAdapter;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.AdminClass;
import hl.iss.whu.edu.laboratoryproject.entity.ChatGroup;
import hl.iss.whu.edu.laboratoryproject.entity.Course;
import hl.iss.whu.edu.laboratoryproject.entity.Notice;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.entity.Teacher;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.manager.SyLinearLayoutManager;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyClassActivity extends AppCompatActivity {
    private static final int STATE_LOADING = 0;
    private static final int STATE_NO_CLASS = 1;
    private static final int STATE_SUCCESS = 2;
    private static final int STATE_ERROR = 3;
    @Bind(R.id.tv_notice_title)
    TextView mTvNoticeTitle;
    @Bind(R.id.tv_notice_content)
    TextView mTvNoticeContent;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATE_LOADING, STATE_ERROR, STATE_NO_CLASS, STATE_SUCCESS})
    public @interface ClassState {
    }

    @Bind(R.id.recycler_class_courses)
    RecyclerView recyclerClassCourses;
    @Bind(R.id.iv_teacher_image)
    ImageView mIvTeacherImage;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_students)
    TextView mTvStudents;
    @Bind(R.id.tv_college_students)
    TextView mTvCollegeStudents;
    //    @Bind(R.id.iv_class_image)
//    ImageView mIvClassImage;
//    @Bind(R.id.toolbar_layout)
//    CollapsingToolbarLayout mToolbarLayout;
    @Bind(R.id.ll_loading)
    LinearLayout mLlLoading;
    @Bind(R.id.ll_no_class)
    LinearLayout mLlNoClass;
    @Bind(R.id.content_my_class)
    NestedScrollView mContentMyClass;
    private RecyclerSubjectAdapter mAdapter;
    private int clid;
    private int tid;
    private int gid;
    private List<Notice> mNotices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_class);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initData();
    }

    private void initView() {
        recyclerClassCourses.setLayoutManager(new SyLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new RecyclerSubjectAdapter(new ArrayList<Course>());
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Course>() {
            @Override
            public void onItemClick(View v, Course data) {
                Intent intent = new Intent(MyClassActivity.this, LessonDetailActivity.class);
                intent.putExtra("cid", data.getId());
                startActivity(intent);
            }
        });
    }

    private void show(@ClassState int state) {
        mContentMyClass.setVisibility(state == STATE_SUCCESS ? View.VISIBLE : View.GONE);
        mLlLoading.setVisibility(state == STATE_LOADING ? View.VISIBLE : View.GONE);
        mLlNoClass.setVisibility(state == STATE_NO_CLASS ? View.VISIBLE : View.GONE);
        findViewById(R.id.ll_error).setVisibility(state == STATE_ERROR ? View.VISIBLE : View.GONE);
    }

    private void initData() {
        show(STATE_LOADING);
        RetrofitUtils.getService().loadMyClass(UserInfo.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AdminClass>() {
                    @Override
                    public void accept(AdminClass adminClass) throws Exception {
                        clid = adminClass.getId();
                        if (clid <= 0) {
                            show(STATE_NO_CLASS);
                            return;
                        }
                        ArrayList<Course> courses = adminClass.getCourses();
                        if (courses != null)
                            mAdapter.setData(courses);
                        mTvStudents.setText(adminClass.getsNumber() + "位学生");
                        mTvCollegeStudents.setText(adminClass.getCsNumber() + "位大学生志愿者");
                        ChatGroup group = adminClass.getChatGroup();
                        gid = group.getId();
                        mNotices = group.getNotices();
                        if (mNotices.size() == 0) {
                            mTvNoticeTitle.setText("无新公告");
                            mTvNoticeContent.setText("无内容");
                        } else {
                            Notice notice = mNotices.get(0);
                            mTvNoticeTitle.setText(notice.getName());
                            mTvNoticeContent.setText(notice.getContent());
                        }
                        Teacher teacher = adminClass.getTeacher();
                        tid = teacher.getId();
                        Glide.with(UiUtils.getContext()).load(Constant.SERVER_URL + teacher.getImageURL())
                                .into(mIvTeacherImage);
                        mTvName.setText(teacher.getRealname());
                        show(STATE_SUCCESS);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MyClassActivity.this, "错误:" + throwable, Toast.LENGTH_SHORT).show();
                        show(STATE_ERROR);
                    }
                });
    }


    @OnClick({R.id.ll_students, R.id.ll_college, R.id.bt_add_class, R.id.bt_retry, R.id.ll_teacher, R.id.ll_notice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_retry:
                retry();
                return;
            case R.id.bt_add_class:
                joinClass();
                return;
            case R.id.ll_teacher:
                Intent intent = new Intent(MyClassActivity.this, PersonalInfoActivity.class);
                intent.putExtra("uid", "t" + tid);
                startActivity(intent);
                return;
            case R.id.ll_notice:
                Intent intentNotice = new Intent(MyClassActivity.this, NoticeActivity.class);
                intentNotice.putExtra("gid", gid);
                startActivity(intentNotice);
                return;
        }
        Intent intent = new Intent(this, ClassMemberActivity.class);
        intent.putExtra("clid", clid);
        switch (view.getId()) {
            case R.id.ll_students:
                intent.putExtra("type", 0);
                break;
            case R.id.ll_college:
                intent.putExtra("type", 1);
                break;
        }
        startActivity(intent);
    }

    private void retry() {
        initData();
    }

    private void joinClass() {
        View view = View.inflate(this, R.layout.dialog_edit_text, null);
        final EditText editText = ButterKnife.findById(view, R.id.et_input);
        editText.setMaxLines(1);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入班级id").setView(view).setNegativeButton("取消", null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MyClassActivity.this, SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText("加载中");
                sweetAlertDialog.show();
                RetrofitUtils.getService().getAdminClass(editText.getText().toString())
                        .unsubscribeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<AdminClass>() {
                            @Override
                            public void accept(AdminClass adminClass) throws Exception {
                                sweetAlertDialog.cancel();
                                if (adminClass.getId() <= 0) {
                                    SweetAlertDialog errorDialog = new SweetAlertDialog(MyClassActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("失败")
                                            .setConfirmText("确认");
                                    errorDialog.setContentText("所查询的班级不存在");
                                    errorDialog.show();
                                    return;
                                }
                                populateView(adminClass);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                sweetAlertDialog.cancel();
                                Toast.makeText(MyClassActivity.this, "" + throwable, Toast.LENGTH_SHORT).show();
                                SweetAlertDialog errorDialog = new SweetAlertDialog(MyClassActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("失败")
                                        .setConfirmText("确认");
                                errorDialog.setContentText("请稍后重试");
                                errorDialog.show();
                            }
                        });
            }
        });
        builder.show();


    }

    private void populateView(final AdminClass adminClass) {
        View view = View.inflate(this, R.layout.dialog_class_info, null);
        ImageView clzImage = ButterKnife.findById(view, R.id.iv_class_image);
        TextView clzRegion = ButterKnife.findById(view, R.id.tv_class_region);
        TextView clzSchool = ButterKnife.findById(view, R.id.tv_class_school);
        TextView clzName = ButterKnife.findById(view, R.id.tv_class_name);
        Glide.with(this).load(Constant.SERVER_URL + adminClass.getImageURL())
                .into(clzImage);
        clzRegion.setText(adminClass.getRegion());
        clzSchool.setText(adminClass.getSchool());
        clzName.setText(adminClass.getGrade() + adminClass.getsNumber() + "班");
        new AlertDialog.Builder(this).setView(view).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                RetrofitUtils.getService().joinClass(UserInfo.id, adminClass.getId())
                        .unsubscribeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Result>() {
                            @Override
                            public void accept(Result result) throws Exception {
                                dialog.dismiss();
                                new SweetAlertDialog(MyClassActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("成功")
                                        .setContentText("请求已发送\n等待班主任同意")
                                        .setConfirmText("确认")
                                        .show();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(MyClassActivity.this, "" + throwable, Toast.LENGTH_SHORT).show();
                                new SweetAlertDialog(MyClassActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("失败")
                                        .setContentText("请稍后重试")
                                        .setConfirmText("确认")
                                        .show();
                            }
                        });

            }
        }).setNegativeButton("取消", null).show();
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
