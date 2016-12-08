package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.glide.GlideRoundTransform;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.vov.vitamio.utils.IOUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SettingActivity extends AppCompatActivity {

    @Bind(R.id.iv_setting_image)
    ImageView ivSettingImage;
    @Bind(R.id.et_setting_signiture)
    EditText etSettingSigniture;
    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        Glide.with(this).load(Constant.SERVER_URL+ UserInfo.imageURL)
                .transform(new GlideRoundTransform(this,35))
                .into(ivSettingImage);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            askForPermissions();
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void askForPermissions(){
        requestPermissions(permissions,REQUEST_EXTERNAL_STORAGE);
    }
    @OnClick({R.id.tr_change_image, R.id.bt_change_signiture})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tr_change_image:
                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, 1);
                break;
            case R.id.bt_change_signiture:
                requestChangeSigniture();
                break;
        }
    }

    private void requestChangeSigniture() {
        String signiture = etSettingSigniture.getText().toString();
        Observable<Result> observable = RetrofitUtils.getService().changeSigniture(UserInfo.username, signiture);
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result value) {
                if (value.getCode() == 0)
                    new AlertDialog.Builder(SettingActivity.this).setMessage("更改成功").show();
                else
                    new AlertDialog.Builder(SettingActivity.this).setMessage("更改失败").show();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                new AlertDialog.Builder(SettingActivity.this).setMessage("更改失败:"+e).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                Log.e("result",resultCode+"");
        if (resultCode == RESULT_OK){
            ContentResolver resolver = getContentResolver();
            try {
                Log.e("result",data.getData().toString());
                InputStream inputStream = resolver.openInputStream(data.getData());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ( (len =inputStream.read(buffer))!=-1){
                    outputStream.write(buffer,0,len);
                }
                Log.e("result",outputStream.toString());
                RequestBody body = RequestBody.create(MediaType.parse("1.jpg"), outputStream.toByteArray());
                Observable<Result> observable = RetrofitUtils.getService().uploadImage(UserInfo.username, body);
                observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result value) {
                        if (value.getCode() == 0)
                        new AlertDialog.Builder(SettingActivity.this).setMessage("上传成功").show();
                        else
                        new AlertDialog.Builder(SettingActivity.this).setMessage("上传失败").show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        new AlertDialog.Builder(SettingActivity.this).setMessage("上传失败:"+e).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
