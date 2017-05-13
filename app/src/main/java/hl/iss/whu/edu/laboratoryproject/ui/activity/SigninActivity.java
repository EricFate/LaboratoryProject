package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Info;
import hl.iss.whu.edu.laboratoryproject.listener.RecordSendMessageListener;
import hl.iss.whu.edu.laboratoryproject.service.IService;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.utils.MD5Utils;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import retrofit2.Response;


public class SigninActivity extends AppCompatActivity {
    @Bind(R.id.et_login_account)
    EditText etAccount;
    @Bind(R.id.et_login_password)
    EditText etPassword;
    @Bind(R.id.cb_remember)
    CheckBox cbRemember;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCE_USERINFO, MODE_PRIVATE);
        etAccount.setText(preferences.getString("username", ""));
    }


    @OnClick(R.id.bt_signup)
    public void onClickSignup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.bt_login)
    public void onClickLogin() {
        final String username = etAccount.getText().toString();
        final String password = etPassword.getText().toString();
        final String encrypted = MD5Utils.Encode(password);
        IService service = RetrofitUtils.getService();
        Observable<Result> observable = service.login(username, encrypted);
        mDialog = new AlertDialog.Builder(SigninActivity.this).setMessage("登录中").create();
        mDialog.show();
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Result value) {
                if (value.getCode() == 0) {
                    UserInfo.username = username;
                    UserInfo.imageURL = value.getImageURL();
                    UserInfo.nickname = value.getNickname();
                    UserInfo.id = value.getId();
                    UserInfo.uid = "s" + UserInfo.id;
                    UserInfo.token = value.getToken();
                    UserInfo.signiture = value.getSigniture();
                    UserInfo.grade = value.getGrade();
                    storeInfo(username, encrypted);
                    RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
                        @Override
                        public Group getGroupInfo(String gid) {
                            return UserInfo.getGroupInfoById(gid);
                        }
                    },true);
                    RongIM.setGroupUserInfoProvider(new RongIM.GroupUserInfoProvider() {
                        @Override
                        public GroupUserInfo getGroupUserInfo(String gid, String uid) {

                            return UserInfo.findGroupUserByUid(gid,uid);
                        }
                    },true);
                    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                        @Override
                        public io.rong.imlib.model.UserInfo getUserInfo(String uid) {
                            return UserInfo.findUserByUid(uid);
                        }
                    },true);
                    RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
                        @Override
                        public boolean onUserPortraitClick(Context context, Conversation.ConversationType type, io.rong.imlib.model.UserInfo info) {
                            Intent intent = new Intent(context, PersonalInfoActivity.class);
                            intent.putExtra("uid",info.getUserId());
                            startActivity(intent);
                            return false;
                        }

                        @Override
                        public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType type, io.rong.imlib.model.UserInfo info) {
                            return false;
                        }

                        @Override
                        public boolean onMessageClick(Context context, View view, Message message) {
                            return false;
                        }

                        @Override
                        public boolean onMessageLinkClick(Context context, String s) {
                            return false;
                        }

                        @Override
                        public boolean onMessageLongClick(Context context, View view, Message message) {
                            return false;
                        }
                    });
                    connect();
                } else {
                    new AlertDialog.Builder(SigninActivity.this)
                            .setMessage("登录失败:" + value.getMessage())
                            .setNegativeButton("确定", null).show();
                    mDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable e) {
                mDialog.setMessage("错误:" + e.toString());
            }

            @Override
            public void onComplete() {
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
                mDialog.dismiss();
                Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(RongIMClient.ErrorCode code) {

            }
        });
    }

    private void requestForToken() {
        RetrofitUtils.getService().requestToken(UserInfo.uid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result value) {
                        if (value.getCode()==0) {
                            UserInfo.token = value.getToken();
                            connect();
                        }
                        else {
                            new AlertDialog.Builder(SigninActivity.this).setMessage("错误:" + value.getMessage()).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        new AlertDialog.Builder(SigninActivity.this).setMessage("错误:" + e.toString()).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void storeInfo(String username, String password) {
        SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCE_USERINFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putBoolean(Constant.KEY_NEED_LOGIN, !cbRemember.isChecked());
        editor.commit();
    }


}
