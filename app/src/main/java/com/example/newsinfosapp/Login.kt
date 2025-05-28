package com.example.newsinfosapp

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    lateinit var login: Button
    lateinit var register: TextView
    lateinit var name: EditText
    lateinit var psw:EditText

    public class Global : Application() {
        companion object {
            @JvmField
            var user =User ("","","")
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login = findViewById<Button>(R.id.login)
        psw = findViewById<EditText>(R.id.psw_)
        name = findViewById<EditText>(R.id.name_)
        register = findViewById<TextView>(R.id.sign)


        login.setOnClickListener(View.OnClickListener {
            val u = DBUser(baseContext)
            if (u.check(name.getText().toString(), psw.getText().toString())) {
                val intent = Intent()
                intent.setClass(this@Login, MainActivity::class.java)
                Global.user = u.getByName(name.getText().toString())
                startActivity(intent)
                Toast.makeText(this@Login, "登录成功！", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@Login, "登录失败，密码或账号错误！", Toast.LENGTH_SHORT).show()
            }
        })

        register.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.setClass(this@Login, Register::class.java)
            startActivity(intent)
        })

    }
}