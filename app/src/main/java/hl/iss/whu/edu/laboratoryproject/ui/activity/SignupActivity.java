package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.engine.RegionDAO;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.utils.MD5Utils;
import hl.iss.whu.edu.laboratoryproject.utils.MyDialog;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
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
    @Bind(R.id.spinner_school)
    Spinner spinnerSchool;
    @Bind(R.id.et_nickname)
    EditText etNickname;
    @Bind(R.id.spinner_gender)
    Spinner spinnerGender;
    @Bind(R.id.bt_regist)
    Button mBtRegist;
    @Bind(R.id.et_confirm)
    EditText etConfirm;
    @Bind(R.id.spinner_grade)
    Spinner spinnerGrade;
    @Bind(R.id.et_class)
    EditText etClass;
    @Bind(R.id.spinner_province)
    Spinner spinnerProvince;
    @Bind(R.id.spinner_city)
    Spinner spinnerCity;
    @Bind(R.id.spinner_area)
    Spinner spinnerArea;
    private ArrayAdapter<String> mCityAdapter;
    private ArrayAdapter<String> mAreaAdapter;
    private List<Map<String, String>> mProvinces;
    private List<Map<String, String>> mCities;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, new ArrayList<String>());
        spinnerProvince.setAdapter(provinceAdapter);
        mCityAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, new ArrayList<String>());
        spinnerCity.setAdapter(mCityAdapter);
        mAreaAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, new ArrayList<String>());
        spinnerArea.setAdapter(mAreaAdapter);
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int number = refreshCityByProvince(position);
                if (number != 0) {
                    int selectedItemPosition = spinnerCity.getSelectedItemPosition();
                    spinnerCity.setSelection(0);
                    spinnerArea.setSelection(0);
                    if (selectedItemPosition == 0)
                        refreshAreaByCity(0);
                } else {
                    mAreaAdapter.clear();
                    mAreaAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshAreaByCity(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mProvinces = RegionDAO.getProvinces();
        provinceAdapter.clear();
        provinceAdapter.addAll(flatList(mProvinces));
        provinceAdapter.notifyDataSetChanged();
    }

    private void refreshAreaByCity(int position) {

        Map<String, String> map = mCities.get(position);
        String cityID = map.get("id");
        List<String> areas = RegionDAO.getAreaByCity(cityID);
        mAreaAdapter.clear();
        if (areas.size() != 0)
            mAreaAdapter.addAll(areas);
        mAreaAdapter.notifyDataSetChanged();

    }

    private int refreshCityByProvince(int position) {

        Map<String, String> map = mProvinces.get(position);
        mCities = RegionDAO.getCityByProvince(map.get("id"));
        mCityAdapter.clear();
        if (mCities.size() != 0)
            mCityAdapter.addAll(flatList(mCities));
        mCityAdapter.notifyDataSetChanged();
        return mCities.size();
    }

    private List<String> flatList(List<Map<String, String>> list) {
        List<String> names = new ArrayList<>();
        for (Map<String, String> item : list) {
            names.add(item.get("name"));
        }
        return names;
    }


    @OnClick(R.id.bt_regist)
    public void onClick() {
        final String username = etAccount.getText().toString();
        if (username.length() < 4) {
            Toast.makeText(this, "用户名必须超过四位", Toast.LENGTH_SHORT).show();
            return;
        }
        final String password = etPassword.getText().toString();
        String confirm = etConfirm.getText().toString();
        if (!confirm.equals(password)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        String encode = MD5Utils.Encode(password);
        final String nickname = etNickname.getText().toString();
        String email = etEmail.getText().toString();
        String number = etNumber.getText().toString();
        String realname = etRealname.getText().toString();
        String classname = etClass.getText().toString();
        String area = spinnerArea.getSelectedItem().toString();
        if (TextUtils.isEmpty(area)) {
            MyDialog.showAlertDialgo(this, "请选择完整地区");
            return;
        }
        String region = spinnerProvince.getSelectedItem().toString() + "_" + spinnerCity.getSelectedItem().toString() + "_" + area;
        String school = spinnerSchool.getSelectedItem().toString();
        String phone = etPhone.getText().toString();
        String gender = spinnerGender.getSelectedItem().toString();
        String grade = spinnerGrade.getSelectedItem().toString();
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", encode);
        map.put("nickname", nickname);
        map.put("email", email);
        map.put("number", number);
        map.put("realname", realname);
        map.put("classname", classname);
        map.put("region", region);
        map.put("school", school);
        map.put("phone", phone);
        map.put("gender", gender);
        map.put("grade", grade);
        Observable<Result> observable = RetrofitUtils.getService().signup(map);
        final AlertDialog dialog = new AlertDialog.Builder(SignupActivity.this).setMessage("注册中").create();
        dialog.show();
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Result value) {
                dialog.dismiss();
                if (value.getCode() == 0) {
                    Toast.makeText(UiUtils.getContext(), "注册成功", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    new AlertDialog.Builder(SignupActivity.this)
                            .setMessage("注册失败:" + value.getMessage())
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                    dialog.dismiss();
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
}
