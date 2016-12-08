package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.lv_search)
    ListView lvSearch;
    @Bind(R.id.searchview)
    SearchView searchview;
    private String[] test = {"aaa", "bbb", "ccc"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) lvSearch.setFilterText(newText);
                else lvSearch.clearTextFilter();
                return false;
            }
        });
        lvSearch.setTextFilterEnabled(true);
        lvSearch.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, test));
    }

}
