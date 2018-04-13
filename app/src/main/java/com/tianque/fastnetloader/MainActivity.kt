package com.tianque.fastnetloader

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*set cache*/
        findViewById<Button>(R.id.button).setOnClickListener({

            var url1 = "https://github.com/WayJ/FastNetLoader/blob/master/README.md"
            var intent = Intent(this,GetCacheActivity::class.java)
            intent.putExtra("url",url1)
            startActivity(intent)
        })

//        get cache
        findViewById<Button>(R.id.button2).setOnClickListener({
            var url1 = "http://img.netbian.com/file/2018/0413/5a2411796f04babecffe00034066608a.jpg"
            var intent = Intent(this,GetCacheActivity::class.java)
            intent.putExtra("url",url1)
            startActivity(intent)
        })


    }
}
