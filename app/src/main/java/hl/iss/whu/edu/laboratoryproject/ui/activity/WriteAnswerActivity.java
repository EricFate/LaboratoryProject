package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WriteAnswerActivity extends AppCompatActivity {


    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.switch_anonymous)
    Switch switchAnonymous;
    @Bind(R.id.toolbar)
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_answer);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ask, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_submit) {
            submit();
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit() {
        boolean anonymous = switchAnonymous.isChecked();
        int iid = getIntent().getIntExtra("iid", -1);
        String content = etContent.getText().toString();
        if (!content.trim().isEmpty()) {
            Observable<Result> observable = RetrofitUtils.getService().submitAnswer(content, anonymous, iid,UserInfo.uid);
            observable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread()).subscribe(new Observer<Result>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Result value) {
                    if (value.getCode() == 0) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        new AlertDialog.Builder(WriteAnswerActivity.this).setMessage("错误:" + value.getMessage()).setPositiveButton("确定", null).show();
                    }
                }

                @Override
                public void onError(Throwable e) {
                        new AlertDialog.Builder(WriteAnswerActivity.this).setMessage("错误:" +e).setPositiveButton("确定", null).show();

                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            new AlertDialog.Builder(this).setMessage("请输入回答内容").setPositiveButton("确定", null).show();
        }

    }
}
