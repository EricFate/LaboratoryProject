package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.BaseApplication;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.listener.RecordSendMessageListener;
import hl.iss.whu.edu.laboratoryproject.service.ChatMessageRecordService;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class SplashActivity extends AppCompatActivity {
    @Bind(R.id.iv_logo)
    ImageView ivLogo;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.fl_container)
    FrameLayout flContainer;
    AlphaAnimation showLogo;
    TranslateAnimation showTitleLayout, showTitleView;
    private boolean animationEnd;
    private boolean requestEnd;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent;
            switch (msg.what) {
                case 0:
                    intent = new Intent(SplashActivity.this, SigninActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.splash_enter, R.anim.splash_exit);
                    break;
                case 1:
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.splash_enter, R.anim.splash_exit);
                    break;
            }
            RongIM.getInstance().setSendMessageListener(new RecordSendMessageListener(mService));
        }
    };
    private boolean needLogin;
    private SharedPreferences mPreferences;
    private ChatMessageRecordService mService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((ChatMessageRecordService.MyBinder)service).getService();
            Log.i("onServiceConnected:", "onServiceConnected: "+mService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initDB();
        initService();
        mPreferences = getSharedPreferences(Constant.PREFERENCE_USERINFO, MODE_PRIVATE);
        needLogin = mPreferences.getBoolean(Constant.KEY_NEED_LOGIN, true);
        if (!needLogin)
            sendRequest();
        else requestEnd = true;
        startAnimation();
    }

    private void initService() {
        Intent intent = new Intent(this, ChatMessageRecordService.class);
        bindService(intent,mConnection,BIND_AUTO_CREATE);
    }

    private void initDB() {
        InputStream stream = null;
        FileOutputStream outputStream = null;
        try {
            String fileName = "region.db";
            File file = new File(getFilesDir(), fileName);
            if (file.exists())
                return;
            stream = getAssets().open(fileName);
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = stream.read(buffer)) != -1)
                outputStream.write(buffer, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null)
                    stream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        Toast.makeText(this, "拷贝完成", Toast.LENGTH_SHORT).show();
    }

    private void sendRequest() {
        String username = mPreferences.getString("username", "");
        String password = mPreferences.getString("password", "");
        RetrofitUtils.getService().login(username, password)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        if (result.getCode() == 0) {
                            UserInfo.imageURL = result.getImageURL();
                            UserInfo.nickname = result.getNickname();
                            UserInfo.id = result.getId();
                            UserInfo.uid = "s" + UserInfo.id;
                            UserInfo.token = result.getToken();
                            UserInfo.signiture = result.getSigniture();
                            UserInfo.grade = result.getGrade();
                            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                                @Override
                                public io.rong.imlib.model.UserInfo getUserInfo(String uid) {
                                    return UserInfo.findUserByUid(uid);
                                }
                            }, true);
                            RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
                                @Override
                                public boolean onUserPortraitClick(Context context, Conversation.ConversationType type, io.rong.imlib.model.UserInfo info) {
                                    Intent intent = new Intent(context, PersonalInfoActivity.class);
                                    intent.putExtra("uid", info.getUserId());
                                    startActivity(intent);
                                    return false;
                                }

                                @Override
                                public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType type, io.rong.imlib.model.UserInfo info) {
                                    return false;
                                }

                                @Override
                                public boolean onMessageClick(Context context, View view, io.rong.imlib.model.Message message) {
                                    return false;
                                }

                                @Override
                                public boolean onMessageLinkClick(Context context, String s) {
                                    return false;
                                }

                                @Override
                                public boolean onMessageLongClick(Context context, View view, io.rong.imlib.model.Message message) {
                                    return false;
                                }
                            });
                            connect();
                        } else {
                            new AlertDialog.Builder(SplashActivity.this)
                                    .setMessage("登录失败:" + result.getMessage())
                                    .setNegativeButton("确定", null).create().show();
                            requestEnd = true;
                            handler.sendEmptyMessageDelayed(0, 500);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(SplashActivity.this, "登录失败" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        requestEnd = true;
                        handler.sendEmptyMessageDelayed(0, 500);
                    }
                });
    }

    public void connect() {

        RongIM.connect(UserInfo.token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                requestForToken();

            }

            @Override
            public void onSuccess(String s) {
                requestEnd = true;
                if (animationEnd)
                    sendMessage();
            }

            @Override
            public void onError(RongIMClient.ErrorCode code) {
                Toast.makeText(SplashActivity.this, "登录失败" + code.getMessage(), Toast.LENGTH_SHORT).show();
                requestEnd = true;
                handler.sendEmptyMessageDelayed(0, 500);
            }
        });
    }


    private void requestForToken() {
        RetrofitUtils.getService().requestToken(UserInfo.uid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<Result>() {
                               @Override
                               public void accept(Result result) throws Exception {
                                   if (result.getCode() == 0) {
                                       UserInfo.token = result.getToken();
                                       connect();
                                   } else {
                                       new AlertDialog.Builder(SplashActivity.this).setMessage("错误:" + result.getMessage()).show();
                                   }

                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   new AlertDialog.Builder(SplashActivity.this).setMessage("错误:" + throwable.toString()).show();
                               }
                           }
                );
    }

    private void startAnimation() {
        showLogo = new AlphaAnimation(0, 1);
        showLogo.setDuration(800);
        showTitleLayout = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        showTitleLayout.setDuration(800);
        showTitleView = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        showTitleView.setDuration(800);
        showTitleView.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationEnd = true;
                if (requestEnd)
                    sendMessage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        showLogo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                flContainer.setVisibility(View.VISIBLE);
                flContainer.startAnimation(showTitleLayout);
                tvTitle.startAnimation(showTitleView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivLogo.startAnimation(showLogo);
    }

    private void sendMessage() {
        handler.sendEmptyMessageDelayed(needLogin ? 0 : 1, 500);
    }


}
