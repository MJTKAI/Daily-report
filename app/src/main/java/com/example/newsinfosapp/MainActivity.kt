package com.example.newsinfosapp

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var main: FrameLayout? = null
        var navigation: BottomNavigationView? = null
        main = findViewById<FrameLayout>(R.id.main)
        navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            if (item.itemId == R.id.home) {
                val fragment3 = BlankFragment_Main()
                val tran3 = supportFragmentManager.beginTransaction()
                tran3.replace(R.id.main, fragment3)
                tran3.commit()
                true
            } else {
                val fragment22 = BlankFragmentMe()
                val tran22 = supportFragmentManager.beginTransaction()
                tran22.replace(R.id.main, fragment22)
                tran22.commit()
                true
            }
        })
        navigation.setSelectedItemId(R.id.home)
    }


}