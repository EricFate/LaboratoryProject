package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import hl.iss.whu.edu.laboratoryproject.R;

public class LessonDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_lesson_detail);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_downward_grey_600_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
