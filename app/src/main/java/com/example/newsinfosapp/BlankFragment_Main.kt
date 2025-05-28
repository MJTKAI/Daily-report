package com.example.newsinfosapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment_Main.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment_Main : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var llEmpty: LinearLayout
    lateinit var rvList: RecyclerView


    var mNews = ArrayList<News>()
    lateinit var mNewsAdapter: NewsAdapter
    lateinit var dbNews: DBNews

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_blank__main, container, false)
        dbNews = DBNews(context)
        llEmpty = v.findViewById(R.id.ll_empty)
        rvList = v.findViewById(R.id.rv_list)
        set()
        return v
    }

    fun set() { //设置列表
        val layoutManager = LinearLayoutManager(context)
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //=1.3、设置recyclerView的布局管理器
        rvList.setLayoutManager(layoutManager)
        //==2、实例化适配器
        //=2.1、初始化适配器
        mNewsAdapter = NewsAdapter()
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(mNewsAdapter)
        mNewsAdapter.setItemListener(object : NewsAdapter.ItemListener {


            override fun ItemClick(news: News?) {
                val intent = Intent(context, NewsDetail::class.java)
                if (news != null) {
                    intent.putExtra("id", news.uniquekey)
                }
                startActivity(intent)
            }

            override fun DeleteClick(news: News?) {
                TODO("Not yet implemented")
            }

        })
        begin()
    }

    var SUCCESS = 100
    private fun initData() { // 获取新闻数据
        if (mNews != null && mNews.size > 0) {
            rvList.setVisibility(View.VISIBLE)
            llEmpty.setVisibility(View.GONE)
            mNewsAdapter.addItem(mNews)
        } else {
            rvList.setVisibility(View.GONE)
            llEmpty.setVisibility(View.VISIBLE)
        }
    }

    private val myhandler: Handler = object : Handler() {
        @SuppressLint("HandlerLeak")
        override fun handleMessage(message: Message) {
            if (message.what == SUCCESS) { // 假设SUCCESS是一个已经定义的常量
                val bundle = message.data
                val result = bundle.getString("data")
                try {
                    var jsonObject = JSONObject(result)
                    val data = jsonObject.getString("data")
                    val jsonArray = JSONArray(data)
                    for (i in 0 until jsonArray.length()) {
                        jsonObject = jsonArray.getJSONObject(i)
                        val t = News(
                            jsonObject.getString("uniquekey"),
                            "top",
                            jsonObject.getString("title"),
                            jsonObject.getString("thumbnail_pic_s"),
                            jsonObject.getString("url"),
                            jsonObject.getString("author_name"),
                            jsonObject.getString("date")
                        )
                        mNews.add(t)
                        dbNews?.add(t)
                        initData()
                        Log.d("JsonToEntity:", t.toString())
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun begin() {
        val exist = dbNews!!.checkType("top")
        if (exist) {
            mNews = dbNews!!.getByKind("top") as ArrayList<News>
            initData()
            return
        }
        println("调用了新闻API")
        mNews = ArrayList<News>()
        Thread {
            val jsonData = askApi()
            if (jsonData != null) {
                Log.d("jsonData：", jsonData)
                var jsonObject: JSONObject? = null
                try {
                    jsonObject = JSONObject(jsonData)
                    if (jsonObject.getString("reason").contains("success")) {
                        val result = jsonObject.getString("result")
                        val message = Message.obtain()
                        val bundle = Bundle()
                        bundle.putString("data", result)
                        message.data = bundle
                        message.what = SUCCESS // 假设 SUCCESS 是一个常量或变量，表示消息码
                        myhandler.sendMessage(message)
                    } else {
                        Looper.prepare()
                        Toast.makeText(context, "请求失败，请稍后重试", Toast.LENGTH_SHORT).show()
                        Looper.loop()
                    }
                } catch (e: JSONException) {
                    Looper.prepare()
                    Toast.makeText(context, "请求失败，请稍后重试", Toast.LENGTH_SHORT).show()
                    Looper.loop()
                    throw RuntimeException(e)
                }
            }
        }.start()
    }

    fun askApi(): String? { //调用新闻api，获取新闻数据
        try {
            val k = "top"
            val t =
                "http://v.juhe.cn/toutiao/index?key=e867142983fcfa0a59a74b2d1715cdd2&page=1&page_size=&is_filter=1&type=$k"
            val url = URL(t)
            val connection = url.openConnection() as HttpURLConnection
            if (connection.responseCode == 200) {
                val inputStream = connection.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
                val stringBuilder = StringBuilder()
                var strRead: String?
                while (bufferedReader.readLine().also { strRead = it } != null) {
                    stringBuilder.append(strRead).append("\n") // 注意：在 Java 中不需要额外的 trimIndent()
                }
                bufferedReader.close()
                inputStream.close()
                connection.disconnect()
                return stringBuilder.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // 通常情况下，不建议返回异常的字符串形式作为 API 响应，这里为了保持一致性而保留
            return e.toString()
        }
        // 如果请求失败或发生异常，返回 "-1"
        return "-1"
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment_Main.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment_Main().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}