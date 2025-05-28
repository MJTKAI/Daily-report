package com.example.newsinfosapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

class DBNews : SQLiteOpenHelper {
    constructor(context: Context?, name: String?, factory: CursorFactory?, version: Int) : super(
        context,
        name,
        factory,
        version
    ) {
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val sql = "create table news(uniquekey text primary key," +
                "type text not null,title text not null,thumbnail_pic_s text not null" +
                ",url text not null,author_name text not null,date text not null)"
        sqLiteDatabase.execSQL(sql)
    }

    private var db: SQLiteDatabase? = null

    constructor(context: Context?) : super(context, "news.db", null, 1) {
        db = this.writableDatabase
    }

    public fun add(u: News): Boolean {
        val values = ContentValues()
        values.put("uniquekey", u.uniquekey)
        values.put("type", u.type)
        values.put("title", u.title)
        values.put("thumbnail_pic_s", u.thumbnail_pic_s)
        values.put("url", u.url)
        values.put("author_name", u.author_name)
        values.put("date", u.date)
        val i = db!!.insert("news", null, values)
        return if (i > 0) {
            true
        } else false
    }

    fun checkType(k: String): Boolean {
        val cursor = db!!.query("news", null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            @SuppressLint("Range") val type = cursor.getString(cursor.getColumnIndex("type"))
            if (k == type) {
                return true
            }
        }
        return false
    }

    fun getByKind(t: String): List<News> {
        val u: MutableList<News> = ArrayList()
        val cursor = db!!.query("news", null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            @SuppressLint("Range") val uniquekey =
                cursor.getString(cursor.getColumnIndex("uniquekey"))
            @SuppressLint("Range") val type = cursor.getString(cursor.getColumnIndex("type"))
            @SuppressLint("Range") val title = cursor.getString(cursor.getColumnIndex("title"))
            @SuppressLint("Range") val thumbnail_pic_s =
                cursor.getString(cursor.getColumnIndex("thumbnail_pic_s"))
            @SuppressLint("Range") val url = cursor.getString(cursor.getColumnIndex("url"))
            @SuppressLint("Range") val author_name =
                cursor.getString(cursor.getColumnIndex("author_name"))
            @SuppressLint("Range") val date = cursor.getString(cursor.getColumnIndex("date"))
            if (t == type) {
                u.add(News(uniquekey, type, title, thumbnail_pic_s, url, author_name, date))
            }
        }
        return u
    }

    fun getById(t: String): News {
        var u = News("","","","","","","")
        val cursor = db!!.query("news", null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            @SuppressLint("Range") val uniquekey =
                cursor.getString(cursor.getColumnIndex("uniquekey"))
            @SuppressLint("Range") val type = cursor.getString(cursor.getColumnIndex("type"))
            @SuppressLint("Range") val title = cursor.getString(cursor.getColumnIndex("title"))
            @SuppressLint("Range") val thumbnail_pic_s =
                cursor.getString(cursor.getColumnIndex("thumbnail_pic_s"))
            @SuppressLint("Range") val url = cursor.getString(cursor.getColumnIndex("url"))
            @SuppressLint("Range") val author_name =
                cursor.getString(cursor.getColumnIndex("author_name"))
            @SuppressLint("Range") val date = cursor.getString(cursor.getColumnIndex("date"))
            if (t == uniquekey) {
                u = News(uniquekey, type, title, thumbnail_pic_s, url, author_name, date)
                return u
            }
        }
        return u
    }


    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {}
}