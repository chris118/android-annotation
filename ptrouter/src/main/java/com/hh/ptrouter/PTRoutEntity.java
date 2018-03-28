package com.hh.ptrouter;

import android.app.Activity;

/**
 * Created by chrisw on 2018/3/27.
 */

public class PTRoutEntity {
    public PTRoutEntity(String url, Class<? extends Activity> activity) {
        this.url = url;
        this.activity = activity;
    }

    private final String url;
    private final Class<? extends Activity> activity;

    public String getUrl() {
        return url;
    }

    public Class<? extends Activity> getActivity() {
        return activity;
    }
}
