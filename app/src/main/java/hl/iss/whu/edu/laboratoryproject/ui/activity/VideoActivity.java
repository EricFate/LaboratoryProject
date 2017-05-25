package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.ui.fragment.ChapterFragment;
import hl.iss.whu.edu.laboratoryproject.ui.fragment.LessonDetailFragment;
import hl.iss.whu.edu.laboratoryproject.ui.fragment.QuestionFragment;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;
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
    @Bind(R.id.tv_video_buffer)
    TextView tvVideoBuffer;
    @Bind(R.id.tv_video_speed)
    TextView tvVideoSpeed;
    @Bind(R.id.ll_brightness)
    LinearLayout llBrightness;
    @Bind(R.id.ll_sound)
    LinearLayout llSound;
    @Bind(R.id.pg_sound)
    ProgressBar pgSound;
    @Bind(R.id.pg_brightness)
    ProgressBar pgBrightness;

    private static final int MODIFY_DISABLE = 0;
    private static final int MODIFY_VOLUME = 1;
    private static final int MODIFY_BRIGHTNESS = 2;
    private static final int MODIFY_PROGRESS = 3;
    @Bind(R.id.iv_brightness)
    ImageView ivBrightness;
    @Bind(R.id.iv_sound)
    ImageView ivSound;
    @Bind(R.id.tv_progress)
    TextView tvProgress;
    @Bind(R.id.iv_ff)
    ImageView ivFf;
    @Bind(R.id.ll_ff)
    LinearLayout llFf;
    @Bind(R.id.ll_root)
    LinearLayout llRoot;
    private int modify_flag = 0;

    private Handler mHandler = new Handler();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private boolean isFullScreen = false;
    private boolean isControllerHidden = false;
    private SimpleDateFormat format = new SimpleDateFormat("mm:ss");
    private final int UPDATE_INTERVAL = 1000;
    private final int HIDE_INTERVAL = 3000;
    private HideControllerRunnable mHideControllerRunnable = new HideControllerRunnable();
    private GestureDetector mGestureDetector;
    private Timer timer = new Timer();
    private TimerTask task;
    private boolean firstScroll;
    private int currentVolume;
    private int maxVolume;
    private AudioManager audiomanager;
    private float mBrightness;
    private int lastVolume;
    private long playingTime;
    private long lastTime;
    private LessonDetailFragment mLessonDetailFragment;
    private ChapterFragment mChapterFragment;
    private QuestionFragment mQuestionFragment;
    private ConversationFragment mConversationFragment;

    private long duration = 0;
    private static final int RECORD_INTERVAL = 1000;



    private int lid;
    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }
    //    private int playerWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Vitamio.isInitialized(this);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        initListener();
        initVideoView();
        initData();
        startUpdate();
        startRecord();
    }

    private void startRecord() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mVideoView.isPlaying())
                    duration+=RECORD_INTERVAL;
            }
        },RECORD_INTERVAL,RECORD_INTERVAL);
    }

    public void playVideo(String url) {
        mVideoView.stopPlayback();
        mVideoView.setVideoPath(url);
        sbProgress.setMax((int) mVideoView.getDuration());
        updateController();
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
        timer.schedule(task, UPDATE_INTERVAL, UPDATE_INTERVAL);
    }

    private void initListener() {
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.e("doubleTap", "confirm");
                toggleFullScreen();
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.e("singleclick", "confirm");
                toggleHideController();
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                firstScroll = true;
                return super.onDown(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                float mOldX = e1.getX(), mOldY = e1.getY(),
                        y = e2.getY(), x = e2.getX();
//                Log.i("this", "onScroll:mOldy = " + mOldY + " y = " + y);
                int playerWidth = clVideo.getWidth();
                int playerHeight = clVideo.getHeight();
//                Log.i("this", "onScroll: " + playerHeight);
                if (firstScroll) {// 以触摸屏幕后第一次滑动为标准，避免在屏幕上操作切换混乱
                    // 横向的距离变化大则调整进度，纵向的变化大则调整音量
                    if (Math.abs(distanceX) >= Math.abs(distanceY)) {
                        llFf.setVisibility(View.VISIBLE);
                        lastTime = mVideoView.getCurrentPosition();
                        llSound.setVisibility(View.GONE);
                        llBrightness.setVisibility(View.GONE);
                        modify_flag = MODIFY_PROGRESS;
                    } else {
                        if (mOldX > playerWidth * 3.0 / 5) {// 音量
                            llSound.setVisibility(View.VISIBLE);
                            llBrightness.setVisibility(View.GONE);
                            llFf.setVisibility(View.GONE);
                            modify_flag = MODIFY_VOLUME;
                        } else if (mOldX < playerWidth * 2.0 / 5) {// 亮度
                            llBrightness.setVisibility(View.VISIBLE);
                            llSound.setVisibility(View.GONE);
                            llFf.setVisibility(View.GONE);
                            modify_flag = MODIFY_BRIGHTNESS;
                        }
                    }
                }
                // 如果每次触摸屏幕后第一次scroll是调节进度，那之后的scroll事件都处理音量进度，直到离开屏幕执行下一次操作
                if (modify_flag == MODIFY_PROGRESS) {

                    playingTime = (long) (lastTime + (x - mOldX) * 60000 / playerWidth);

                    Log.i("this", "onScroll:playingTime = " + playingTime + " lastTime = " + lastTime);
                    ivFf.setImageResource(playingTime < lastTime ? android.R.drawable.ic_media_rew : android.R.drawable.ic_media_ff);
                    tvProgress.setText(format.format(playingTime) + "/" + format.format(mVideoView.getDuration()));
                }

                // 如果每次触摸屏幕后第一次scroll是调节音量，那之后的scroll事件都处理音量调节，直到离开屏幕执行下一次操作
                else if (modify_flag == MODIFY_VOLUME) {
                    currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值


                    currentVolume = (int) (lastVolume + (mOldY - y) / playerHeight * maxVolume);
                    if (currentVolume <= 0) {
                        ivSound.setImageResource(android.R.drawable.ic_lock_silent_mode);
                        currentVolume = 0;
                    } else {
                        ivSound.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
                        if (currentVolume > maxVolume)
                            currentVolume = maxVolume;
                    }
                    pgSound.setProgress(currentVolume);
                    audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
                }

                // 如果每次触摸屏幕后第一次scroll是调节亮度，那之后的scroll事件都处理亮度调节，直到离开屏幕执行下一次操作
                else if (modify_flag == MODIFY_BRIGHTNESS) {

                    WindowManager.LayoutParams lpa = getWindow().getAttributes();
                    lpa.screenBrightness = mBrightness + (mOldY - y) / playerHeight;
                    if (lpa.screenBrightness > 1.0f)
                        lpa.screenBrightness = 1.0f;
                    else if (lpa.screenBrightness < 0.01f)
                        lpa.screenBrightness = 0.01f;
                    getWindow().setAttributes(lpa);

                    pgBrightness.setProgress((int) (lpa.screenBrightness * 100));
                }
                firstScroll = false;// 第一次scroll执行完成，修改标志
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
        clVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (modify_flag == MODIFY_PROGRESS)
                        mVideoView.seekTo(playingTime);
                    modify_flag = 0;// 手指离开屏幕后，重置调节音量或进度的标志
                    llBrightness.setVisibility(View.GONE);
                    llSound.setVisibility(View.GONE);
                    llFf.setVisibility(View.GONE);
                    mBrightness = getWindow().getAttributes().screenBrightness;
                    lastVolume = currentVolume;
                }
                return mGestureDetector.onTouchEvent(event);
            }
        });
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    //开始缓冲
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        tvVideoBuffer.setVisibility(View.VISIBLE);
                        tvVideoSpeed.setVisibility(View.VISIBLE);
                        mp.pause();
                        break;
                    //缓冲结束
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        tvVideoBuffer.setVisibility(View.GONE);
                        tvVideoSpeed.setVisibility(View.GONE);
                        mp.start();
                        break;
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        tvVideoSpeed.setText("当前网速" + extra + "kb/s");
                        break;
                }
                ibPlay.setImageResource(mVideoView.isPlaying() ? R.drawable.ic_pause_white_24dp : R.drawable.ic_play_arrow_white_24dp);
                return true;
            }
        });
        mVideoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                tvVideoBuffer.setText("已经缓冲" + percent + "%");
            }
        });

    }

    private void toggleHideController() {
        isControllerHidden = !isControllerHidden;
        llControlBottom.setVisibility(isControllerHidden ? View.GONE : View.VISIBLE);
        if (!isControllerHidden) {
            planHideController();
        }
    }


    private void initController() {
//        planHideController();
        sbProgress.setMax((int) mVideoView.getDuration());
        updateController();
        ibPlay.setImageResource(mVideoView.isPlaying() ? R.drawable.ic_pause_white_24dp : R.drawable.ic_play_arrow_white_24dp);
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
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        final ViewGroup.LayoutParams params = clVideo.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = params.width * metrics.widthPixels / metrics.heightPixels;
        clVideo.setLayoutParams(params);
        if (intent != null) {
            if (intent.getData() != null) {
                mVideoView.setVideoURI(intent.getData());
            }
        }
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                initController();
                ViewGroup.LayoutParams params1 = clVideo.getLayoutParams();
                params1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                params1.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                clVideo.setLayoutParams(params1);
            }
        });

    }

    private void initData() {
        audiomanager = (AudioManager) getSystemService(AUDIO_SERVICE);
        maxVolume = audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // 获取系统最大音量
        currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
        pgSound.setMax(maxVolume);
        pgSound.setProgress(currentVolume);
        pgBrightness.setMax(100);
        titles.add("课时");
        titles.add("讨论");
        titles.add("详情");
        titles.add("练习");
        mLessonDetailFragment = new LessonDetailFragment();
        mChapterFragment = new ChapterFragment();
        mQuestionFragment = new QuestionFragment();
        mConversationFragment = new ConversationFragment();
        int gid = getIntent().getIntExtra("gid", 0);
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(Conversation.ConversationType.GROUP.getName().toLowerCase())
                .appendQueryParameter("targetId", "g"+gid).build();
        mConversationFragment.setUri(uri);
//        RetrofitUtils.getService().joinGroup(UserInfo.uid,"cid")
//        .subscribeOn(Schedulers.newThread())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Consumer<Result>() {
//            @Override
//            public void accept(Result result) throws Exception {
//                Toast.makeText(VideoActivity.this, "加入群组成功", Toast.LENGTH_SHORT).show();
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//                Toast.makeText(VideoActivity.this, "加入群组失败"+throwable, Toast.LENGTH_SHORT).show();
//            }
//        });

        fragments.add(mChapterFragment);
        fragments.add(mConversationFragment);
        fragments.add(mLessonDetailFragment);
        fragments.add(mQuestionFragment);
        //初始化ViewPager 和TabLayout
        mViewPager.setAdapter(new VideoFragmentPagerAdapter(getSupportFragmentManager(), fragments, titles));
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==2)
                    mLessonDetailFragment.refresh(lid);
                if (position==3)
                    mQuestionFragment.refresh(lid);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.ib_play, R.id.ib_full})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_play:
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                } else {
                    mVideoView.start();
                }
                ibPlay.setImageResource(mVideoView.isPlaying() ? R.drawable.ic_pause_white_24dp : R.drawable.ic_play_arrow_white_24dp);
                break;
            case R.id.ib_full:
                toggleFullScreen();
                break;
        }
        planHideController();
    }

    private void toggleFullScreen() {
        isFullScreen = !isFullScreen;
        setRequestedOrientation(isFullScreen ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        llRoot.setSystemUiVisibility(isFullScreen? View.SYSTEM_UI_FLAG_FULLSCREEN:View.SYSTEM_UI_FLAG_VISIBLE);
        mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
        ibFull.setImageResource(isFullScreen ? R.drawable.ic_fullscreen_exit_white_24dp : R.drawable.ic_fullscreen_white_24dp);
    }

    @Override
    public void onBackPressed() {
        if (isFullScreen) toggleFullScreen();
        else super.onBackPressed();
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

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(Constant.INTENT_COURSE_LEARNING);
        intent.putExtra("sid",UserInfo.id);
        intent.putExtra("cid",getIntent().getIntExtra("cid",0));
        intent.putExtra("duration",duration);
        sendBroadcast(intent);
        super.onDestroy();
    }
}
