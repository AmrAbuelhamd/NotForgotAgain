package com.blogspot.soyamr.notforgotagain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.blogspot.soyamr.notforgotagain.model.NotesDataBase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.appTheme)
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)

    }




}