package com.jssdeveloper.mydoctor.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jssdeveloper.mydoctor.util.PreferenceHelper
import kotlinx.android.synthetic.main.activity_menu.*
import com.jssdeveloper.mydoctor.util.PreferenceHelper.set
import com.jssdeveloper.mydoctor.util.PreferenceHelper.get
import com.jssdeveloper.mydoctor.R
import com.jssdeveloper.mydoctor.io.ApiService
import com.jssdeveloper.mydoctor.util.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuActivity : AppCompatActivity() {

    private val apiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
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
            performLogout()

        }

    }

    private fun performLogout()
    {
        val jwt = preferences["jwt", ""]
        val call = apiService.postLogout("Bearer $jwt")

        call.enqueue(object: Callback<Void>{

            override fun onFailure(call: Call<Void>, t: Throwable) {
                toast(t.localizedMessage)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                clearSessionPerference();
                val intent = Intent(this@MenuActivity, MainActivity::class.java);
                startActivity(intent);
                finish();
            }



        })

    }

    private fun clearSessionPerference()
    {

        preferences["jwt"] = "";

    }
}