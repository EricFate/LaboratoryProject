package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2016/10/29.
 */

public class RecyclerMyLessonsAdapter extends BaseRecyclerViewAdapter<String, RecyclerMyLessonsAdapter.ListViewHoder> {


    public RecyclerMyLessonsAdapter(ArrayList<String> data) {
        super(data);
    }


    @Override
    public ListViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_my_lessons, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecyclerMyLessonsAdapter.this.mListener.onItemClick(v, (String) v.getTag());
                }
            });
            return new ListViewHoder(view);

    }

    @Override
    public void onBindViewHolder(ListViewHoder holder, int position) {
            holder.tv.setText(data.get(position ));
            holder.itemView.setTag(data.get(position));

    }

    static class ListViewHoder extends RecyclerView.ViewHolder {
        TextView tv;
        View itemView;

        public ListViewHoder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv = (TextView) itemView.findViewById(R.id.tv_class_name);
        }
    }

//    static class HeaderViewHolder extends RecyclerView.ViewHolder {
//        public HeaderViewHolder(View itemView) {
//            super(itemView);
//
//            //初始化ViewPager
//            RelativeLayout relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rl_viewpager);
//            ViewPager viewPager = (ViewPager) itemView.findViewById(R.id.vp_lesson_selection);
//            VpAdapte vpAdapter = new VpAdapte(headerData);
//            viewPager.setAdapter(vpAdapter);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//            final LinearLayout linearLayout = new LinearLayout(UiUtils.getContext());
//            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//            for (int i = 0; i < headerData.size(); i++) {
//                ImageView iv = new ImageView(UiUtils.getContext());
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                if (i == 0) {
//                    iv.setImageResource(R.drawable.shape_indicator_checked);
//                } else {
//                    iv.setImageResource(R.drawable.shape_indicator_unchecked);
//                    layoutParams.setMargins(5, 0, 0, 0);
//                }
//                linearLayout.addView(iv, layoutParams);
//            }
//            relativeLayout.addView(linearLayout, params);
//            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                @Override
//                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                }
//
//                @Override
//                public void onPageSelected(int position) {
//                    int size = headerData.size();
//                    for (int i = 0; i < size; i++) {
//                        ((ImageView) linearLayout.getChildAt(i)).setImageResource(R.drawable.shape_indicator_unchecked);
//                    }
//                    ((ImageView) linearLayout.getChildAt(position % size)).setImageResource(R.drawable.shape_indicator_checked);
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int state) {
//
//                }
//            });
//            viewPager.setCurrentItem(headerData.size() * 10000);
//
//        }
//
//        private static class VpAdapte extends PagerAdapter {
//            ArrayList<Integer> picIds;
//
//            public VpAdapte(ArrayList<Integer> picIds) {
//                this.picIds = picIds;
//            }
//
//            @Override
//            public int getCount() {
//                return Integer.MAX_VALUE;
//            }
//
//            @Override
//            public void destroyItem(ViewGroup container, int position, Object object) {
//                container.removeView((View) object);
//            }
//
//            @Override
//            public boolean isViewFromObject(View view, Object object) {
//                return view == object;
//            }
//
//
//            @Override
//            public Object instantiateItem(ViewGroup container, int position) {
//                ImageView imageView = new ImageView(UiUtils.getContext());
//                imageView.setImageResource(picIds.get(position % picIds.size()));
//                container.addView(imageView);
//                return imageView;
//            }
//        }
//    }
}



