package com.xugaoxiang.ott.setting.module.main;



/**
 * Created by Administrator on 2017/3/2 0002.
 */
public class WirePresenter {
    private static WireView wireView;
    private static String string;

    public WirePresenter(WireView wireView, String s){
        this.wireView = wireView;
        this.string = s;
    }
    public static void changeText(){
        wireView.changeText(string);
    }
}
