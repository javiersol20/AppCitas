package com.jssdeveloper.mydoctor

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_menu.*

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
    }
}