package com.tianque.fastnetloader

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.tianque.preloader.FastNetLoader

class GetCacheActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcache)
        var loader = FastNetLoader(this)

//        get url from other api,load url
        var url = intent.getStringExtra("url")

        if(url.contains("img")) {
            loader.getBitmap(url) { url, found, data ->
                if (found)
                    findViewById<ImageView>(R.id.imageView).setImageBitmap(data)
            }
        }
        else
            loader.getString(url){ url, found, data ->
                if(found)
                    findViewById<TextView>(R.id.textView).text = data
            }

    }
}