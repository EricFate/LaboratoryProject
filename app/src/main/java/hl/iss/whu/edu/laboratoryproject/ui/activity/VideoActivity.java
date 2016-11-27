package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.VideoFragmentPagerAdapter;
import hl.iss.whu.edu.laboratoryproject.ui.fragment.ChapterFragment;
import hl.iss.whu.edu.laboratoryproject.ui.fragment.DiscoverFragment;
import hl.iss.whu.edu.laboratoryproject.ui.fragment.DiscussFragment;
import hl.iss.whu.edu.laboratoryproject.ui.fragment.LessonSelectionFragment;
import hl.iss.whu.edu.laboratoryproject.ui.fragment.MyLessonsFragment;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.CenterLayout;
import io.vov.vitamio.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    @Bind(R.id.vv_video)
    VideoView mVideoView;
    @Bind(R.id.tl_video)
    TabLayout mTabLayout;
    @Bind(R.id.vp_video)
    ViewPager mViewPager;
    @Bind(R.id.ib_play)
    ImageButton ibPlay;
    @Bind(R.id.sb_progress)
    SeekBar sbProgress;
    @Bind(R.id.ib_full)
    ImageButton ibFull;
    @Bind(R.id.ll_control_bottom)
    LinearLayout llControlBottom;
    @Bind(R.id.tv_current)
    TextView tvCurrent;
    @Bind(R.id.tv_total)
    TextView tvTotal;
    @Bind(R.id.cl_video)
    CenterLayout clVideo;
    private Handler mHandler = new Handler();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private boolean isPlaying = true;
    private boolean isFullScreen = false;
    private boolean isControllerHidden = false;
    private SimpleDateFormat format = new SimpleDateFormat("mm:ss");
    private final int UPDATE_INTERVAL = 1000;
    private final int HIDE_INTERVAL = 3000;
    private HideControllerRunnable mHideControllerRunnable = new HideControllerRunnable();
    private GestureDetector mGestureDetector;
    private Timer timer = new Timer();
    private TimerTask task ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        Vitamio.initialize(this);
        initListener();
        initVideoView();
        initData();
        startUpdate();
    }

    private void startUpdate() {
        task = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateController();
                    }
                });
            }
        };
        timer.schedule(task,UPDATE_INTERVAL,UPDATE_INTERVAL);
    }

    private void initListener() {
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.e("doubleTap","confirm");
                toggleFullScreen();
                return true;
            }
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.e("singleclick","confirm");
                toggleHideController();
                return true;
            }
        });
        clVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("cl","touch");
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }

    private void toggleHideController() {
        isControllerHidden = !isControllerHidden;
        llControlBottom.setVisibility(isControllerHidden?View.GONE:View.VISIBLE);
        if (!isControllerHidden){
            planHideController();
        }
    }


    private void initController() {
//        planHideController();
        sbProgress.setMax((int) mVideoView.getDuration());
        updateController();
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mVideoView.seekTo(seekBar.getProgress());
                planHideController();
            }
        });
    }

    private void planHideController() {
        mHandler.removeCallbacks(mHideControllerRunnable);
        mHandler.postDelayed(mHideControllerRunnable, HIDE_INTERVAL);
    }


    private void updateController() {
        sbProgress.setProgress((int) mVideoView.getCurrentPosition());
        tvCurrent.setText(format.format(mVideoView.getCurrentPosition()));
        tvTotal.setText(format.format(mVideoView.getDuration()));
    }

    private void initVideoView() {
        //初始化VideoView
        final Intent intent = getIntent();

        if (intent != null) {
            if (intent.getData() != null) {
                mVideoView.setVideoURI(intent.getData());
            }
        }
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                initController();
            }
        });

    }

    private void initData() {
        titles.add("课时");
        titles.add("详情");
        titles.add("问答");
        titles.add("讨论");
        fragments.add(new ChapterFragment());
        fragments.add(new DiscussFragment());
        fragments.add(new LessonSelectionFragment());
        fragments.add(new MyLessonsFragment());
        //初始化ViewPager 和TabLayout
        mViewPager.setAdapter(new VideoFragmentPagerAdapter(getSupportFragmentManager(), fragments, titles));
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @OnClick({R.id.ib_play, R.id.ib_full})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_play:
                if (isPlaying) {
                    mVideoView.pause();
                } else {
                    mVideoView.start();
                }
                isPlaying = !isPlaying;
                ibPlay.setImageResource(isPlaying ? R.drawable.ic_pause_white_24dp : R.drawable.ic_play_arrow_white_24dp);
                break;
            case R.id.ib_full:
                toggleFullScreen();
                break;
        }
        planHideController();
    }

    private void toggleFullScreen() {
        isFullScreen = !isFullScreen;
        setRequestedOrientation(isFullScreen?ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE,0);
        ibFull.setImageResource(isFullScreen?R.drawable.ic_fullscreen_exit_white_24dp:R.drawable.ic_fullscreen_white_24dp);
    }

    private class HideControllerRunnable implements Runnable {

        @Override
        public void run() {
            if (!isControllerHidden) {
                llControlBottom.setVisibility(View.GONE);
                isControllerHidden = true;
            }
        }
    }
}
