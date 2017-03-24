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

public class DisplayBoundInfo {
    public int Left;
    public int Right;
    public int Top;
    public int Bottom;

    private static final String TAG = "DisplayBoundInfo";

    public DisplayBoundInfo(){}

    public DisplayBoundInfo(int normalize){
        Left = normalize;
        Right = normalize;
        Top = normalize;
        Bottom = normalize;
    }

    public void dump(){dump(TAG);}
    public void dump(String _tag){
        Log.i(_tag,"  Left:"+this.Left+" Right:"+this.Right+" Top:"+this.Top+
                  " Bottom:"+this.Bottom);
    }
    public static DisplayBoundInfo unflatten(Parcel in){
        DisplayBoundInfo c = new DisplayBoundInfo();
        c.Left = in.readInt();
        c.Right = in.readInt();
        c.Top = in.readInt();
        c.Bottom = in.readInt();
        return c;
    }

    public void flatten(Parcel out){
        out.writeInt(Left);
        out.writeInt(Right);
        out.writeInt(Top);
        out.writeInt(Bottom);
    }
}

