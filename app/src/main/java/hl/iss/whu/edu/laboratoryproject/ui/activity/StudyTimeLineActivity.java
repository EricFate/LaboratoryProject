package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.TimeLineAdapter;
import hl.iss.whu.edu.laboratoryproject.entity.Answer;
import hl.iss.whu.edu.laboratoryproject.entity.CourseLearning;
import hl.iss.whu.edu.laboratoryproject.entity.DataModal;
import hl.iss.whu.edu.laboratoryproject.entity.DayStudyInfo;
import hl.iss.whu.edu.laboratoryproject.entity.Issue;
import hl.iss.whu.edu.laboratoryproject.entity.Level;
import hl.iss.whu.edu.laboratoryproject.listener.OnGetIdListener;
import hl.iss.whu.edu.laboratoryproject.utils.MyDialog;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.TimeUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import hl.iss.whu.edu.laboratoryproject.utils.ViewUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class StudyTimeLineActivity extends BaseInternetWithEmptyActivity {
    //    int id;
    @Bind(R.id.rv)
    RecyclerView rv;

    TimeLineAdapter rvAdapter;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
//    String studentName;
//    ArrayList<Integer> idArray;
//    ArrayList<String> nameArray;

//    float x1 = 0;
//    float x2 = 0;
//    float y1 = 0;
//    float y2 = 0;

//    private FragmentManager manager;
//    private FragmentTransaction transaction;
//    private ViewPager viewPager;
//    private FragAdapter adapter;
//    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_time_line);
        ButterKnife.bind(this);
//
//        Intent intent = getIntent();
//        //View view=(RelativeLayout)findViewById(R.id.rl_root);
//        id = intent.getIntExtra("studentId", 0);
        initView();
        retrofitGetStudy(UserInfo.id);

//        studentName=intent.getStringExtra("studentName");
//        idArray=intent.getIntegerArrayListExtra("idArray");
//        nameArray=intent.getStringArrayListExtra("nameArray");
//
////        manager = getSupportFragmentManager();
//
//        viewPager=(ViewPager)findViewById(R.id.viewpager);
//        actionBar=this.getSupportActionBar();
//
//        ArrayList<StudyInfoFragment> fragments = new ArrayList<StudyInfoFragment>();
//
//        for(int i=0;i<idArray.size();i++){
//            StudyInfoFragment fragment= new StudyInfoFragment();
//            Bundle bundle = new Bundle();
//            bundle.putInt("id",idArray.get(i));
//            bundle.putString("studentName",nameArray.get(i));
//            bundle.putIntegerArrayList("idArray",idArray);
//            bundle.putStringArrayList("nameArray",nameArray);
//            fragment.setArguments(bundle);
//            fragments.add(fragment);
//        }
//
//        adapter = new FragAdapter(getSupportFragmentManager(), fragments);
//        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                actionBar.setTitle(nameArray.get(position));
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        viewPager.setCurrentItem(idArray.indexOf(id));

    }

    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvAdapter = new TimeLineAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        ViewUtils.handleVerticalLines(findViewById(R.id.view_line_2), findViewById(R.id.view_line_3));
        rvAdapter.setGetIdListener(new OnGetIdListener() {
            @Override
            public void onGetIdListener(String tid) {
                int id = Integer.parseInt(tid.substring(1));
                char c = tid.charAt(0);
                switch (c) {
                    case 'c':
                        Intent intentC = new Intent(StudyTimeLineActivity.this, StudyInfoToCourseInfo.class);
                        intentC.putExtra("cid", id);
                        startActivity(intentC);
                        break;
                    case 'i':
                        retrofitGetIssue(id);
                        break;
                    case 'a':
                        Intent intentA = new Intent(StudyTimeLineActivity.this, StudyInfoToAnswer.class);
                        intentA.putExtra("answerId", id);
                        startActivity(intentA);
                }
            }
        });
    }

    private void retrofitGetIssue(int id) {
        final MyDialog alertDialog = new MyDialog();
        RetrofitUtils.getService().getSingleIssue(id)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Issue>() {
                    @Override
                    public void accept(Issue issue) throws Exception {
                        Intent intent = new Intent(StudyTimeLineActivity.this, QuestionActivity.class);
                        intent.putExtra("issue", issue);
                        startActivity(intent);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable t) throws Exception {
                        alertDialog.showAlertDialgo(StudyTimeLineActivity.this, t.toString());

                    }
                });

    }


    private void retrofitGetStudy(int id) {

        showProgress();
        //rvAdapter.clear();
        final MyDialog alertDialog = new MyDialog();

        RetrofitUtils.getService().getStudyInfo(id)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<DayStudyInfo>>() {
                    @Override
                    public void accept(ArrayList<DayStudyInfo> infos) throws Exception {
                        if (infos.size() != 0) {
                            for (DayStudyInfo dsi : infos) {
                                rvAdapter.addItem(new DataModal(Level.LEVEL_ONE, TimeUtils.format(dsi.getTime())));
                                if (dsi.getLearnings().size() != 0) {
                                    rvAdapter.addItem(new DataModal(Level.LEVEL_TWO, "观看课程"));
                                    for (CourseLearning cl : dsi.getLearnings()) {
                                        DataModal dm = new DataModal(Level.LEVEL_THREE, 0, cl.getCourse().getName() + " " + cl.getDuration() / 60000 + "分钟");
                                        dm.setCourseLearning(cl);
                                        rvAdapter.addItem(dm);
                                    }
                                }
                                if (dsi.getIssues().size() != 0) {
                                    rvAdapter.addItem(new DataModal(Level.LEVEL_TWO, "提出问题"));
                                    for (Issue i : dsi.getIssues()) {
                                        DataModal dm = new DataModal(Level.LEVEL_THREE, 1, i.getTitle());
                                        dm.setIssue(i);
                                        rvAdapter.addItem(dm);
                                    }
                                }
                                if (dsi.getAnswers().size() != 0) {
                                    rvAdapter.addItem(new DataModal(Level.LEVEL_TWO, "回答问题"));
                                    for (Answer a : dsi.getAnswers()) {
                                        DataModal dm = new DataModal(Level.LEVEL_THREE, 2, a.getIssue().getTitle());
                                        dm.setAnswer(a);
                                        rvAdapter.addItem(dm);
                                    }
                                }
                                if (dsi.getMessages().size() != 0) {
                                    rvAdapter.addItem(new DataModal(Level.LEVEL_TWO, "在线交流"));
                                    rvAdapter.addItem(new DataModal(Level.LEVEL_THREE, 3, "发出消息 " + dsi.getMessages().size() + " 次"));
                                }
                            }
                            rv.setAdapter(rvAdapter);
                            showSuccess();
                        } else
                            showEmpty("没有记录");


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable t) throws Exception {

                        alertDialog.showAlertDialgo(StudyTimeLineActivity.this, t.toString());
                        showError();
                    }
                });


    }

    @Override
    protected void retry() {
        retrofitGetStudy(UserInfo.id);
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
