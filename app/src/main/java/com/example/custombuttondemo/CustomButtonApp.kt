package com.example.custombuttondemo

import android.app.Application




class CustomButtonApp : Application() {

    var startTime: Long = 0
    var startTimeBackground : Long = 0
    override fun onCreate() {
        super.onCreate()
    }


}