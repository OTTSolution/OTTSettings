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


import android.os.Parcel;
import android.util.Log;

//import android.os.Parcelable;

//Parcelable interface is not suitable for transaction across native/java
public class DisplayModeInfo {
    // value for @OutputSignal
    public static final int ZX_WL_OUTPUT_SIGNAL_RGB         = 0;
    public static final int ZX_WL_OUTPUT_SIGNAL_YCBCR422    = 1;
    public static final int ZX_WL_OUTPUT_SIGNAL_YCBCR444    = 2;
    public static final int ZX_WL_OUTPUT_SIGNAL_YCBCR420    = 3;
    public static final int ZX_WL_OUTPUT_SIGNAL_USE_DEFAULT = 4;
    public static final int ZX_WL_OUTPUT_SIGNAL_INVALID     = 5;

    public int Width;
    public int Height;
    public int RefreshRate;
    public int bInterlace; //0-p, 1-i
    public int b3DMode;
    public int OutputSignal;
    public String signature=null;
    public int signature_index;

    private static final String TAG = "DisplayModeInfo";

    public DisplayModeInfo(){}

    public DisplayModeInfo(int normalize){
        Width = normalize;
        Height = normalize;
        RefreshRate = normalize;
        bInterlace = normalize;
        b3DMode = normalize;
        OutputSignal = normalize;
        //signature_index = normalize;
    }

    public boolean isEqual(DisplayModeInfo other){
        return Width == other.Width &&
               Height == other.Height &&
               RefreshRate == other.RefreshRate &&
               bInterlace == other.bInterlace &&
               OutputSignal == other.OutputSignal;
    }

    public void dump(){
        dump(TAG);
    }
    public void dump(String _tag){
        Log.i(_tag,"  Width:"+this.Width+" Height:"+this.Height+" RefreshRate:"+this.RefreshRate+
                  " bInterlace:"+this.bInterlace+" b3DMode:"+this.b3DMode+
                  " OutputSignal:"+this.OutputSignal+
                  (this.signature==null?"":("("+this.signature+")"))
              );
    }

    public static DisplayModeInfo unflatten(Parcel in){
        DisplayModeInfo m = new DisplayModeInfo();
        m.Width = in.readInt();
        m.Height = in.readInt();
        m.RefreshRate = in.readInt();
        m.bInterlace = in.readInt();
        m.b3DMode = in.readInt();
        m.OutputSignal = in.readInt();
        m.signature_index = in.readInt();
        return m;
    }

    public void flatten(Parcel out){
        out.writeInt(Width);
        out.writeInt(Height);
        out.writeInt(RefreshRate);
        out.writeInt(bInterlace);
        out.writeInt(b3DMode);
        out.writeInt(OutputSignal);
        out.writeInt(signature_index);
    }
}

