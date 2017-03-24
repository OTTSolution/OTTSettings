package com.xugaoxiang.ott.setting.ui.fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xugaoxiang.ott.setting.R;

import com.xugaoxiang.ott.setting.util.systemutils.LanguageUtil;
import com.xugaoxiang.ott.setting.util.systemutils.PreferenceUtil;

/**
 * Created by Administrator on 2016/9/22.
 */
public class LanguageFragment extends android.support.v4.app.Fragment{

    private RadioGroup mRadioGroup=null;
    private RadioButton mRadioZh= null;
    private RadioButton mRadioEn = null;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_lan,container,false);
        initView();
        initListener();
        return view;
    }

    private void initView() {
        mRadioGroup = (RadioGroup)view.findViewById(R.id.radiogroup_lan);
        mRadioZh= (RadioButton) view.findViewById(R.id.btn_lansetting_zh);
        mRadioEn= (RadioButton) view.findViewById(R.id.btn_lansetting_en);
        String string= PreferenceUtil.getString("language", "zh");
        initFocuse(string);
    }

    private void initFocuse(String string) {
        if(string.equals("zh")) mRadioZh.setChecked(true);
        if(string.equals("en")) mRadioEn.setChecked(true);
    }

    private void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btn_lansetting_zh:
                        Toast.makeText(getActivity(),"选择中文",Toast.LENGTH_LONG).show();
                        LanguageUtil languageUtil=new LanguageUtil(getActivity());
                        languageUtil.switchLanguage("zh");
                        getActivity().finish();
//                        //更新语言后，destroy当前页面，重新绘制
//                        getActivity().finish();
//                        Intent it = new Intent(getActivity(), MainActivity.class);
//                        startActivity(it);
//                        final Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(getActivity().getPackageName());
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                        break;
                    case R.id.btn_lansetting_en:
                        Toast.makeText(getActivity(),"Select English",Toast.LENGTH_LONG).show();
                        LanguageUtil languageUti=new LanguageUtil(getActivity());
                        languageUti.switchLanguage("en");
                        getActivity().finish();
//                        Intent intent= getActivity().getPackageManager().getLaunchIntentForPackage(getActivity().getPackageName());
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                        break;
                }
            }
        });
    }

//    private void init() {
//
//
//
//       // listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1,getData()));  //注意一定要匹配数据类型
//
//    }

//    private List<String> getData(){
//        List<String> data = new ArrayList<String>();
//        data.add("中文");
//        data.add("English");
//        return data;
//    }



//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        getActivity().finish();
////        Intent intent=new Intent(getActivity(), MainActivity.class);
////        startActivity(intent);
//    }

}
