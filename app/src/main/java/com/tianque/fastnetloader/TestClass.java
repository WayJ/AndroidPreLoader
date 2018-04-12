package com.tianque.fastnetloader;

import android.app.Activity;

public class TestClass extends Activity{

    public void test(){
        new FastNetLoader(this).getString("", new FastNetLoader.Action<String>() {
            @Override
            public void action(String url, boolean found, String data) {

            }
        });
    }
}
