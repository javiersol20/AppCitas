package com.jssdeveloper.mydoctor.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Adapter
import com.jssdeveloper.mydoctor.R
import com.jssdeveloper.mydoctor.model.Appointment
import kotlinx.android.synthetic.main.activity_appointments.*
import java.util.ArrayList

class AppointmentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)

        val appointments = ArrayList<Appointment>();
        appointments.add(Appointment(1, "Medico A", "12/12/2022", "3:00 PM"));
        appointments.add(Appointment(2, "Medico B", "11/12/2022", "3:30 PM"));
        appointments.add(Appointment(3, "Medico C", "2/12/2022", "8:00 AM"));

        rvAppointments.layoutManager = LinearLayoutManager(this);

        rvAppointments.adapter = AppointmentAdapter(appointments);

    }
}