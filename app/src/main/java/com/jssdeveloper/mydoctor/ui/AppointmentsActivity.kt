package com.jssdeveloper.mydoctor.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.jssdeveloper.mydoctor.R
import com.jssdeveloper.mydoctor.io.ApiService
import com.jssdeveloper.mydoctor.model.Appointment
import com.jssdeveloper.mydoctor.util.PreferenceHelper
import com.jssdeveloper.mydoctor.util.PreferenceHelper.get
import com.jssdeveloper.mydoctor.util.toast
import kotlinx.android.synthetic.main.activity_appointments.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class AppointmentsActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy{
        PreferenceHelper.defaultPrefs(this)
    }

    private val appointmentAdapter = AppointmentAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)

        loadAppointments()


        rvAppointments.layoutManager = LinearLayoutManager(this);

        rvAppointments.adapter = appointmentAdapter

    }

    private fun loadAppointments() {
        val jwt = preferences["jwt", ""]
        val call = apiService.getAppointments("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Appointment>>{
            override fun onResponse(
                call: Call<ArrayList<Appointment>>,
                response: Response<ArrayList<Appointment>>
            ) {
                if(response.isSuccessful)
                {
                    response.body()?.let {

                        appointmentAdapter.appointments = it
                        appointmentAdapter.notifyDataSetChanged()
                    }

                }
            }

            override fun onFailure(call: Call<ArrayList<Appointment>>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })
    }
}