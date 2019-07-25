package com.example.siginfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_logged.*

class LoggedActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged)
 
        if(intent != null)
            txt_logged_email.text=intent.getStringExtra("email")
    }
}
