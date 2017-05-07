package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import hl.iss.whu.edu.laboratoryproject.R;

/**
 * Created by fate on 2017/3/25.
 */

public abstract class BaseInternetWithEmptyActivity extends AppCompatActivity {
    protected void showProgress(){
        findViewById(R.id.ll_loading).setVisibility(View.VISIBLE);
        findViewById(R.id.recycler_general).setVisibility(View.GONE);
        findViewById(R.id.ll_error).setVisibility(View.GONE);
        findViewById(R.id.ll_nodata).setVisibility(View.GONE);
    }
    protected void showSuccess(){
        findViewById(R.id.ll_loading).setVisibility(View.GONE);
        findViewById(R.id.recycler_general).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_error).setVisibility(View.GONE);
        findViewById(R.id.ll_nodata).setVisibility(View.GONE);
    }
    protected void showError(){
        findViewById(R.id.ll_loading).setVisibility(View.GONE);
        findViewById(R.id.recycler_general).setVisibility(View.GONE);
        findViewById(R.id.ll_error).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_nodata).setVisibility(View.GONE);
        findViewById(R.id.bt_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        });
    }
    protected void showEmpty(String text){
        findViewById(R.id.ll_loading).setVisibility(View.GONE);
        findViewById(R.id.recycler_general).setVisibility(View.GONE);
        findViewById(R.id.ll_error).setVisibility(View.GONE);
        findViewById(R.id.ll_nodata).setVisibility(View.VISIBLE);
        TextView textView = (TextView) findViewById(R.id.tv_nodata);
        textView.setText(text);
    }
    protected void showEmpty(@DrawableRes int drawableId, String text){
        findViewById(R.id.ll_loading).setVisibility(View.GONE);
        findViewById(R.id.recycler_general).setVisibility(View.GONE);
        findViewById(R.id.ll_error).setVisibility(View.GONE);
        findViewById(R.id.ll_nodata).setVisibility(View.VISIBLE);
        ImageView imageView = (ImageView) findViewById(R.id.iv_nodata);
        imageView.setImageResource(drawableId);
        TextView textView = (TextView) findViewById(R.id.tv_nodata);
        textView.setText(text);
    }
    protected abstract void retry();
}
