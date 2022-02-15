package com.jssdeveloper.mydoctor.ui

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jssdeveloper.mydoctor.PreferenceHelper
import kotlinx.android.synthetic.main.activity_menu.*
import com.jssdeveloper.mydoctor.PreferenceHelper.set
import com.jssdeveloper.mydoctor.R

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        btnCreateAppointment.setOnClickListener{
            val intent = Intent(this, CreateAppointmentActivity::class.java);
            startActivity(intent);
        }

        btnMyAppointments.setOnClickListener{
            val intent = Intent(this, AppointmentsActivity::class.java);
            startActivity(intent);
        }

        btnLogout.setOnClickListener{
            clearSessionPerference();
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent);
        }

    }

    private fun clearSessionPerference()
    {


        val preferences = PreferenceHelper.defaultPrefs(this);
        preferences["session"] = false;

    }
}