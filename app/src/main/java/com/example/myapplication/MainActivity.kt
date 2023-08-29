package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.main_start -> {
                val intent = Intent(applicationContext,GameActivity::class.java)
                startActivity(intent)
            }
            R.id.main_about -> {
                val intent = Intent(applicationContext,AboutActivity::class.java)
                startActivity(intent)
            }
            R.id.main_exit -> finish()
        }
    }
}

