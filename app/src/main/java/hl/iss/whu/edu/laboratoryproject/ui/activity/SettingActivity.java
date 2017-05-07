package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.service.IService;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;

public class SettingActivity extends AppCompatActivity {



    private IService mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mService = RetrofitUtils.getService();
        Toolbar toolbar = ButterKnife.findById(this,R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @OnClick({R.id.more_page_info, R.id.bt_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more_page_info:
                Intent intent = new Intent(this, InfoSettingActivity.class);
                startActivityForResult(intent, Constant.REQURST_SETTING);
                break;
            case R.id.bt_logout:
                Intent intentLogout = new Intent(this, SigninActivity.class);
                intentLogout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentLogout);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQURST_SETTING && resultCode == RESULT_OK)
            setResult(RESULT_OK);
        super.onActivityResult(requestCode, resultCode, data);
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
    //    @OnClick({R.id.tr_change_image, R.id.bt_change_signiture, R.id.bt_change_nickname})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.tr_change_image:
//
//                showChoosePicDialog();
//                break;
//            case R.id.bt_change_signiture:
//                requestChangeSigniture();
//                break;
//            case R.id.bt_change_nickname:
//                requestChangeNickname();
//                break;
//        }
//    }

//    private void requestChangeNickname() {
//        final String nickname = etSettingNickname.getText().toString();
//        mService.changeInfo(UserInfo.uid, nickname, Constant.Info.NICKNAME)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Result>() {
//                    @Override
//                    public void accept(Result result) throws Exception {
//                        if (result.getCode() == 0) {
//                            new AlertDialog.Builder(SettingActivity.this).setMessage("更改成功").show();
//                            UserInfo.nickname = nickname;
//                            setResult(RESULT_OK);
//                        }
//                        else
//                            new AlertDialog.Builder(SettingActivity.this).setMessage("更改失败").show();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        new AlertDialog.Builder(SettingActivity.this).setMessage("更改失败:" + throwable).show();
//                    }
//                });
//    }
//
//    private void requestChangeSigniture() {
//        final String signiture = etSettingSigniture.getText().toString();
//        Observable<Result> observable = mService.changeInfo(UserInfo.uid, signiture, Constant.Info.SIGNATURE);
//        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Result value) {
//                if (value.getCode() == 0) {
//
//                    new AlertDialog.Builder(SettingActivity.this).setMessage("更改成功").show();
//                    UserInfo.signiture = signiture;
//                    setResult(RESULT_OK);
//                }else
//                    new AlertDialog.Builder(SettingActivity.this).setMessage("更改失败").show();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                new AlertDialog.Builder(SettingActivity.this).setMessage("更改失败:" + e).show();
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
//    }


}
