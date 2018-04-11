package com.tianque.fastnetloader

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.tianque.fastnetloader.FastNetLoader

class GetCacheActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcache)
        var loader = FastNetLoader()

//        get url from other api,load url
        var url1 = "https://github.com/WayJ/FastNetLoader/blob/master/README.md"
        findViewById<TextView>(R.id.textView).text = loader.getString(url1)

    }
}