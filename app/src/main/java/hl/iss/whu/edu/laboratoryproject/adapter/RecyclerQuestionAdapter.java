package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.Question;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2016/12/8.
 */

public class RecyclerQuestionAdapter extends BaseRecyclerViewAdapter<Question,RecyclerQuestionAdapter.MyViewHolder> {
    public RecyclerQuestionAdapter(ArrayList<Question> data) {
        super(data);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_question, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.llQuestionContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.llAnswerContainer.setVisibility(holder.llAnswerContainer.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
                holder.ivArrow.setImageResource(holder.llAnswerContainer.getVisibility()==View.VISIBLE?R.drawable.ic_keyboard_arrow_up_grey_800_24dp:R.drawable.ic_keyboard_arrow_down_grey_800_24dp);
            }
        });
        holder.btAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Toast.makeText(UiUtils.getContext(),"你的回答是:"+holder.etAnswer.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.tvQuestion.setText(data.get(position).getContent());
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvQuestion;
        EditText etAnswer;
        LinearLayout llAnswerContainer;
        Button btAnswer;
        LinearLayout llQuestionContainer;
        ImageView ivArrow;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvQuestion = ButterKnife.findById(itemView, R.id.tv_question);
            etAnswer = ButterKnife.findById(itemView, R.id.et_answer);
            llAnswerContainer = ButterKnife.findById(itemView, R.id.ll_answer_container);
            llQuestionContainer = ButterKnife.findById(itemView, R.id.ll_question_container);
            btAnswer = ButterKnife.findById(itemView,R.id.bt_answer);
            ivArrow = ButterKnife.findById(itemView,R.id.iv_arrow);
        }
    }
}
