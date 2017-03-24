/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xugaoxiang.ott.setting.util.systemutils;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class DisplayDAgent {
    private static final String TAG = "DisplayDAgent";
    private final IBinder mRemote;

    //keep sync with IDisplayDService, IMPLEMENT_META_INTERFACE(DisplayDService, xxx);
    private static final String DESCRIPTOR = "android.displayd.service";

    //keep sync with IDisplayDService, enum in BnDisplayDService
    static final int TRANSACTION_GET_MODE_LIST = IBinder.FIRST_CALL_TRANSACTION + 0;
    static final int TRANSACTION_GET_MODE = IBinder.FIRST_CALL_TRANSACTION + 1;
    static final int TRANSACTION_SET_MODE = IBinder.FIRST_CALL_TRANSACTION + 2;
    static final int TRANSACTION_UPDATE_CACHE = IBinder.FIRST_CALL_TRANSACTION + 3;
    static final int TRANSACTION_GET_MODE_PREFERED = IBinder.FIRST_CALL_TRANSACTION + 4;
    static final int TRANSACTION_GET_COLOR = IBinder.FIRST_CALL_TRANSACTION + 5;
    static final int TRANSACTION_SET_COLOR = IBinder.FIRST_CALL_TRANSACTION + 6;
    static final int TRANSACTION_GET_BOUNDS = IBinder.FIRST_CALL_TRANSACTION + 7;
    static final int TRANSACTION_SET_BOUNDS = IBinder.FIRST_CALL_TRANSACTION + 8;
    static final int TRANSACTION_GET_SCALE = IBinder.FIRST_CALL_TRANSACTION + 9;
    static final int TRANSACTION_SET_SCALE = IBinder.FIRST_CALL_TRANSACTION + 10;
    //keep sync with mref
    static final String refLabel [] = {"576p","576i","720p","1080p",
        "1080i","4k@24Hz","4k@25Hz","4k@30Hz","4k@60Hz","other"};

    public DisplayDAgent(){
        IBinder service = null;
        try {
            Class<?> serviceManager = null;
            if(serviceManager == null){
                serviceManager = Class.forName("android.os.ServiceManager");
                Method method = serviceManager.getDeclaredMethod("getService", String.class);
                 service = (IBinder) method.invoke(serviceManager,"displayd.service");
                //serviceManager.newInstance()  getDeclaredMethod
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         //ServiceManager.getService("displayd.service");
        if(service == null){
            mRemote = null;
            return;
        }
        mRemote = service;
    }

    public ArrayList<DisplayModeInfo> getModeList() throws RemoteException {
        if(mRemote==null)
            return null;
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        ArrayList<DisplayModeInfo> modes;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            mRemote.transact(TRANSACTION_GET_MODE_LIST, _data, _reply, 0);
            //_reply.readException();
            int size = _reply.readInt();
            Log.i(TAG, "request modelist size"+size);
            modes = new ArrayList<DisplayModeInfo>();
            int i;
            for(i=0;i<size;i++){
                DisplayModeInfo m = DisplayModeInfo.unflatten(_reply);
                modes.add(m);
            }
        } finally {
            _reply.recycle();
            _data.recycle();
        }
        return modes;
    }

    public DisplayModeInfo getMode() throws RemoteException {
        if(mRemote==null)
            return null;
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        DisplayModeInfo m;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            mRemote.transact(TRANSACTION_GET_MODE, _data, _reply, 0);
            m = DisplayModeInfo.unflatten(_reply);
        } finally {
            _reply.recycle();
            _data.recycle();
        }
        if(m.signature_index<refLabel.length)
            m.signature=refLabel[m.signature_index];
        return m;
    }
    public void setMode(DisplayModeInfo m) throws RemoteException {
        if(mRemote==null)
            return;
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            m.flatten(_data);
            mRemote.transact(TRANSACTION_SET_MODE, _data, _reply, 0);
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }
    
    public ArrayList<DisplayModeInfo> getModePreferd() throws RemoteException {
        if(mRemote==null)
            return null;
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        ArrayList<DisplayModeInfo> modes;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            mRemote.transact(TRANSACTION_GET_MODE_PREFERED, _data, _reply, 0);

            int size = _reply.readInt();
            Log.i(TAG, "preferd mode size:"+size);
            modes = new ArrayList<DisplayModeInfo>();
            int i;
            for(i=0;i<size;i++){
                DisplayModeInfo m = DisplayModeInfo.unflatten(_reply);
                m.signature=refLabel[i];
                modes.add(m);
            }
        } finally {
            _reply.recycle();
            _data.recycle();
        }
        return modes;
    }

    public DisplayColorInfo getColor() throws RemoteException {
        if(mRemote==null)
            return null;
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        DisplayColorInfo color;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            mRemote.transact(TRANSACTION_GET_COLOR, _data, _reply, 0);
            color = DisplayColorInfo.unflatten(_reply);
        } finally {
            _reply.recycle();
            _data.recycle();
        }
        return color;
    }

    public void setColor(DisplayColorInfo color) throws RemoteException {
        if(mRemote==null)
            return ;
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            color.flatten(_data);
            mRemote.transact(TRANSACTION_SET_COLOR, _data, _reply, 0);
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public DisplayBoundInfo getBounds() throws RemoteException {
        if(mRemote==null)
            return null;
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        DisplayBoundInfo bound;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            mRemote.transact(TRANSACTION_GET_BOUNDS, _data, _reply, 0);
            bound = DisplayBoundInfo.unflatten(_reply);
        } finally {
            _reply.recycle();
            _data.recycle();
        }
        return bound;
    }
    public void setBounds (DisplayBoundInfo bound) throws RemoteException {
        if(mRemote==null)
            return;
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            bound.flatten(_data);
            mRemote.transact(TRANSACTION_SET_BOUNDS, _data, _reply, 0);
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }
    public int getScale() throws RemoteException {
        if(mRemote==null)
            return -1;
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        int scale=-1;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            mRemote.transact(TRANSACTION_GET_SCALE, _data, _reply, 0);
            scale = _reply.readInt();
        } finally {
            _reply.recycle();
            _data.recycle();
        }
        return scale;
    }
    public void setScale(int scale) throws RemoteException {
        if(mRemote==null)
            return ;
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeInt(scale);
            mRemote.transact(TRANSACTION_SET_SCALE, _data, _reply, 0);
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }
}
