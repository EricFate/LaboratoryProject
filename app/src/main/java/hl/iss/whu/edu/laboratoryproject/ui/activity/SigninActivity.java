package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.IService;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Part;

public class SigninActivity extends AppCompatActivity {
    @Bind(R.id.et_login_account)
    EditText etAccount;
    @Bind(R.id.et_login_password)
    EditText etPassword;
    @Bind(R.id.cb_remember)
    CheckBox cbRemember;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCE_USERINFO, MODE_PRIVATE);
        boolean remember = preferences.getBoolean("remember", false);
        cbRemember.setChecked(remember);
        if (remember) {
            etPassword.setText(preferences.getString("password", ""));
        }
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
        IService service = RetrofitUtils.getService();
        Observable<Result> observable = service.login(username, password);
        final AlertDialog dialog = new AlertDialog.Builder(SigninActivity.this).setMessage("登录中").create();
        dialog.show();
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(Result value) {
                dialog.dismiss();
                if (value.getCode() == 0) {
                    UserInfo.username = username;
                    UserInfo.password = password;
                    storeInfo(username, password);
                    Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(SigninActivity.this)
                            .setMessage("登录失败:" + value.getMessage())
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                }
            }
            @Override
            public void onError(Throwable e) {
                dialog.setMessage("错误:" + e.toString());
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
        editor.putBoolean("remember", cbRemember.isChecked());
        editor.commit();
    }
}
