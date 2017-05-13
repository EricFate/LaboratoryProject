package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.Answer;
import hl.iss.whu.edu.laboratoryproject.utils.MyDialog;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.TimeUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.vov.vitamio.utils.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudyInfoToAnswer extends AppCompatActivity {

    @Bind(R.id.layout_study_info_to_answer)
    LinearLayout layout_study_info_to_answer;

    @Bind(R.id.tv_issue_title)
    TextView tvTitle;
    @Bind(R.id.tv_answer_content)
    TextView tvContent;
    @Bind(R.id.tv_agree)
    TextView tvAgree;
    @Bind(R.id.tv_answer_name)
    TextView tvName;
    @Bind(R.id.iv_answer_image)
    ImageView ivImage;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.ll_answer)
    LinearLayout llUser;

    Answer answer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_info_to_answer);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        final int answerId = intent.getIntExtra("answerId", 0);
        retrofitGetSingleAnswer(answerId);

        layout_study_info_to_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer != null) {
                    Intent intent = new Intent(StudyInfoToAnswer.this, QuestionActivity.class);
                    intent.putExtra("issue", answer.getIssue());
                    startActivity(intent);
                }
            }
        });
        llUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyInfoToAnswer.this, PersonalInfoActivity.class);
                intent.putExtra("uid", "s" + answer.getIssue().getUser().getId());
                startActivity(intent);
            }
        });
    }

    private void retrofitGetSingleAnswer(int id) {
        final MyDialog alertDialog = new MyDialog();

        RetrofitUtils.getService().getSingleAnswer(id)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Answer>() {
                    @Override
                    public void accept(Answer answer) throws Exception {

                        Log.e("answerrrr", answer.toString());
                        tvName.setText(answer.getIssue().getUser().getNickname());
                        tvTime.setText(TimeUtils.format(answer.getTime()));
                        tvContent.setText(answer.getContent());
                        tvTitle.setText(answer.getIssue().getTitle());
                        tvAgree.setText(answer.getAgree() + "人赞同");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable t) throws Exception {
                        alertDialog.showAlertDialgo(StudyInfoToAnswer.this, t.toString());

                    }
                });
    }

}

