package com.development.austin.insight;

import android.app.Activity;
import android.content.Context;

/**
 * Created by ARL on 8/31/2016.
 */
public class MyApplication {

    private static String THIS_FILE = "MyApplication";
    public static volatile Context applicationContext = null;
    public static volatile Activity applicationActivity = null;
    public static volatile String userAPI = null;


    public MyApplication() {
        super();
    }

}
