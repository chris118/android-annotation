package com.hh.ptapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.hh.ptannotation.PTRoutRule;


@PTRoutRule(value = "putao://home")
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}
