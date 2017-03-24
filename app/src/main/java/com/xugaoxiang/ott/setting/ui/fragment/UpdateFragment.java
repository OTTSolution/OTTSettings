package com.xugaoxiang.ott.setting.ui.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.RecoverySystem;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xugaoxiang.ott.setting.R;
import com.xugaoxiang.ott.setting.util.UpdateUtils;
import com.xugaoxiang.ott.setting.util.systemutils.OtaPackageInfo;
import com.xugaoxiang.ott.setting.util.systemutils.PreferenceUtil;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */
public class UpdateFragment extends Fragment {

    private String TAG = "UpdateFragment";
    private static final String FILENAME_PROC_VERSION = "/proc/version";
    private static final String SERVER_URL = "http://10.10.10.166:8080/download/OTA/";
    private static final String mXmlFileName = UpdateUtils.getHeadOfBSPVersion(FILENAME_PROC_VERSION) + "-" + "update.xml";
    private static final String mOtaFileName = UpdateUtils.getHeadOfBSPVersion(FILENAME_PROC_VERSION) + "-" + "update.zip";
    private static final String mOtaFileUri = PreferenceUtil.getString("server_url",SERVER_URL) + mOtaFileName;
    private final String xmlFilePath = Environment.getDownloadCacheDirectory().getPath() + File.separator + "update.xml";
    private final String otaFilePath = Environment.getDownloadCacheDirectory().getPath() + File.separator + "update.zip";
    private  String mXmlFileUri = PreferenceUtil.getString("server_url",SERVER_URL) + mXmlFileName;

    private Context mContext;
    private ProgressBar probar;
    private TextView tvDownloadsize;
    private TextView tvDownloadpercent;
    private TextView tvSoftwareVersion;
    private TextView tvHardwareVersion;
    private TextView tvUpdateState;
    private Button btnCheckUpdate;
    private ProgressDialog checkingDialog;
    private DownloadManager downloadManager;
    private List<OtaPackageInfo> mInfoList;

    private boolean isFinish;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update,container,false);
        probar = (ProgressBar) view.findViewById(R.id.progressbar_id);
        probar.setIndeterminate(false);
        tvDownloadsize = (TextView) view.findViewById(R.id.download_size);
        tvDownloadpercent = (TextView) view.findViewById(R.id.download_percent);
        tvSoftwareVersion = (TextView) view.findViewById(R.id.software_version_id);
        tvHardwareVersion = (TextView) view.findViewById(R.id.firmware_version_id);
        tvUpdateState = (TextView) view.findViewById(R.id.update_state_id);
        String version = UpdateUtils.formatKernelVersion(FILENAME_PROC_VERSION);
        tvSoftwareVersion.setText(version);
        tvHardwareVersion.setText(version);
        downloadManager = DownloadService.getDownloadManager();
        downloadManager.setTargetFolder(Environment.getDownloadCacheDirectory().getPath());
        downloadManager.removeAllTask();
        btnCheckUpdate = (Button) view.findViewById(R.id.check_update);
        btnCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "button clicked");
                checkNetworkStatus();
            }
        });
        return view;
    }

    private void checkNetworkStatus() {
        if (!UpdateUtils.isNetworkConnected(mContext)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.dialog);
            builder.setTitle(R.string.Unable_to_connect_updata);
            builder.setMessage(R.string.check_net_updata);
            builder.setPositiveButton(R.string.sure_updata, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.canc_updata, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else checkUpdateXml();
    }

    private void checkUpdateXml() {
        showDialog("", getResources().getString(R.string.later_updata));
        deleteFile(xmlFilePath);
        GetRequest request = OkGo.get(mXmlFileUri).connTimeOut(5000);
        downloadManager.addTask("update.xml", "update.xml", request,
                new com.lzy.okserver.listener.DownloadListener() {

                    @Override
                    public void onProgress(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onFinish(DownloadInfo downloadInfo) {
                        if (isFinish) return;
                        else isFinish = true;
                        mInfoList = getXmlFileInfo(downloadInfo.getTargetPath());
                        Iterator it = mInfoList.iterator();
                        for (final OtaPackageInfo tmp: mInfoList) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(R.string.version_updata + tmp.getVersion()).append("\n");
                            sb.append(R.string.descrption_updata + tmp.getDescEn()).append("\n");
                            sb.append("MD5: " + tmp.getMd5());
                            if (compareOtaVersion(tmp.getVersion())) {
                                Log.i(TAG, "Newer version OTA package is available to be downloaded to update");
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.dialog);
                                builder.setTitle(R.string.info_updata);
                                builder.setMessage(sb.toString());
                                builder.setPositiveButton(R.string.download_updata, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        downloadOtaFile(tmp.getDownloadUrl());
                                        isFinish = false;
                                    }
                                });
                                builder.setNegativeButton(R.string.canc_updata, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        isFinish = false;
                                    }
                                });
                                builder.create().show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.dialog);
                                builder.setTitle(R.string.system_information_updata);
                                builder.setMessage(R.string.system_latest_updata);
                                builder.setPositiveButton(R.string.sure_updata, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        isFinish = false;
                                    }
                                });
                                builder.create().show();
                            }
                        }
                        dismissDialog();
                    }

                    @Override
                    public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
                        Log.i(TAG, "xml下载失败");
                        dismissDialog();
                        downloadManager.removeTask("update.xml", true);
                        showAlertDialog(getResources().getString(R.string.Unable_to_connect_server_updata),getResources().getString(R.string.check_again_updata));
                    }
                });
    }


    private void downloadOtaFile(String downloadAddress) {
        deleteFile(otaFilePath);
        GetRequest request = OkGo.get(downloadAddress);
        downloadManager.addTask("update.zip", "update.zip", request,
                new com.lzy.okserver.listener.DownloadListener() {
                    @Override
                    public void onProgress(DownloadInfo downloadInfo) {
                        String progress = (Math.round(downloadInfo.getProgress() * 10000) * 1.0f / 100) + "%";
                        String downloadLength = Formatter.formatFileSize(mContext, downloadInfo.getDownloadLength());
                        String totalLength = Formatter.formatFileSize(mContext, downloadInfo.getTotalLength());
                        probar.setProgress((int) (Math.round(downloadInfo.getProgress() * 10000) * 1.0f / 100));
                        tvDownloadsize.setText(downloadLength + "/" + totalLength);
                        tvDownloadpercent.setText(R.string.percent_updata + progress);
                    }

                    @Override
                    public void onFinish(DownloadInfo downloadInfo) {
                        String filePath = downloadInfo.getTargetPath();
                        Log.i(TAG, "ota下载完成: " + filePath);
                        btnCheckUpdate.post(new Runnable() {
                            public void run() {
                                for (final OtaPackageInfo tmp: mInfoList) {
                                    boolean verifyStatus;
                                    Log.i(TAG, "校验开始");
//                                    showDialog("VerifyPackage", "Verifying Package , Please wait...");
                                    verifyStatus = verifyOtaPackage(new RecoverySystem.ProgressListener() {
                                        @Override
                                        public void onProgress(int progress) {
                                            Log.i(TAG, "校验进度: " + progress);
                                        }
                                    });
                                    // verifyInstallPackageWithMd5(filePath, tmp.getMd5())
                                    if(verifyStatus) {
                                        boolean b = installOtaPackage();
                                        if (b) {
                                            tvUpdateState.setText(R.string.install_successfully);
                                            Toast.makeText(mContext, R.string.install_successfully, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    else
                                        showAlertDialog(getResources().getString(R.string.package_broken_updata), getResources().getString(R.string.check_again_updata));
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
                        Log.i(TAG, "ota下载失败");
                        downloadManager.removeTask("update.zip", true);
                        showAlertDialog(getResources().getString(R.string.status_download_failed), getResources().getString(R.string.check_again_updata));
                    }
                });
    }

    private void showAlertDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.dialog);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.sure_updata, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 安装升级包
     * @return
     */
    public boolean installOtaPackage() {
        try {
            File packageFile = new File(otaFilePath);
            Log.i(TAG, "Installed OTA package path: " + packageFile.getPath());
            Log.i(TAG, "Installed OTA package name: " + packageFile.getName());
            RecoverySystem.installPackage(mContext, packageFile);
        } catch (Exception e) {
            Log.i(TAG, "Error while install OTA package:" + e);
            Log.i(TAG, "Please retry download");
            return false;
        }
        return true;
    }

    /**
     * 校验升级包
     * @param listener
     * @return
     */
    public boolean verifyOtaPackage(RecoverySystem.ProgressListener listener) {
        File packageFile = new File(otaFilePath);
        if (!packageFile.exists())
            return false;
        try {
            RecoverySystem.verifyPackage(packageFile, listener, null);
            Log.i(TAG, "Successfuly verified ota package.");
        } catch (Exception e) {
            Log.i(TAG, "Corrupted package: " + e);
            return false;
        }
        return true;
    }

    public void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) file.delete();
    }

    public boolean compareOtaVersion(String verXml) {
        try {
            Class<?> systemProperty = Class.forName("android.os.SystemProperties");
            Method getMethod = systemProperty.getDeclaredMethod("get", String.class, String.class);
            String utcDate = (String) getMethod.invoke(systemProperty, "ro.build.date.utc", "1452479343");
            Date date = new Date(Long.valueOf(utcDate) * 1000);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date1 = df.format(date);
            String date2 = verXml + " 00:00:00";
            String date3 = UpdateUtils.utcToLocal(date1, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
            Log.e(TAG, "date1: " + date1 + " date2: " + date2 + " date3: " + date3);
            Log.e(TAG, "Model number: " + Build.MODEL);
            if (UpdateUtils.compareDate(date3, date2) < 0)
                return true;
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<OtaPackageInfo> getXmlFileInfo(String filePath) {
        List<OtaPackageInfo> infoList = null;
        OtaPackageInfo info = null;
        try {
            InputStream inputStream = new FileInputStream(new File(filePath));
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(inputStream, "utf-8");
            int eventType = xmlPullParser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch(eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        infoList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("firmwareudpate")) {
                            Log.i(TAG, "firmwareupdate");
                        } else if (xmlPullParser.getName().equals("firmware")) {
                            info = new OtaPackageInfo();
                            String ver = xmlPullParser.getAttributeValue(0);
                            Log.i(TAG, "OtaPackage.mVersion: " + ver);
                            info.setVersion(ver);
                            eventType = xmlPullParser.next();
                        } else if (xmlPullParser.getName().equals("name")) {
                            String name = xmlPullParser.nextText();
                            Log.i(TAG, "Otapackage.mName: " + name);
                            info.setName(name);
                            eventType = xmlPullParser.next();
                        } else if (xmlPullParser.getName().equals("size")) {
                            String size = xmlPullParser.nextText();
                            Log.i(TAG, "Otapackage.mSize: " + size);
                            info.setSize(Integer.valueOf(size));
                            eventType = xmlPullParser.next();
                        } else if (xmlPullParser.getName().equals("desc_en")) {
                            String desc = xmlPullParser.nextText();
                            Log.i(TAG, "Otapackage.mDescEn: " + desc);
                            info.setDescEn(desc);
                            eventType = xmlPullParser.next();
                        } else if (xmlPullParser.getName().equals("md5")) {
                            String md5 = xmlPullParser.nextText();
                            Log.i(TAG, "Otapackage.mMd5: " + md5);
                            info.setMd5(md5);
                            eventType = xmlPullParser.next();
                        } else if (xmlPullParser.getName().equals("downloadurl")) {
                            String url = xmlPullParser.nextText();
                            Log.i(TAG, "Otapackage.mDownloadUrl: " + url);
                            info.setDownloadUrl(url);
                            eventType = xmlPullParser.next();
                        } else if (xmlPullParser.getName().equals("level")) {
                            String level = xmlPullParser.nextText();
                            Log.i(TAG, "Otapackage.mLevel: " + level);
                            info.setLevel(Integer.parseInt(level));
                            eventType = xmlPullParser.next();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xmlPullParser.getName().equals("firmware")) {
                            infoList.add(info);
                            info = null;
                        }
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return infoList;
    }

    private void showDialog(String title, String msg) {
        checkingDialog = new ProgressDialog(mContext, R.style.dialog);
        checkingDialog.setCanceledOnTouchOutside(false);
        checkingDialog.setCancelable(true);
        if (!TextUtils.isEmpty(title))
            checkingDialog.setTitle(title);
        checkingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                downloadManager.removeTask("update.xml", true);
            }
        });
        checkingDialog.setMessage(msg);
        checkingDialog.show();
    }

    private void dismissDialog() {
        if (checkingDialog != null &&  checkingDialog.isShowing())
            checkingDialog.dismiss();
        checkingDialog = null;
    }
}
