package com.hh.ptrouter;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chrisw on 2018/3/27.
 */

public class PTRouter {
    private static List<PTRoutEntity> entity_list = new ArrayList<>();

    public static void Register(String url, Class<? extends Activity> activity){
        entity_list.add(new PTRoutEntity(url, activity));
    }
}
