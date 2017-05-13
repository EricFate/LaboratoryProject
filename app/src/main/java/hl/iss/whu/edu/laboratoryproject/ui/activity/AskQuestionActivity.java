package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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

public class AskQuestionActivity extends AppCompatActivity {

    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.switch_anonymous)
    Switch switchAnonymous;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ask, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_submit) {
            Log.e("this", "onOptionsItemSelected: stepin");
            submit();
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit() {
        boolean anonymous = switchAnonymous.isChecked();
        String content = etContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            new AlertDialog.Builder(AskQuestionActivity.this).setMessage("内容不能为空").setPositiveButton("确定", null).show();
            return;
        }
        String title = etTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            new AlertDialog.Builder(AskQuestionActivity.this).setMessage("标题不能为空").setPositiveButton("确定", null).show();
            return;
        }
        if (!title.trim().isEmpty()) {
            Observable<Result> observable = RetrofitUtils.getService().submitQuestion(title, content, anonymous,UserInfo.id);
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
                        new AlertDialog.Builder(AskQuestionActivity.this).setMessage("错误:" + value.getMessage()).setPositiveButton("确定", null).show();
                    }
                }

                @Override
                public void onError(Throwable e) {
                        new AlertDialog.Builder(AskQuestionActivity.this).setMessage("错误:" + e).setPositiveButton("确定", null).show();

                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            new AlertDialog.Builder(this).setMessage("请输入标题").setPositiveButton("确定", null).show();
        }

    }
}
