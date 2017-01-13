package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.manager.SmackManager;
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
    @Bind(R.id.spinner_class)
    Spinner spinnerClass;
    @Bind(R.id.spinner_location)
    Spinner spinnerLocation;
    @Bind(R.id.spinner_school)
    Spinner spinnerSchool;
    @Bind(R.id.et_nickname)
    EditText etNickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.bt_regist)
    public void onClick() {
        final String username = etAccount.getText().toString();
        final String password = etPassword.getText().toString();
        final String nickname = etNickname.getText().toString();
        String email = etEmail.getText().toString();
        String number = etNumber.getText().toString();
        String realname = etRealname.getText().toString();
        String classname = spinnerClass.getSelectedItem().toString();
        String region = spinnerLocation.getSelectedItem().toString();
        String school = spinnerSchool.getSelectedItem().toString();
        String phone = etPhone.getText().toString();

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("nickname", nickname);
        map.put("email", email);
        map.put("stu_number", number);
        map.put("realname", realname);
        map.put("class", classname);
        map.put("region", region);
        map.put("school", school);
        map.put("phone", phone);

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
                    new SmackSignupAsyncTask(username,password,nickname).execute();
//                    Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
//                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(SignupActivity.this)
                            .setMessage("注册失败:" + value.getMessage())
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


    class SmackSignupAsyncTask extends AsyncTask <Void,Void,Boolean>{
        private String username;
        private String password;
        private String nickname;

        public SmackSignupAsyncTask(String username, String password, String nickname) {
            this.username = username;
            this.password = password;
            this.nickname = nickname;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                AbstractXMPPConnection connection = SmackManager.getConnection();
                if (!connection.isConnected())connection.connect();
                AccountManager manager = AccountManager.getInstance(connection);
                manager.sensitiveOperationOverInsecureConnection(true);
                manager.createAccount(Localpart.from(username),password);
                connection.login(username,password);
                VCardManager cardManager = VCardManager.getInstanceFor(connection);
                VCard vCard = cardManager.loadVCard();
                vCard.setNickName(nickname);
                cardManager.saveVCard(vCard);
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean){
                Toast.makeText(UiUtils.getContext(),"注册成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(intent);
            }else {
                new AlertDialog.Builder(SignupActivity.this)
                        .setMessage("注册失败")
                        .setNegativeButton("确定", null).create().show();
            }

        }
    }
}
