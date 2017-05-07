package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import hl.iss.whu.edu.laboratoryproject.R;


/**
 * Created by fate on 2017/3/9.
 */

public abstract class BaseInternetRequestActivity extends AppCompatActivity {


    protected void showProgress(){
        findViewById(R.id.ll_loading).setVisibility(View.VISIBLE);
        findViewById(R.id.root).setVisibility(View.GONE);
        findViewById(R.id.ll_error).setVisibility(View.GONE);
    }
    protected void showSuccess(){
        findViewById(R.id.ll_loading).setVisibility(View.GONE);
        findViewById(R.id.root).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_error).setVisibility(View.GONE);
    }
    protected void showError(){
        findViewById(R.id.ll_loading).setVisibility(View.GONE);
        findViewById(R.id.root).setVisibility(View.GONE);
        findViewById(R.id.ll_error).setVisibility(View.VISIBLE);
        findViewById(R.id.bt_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        });
    }
    protected void showEmpty(){
        findViewById(R.id.ll_loading).setVisibility(View.GONE);
        findViewById(R.id.root).setVisibility(View.GONE);
        findViewById(R.id.ll_error).setVisibility(View.GONE);
    }
    protected abstract void retry();

}
