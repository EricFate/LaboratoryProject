package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.InfoDetail;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.glide.GlideCircleTransform;
import hl.iss.whu.edu.laboratoryproject.utils.MyDialog;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class InfoSettingActivity extends AppCompatActivity {

    private static final int CHOOSE_PICTURE = 0;
    private static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    @Bind(R.id.iv_person_info_photo)
    ImageView mIvPersonInfoPhoto;
    @Bind(R.id.tv_person_info_name)
    TextView mTvPersonInfoName;
    @Bind(R.id.tv_person_info_identity)
    TextView mTvPersonInfoIdentity;
    @Bind(R.id.tv_person_info_gender)
    TextView mTvPersonInfoGender;
    @Bind(R.id.tv_person_info_rank)
    TextView mTvPersonInfoRank;
    @Bind(R.id.tv_person_info_school)
    TextView mTvPersonInfoSchool;
    @Bind(R.id.tv_person_info_region)
    TextView mTvPersonInfoRegion;
    @Bind(R.id.tv_person_info_signature)
    TextView mTvPersonInfoSignature;
    @Bind(R.id.tv_person_info_phone)
    TextView mTvPersonInfoPhone;
    @Bind(R.id.tv_person_info_email)
    TextView mTvPersonInfoEmail;
    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private Uri tempUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_setting);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askForPermissions();
        }
        initData();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void askForPermissions() {
        requestPermissions(permissions, REQUEST_EXTERNAL_STORAGE);
    }
    private void initData() {
        RetrofitUtils.getService().getDetailedInfo(UserInfo.uid).subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<InfoDetail>() {
            @Override
            public void accept(InfoDetail detail) throws Exception {
                populateInfo(detail);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                MyDialog.showAlertDialgo(InfoSettingActivity.this,"获取信息失败"+throwable);
            }
        });
    }

    @OnClick({R.id.bt_submit,R.id.iv_person_info_photo, R.id.tv_person_info_name, R.id.tv_person_info_identity, R.id.tv_person_info_gender, R.id.tv_person_info_rank, R.id.tv_person_info_school, R.id.tv_person_info_region, R.id.tv_person_info_signature, R.id.tv_person_info_phone, R.id.tv_person_info_email})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
                submitInfo();
                break;
            case R.id.iv_person_info_photo:
                showChoosePicDialog();
                break;
            case R.id.tv_person_info_name:
                MyDialog.showSingleLineInputDialog(this,"设置昵称",mTvPersonInfoName);
                break;
            case R.id.tv_person_info_gender:
                MyDialog.showGenderSelectDialog(this,"设置性别",mTvPersonInfoGender);
                break;
            case R.id.tv_person_info_rank:
                MyDialog.showSingleLineInputDialog(this,"设置年级",mTvPersonInfoRank);
                break;
            case R.id.tv_person_info_school:
                MyDialog.showSingleLineInputDialog(this,"设置学校",mTvPersonInfoSchool);
                break;
            case R.id.tv_person_info_region:
                MyDialog.showRegionReviseDialog(this,"设置地区",mTvPersonInfoRegion);
                break;
            case R.id.tv_person_info_signature:
                MyDialog.showMultiLineInputDialog(this,"设置签名",mTvPersonInfoSignature);
                break;
            case R.id.tv_person_info_phone:
                MyDialog.showSingleLineInputDialog(this,"设置电话",mTvPersonInfoPhone);
                break;
            case R.id.tv_person_info_email:
                MyDialog.showSingleLineInputDialog(this,"设置邮箱",mTvPersonInfoEmail);
                break;
        }
    }

    private void submitInfo() {
        Map<String,String> map = new HashMap<>();
        final String nickname = mTvPersonInfoName.getText().toString();
        String email = mTvPersonInfoEmail.getText().toString();
        String gender = mTvPersonInfoGender.getText().toString();
        String phone = mTvPersonInfoPhone.getText().toString();
        String region = mTvPersonInfoRegion.getText().toString();
        String school = mTvPersonInfoSchool.getText().toString();
        String grade = mTvPersonInfoRank.getText().toString();
        final String signature = mTvPersonInfoSignature.getText().toString();
        map.put("id",UserInfo.id+"");
        map.put("nickname", nickname);
        map.put("email", email);
        map.put("gender",gender);
        map.put("grade", grade);
        map.put("region", region);
        map.put("school", school);
        map.put("phone", phone);
        map.put("gender", gender);
        map.put("signature",signature);
        RetrofitUtils.getService().changeInfo(map).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
//                        MyDialog.showAlertDialgo(InfoSettingActivity.this,"上传成功");
                        UserInfo.nickname = nickname;
                        UserInfo.signiture = signature;
                        setResult(RESULT_OK);
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        MyDialog.showAlertDialgo(InfoSettingActivity.this,"上传失败"+throwable);
                    }
                });
    }

    private void populateInfo(InfoDetail detail) {

        setText(mTvPersonInfoName, detail.getNickname());
        setText(mTvPersonInfoEmail, detail.getEmail());
        setText(mTvPersonInfoGender, detail.getGender());
        setText(mTvPersonInfoPhone, detail.getPhone());
        setText(mTvPersonInfoRegion, detail.getRegion());
        setText(mTvPersonInfoSignature, detail.getSignature());
        setText(mTvPersonInfoIdentity, UserInfo.getIdentity(UserInfo.uid));
        setText(mTvPersonInfoSchool, detail.getSchool());
        setText(mTvPersonInfoRank,detail.getGrade());
        Glide.with(UiUtils.getContext()).load(Constant.SERVER_URL + detail.getImageURL())
                .placeholder(R.drawable.default_photo)
                .transform(new GlideCircleTransform(this))
                .into(mIvPersonInfoPhoto);
    }

    private void setText(TextView textView, String text) {
        if (TextUtils.isEmpty(text))
            textView.setText("未填写");
        else
            textView.setText(text);
    }



    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
//                        Intent openAlbumIntent = new Intent(
//                                Intent.ACTION_GET_CONTENT);
//                        openAlbumIntent.setType("image/*");
                        Intent openAlbumIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
//                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                        uploadPic(data);
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

//    /**
//     * 保存裁剪之后的图片数据
//     *
//     * @param
//     *
//     * @param picdata
//     */
//    protected void setImageToView(Intent data) {
//        Bundle extras = data.getExtras();
//        if (extras != null) {
//            Bitmap photo = extras.getParcelable("data");
//            photo = Utils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
//            iv_personal_icon.setImageBitmap(photo);
//            uploadPic(photo);
//        }
//    }

    private void uploadPic(Intent data) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
//        String imagePath = ImageUtils.savePhoto(bitmap, Environment
//                .getExternalStorageDirectory().getAbsolutePath(), String
//                .valueOf(System.currentTimeMillis()));
//        Log.e("imagePath", imagePath+"");
//        if(imagePath != null){
//            // 拿着imagePath上传了
//            // ...
//        }
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            RequestBody body = RequestBody.create(MediaType.parse("1.jpg"), outputStream.toByteArray());

            Observable<Result> observable = RetrofitUtils.getService().uploadImage(UserInfo.uid, body);
            observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Result value) {
                    if (value.getCode() == 0) {
                        new AlertDialog.Builder(InfoSettingActivity.this).setMessage("上传成功").show();
                        UserInfo.imageURL = value.getMessage();
                        Log.i("imageURL", "onNext: "+value.getMessage());
                        Glide.with(UiUtils.getContext()).load(Constant.SERVER_URL + UserInfo.imageURL)
                                .transform(new GlideCircleTransform(InfoSettingActivity.this))
                                .into(mIvPersonInfoPhoto);
                        setResult(RESULT_OK);
                    }else
                        new AlertDialog.Builder(InfoSettingActivity.this).setMessage("上传失败").show();
                }

                @Override
                public void onError(Throwable e) {
                    new AlertDialog.Builder(InfoSettingActivity.this).setMessage("上传失败:" + e).show();
                }

                @Override
                public void onComplete() {

                }
            });
        }else
            Toast.makeText(this, "图片获取错误", Toast.LENGTH_SHORT).show();

    }
}
