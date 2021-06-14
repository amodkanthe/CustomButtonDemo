package com.example.custombuttondemo

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        if(sharedPref.getBoolean(Constants.KEY_ISREGISTERD,false)) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, LoginFragment.newInstance()).commit()
        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, RegisterFragment.newInstance()).commit()
        }
    }
}