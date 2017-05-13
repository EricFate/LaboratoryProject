package hl.iss.whu.edu.laboratoryproject.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2017/5/10.
 */

public class SelectionView extends FrameLayout {
    private boolean checked = false;
//    private OnSelectionClickListener mListener;
//
//    public void setListener(OnSelectionClickListener listener) {
//        mListener = listener;
//    }

    private int type = 0;

    public int getType() {
        return type;
    }

    private static final String[] text = {"A","B","C","D"};
    private TextView mImage;
    private TextView mText;

    public SelectionView(Context context) {
        this(context,null);
    }

    public SelectionView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SelectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs!=null){
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SelectionView);
            type = array.getInteger(R.styleable.SelectionView_selectionType,0);
        }
        init();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    private void init() {
        View rootView = View.inflate(UiUtils.getContext(), R.layout.view_selection, this);
        mImage = (TextView) rootView.findViewById(R.id.option_image);
        mText = (TextView) rootView.findViewById(R.id.option_text);

//        rootView.findViewById(R.id.option_root).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener!=null)
//                    mListener.onSelectionClick();
//            }
//        });
        mImage.setText(text[type]);
        setClickable(true);
    }
    public void check(){
        checked = !checked;
        mImage.setBackgroundResource(checked==true?R.drawable.answer_circle:R.drawable.option_circle);
        mImage.setTextColor(getResources().getColor(checked==true?android.R.color.white:R.color.colorPrimary));
        mText.setTextColor(getResources().getColor(checked==true?R.color.colorPrimary:android.R.color.black));
    }
    public void setText(String text){
        mText.setText(text);
    }
    public boolean isChecked() {
        return checked;
    }
    public void setError(){
        mImage.setBackgroundResource(R.drawable.error_circle);
        mImage.setTextColor(getResources().getColor( android.R.color.white));
        mText.setTextColor(getResources().getColor( android.R.color.holo_red_light));
    }
//    public interface OnSelectionClickListener {
//        void onSelectionClick();
//    }
}
