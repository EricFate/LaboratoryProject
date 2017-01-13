package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerDiscoverAdapter;
import hl.iss.whu.edu.laboratoryproject.entity.Discover;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.ui.activity.QuestionActivity;
import hl.iss.whu.edu.laboratoryproject.ui.view.LoadingPage;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import io.reactivex.Observable;

/**
 * Created by fate on 2016/10/29.
 */

public class DiscoverFragment extends BaseFragment<ArrayList<Discover>> {
    LoadingPage.ResultState state = LoadingPage.ResultState.STATE_ERROR;
    @Override
    public View onCreateSuccessPage() {
        View rootView = UiUtils.inflate(R.layout.fragment_discover);
        RecyclerView recyclerView = ButterKnife.findById(rootView,R.id.recycler_discover);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerDiscoverAdapter adapter = new RecyclerDiscoverAdapter(data);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Discover>() {
            @Override
            public void onItemClick(View v, Discover data) {
                Intent intent = new Intent(getActivity(), QuestionActivity.class);
                intent.putExtra("discover",data);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public Observable<ArrayList<Discover>> sendRequest() {
        return RetrofitUtils.getService().loadDiscover();
    }


}
