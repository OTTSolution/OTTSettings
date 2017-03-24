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

public class DisplayColorInfo {
    public int Contrast;
    public int Saturation;
    public int Hue ;
    public int Bright;
    public int flag;   //casted from unsigned int, higher bit reserved

    private static final String TAG = "DisplayModeInfo";

    public DisplayColorInfo(){}

    public DisplayColorInfo(int normalize){
        Contrast = normalize;
        Saturation = normalize;
        Hue = normalize;
        Bright = normalize;
        flag = normalize;
    }

    public void dump(){dump(TAG);}
    public void dump(String _tag){
        Log.i(_tag,"  Contrast:"+this.Contrast+" Saturation:"+this.Saturation+" Hue:"+this.Hue+
                  " Bright:"+this.Bright+" flag:0x"+Integer.toHexString(this.flag)
              );
    }
    public static DisplayColorInfo unflatten(Parcel in){
        DisplayColorInfo c = new DisplayColorInfo();
        c.Contrast = in.readInt();
        c.Saturation = in.readInt();
        c.Hue = in.readInt();
        c.Bright = in.readInt();
        c.flag = in.readInt();
        return c;
    }

    public void flatten(Parcel out){
        out.writeInt(Contrast);
        out.writeInt(Saturation);
        out.writeInt(Hue);
        out.writeInt(Bright);
        out.writeInt(flag);
    }

    public boolean valid(){
        return Contrast >= -100 && Contrast<= 100 &&
               Saturation >= -100 && Saturation<= 100 &&
               Hue >= -180 && Hue <= 180 &&
               Bright >= -100 && Bright<= 100;
    }
}
