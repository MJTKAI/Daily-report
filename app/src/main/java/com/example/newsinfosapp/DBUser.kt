package com.example.newsinfosapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBUser : SQLiteOpenHelper {
    constructor(context: Context?, name: String?, factory: CursorFactory?, version: Int) : super(
        context,
        name,
        factory,
        version
    ) {
    }

    private var dp: SQLiteDatabase? = null

    constructor(context: Context?) : super(context, "User.db", null, 1) {
        dp = this.writableDatabase
    }

    fun add(name: String?, psw: String?, tel: String?): Boolean {
        val values = ContentValues()
        values.put("name", name)
        values.put("psw", psw)
        values.put("tel", tel)
        val i = dp!!.insert("Users", null, values)
        if (i > 0) {
            Log.d("", "插入成功")
            return true
        }
        return false
    }

    fun getByName(t: String): User {
        var u = User("","","")
        val cursor = dp!!.query("Users", null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            @SuppressLint("Range") val tel = cursor.getString(cursor.getColumnIndex("tel"))
            @SuppressLint("Range") val psw = cursor.getString(cursor.getColumnIndex("psw"))
            @SuppressLint("Range") val name = cursor.getString(cursor.getColumnIndex("name"))
            if (t == name) {
                u = User(name, psw, tel)
                return u
            }
        }
        return u
    }



    fun check(n: String, p: String): Boolean {
        val cursor = dp!!.query("Users", null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            @SuppressLint("Range") val name = cursor.getString(cursor.getColumnIndex("name"))
            @SuppressLint("Range") val psw = cursor.getString(cursor.getColumnIndex("psw"))
            if (n .equals(name)  && p == psw) {
                return true
            }
        }
        return false
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val sql = "create table Users(name text primary key,psw text not null,tel text)"
        sqLiteDatabase.execSQL(sql)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {}
}
