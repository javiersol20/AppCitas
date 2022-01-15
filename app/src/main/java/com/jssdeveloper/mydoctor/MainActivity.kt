package com.jssdeveloper.mydoctor

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_login.setOnClickListener{
            val intent = Intent(this, MenuActivity::class.java);
            startActivity(intent);
        };

        tvGoToRegister.setOnClickListener {

            Toast.makeText(this, getString(R.string.please_fill_your_register), Toast.LENGTH_SHORT).show()

            val intent = Intent(this, RegisterActivity::class.java);
            startActivity(intent);
        }
    }

}