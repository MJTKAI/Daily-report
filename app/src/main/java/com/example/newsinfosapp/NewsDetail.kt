package com.example.newsinfosapp

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class NewsDetail : AppCompatActivity() {

    lateinit var back: ImageView
    lateinit var image: ImageView
    lateinit var title: TextView
    lateinit var time: TextView
    lateinit var web: WebView

    var dbNews: DBNews? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        dbNews = DBNews(baseContext)
        back = findViewById<View>(R.id.back) as ImageView
        image = findViewById<View>(R.id.image) as ImageView
        title = findViewById<View>(R.id.title) as TextView
        time = findViewById<View>(R.id.time) as TextView
        web = findViewById<View>(R.id.web) as WebView
        back.setOnClickListener(View.OnClickListener { finish() })

        val news: News? = intent.getStringExtra("id")?.let { dbNews!!.getById(it) }
        val webSettings: WebSettings = web.getSettings()
        webSettings.javaScriptEnabled = true // 启用JavaScript以确保加载网页中的一些交互元素正确工作


        // 加载指定的URL

        // 加载指定的URL
        if (news != null) {
            web.loadUrl(news.url)
            title.setText(news.title)
            time.setText(news.author_name+ " " + news.date)
            Glide.with(this@NewsDetail).load(news.thumbnail_pic_s).into(image)
            Glide.with(this@NewsDetail)
                .asBitmap() // 确保加载的是 Bitmap
                .load(news.thumbnail_pic_s)
                .into(object : CustomTarget<Bitmap?>() {

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        image.setBackground(BitmapDrawable(resources, resource))
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // 当图片加载被清除时调用（如内存不足）
                        image.setBackground(null)
                    }
                })
        }



    }
}