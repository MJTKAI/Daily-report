package com.example.newsinfosapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Register : AppCompatActivity() {
    lateinit var name: EditText
    lateinit var _psw: EditText
    lateinit var tel:EditText

    lateinit var join_now: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        tel = findViewById<EditText>(R.id.tel)
        name = findViewById<EditText>(R.id.phone)
        _psw = findViewById<EditText>(R.id._psw)

        join_now = findViewById<Button>(R.id.join_now)
        val db = DBUser(this)

        join_now.setOnClickListener(View.OnClickListener {
            if (_psw.text.toString() == "" || name.text.toString() == "" || tel.text
                    .toString() == ""
            ) {
                Toast.makeText(this@Register, "不能为空", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val n: String = name.text.toString()
            val p: String = _psw.text.toString()
            if (db.add(n, p, tel.text.toString())) {
                Toast.makeText(this@Register, "注册成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@Register, "注册失败，用户或已存在", Toast.LENGTH_SHORT).show()
            }
        })
    }

}