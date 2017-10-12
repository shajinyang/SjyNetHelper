package com.zx.xsk.nethelper.icontext;

import android.app.Application;

/**
 * Created by sjy on 2017/10/10.
 */

public class IApplication {
    public static Application application;
    public static void setApplication(Application application){
        IApplication.application=application;
    }

}
