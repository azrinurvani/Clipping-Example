package com.mobile.azrinurvani.clippingexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: Step 1.2 Replace the default content view and set the content view to a new instance of ClippedView
        setContentView(ClippedView(this))
    }
}
