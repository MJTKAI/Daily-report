package com.example.newsinfosapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    private val list: MutableList<News> = ArrayList()
    private var mActivity: Context? = null
    private var mItemListener: ItemListener? = null
    fun setItemListener(itemListener: ItemListener?) {
        mItemListener = itemListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        mActivity = viewGroup.context
        val view: View =
            LayoutInflater.from(mActivity).inflate(R.layout.item_news, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val news = list[i]
        if (news != null) {
            viewHolder.title.setText(news.title)
            viewHolder.author_name.setText(news.author_name)
            viewHolder.date.setText(news.date)
            if (!news.thumbnail_pic_s.equals("")) {
                mActivity?.let {
                    Glide.with(it)
                        .asBitmap()
                        .load(news.thumbnail_pic_s)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(viewHolder.img)
                }
            }
            viewHolder.itemView.setOnClickListener {
                if (mItemListener != null) {
                    mItemListener!!.ItemClick(news)
                }
            }
            viewHolder.itemView.setOnLongClickListener {
                if (mItemListener != null) {
                    mItemListener!!.DeleteClick(news)
                }
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItem(listAdd: List<News>?) {
        //如果是加载第一页，需要先清空数据列表
        list.clear()
        if (listAdd != null) {
            //添加数据
            list.addAll(listAdd)
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView
        val author_name: TextView
        val date: TextView
        val img: ImageButton

        init {
            title = itemView.findViewById<TextView>(R.id.title)
            author_name = itemView.findViewById<TextView>(R.id.author_name)
            date = itemView.findViewById<TextView>(R.id.date)
            img = itemView.findViewById<ImageButton>(R.id.img)
        }
    }

    interface ItemListener {
        fun ItemClick(news: News?)
        fun DeleteClick(news: News?)
    }
}
