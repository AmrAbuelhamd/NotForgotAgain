package com.blogspot.soyamr.notforgotagain.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.soyamr.notforgotagain.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.appTheme)
        super.onCreate(savedInstanceState)
//        deleteDatabase("notes_database")

        setContentView(R.layout.activity_main)

    }




}