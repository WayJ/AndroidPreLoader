package com.tianque.fastnetloader

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
//import com.tianque.fastnetloader.FastNetLoader

class MainActivity : AppCompatActivity() {
//    var mFastNetLoader : FastNetLoader = FastNetLoader()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*set cache*/
        findViewById<Button>(R.id.button).setOnClickListener({
//            mFastNetLoader.put("cacheStr","this is a test String.")
//            mFastNetLoader.put("cacheStr","this is a test String.")
        })

//        get cache
        findViewById<Button>(R.id.button2).setOnClickListener({
            startActivity(Intent(this,GetCacheActivity::class.java))
        })


    }
}
