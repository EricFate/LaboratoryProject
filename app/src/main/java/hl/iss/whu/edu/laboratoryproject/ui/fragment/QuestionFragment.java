package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerQuestionAdapter;
import hl.iss.whu.edu.laboratoryproject.entity.Question;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import io.reactivex.Observable;

/**
 * Created by fate on 2016/12/8.
 */

public class QuestionFragment extends BaseFragment<ArrayList <Question>> {
    @Override
    public View onCreateSuccessPage() {
        View view = UiUtils.inflate(R.layout.fragment_question);
        RecyclerView recyclerView = ButterKnife.findById(view,R.id.recycler_question);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerQuestionAdapter adapter = new RecyclerQuestionAdapter(data);
//        Toast.makeText(getActivity(),new Gson().toJson(data),Toast.LENGTH_LONG).show();
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public Observable<ArrayList<Question>> sendRequest() {
        return RetrofitUtils.getService().loadQuestion();
    }
}
