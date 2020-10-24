package com.blogspot.soyamr.notforgotagain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.appTheme)//lidia no additional activities for splashscreen
        super.onCreate(savedInstanceState)//lidia actually it's the only activity i have in this application
        setContentView(R.layout.activity_main)

    }




}