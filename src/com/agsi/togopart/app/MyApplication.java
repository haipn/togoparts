
package com.agsi.togopart.app;

import android.app.Application;

/**
 * Application class for init ImageLoader, Volley. Used to ensure that MyVolley is initialized. {@see MyVolley}
 * @author haipn
 *
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        init();
    }


    private void init() {
        MyVolley.init(this);
    }
}
