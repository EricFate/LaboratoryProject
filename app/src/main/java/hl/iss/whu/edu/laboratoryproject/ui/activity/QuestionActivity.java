package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerAnswerAdapter;
import hl.iss.whu.edu.laboratoryproject.entity.Answer;
import hl.iss.whu.edu.laboratoryproject.entity.Discover;
import hl.iss.whu.edu.laboratoryproject.entity.User;
import hl.iss.whu.edu.laboratoryproject.manager.FullyLinearLayoutManager;

public class QuestionActivity extends AppCompatActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.etv_question_content)
    ExpandableTextView tvDetail;
    @Bind(R.id.tv_discover_number)
    TextView tvDiscoverNumber;
    @Bind(R.id.recycler_answer)
    RecyclerView recyclerAnswer;
    private RecyclerAnswerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initview();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initview() {
        Discover discover = (Discover) getIntent().getSerializableExtra("discover");
        tvTitle.setText(discover.getTitle());
        tvDetail.setText(discover.getDetail());
        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer(getResources().getString(R.string.question_answer1),new User("命运13号","","")));
        answers.add(new Answer(getResources().getString(R.string.question_answer2),new User("西行寺幽幽子","","")));
        adapter = new RecyclerAnswerAdapter(answers);
        recyclerAnswer.setLayoutManager(new FullyLinearLayoutManager(this));
        recyclerAnswer.setAdapter(adapter);
    }

    @OnClick(R.id.ib_share)
    public void onClick() {
    }
}
