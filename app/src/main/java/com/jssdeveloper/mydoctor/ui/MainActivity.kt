package com.jssdeveloper.mydoctor.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.jssdeveloper.mydoctor.PreferenceHelper

import kotlinx.android.synthetic.main.activity_main.*
import com.jssdeveloper.mydoctor.PreferenceHelper.get
import com.jssdeveloper.mydoctor.PreferenceHelper.set
import com.jssdeveloper.mydoctor.R

class MainActivity : AppCompatActivity() {

    private val snackBar by lazy { Snackbar.make(mainLayout, R.string.press_back_again, Snackbar.LENGTH_SHORT) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        val preferences = PreferenceHelper.defaultPrefs(this);

        if(preferences["session", false])
        {
            goToMenuActivity();
        }

        btn_login.setOnClickListener{


            createSessionPreference();
            goToMenuActivity();
        };

        tvGoToRegister.setOnClickListener {

            Toast.makeText(this, getString(R.string.please_fill_your_register), Toast.LENGTH_SHORT).show()

            val intent = Intent(this, RegisterActivity::class.java);
            startActivity(intent);
        }
    }

    private fun createSessionPreference()
    {

        val preferences = PreferenceHelper.defaultPrefs(this);
        preferences["session"] = true;
    }
    private fun goToMenuActivity()
    {
        val intent = Intent(this, MenuActivity::class.java);
        startActivity(intent);
        finish();
    }

    override fun onBackPressed() {

        if(snackBar.isShown)
        {
            super.onBackPressed();

        }else{

            snackBar.show();

        }

    }
}