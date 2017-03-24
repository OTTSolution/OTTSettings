package com.xugaoxiang.ott.setting.ui.fragment;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xugaoxiang.ott.setting.R;

import java.util.List;

import com.xugaoxiang.ott.setting.util.systemutils.ApkManagerUtil;
import com.xugaoxiang.ott.setting.util.systemutils.ApkTool;
import com.xugaoxiang.ott.setting.util.systemutils.UninstallUtils;

/**
 * Created by Administrator on 2016/9/22.
 */
public class RecoverFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "RecoveryFragment";
    private Context context;
    private DevicePolicyManager manager;
    private ComponentName mAdminName;
    // private View rootView;
    private Button rev_btn;
    // boolean canRecovery = true;
    // boolean mExternalStorageErase = true;

//    private static final int KEYGUARD_REQUEST = 55;
//    static final String ERASE_EXTERNAL_EXTRA = "erase_sd";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        if (!Process.myUserHandle().isOwner()||UserManager.get(getActivity()).hasUserRestriction(
//                UserManager.DISALLOW_FACTORY_RESET)) {
//            canRecovery  = false;
//            Log.i(TAG, "can NOT do factory reset");
//        }

        View view=inflater.inflate(R.layout.fragment_recovery,container,false);
        context = getActivity();
        rev_btn= (Button) view.findViewById(R.id.recovery_btn);
//        initView();
//        establishInitialState() ;
        setlistener();
        return view;
    }

    private void setlistener() {

//        // 获取IDevicePolicyManager
//        manager = (DevicePolicyManager) getActivity().getSystemService(getActivity().DEVICE_POLICY_SERVICE);
//        // 注册广播接受者为admin设备
//        mAdminName = new ComponentName("com.longjing.ott.setting", String.valueOf(MyAdmin.class));
//        if (!manager.isAdminActive(mAdminName)) {
//            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
//            startActivity(intent);
//        }


        rev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });
    }

    private void showConfirmDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.recovery_dialog_title);
        dialog.setMessage(getResources().getString(R.string.recovery_dialog_msg));

        dialog.setCancelable(false);
        dialog.setPositiveButton(R.string.recovery_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                //获取系统管理权限
//                DevicePolicyManager devicePolicyManager = (DevicePolicyManager)getActivity().getSystemService(Context.DEVICE_POLICY_SERVICE);
//                //申请权限
//                ComponentName componentName = new ComponentName(getActivity(), MyAdmin.class);
//                //判断该组件是否有系统管理员的权限
//                boolean isAdminActive = devicePolicyManager.isAdminActive(componentName);
//                Log.e("系统权限",isAdminActive+"");
//                if(isAdminActive){
//                    //恢复出厂设置（建议不要真机测试）
//                    devicePolicyManager.wipeData(0);
//                }else{
//                    Intent intent = new Intent();
//                    //指定动作
//                    intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//                    //指定给哪个组件授权
//                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
//                    //startActivityForResult(intent, 20);
//                    startActivity(intent);
//                }

                getActivity().sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
                List<String> apppackagename= ApkTool.scanLocalInstallAppList(getActivity().getPackageManager());
                UninstallUtils uninstall=new UninstallUtils(getActivity());
                for(int i=0;i<apppackagename.size();i++){
                    String appname=apppackagename.get(i);
                    boolean isinstall=uninstall.checkApplication(appname);
                    if(isinstall)
                        ApkManagerUtil.uninstallAppSlient(getActivity(),appname);
                }
            }
        });
        dialog.setNegativeButton(R.string.recovery_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
        dialog.show();
    }

}
