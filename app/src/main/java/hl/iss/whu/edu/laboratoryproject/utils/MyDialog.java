package hl.iss.whu.edu.laboratoryproject.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.engine.RegionDAO;

/**
 * Created by mrwen on 2017/2/9.
 */

public class MyDialog {
    private static List<Map<String, String>> mProvinces;
    private static List<Map<String, String>> mCities;

    //显示提示框
    public static void showAlertDialgo(Context context,String tip){
        new AlertDialog.Builder(context).setTitle("提示")
                .setMessage(tip)
                .setPositiveButton("确认",null)
                .show();
    }

    //弹出多行输入框
    public static void showMultiLineInputDialog(Context context, String title, final TextView textView) {
        String defaultString = textView.getText().toString();
        View view = View.inflate(context, R.layout.dialog_edit_text, null);
        final EditText inputServer = ButterKnife.findById(view,R.id.et_input);
        //inputServer.setSelection(defaultString.length());
        inputServer.setText(defaultString);
        inputServer.setMaxLines(6);
        inputServer.setMinLines(4);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setView(view).setNegativeButton("取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        textView.setText(inputServer.getText());
                    }
                });
        builder.show();
    }


    //弹出单行输入框
    public static void showSingleLineInputDialog(Context context, String title,final TextView textView) {
        String defaultString = textView.getText().toString();

        View view = View.inflate(context, R.layout.dialog_edit_text, null);
        final EditText editText = ButterKnife.findById(view,R.id.et_input);
        editText.setMaxLines(1);
        editText.setText(defaultString);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setView(view).setNegativeButton("取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        textView.setText(editText.getText());
                    }
                });
        builder.show();


    }

    public static void showSingleLineInputDialog(Context context, String title,DialogInterface.OnClickListener listener) {

        View view = View.inflate(context, R.layout.dialog_edit_text, null);
        final EditText editText = ButterKnife.findById(view,R.id.et_input);
        editText.setMaxLines(1);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setView(view).setNegativeButton("取消", null);
        builder.setPositiveButton("确认", listener);
        builder.show();


    }
    //弹出性别选择框
    public static void showGenderSelectDialog(Context context, String title,final TextView textView){
        String defaultString = textView.getText().toString();
        final View view=View.inflate(context,R.layout.gender_select,null);
        RadioButton rb_man=(RadioButton)view.findViewById(R.id.rb_man);
        RadioButton rb_woman=(RadioButton)view.findViewById(R.id.rb_woman);
        final RadioGroup rg_gender=(RadioGroup)view.findViewById(R.id.rg_gender);
        if(defaultString.equals("男"))
            rb_man.setChecked(true);
        else{
            rb_woman.setChecked(true);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setView(view).setNegativeButton("取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    RadioButton selected = (RadioButton)view.findViewById(rg_gender.getCheckedRadioButtonId());
                    textView.setText(selected.getText());
                    }
                });
        builder.show();
    }

    //弹出地区填写框
    public static void showRegionReviseDialog(Context context, String title,final TextView textView){
        final View view=View.inflate(context,R.layout.dialog_region_selection,null);
     final Spinner   spinnerProvince = ButterKnife.findById(view, R.id.spinner_province);
        final Spinner  spinnerCity = ButterKnife.findById(view, R.id.spinner_city);
        final Spinner  spinnerArea = ButterKnife.findById(view, R.id.spinner_area);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>());
        spinnerProvince.setAdapter(provinceAdapter);
        final ArrayAdapter mCityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>());
        spinnerCity.setAdapter(mCityAdapter);
        final ArrayAdapter mAreaAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>());
        spinnerArea.setAdapter(mAreaAdapter);
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int number = refreshCityByProvince(position, mCityAdapter);
                if (number != 0) {
                    int selectedItemPosition = spinnerCity.getSelectedItemPosition();
                    spinnerCity.setSelection(0);
                    spinnerArea.setSelection(0);
                    if (selectedItemPosition == 0)
                        refreshAreaByCity(0, mAreaAdapter);
                } else {
                    mAreaAdapter.clear();
                    mAreaAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshAreaByCity(position, mAreaAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mProvinces = RegionDAO.getProvinces();
        provinceAdapter.clear();
        provinceAdapter.addAll(flatList(mProvinces));
        provinceAdapter.notifyDataSetChanged();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setView(view).setNegativeButton("取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        textView.setText(spinnerProvince.getSelectedItem().toString()+"_"+spinnerCity.getSelectedItem().toString()+ "_"+spinnerArea.getSelectedItem().toString());
                    }
                });
        builder.show();
    }
    private static void refreshAreaByCity(int position, ArrayAdapter mAreaAdapter) {

        Map<String, String> map = mCities.get(position);
        String cityID = map.get("id");
        List<String> areas = RegionDAO.getAreaByCity(cityID);
        mAreaAdapter.clear();
        if (areas.size() != 0)
            mAreaAdapter.addAll(areas);
        mAreaAdapter.notifyDataSetChanged();

    }

    private static int refreshCityByProvince(int position, ArrayAdapter<String> mCityAdapter) {

        Map<String, String> map = mProvinces.get(position);
        mCities = RegionDAO.getCityByProvince(map.get("id"));
        mCityAdapter.clear();
        if (mCities.size() != 0)
            mCityAdapter.addAll(flatList(mCities));
        mCityAdapter.notifyDataSetChanged();
        return mCities.size();
    }

    private static List<String> flatList(List<Map<String, String>> list) {
        List<String> names = new ArrayList<>();
        for (Map<String, String> item : list) {
            names.add(item.get("name"));
        }
        return names;
    }
    //弹出课时数目选择
//    public void showlessonNumberSelectDialog(Context context, String title,final TextView textView){
//        final View view=View.inflate(context,R.layout.lesson_number_revise,null);
//        final Spinner sp_lessonNumber=(Spinner) view.findViewById(R.id.sp_lesson_number_revise);
//        Integer[] lessonNumber=new Integer[]{1,2,3,4,5};
//        ArrayAdapter<Integer> lessonAdapter;
//        lessonAdapter=new ArrayAdapter<Integer>(context,android.R.layout.simple_spinner_item,lessonNumber);
//        sp_lessonNumber.setAdapter(lessonAdapter);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(title).setIcon(
//                R.drawable.ic_arrow).setView(view).setNegativeButton("取消", null);
//        builder.setPositiveButton("确认",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        textView.setText(sp_lessonNumber.getSelectedItem().toString());
//                    }
//                });
//        builder.show();
//    }
}
