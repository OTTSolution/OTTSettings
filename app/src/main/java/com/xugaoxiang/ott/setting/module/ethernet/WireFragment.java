package com.xugaoxiang.ott.setting.module.ethernet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xugaoxiang.ott.setting.R;
import com.xugaoxiang.ott.setting.module.main.MainActivity;
import com.xugaoxiang.ott.setting.module.main.WirePresenter;
import com.xugaoxiang.ott.setting.module.main.WireView;
import com.xugaoxiang.ott.setting.util.networksettingutils.EthernetStatequery;

/**
 * 有线设置页面
 */
public class WireFragment extends Fragment implements WireView{

    private RadioGroup radioGroup;
    private Button btnConfirm;

    private final int MODE_AUTO = 0;
    private final int MODE_MANUAL = 1;
    private int ethernetMode = 0;

    private Context mContext;
    private boolean isFocus;

    //wire模块优化
    private TextView textView;
    private WirePresenter mPresenter;
    private String  string;
    private ConnectivityManager mConnectivityManager;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null)
            isFocus = arguments.getBoolean("isFocus", false);
        View view = inflater.inflate(R.layout.fragment_wire, container, false);
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFocus)
            btnConfirm.requestFocus();
    }

    private void initView(View view) {
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        textView = (TextView) view.findViewById(R.id.wire_tv);
        mPresenter = new WirePresenter(this,mContext.getResources().getString(R.string.ethernet_confignotify));
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ethernetMode == MODE_AUTO)
                {
                    if(!EthernetStatequery.netWorkConnected(mConnectivityManager)){
                        textView.setText(R.string.wire_tv);
                        MainActivity activity = (MainActivity) getActivity();
                        activity.getPresenter().dhcpConnect();
                    }
                    else {
                        Toast.makeText(mContext, R.string.wire_connected,Toast.LENGTH_LONG).show();
                    }
                }
                else if (ethernetMode == MODE_MANUAL)
                {

                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ManuallySetupFragment(), "manuallysetup")
                            .commit();
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_auto:
                        ethernetMode = MODE_AUTO;
                        break;
                    case R.id.rb_manual:
                        ethernetMode = MODE_MANUAL;
                        break;
                }
            }
        });
    }

    @Override
    public void changeText( String s) {

        textView.setText(s);
    }
}
