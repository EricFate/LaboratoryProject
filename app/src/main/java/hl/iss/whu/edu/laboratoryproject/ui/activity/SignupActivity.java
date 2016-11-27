package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.IService;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SignupActivity extends AppCompatActivity {
    @Bind(R.id.et_account)
    EditText etAccount;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_email)
    EditText etEmail;
    @Bind(R.id.et_number)
    EditText etNumber;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_realname)
    EditText etRealname;
    @Bind(R.id.spinner_class)
    Spinner spinnerClass;
    @Bind(R.id.spinner_location)
    Spinner spinnerLocation;
    @Bind(R.id.spinner_school)
    Spinner spinnerSchool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.bt_regist)
    public void onClick() {
        String username = etAccount.getText().toString();
        String password = etPassword.getText().toString();
        String email = etEmail.getText().toString();
        String number = etNumber.getText().toString();
        String realname = etRealname.getText().toString();
        String classname = spinnerClass.getSelectedItem().toString();
        String region = spinnerLocation.getSelectedItem().toString();
        String school = spinnerSchool.getSelectedItem().toString();
        String phone = etPhone.getText().toString();

        Map<String,String> map = new HashMap<>();
        map.put("username",username);
        map.put("password",password);
        map.put("email",email);
        map.put("stu_number",number);
        map.put("realname",realname);
        map.put("class",classname);
        map.put("region",region);
        map.put("school",school);
        map.put("phone",phone);

        Observable<Result> observable = RetrofitUtils.getService().signup(map);
        final AlertDialog dialog = new AlertDialog.Builder(SignupActivity.this).setMessage("登录中").create();
        dialog.show();
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Result value) {
                dialog.dismiss();
                if (value.getCode() == 0){
                    Intent intent = new Intent(SignupActivity.this,SigninActivity.class);
                    startActivity(intent);
                }else {
                    new AlertDialog.Builder(SignupActivity.this)
                            .setMessage("登录失败:"+value.getMessage())
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
                dialog.setMessage("错误:"+e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
