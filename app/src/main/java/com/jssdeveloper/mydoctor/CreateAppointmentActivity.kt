package com.jssdeveloper.mydoctor

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_appointment.*

class CreateAppointmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_appointment)

        btnNext.setOnClickListener{
            cvStep1.visibility = View.GONE;
            cvStep2.visibility = View.VISIBLE;
        }

        btnConfirmAppointment.setOnClickListener{
            Toast.makeText(this, "Cita registrada correctamente", Toast.LENGTH_SHORT).show();
            finish();
        }


        val options = arrayOf("Specialidad A", "Specialidad B", "Specialidad C");
        spinnerSpecialties.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);

        val doctors = arrayOf("Medico A", "Medico B", "Medico C");
        spinnerDoctors.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, doctors)

    }
}